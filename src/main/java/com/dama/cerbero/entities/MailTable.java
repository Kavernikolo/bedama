package com.dama.cerbero.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
	@Table(name = "Mail")
	public class MailTable {
	
	public MailTable () {}
	
		public MailTable(String email, String subject, String message) {
			this.email = email;
			this.ts = System.currentTimeMillis();
			this.status = "new";
			this.subject = subject;
			this.message = message;
		}
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_id_seq")
	    private Long id;
	    private String email;
	    private String subject;
	    private String message;
		private Long ts;
	    private String status;

		// standard constructors / setters / getters / toString

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String subject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
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

		public String getMessagek() {
			return this.message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public String toString() {
			return "TransactionInteraction [id=" + id + ", email=" + email + ", subject=" + subject + ", status="
					+ status + ", ts=" + ts + ", message=" + message + "]";
		}
	    
	    


		
	
}
