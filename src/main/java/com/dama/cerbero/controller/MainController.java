package com.dama.cerbero.controller;

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

import com.dama.cerbero.entities.Customer;
import com.dama.cerbero.entities.interfaces.CustomerRepository;
import com.dama.cerbero.requests.Airdrop;

@RestController
public class MainController {
	  
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
    
	@Autowired
    CustomerRepository userRepository;

	private static final String template = "Hello, %s!";

	@GetMapping("/greeting")
	public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format(template, name);
	}
	
	@PostMapping(path = "/enrol", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Boolean enrol(@RequestBody Airdrop airdrop){
		
		try { 
			userRepository.save(new Customer(airdrop));
		}
		catch (Exception e){
			log.error("Exception occurred while trying to save customer "+airdrop);
			return false;
		}
		log.info("Correctly saved customer "+airdrop);
		return true;
	}
	
}
