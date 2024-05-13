package com.dama.cerbero.requests;

import java.io.Serializable;

public class ConnectRequest implements Serializable {
    
        private static final long serialVersionUID = 101L;
        
        String adapter;
    
        public String getAdapter() {
            return adapter;
        }
    
        public void setAdapter(String adapter) {
            this.adapter = adapter;
        }
        
}
