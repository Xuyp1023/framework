/**
 * 
 */
package com.betterjr.modules.remote.entity;

import java.util.List;
import java.util.Map;

import com.betterjr.common.data.BaseRemoteEntity;

/**
 * @author hewei
 *
 */
public class SignRequestInfo implements BaseRemoteEntity{

    //用户id
    private String custNo;
    
    private String orgName;
    private String orgEmail;
    private String orgTel;
    private String userName;
    private String userPhone;
    private String userEmail;
    //base64，签章图片
    private String stampbase;
    //订单号
    private String requestNo;
    //签署订单id
    private String saleRequestNo;
    //验证码
    private String vcode;
    //sign template id
    private String signTempId;
    //sign subject
    private String signSubject;
    //sign password
    private String signPassword;
    //signer ip
    private String signerIp;
    //signed file content,base64
    private String signedContent;
    //signed file hash(sha2)
    private String signedContentHash;
    //status
    private String status;
    //error code
    private String errorCode;
    //error remark;
    private String remark;
    
    //signer List
    private List<Map<String,Object>> signerList;
    
    public String getCustNo() {
        return this.custNo;
    }
    public void setCustNo(String anCustNo) {
        this.custNo = anCustNo;
    }
    public String getOrgName() {
        return this.orgName;
    }
    public void setOrgName(String anOrgName) {
        this.orgName = anOrgName;
    }
    public String getOrgEmail() {
        return this.orgEmail;
    }
    public void setOrgEmail(String anOrgEmail) {
        this.orgEmail = anOrgEmail;
    }
    public String getOrgTel() {
        return this.orgTel;
    }
    public void setOrgTel(String anOrgTel) {
        this.orgTel = anOrgTel;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String anUserName) {
        this.userName = anUserName;
    }
    public String getUserPhone() {
        return this.userPhone;
    }
    public void setUserPhone(String anUserPhone) {
        this.userPhone = anUserPhone;
    }
    public String getUserEmail() {
        return this.userEmail;
    }
    public void setUserEmail(String anUserEmail) {
        this.userEmail = anUserEmail;
    }
    public String getStampbase() {
        return this.stampbase;
    }
    public void setStampbase(String anStampbase) {
        this.stampbase = anStampbase;
    }
    public String getRequestNo() {
        return this.requestNo;
    }
    public void setRequestNo(String anRequestNo) {
        this.requestNo = anRequestNo;
    }
    public String getSaleRequestNo() {
        return this.saleRequestNo;
    }
    public void setSaleRequestNo(String anSaleRequestNo) {
        this.saleRequestNo = anSaleRequestNo;
    }
    public String getVcode() {
        return this.vcode;
    }
    public void setVcode(String anVcode) {
        this.vcode = anVcode;
    }
    public String getSignTempId() {
        return this.signTempId;
    }
    public void setSignTempId(String anSignTempId) {
        this.signTempId = anSignTempId;
    }
    public String getSignSubject() {
        return this.signSubject;
    }
    public void setSignSubject(String anSignSubject) {
        this.signSubject = anSignSubject;
    }
    public String getSignPassword() {
        return this.signPassword;
    }
    public void setSignPassword(String anSignPassword) {
        this.signPassword = anSignPassword;
    }
    public String getSignerIp() {
        return this.signerIp;
    }
    public void setSignerIp(String anSignerIp) {
        this.signerIp = anSignerIp;
    }
    public String getSignedContent() {
        return this.signedContent;
    }
    public void setSignedContent(String anSignedContent) {
        this.signedContent = anSignedContent;
    }
    public String getSignedContentHash() {
        return this.signedContentHash;
    }
    public void setSignedContentHash(String anSignedContentHash) {
        this.signedContentHash = anSignedContentHash;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String anStatus) {
        this.status = anStatus;
    }
    public String getErrorCode() {
        return this.errorCode;
    }
    public void setErrorCode(String anErrorCode) {
        this.errorCode = anErrorCode;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String anRemark) {
        this.remark = anRemark;
    }
    public List<Map<String, Object>> getSignerList() {
        return this.signerList;
    }
    public void setSignerList(List<Map<String, Object>> anSignerList) {
        this.signerList = anSignerList;
    }

    
}
