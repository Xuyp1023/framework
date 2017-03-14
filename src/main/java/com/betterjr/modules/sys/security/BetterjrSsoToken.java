package com.betterjr.modules.sys.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class BetterjrSsoToken extends UsernamePasswordToken {
    /** 描述 */
    private static final long serialVersionUID = -3178260335127476542L;

    private final String ipAddress;
    private final String ticket;

    public String getTicket() {
        return ticket;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public BetterjrSsoToken() {
        this.ticket = "";
        this.ipAddress = "";
    }

    public BetterjrSsoToken(final String anTicket, final String anUsername, final String anPassword, final String anHost) {
        super(anUsername, anPassword, false, anHost);
        this.ticket = anTicket;
        this.ipAddress = anHost;
    }
}
