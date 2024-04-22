package com.dama.cerbero.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.dama.cerbero.requests.Transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
	@Table(name = "TransactionInteraction")
	public class TransactionInteraction {
	
	public TransactionInteraction () {}
	
		public TransactionInteraction(Transaction t) {
			this.transaction = t.getParams().get(0);
			this.ts = System.currentTimeMillis();
			this.status = "";
			this.txId = "";
		}
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_id_seq")
	    private Long id;
	    
	    private String transaction;
	    
	    private String txId;
	    
	    private String status;

	    private Long ts;

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

		@Override
		public String toString() {
			return "TransactionInteraction [id=" + id + ", transaction=" + transaction + ", txId=" + txId + ", status="
					+ status + ", ts=" + ts + "]";
		}
	    
	    // standard constructors / setters / getters / toString


		
	
}
