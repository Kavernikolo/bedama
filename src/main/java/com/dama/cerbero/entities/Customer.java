package com.dama.cerbero.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.dama.cerbero.requests.Airdrop;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
	@Table(name = "Customers")
	public class Customer {
	
	public Customer () {}
	
		public Customer(Airdrop a) {
			this.address = a.getAddress();
			this.sentdama = 0;
			this.ts = System.currentTimeMillis();
			this.url = a.getUrl();
		}
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private long id;
	    
	    private String address;
	    
	    private String url;

	    private long ts;
	    
	    private int sentdama;
	    // standard constructors / setters / getters / toString

	    
		public String getAddress() {
			return address;
		}

		public long getId() {
			return id;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public long getTs() {
			return ts;
		}

		public void setTs(long ts) {
			this.ts = ts;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getSentdama() {
			return sentdama;
		}

		public void setSentdama(int sentdama) {
			this.sentdama = sentdama;
		}

		@Override
		public String toString() {
			return "Customer [id=" + id + ", address=" + address + ", url=" + url + ", ts=" + ts + ", sentdama="
					+ sentdama + "]";
		}
		
		
	
}
