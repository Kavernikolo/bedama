package com.dama.cerbero.responses;

import java.io.Serializable;

public class Outcome implements Serializable{

	private static final long serialVersionUID = -8383740320747275938L;
	
	private String outcome;
	
	public Outcome() {}
	
	public Outcome(boolean out) {
		this.outcome = out ? "OK" : "KO";
	}
	
	public Outcome(String out) {
		this.outcome = out;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	
	
	

}
