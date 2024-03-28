package com.dama.cerbero.requests;

import java.io.Serializable;

public class Wallet implements Serializable{

	private static final long serialVersionUID = 5222499429271875897L;
	
	String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
