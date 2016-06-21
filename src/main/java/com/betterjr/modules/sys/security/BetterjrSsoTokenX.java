package com.betterjr.modules.sys.security;

import org.apache.shiro.authc.RememberMeAuthenticationToken;

public class BetterjrSsoTokenX implements RememberMeAuthenticationToken {

    private static final long serialVersionUID = 8587329689973009598L;

    // the service ticket returned by the CAS server
    private final String ticket;
 
    // the user identifier
    private String userId = null;

    // is the user in a remember me mode ?
    private boolean isRememberMe = false;

    public BetterjrSsoTokenX(String anTicket) {
        this.ticket = anTicket;
    }

    public Object getPrincipal() {
        return userId;
    }

    public Object getCredentials() {
        return ticket;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRememberMe() {
        return isRememberMe;
    }

    public void setRememberMe(boolean isRememberMe) {
        this.isRememberMe = isRememberMe;
    }
}
