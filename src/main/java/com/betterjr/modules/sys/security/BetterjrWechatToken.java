package com.betterjr.modules.sys.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class BetterjrWechatToken  extends UsernamePasswordToken {
    /** 描述 */
    private static final long serialVersionUID = -3178260335127476542L;

    private final String ipAddress;
     private final String ticket;

    public String getTicket() {
        return ticket;
    } 
 
    public BetterjrWechatToken() {
        this.ticket = "";
        this.ipAddress = "";
    }

    public BetterjrWechatToken(String anTicket, String anUsername, String anPassword, String anHost) {
        super(anUsername, anPassword, false, anHost);
        this.ticket = anTicket;
        this.ipAddress = anHost; 
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
