package com.betterjr.modules.wechat.data.api;

import com.betterjr.common.utils.BetterStringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信JSSDK凭证
 * 
 * @author zhoucy
 */
public class JSTicket {

    /**
     * 调用微信JS接口的临时票据
     */
    private String ticket;

    /**
     * 凭证有效时间,单位:秒
     */
    @JsonProperty(value = "expires_in")
    private long expiresIn;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = System.currentTimeMillis() + (expiresIn - 60) * 1000;
    }

    public boolean isAvailable() {
        if (!BetterStringUtils.isEmpty(ticket) && this.expiresIn >= System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "JSTicket [ticket=" + ticket + ", expiresIn=" + expiresIn + "]";
    }

}
