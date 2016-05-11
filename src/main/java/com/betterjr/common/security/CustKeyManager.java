package com.betterjr.common.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.utils.BetterStringUtils;

import java.security.cert.Certificate;

/**
 * 
 * @author zhoucy 客户证书信息
 */
public class CustKeyManager {

    private static Logger logger = LoggerFactory.getLogger(CustKeyManager.class);

    /**
     * 私钥证书
     */
    private PrivateKey privKey;
    /**
     * 证书公钥
     */
    private Certificate pubKey;

    /**
     * 对方的公钥证书
     */
    private Certificate matchKey;

    private String workCharSet ="UTF-8";
    
    public void setMatchKey(Certificate anMatchKey) {
        this.matchKey = anMatchKey;
    }

    private PublicKey simpleMatchKey;

    private Map<String, Certificate> certMap = new ConcurrentHashMap<String, Certificate>();

    public CustKeyManager(String anPrivKey, String anMatchKey) {
        if (BetterStringUtils.isNotBlank(anMatchKey)) {
            this.simpleMatchKey = KeyReader.readPublicKey(anMatchKey);
        }
        this.privKey = KeyReader.readPrivateKey(anPrivKey);
    }

    public CustKeyManager(String anPrivKey, String anPassword, String anMatchKey) {
        Map<PrivateKey, Certificate> map = KeyReader.readKeyfromPKCS12StoredClassPath(anPrivKey, anPassword);
        for (Map.Entry<PrivateKey, Certificate> ent : map.entrySet()) {
            this.privKey = ent.getKey();
            this.pubKey = ent.getValue();
        }
        if (BetterStringUtils.isNotBlank(anMatchKey)) {
            this.matchKey = KeyReader.fromCerStoredClassPath(anMatchKey);
        }
    }

    public CustKeyManager(PrivateKey anPrivKey, Certificate anPubKey, Certificate anMatchKey) {
        this.privKey = anPrivKey;
        this.pubKey = anPubKey;
        this.matchKey = anMatchKey;
    }

    // 克隆一个证书管理服务
    public CustKeyManager(CustKeyManager anCustKey, Certificate anMatchKey) {
        this.privKey = anCustKey.privKey;
        this.pubKey = anCustKey.pubKey;
        this.matchKey = anMatchKey;
    }

    /**
     * 
     * 增加公钥数字证书
     * 
     * @param 公钥证书关联的KEY
     * @param1 数字证书文件
     * @return 出参说明，结果条件
     * @throws 异常情况
     */
    public void addCertificate(String anToken, String anMatchKey) {
        if (BetterStringUtils.isNotBlank(anMatchKey) && BetterStringUtils.isNotBlank(anToken)) {
            Certificate tmpC = KeyReader.fromCerStoredFile(anMatchKey);
            this.certMap.put(anToken, tmpC);
        }
    }

    /**
     * 
     * 使用对方的公钥验证签名信息
     * 
     * @param 原始内容
     * @param 签名信息
     * @return 验证成功返回True, 否则返回False
     * @throws 异常情况
     */
    public boolean verifySign(String anContent, String anSign) {

        return SignHelper.verifySign(anContent, anSign, this.getMatchKey(), workCharSet);
    }

    /**
     * 
     * 使用对方的公钥验证签名信息
     * 
     * @param 原始内容
     * @param 签名信息
     * @return 验证成功返回True, 否则返回False
     * @throws 异常情况
     */
    public boolean verifySign(String anContent, String anSign, PublicKey anPubKey) {

        return SignHelper.verifySign(anContent, anSign, anPubKey, this.workCharSet);
    }

    /**
     * 
     * 根据提供的证书管理Key验证内容
     * 
     * @param 需要验证的内容
     * @param1 需要验证的签名信息
     * @return 出参说明，结果条件
     * @throws 异常情况
     */
    public boolean verifySign(String anContent, String anSign, String anToken) {
        if (BetterStringUtils.isNotBlank(anToken)) {
            Certificate tmpC = this.certMap.get(anToken);
            if (tmpC != null) {
                return SignHelper.verifySign(anContent, anSign, tmpC, this.workCharSet);
            }
        }
        logger.warn("in function verifySign provide token = " + anToken + ", not find in Certificate List");

        return false;
    }

    /**
     * 
     * 使用私钥签名数据
     * 
     * @param 需要签名的字符串
     * @return 签名结果
     * @throws 异常情况
     */
    public String signData(String anData) {

        return SignHelper.signData(anData, getPrivKey());
    }

    /**
     * 
     * 使用对方的公钥加密数据
     * 
     * @param 需要加密的数据
     * @return 加密结果数据，已经使用Base64编码
     * @throws 异常情况
     */
    public String encrypt(String anData) {

        return SignHelper.encrypt(anData, this.getMatchKey(), workCharSet);
    }

    public String encrypt(String anData, String anToken) {
        if (BetterStringUtils.isNotBlank(anToken)) {
            Certificate tmpC = this.certMap.get(anToken);
            if (tmpC != null) {
                return SignHelper.encrypt(anData, tmpC.getPublicKey(), workCharSet);
            }
        }
        logger.warn("in function encrypt, provide token = " + anToken + ", not find in Certificate List");
        return "";
    }

    /**
     * 
     * 解密数据
     * 
     * @param 需要解密的数据
     * @return 解密结果数据
     * @throws 异常情况
     */
    public String decrypt(String anData) {

        return SignHelper.decrypt(anData, getPrivKey());
    }

    public PrivateKey getPrivKey() {

        return privKey;
    }

    public void setPubKey(Certificate anPubKey) {
        this.pubKey = anPubKey;
    }

    public PublicKey getPubKey() {
        return pubKey.getPublicKey();
    }

    public PublicKey getMatchKey(String anToken) {
        Certificate tmpC = this.certMap.get(anToken);
        if (tmpC != null) {
            return tmpC.getPublicKey();
        }

        return null;
    }

    public PublicKey getMatchKey() {
        if (this.simpleMatchKey == null) {

            return matchKey.getPublicKey();
        }
        else {
            return this.simpleMatchKey;
        }
    }

    public String getWorkCharSet() {
        return this.workCharSet;
    }

    public void setWorkCharSet(String anWorkCharSet) {
        this.workCharSet = anWorkCharSet;
    }
    
    
}
