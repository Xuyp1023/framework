package com.betterjr.modules.wechat.data;

import java.io.Serializable;

/**
 * 微信公众号信息
 *
 * @author zhoucy
 */
public class MPAccount implements Serializable {

    /**
     * 公众号原始ID
     */
    private String mpId;

    /**
     * 公众号昵称
     */
    private String nickName;

    /**
     * 应用Id
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 令牌
     */
    private String token;

    /**
     * AES安全加密密钥
     */
    private String AESKey;

    /**
     * 公众号类型
     * D:订阅号
     * E:企业号
     * S:服务号
     */
    private String mpType;

    /**
     * 是否认证
     */
    private boolean pass;

    public String getMpId() {
        return mpId;
    }

    public void setMpId(final String mpId) {
        this.mpId = mpId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(final String nickName) {
        this.nickName = nickName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(final String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(final String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getAESKey() {
        return AESKey;
    }

    public void setAESKey(final String aESKey) {
        AESKey = aESKey;
    }

    public String getMpType() {
        return mpType;
    }

    public void setMpType(final String mpType) {
        this.mpType = mpType;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(final boolean pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "MPAccount [mpId="
                + mpId
                + ", nickName="
                + nickName
                + ", appId="
                + appId
                + ", appSecret="
                + appSecret
                + ", token="
                + token
                + ", AESKey="
                + AESKey
                + ", mpType="
                + mpType
                + ", pass="
                + pass
                + "]";
    }
}
