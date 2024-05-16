package com.dama.cerbero.requests;

import java.io.Serializable;

public class SendEmailRequest implements Serializable {
    
        private static final long serialVersionUID = 101L;
        
        String email;
        String subject;
        String message;
    
        public String getEmail() {
            return email;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }

        public String getSubject() {
            return subject;
        }
    
        public void setSubject(String subject) {
            this.subject = subject;
        }
        
        public String getMessage() {
            return message;
        }
    
        public void setMessage(String message) {
            this.message = message;
        }
}
