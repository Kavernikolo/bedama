package com.dama.cerbero.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.dama.cerbero.requests.Airdrop;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
	@Table(name = "Subscriber")
	public class Subscriber {
	
	public Subscriber () {}
	
		public Subscriber(Airdrop a) {
			this.address = a.getAddress();
			this.sentdama = 0;
			this.ts = System.currentTimeMillis();
			this.url = a.getUrl();
		}
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_id_seq")
	    private Long id;
	    
	    private String address;
	    
	    private String url;

	    private Long ts;
	    
	    private Integer sentdama;
	    // standard constructors / setters / getters / toString


		@Override
		public String toString() {
			return "Customer [id=" + id + ", address=" + address + ", url=" + url + ", ts=" + ts + ", sentdama="
					+ sentdama + "]";
		}

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

		public Long getTs() {
			return ts;
		}

		public void setTs(Long ts) {
			this.ts = ts;
		}

		public Integer getSentdama() {
			return sentdama;
		}

		public void setSentdama(Integer sentdama) {
			this.sentdama = sentdama;
		}

		public Long getId() {
			return id;
		}
		
		
	
}
