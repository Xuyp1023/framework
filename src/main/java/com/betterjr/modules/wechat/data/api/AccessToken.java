package com.betterjr.modules.wechat.data.api;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信API凭证
 *
 * @author zhoucy
 */
public class AccessToken implements Serializable {

    /**
     * 获取到的凭证
     */
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * 凭证有效时间,单位:秒
     */
    @JsonProperty(value = "expires_in")
    private long expiresIn;

    @JsonIgnore
    private long orginExpireSec;

    @JsonProperty(value = "openid")
    private String openId;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    private String scope;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(final long expiresIn) {
        // 考虑到服务器时间同步,故将刷新时间提前60秒.
        this.orginExpireSec = expiresIn;
        this.expiresIn = System.currentTimeMillis() + (expiresIn - 60) * 1000;
    }

    public long getOrginExpireSec() {
        return orginExpireSec;
    }

    public boolean isAvailable() {
        if (! StringUtils.isEmpty(accessToken) && this.expiresIn >= System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(final String anOpenId) {
        this.openId = anOpenId;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(final String anRefreshToken) {
        this.refreshToken = anRefreshToken;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(final String anScope) {
        this.scope = anScope;
    }

    @Override
    public String toString() {
        return "AccessToken [accessToken=" + accessToken + ", expiresIn=" + expiresIn + "]";
    }
}
