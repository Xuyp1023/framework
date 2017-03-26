package com.betterjr.modules.sys.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class BetterjrSsoToken extends UsernamePasswordToken {
    /** 描述 */
    private static final long serialVersionUID = -3178260335127476542L;

    private final String ipAddress;
    private final String ticket;
    private final String custRole;
    private final String corpId;

    public String getTicket() {
        return ticket;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getCustRole() {
        return custRole;
    }

    public String getCorpId() {
        return corpId;
    }

    public BetterjrSsoToken() {
        this.ticket = "";
        this.ipAddress = "";
        this.custRole = "";
        this.corpId = "";
    }

    public BetterjrSsoToken(final String anTicket, final String anCorpId, final String anCustRole, final String anUsername, final String anPassword, final String anHost) {
        super(anUsername, anPassword, false, anHost);
        this.corpId = anCorpId;
        this.ticket = anTicket;
        this.ipAddress = anHost;
        this.custRole = anCustRole;
    }
}
