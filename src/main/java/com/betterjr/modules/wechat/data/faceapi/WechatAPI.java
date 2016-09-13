package com.betterjr.modules.wechat.data.faceapi;

/**
 * 微信公众平台所有API汇集
 * 
 * @author zhoucy
 */
public interface WechatAPI extends CredentialAPI, MenuAPI, MediaAPI, GroupsAPI, QRCodeAPI, UserAPI,
        TemplateAPI, MessageAPI {

    /**
     * 微信公众平台API入口
     */
    String wechatAPI = "https://api.weixin.qq.com";

    String cgi_bin = wechatAPI + "/cgi-bin";
}
