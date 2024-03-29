package com.dama.cerbero.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dama.cerbero.entities.Subscriber;
import com.dama.cerbero.entities.interfaces.SubscriberRepository;
import com.dama.cerbero.requests.Airdrop;
import com.dama.cerbero.requests.Wallet;
import com.dama.cerbero.responses.Outcome;

@RestController
public class MainController {
	  
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
    
	@Autowired
    SubscriberRepository userRepository;

	private static final String template = "Hello, %s!";

	@GetMapping("/greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format(template, name);
	}
	
	@PostMapping(path = "/enrol", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome enrol(@RequestBody Airdrop airdrop){
		
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
	
	@GetMapping(path = "/check", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Outcome check(@RequestBody Wallet wallet){
		
		try { 
			List<Subscriber> subscribers = userRepository.findByAddress(wallet.getAddress());
			if(subscribers.isEmpty()) {
				if(userRepository.count() < 1000) {
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
	
}
