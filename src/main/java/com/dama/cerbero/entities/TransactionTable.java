package com.dama.cerbero.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.dama.cerbero.requests.Transaction;
import com.dama.cerbero.responses.SolanaResponse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
	@Table(name = "TransactionInteraction")
	public class TransactionTable {
	
	public TransactionTable () {}
	
		public TransactionTable(Transaction t, SolanaResponse s, String network, String service) {
			this.transaction = t.getParams().get(0);
			this.ts = System.currentTimeMillis();
			this.status = "";
			this.txId = s.getResult();
			this.network = network;
			this.service = service;
		}
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_id_seq")
	    private Long id;
	    
	    private String transaction;
	    
	    private String txId;
	    
	    private String status;

	    private Long ts;

		private String network;

		private String service;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTransaction() {
			return transaction;
		}

		public void setTransaction(String transaction) {
			this.transaction = transaction;
		}


		public String getTxId() {
			return txId;
		}

		public void setTxId(String txId) {
			this.txId = txId;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Long getTs() {
			return ts;
		}

		public void setTs(Long ts) {
			this.ts = ts;
		}

		public String getNetwork() {
			return this.network;
		}

		public void setNetwork(String network) {
			this.network = network;
		}

		public String getService() {
			return this.service;
		}

		public void setService(String service) {
			this.service = service;
		}

		@Override
		public String toString() {
			return "TransactionInteraction [id=" + id + ", transaction=" + transaction + ", txId=" + txId + ", status="
					+ status + ", ts=" + ts + ", network=" + network + ", service=" + service + "]";
		}
	    
	    // standard constructors / setters / getters / toString


		
	
}
