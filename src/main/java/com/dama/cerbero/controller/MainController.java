package com.dama.cerbero.controller;

import java.io.File;
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

import com.dama.cerbero.entities.Subscriber;
import com.dama.cerbero.entities.TransactionInteraction;
import com.dama.cerbero.entities.interfaces.SubscriberRepository;
import com.dama.cerbero.entities.interfaces.TransactionInteractionRepository;
import com.dama.cerbero.requests.Airdrop;
import com.dama.cerbero.requests.Transaction;
import com.dama.cerbero.requests.Wallet;
import com.dama.cerbero.responses.Outcome;
import com.dama.cerbero.responses.SolanaResponse;
import com.google.gson.Gson;


@RestController
public class MainController {
		  
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
    
	@Value("${url.solana}")
	String url;
	
	@Autowired
    SubscriberRepository userRepository;
	
	@Autowired
	TransactionInteractionRepository txRepository;

	private static final String template = "Hello, %s!";

	@GetMapping("/greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format(template, name);
	}
	
	@CrossOrigin
	@PostMapping(path = "/enrol", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome enrol(@RequestBody Airdrop airdrop){
		
		log.info("POST /enrol");

		try { 
			if(userRepository.count() < 1000) {
				userRepository.save(new Subscriber(airdrop));
			}
			else {return new Outcome("POOL MAX SIZE REACHED");}
		}
		catch (Exception e){
			
			log.error("Exception occurred while trying to save customer "+airdrop);
			log.debug(e.getMessage(),e.getCause());
			if(e.getMessage().contains("Duplicate")) return new Outcome("DUPLICATE");
			return new Outcome(false);
		}
		log.info("Correctly saved customer "+airdrop);
		return new Outcome(true);
	}

	@CrossOrigin
	@PostMapping(path = "/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome check(@RequestBody Wallet wallet){
		
		log.info("POST /check");

		try { 
			List<Subscriber> subscribers = userRepository.findByAddress(wallet.getAddress());
			if(subscribers.isEmpty()) {
				if(userRepository.count() <= 1000) {
					return new Outcome("CAN");
				}
				else return new Outcome("FU");
			}
			for (Subscriber s : subscribers) {
				return new Outcome("WIN");
			}
			
		}
		catch (Exception e){
			log.error("Exception occurred while trying to get customer "+wallet);
			log.debug(e.getMessage(),e.getCause());
			return new Outcome("ERROR");
		}
		log.info("Correctly investigated customer "+wallet);
		return new Outcome(true);
	}
	@CrossOrigin
	@PostMapping(path = "/sendtransaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome rpcSend(@RequestBody Transaction tx){
		
		log.info("POST /sendtransaction");

		try { 

			JSONObject json = new JSONObject();
			json.put("jsonrpc", "2.0");    
			json.put("id", 1);    
			json.put("method", "sendTransaction");    
			json.put("params", tx.getParams());
			
			HttpClient client = HttpClient.newHttpClient();
		    HttpRequest request = HttpRequest.newBuilder()
		                .uri(URI.create(url))
		                .POST(BodyPublishers.ofString(json.toString()))
		                .build();
		       
		              HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		              log.info("Outcome: " +response.statusCode());
		              log.info(response.body());
		       
		              SolanaResponse solanaResponse = new Gson().fromJson(response.body(), SolanaResponse.class);
		              
		              log.info("Ho mappato la risposta di Solana");
		              
		              try {
		            	  txRepository.save(new TransactionInteraction(tx, solanaResponse));
		              }
		              catch (Exception e) {
		            	  log.error("Errore durante il salvataggio sul DB. Eccezione: ");
		            	  log.error(e.getMessage(), e.getCause());
		      			return new Outcome("ERROR");
		              }

			
		}
		catch (Exception e){
			log.error("Eserrotto ");
			log.debug(e.getMessage(),e.getCause());
			return new Outcome("ERROR");
		}
		log.info("Done ");
		return new Outcome(true);
	}

	@CrossOrigin
	@PostMapping(path = "/confirmtransaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome rpcCheck(@RequestBody SolanaResponse tx){
		
		log.info("POST /confirmtransaction");

		try { 

			ProcessBuilder pd = new ProcessBuilder("/bin/sh -c solana confirm %s", tx.getResult());
			File f = new File(tx.getResult());
			pd.redirectOutput(f);
			Process proc = pd.start();
			if(proc.waitFor(30L, TimeUnit.SECONDS)) {
				//leggere output e salvare su DB risultato
			}
			
			
		}
		catch (Exception e){
			log.debug(e.getMessage(),e.getCause());
			return new Outcome("ERROR");
		}
		log.info("Done ");
		return new Outcome(true);
	}
	
}
