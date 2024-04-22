package com.dama.cerbero.requests;

import java.io.Serializable;
import java.util.ArrayList;

public class Transaction implements Serializable {
	
	private static final long serialVersionUID = -4827851848871337274L;
	String jsonrpc;
	Integer id;
	String method;
	ArrayList<String> params;
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public ArrayList<String> getParams() {
		return params;
	}
	public void setParams(ArrayList<String> params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return "Transaction [jsonrpc=" + jsonrpc + ", id=" + id + ", method=" + method + ", params=" + params + "]";
	}
	
	
	

	
}
