package com.dama.cerbero.responses;

import java.io.Serializable;

public class SolanaResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String jsonrpc;
	String result;
	Integer id;
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "SolanaResponse [jsonrpc=" + jsonrpc + ", result=" + result + ", id=" + id + "]";
	}
	
	

}
