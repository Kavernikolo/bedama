package com.dama.cerbero.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dama.cerbero.entities.MailTable;
import com.dama.cerbero.entities.Subscriber;
import com.dama.cerbero.entities.TransactionTable;
import com.dama.cerbero.entities.interfaces.MailRepository;
import com.dama.cerbero.entities.interfaces.SubscriberRepository;
import com.dama.cerbero.entities.interfaces.TransactionRepository;
import com.dama.cerbero.requests.Airdrop;
import com.dama.cerbero.requests.Transaction;
import com.dama.cerbero.requests.ConnectRequest;
import com.dama.cerbero.requests.SendEmailRequest;
import com.dama.cerbero.requests.Wallet;
import com.dama.cerbero.responses.Outcome;
import com.dama.cerbero.responses.SolanaResponse;
import com.google.gson.Gson;

@RestController
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	private static final Runtime rt = Runtime.getRuntime();

	@Value("${url.solana}")
	String url;

	@Autowired
	SubscriberRepository userRepository;

	@Autowired
	TransactionRepository txRepository;

	@Autowired
	MailRepository mailRepository;

	private static final String template = "Hello, %s!";

	@GetMapping("/greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format(template, name);
	}

	@CrossOrigin
	@PostMapping(path = "/connect", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome connect(@RequestBody ConnectRequest request) {

		log.info("POST /connect");
		try {
			log.info("Wallet Adapter Name: " + request.getAdapter());
		} catch (Exception e) {

			log.error("Canìt parse body");

		}

		return new Outcome(true);
	}

	@CrossOrigin
	@PostMapping(path = "/sendemail", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome sendemail(@RequestBody SendEmailRequest request) {

		log.info("POST /sendemail");

		try {

			String email = request.getEmail();
			String subject = request.getSubject();
			String message = request.getMessage();

			log.info("Message From: " + email);
			log.info("Message Subject: " + subject);
			log.info("Message: " + request.getMessage());

			try {
				mailRepository.save(new MailTable(email, subject, message));
			} catch (Exception e) {
				log.error("Errore durante il salvataggio sul DB. Eccezione: ");
				log.error(e.getMessage(), e.getCause());
				return new Outcome("ERROR");
			}

		} catch (Exception e) {

			log.error("Canìt parse body");

		}

		return new Outcome(true);
	}

	@CrossOrigin
	@PostMapping(path = "/enrol", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome enrol(@RequestBody Airdrop airdrop) {

		log.info("POST /enrol");

		try {
			if (userRepository.count() < 1000) {
				userRepository.save(new Subscriber(airdrop));
			} else {
				return new Outcome("POOL MAX SIZE REACHED");
			}
		} catch (Exception e) {

			log.error("Exception occurred while trying to save customer " + airdrop);
			log.debug(e.getMessage(), e.getCause());
			if (e.getMessage().contains("Duplicate"))
				return new Outcome("DUPLICATE");
			return new Outcome(false);
		}
		log.info("Correctly saved customer " + airdrop);
		return new Outcome(true);
	}

	@CrossOrigin
	@PostMapping(path = "/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome check(@RequestBody Wallet wallet) {

		log.info("POST /check");

		try {
			List<Subscriber> subscribers = userRepository.findByAddress(wallet.getAddress());
			if (subscribers.isEmpty()) {
				if (userRepository.count() <= 1000) {
					return new Outcome("CAN");
				} else
					return new Outcome("FU");
			}
			for (Subscriber s : subscribers) {
				return new Outcome("WIN");
			}

		} catch (Exception e) {
			log.error("Exception occurred while trying to get customer " + wallet);
			log.debug(e.getMessage(), e.getCause());
			return new Outcome("ERROR");
		}
		log.info("Correctly investigated customer " + wallet);
		return new Outcome(true);
	}

	@CrossOrigin
	@PostMapping(path = "/sendtransaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome rpcSend(@RequestBody Transaction tx) {

		log.info("POST /sendtransaction");

		try {

			JSONObject json = new JSONObject();
			json.put("jsonrpc", "2.0");
			json.put("id", 1);
			json.put("method", "sendTransaction");
			json.put("params", tx.getParams());

			log.info(json.toString());
			int run = 10;

			HttpResponse<String> response = null;

			while (run > 0) {
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create(url))
						.header("Content-type", "application/json")
						.header("Accept", "application/json")
						.POST(BodyPublishers.ofString(json.toString()))
						.build();

				response = client.send(request, BodyHandlers.ofString());
				log.info("Outcome: " + response.statusCode());
				log.info(response.body());
				if (response.body().contains("result")) {
					run = 0;
				} else {
					run--;
					TimeUnit.SECONDS.sleep(4);
				}
			}

			SolanaResponse solanaResponse = new Gson().fromJson(response.body(), SolanaResponse.class);

			log.info("Ho mappato la risposta di Solana");

			try {
				txRepository.save(new TransactionTable(tx, solanaResponse, "mainnet", ""));
			} catch (Exception e) {
				log.error("Errore durante il salvataggio sul DB. Eccezione: ");
				log.error(e.getMessage(), e.getCause());
				return new Outcome("ERROR");
			}
		} catch (Exception e) {
			log.error("Eserrotto ");
			log.debug(e.getMessage(), e.getCause());
			return new Outcome("ERROR");
		}
		log.info("Done ");
		return new Outcome(true);
	}

	@CrossOrigin
	@PostMapping(path = "/sendtransactiondevnet", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome devnetSend(@RequestBody Transaction tx) {

		log.info("POST /sendtransactiondevnet");

		try {

			JSONObject json = new JSONObject();
			json.put("jsonrpc", "2.0");
			json.put("id", 1);
			json.put("method", "sendTransaction");
			json.put("params", tx.getParams());

			log.info(json.toString());
			int run = 10;

			HttpResponse<String> response = null;

			while (run > 0) {
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create("https://api.devnet.solana.com"))
						.header("Content-type", "application/json")
						.header("Accept", "application/json")
						.POST(BodyPublishers.ofString(json.toString()))
						.build();

				response = client.send(request, BodyHandlers.ofString());
				log.info("Outcome: " + response.statusCode());
				log.info(response.body());
				if (response.body().contains("result")) {
					run = 0;
				} else {
					run--;
					TimeUnit.SECONDS.sleep(4);
				}
			}

			SolanaResponse solanaResponse = new Gson().fromJson(response.body(), SolanaResponse.class);

			log.info("Ho mappato la risposta di Solana");

			try {
				txRepository.save(new TransactionTable(tx, solanaResponse, "devnet", ""));
			} catch (Exception e) {
				log.error("Errore durante il salvataggio sul DB. Eccezione: ");
				log.error(e.getMessage(), e.getCause());
				return new Outcome("ERROR");
			}
		} catch (Exception e) {
			log.error("Eserrotto ");
			log.debug(e.getMessage(), e.getCause());
			return new Outcome("ERROR");
		}
		log.info("Done ");
		return new Outcome(true);
	}

	@CrossOrigin
	@PostMapping(path = "/confirmtransaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome rpcCheck(@RequestBody SolanaResponse tx) {

		String response = "KO";
		String command = "/home/solana/.local/share/solana/install/releases/stable-d0ed878d573c7f5391cd2cba20465407f63f11a8/solana-release/bin/solana confirm ";
		log.info("POST /confirmtransactioj");
		
		try {

			Process proc = rt.exec(command + tx.getResult());
			log.info("TX = "+tx.getResult());
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			// Read the output from the command
			String s = null;
			try {
				while ((s = stdInput.readLine()) != null) {
					System.out.println(s);
					response= s;
					log.info("RESPONSE = ["+s+"]");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Read any errors from the attempted command
			try {
				while ((s = stdError.readLine()) != null) {
					System.out.println(s);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.debug(e.getMessage(), e.getCause());
			return new Outcome("ERROR");
		}
		log.info("Done ");
		return new Outcome(response);
	}

}
