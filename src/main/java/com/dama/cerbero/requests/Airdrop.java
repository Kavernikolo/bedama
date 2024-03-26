package com.dama.cerbero.requests;

import java.io.Serializable;

public class Airdrop implements Serializable{

	private static final long serialVersionUID = 210387305501728139L;

	private String url, address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Airdrop [url=" + url + ", address=" + address + "]";
	}
	
	
	
}
