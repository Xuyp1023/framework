package com.betterjr.modules.account.data;

import com.betterjr.mapper.entity.ReferClass;
import com.betterjr.modules.account.entity.CustOperatorInfo;

@ReferClass(CustOperatorInfo.class)
public class CustOptData implements java.io.Serializable {

    private static final long serialVersionUID = -4451083897668047617L;

    /**
     * 经办人编号
     */
    private Long id;

    private String contIdentType;
    /**
     * 经办人名字
     */
    private String name;

    /**
     * 证件号码
     */
    private String contIdentNo;

    /**
     * 证件有效期
     */
    private String contCertValidDate;

    /**
     * 性别
     */
    private String sex;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 通讯地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 传真号码
     */
    private String faxNo;

    /**
     * 手机号码
     */
    private String mobileNo;

    /**
     * Email地址
     */
    private String email;

    /**
     * 经办人识别方式(1-书面委托 2-印鉴 3-密码 4-证件)
     */
    private String identClass;

    /**
     * 允许办理的业务；业务代码使用逗号分隔；如果没有表示全部
     */
    private String transBusin;

    /**
     * 允许撤单的业务；业务代码使用逗号分隔；如果没有表示全部
     */
    private String revokeBusin;

    /**
     * 默认经办人；1默认经办人，0非默认经办人
     */

    /**
     * 经办人状态；1正常，2暂停业务，3注销
     */
    private String status;

    /**
     * 经办人上次状态；1正常，2暂停业务，3注销
     */
    private String lastStatus;

    /**
     * 登记日期
     */
    private String regDate;

    /**
     * 修改日期
     */
    private String modiDate;

    /**
     * 操作员编码
     */
    private String operCode;

    /**
     * 操作员所在机构，如果证书登录，则
     */
    private String operOrg;

    /**
     * 经办人编号序列号，用于大成基金
     */
    private Long contactorSerial;

    /**
     * 经办人编号序列号，用于大成基金
     */
    private String ruleList;

    /**
     * 默认经办人；1默认经办人，0非默认经办人
     */
    private String defOper;

    public CustOptData() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDefOper() {
        return defOper;
    }

    public void setDefOper(String defOper) {
        this.defOper = defOper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentClass() {
        return identClass;
    }

    public void setIdentClass(String identClass) {
        this.identClass = identClass;
    }

    public String getTransBusin() {
        return transBusin;
    }

    public void setTransBusin(String transBusin) {
        this.transBusin = transBusin;
    }

    public String getRevokeBusin() {
        return revokeBusin;
    }

    public void setRevokeBusin(String revokeBusin) {
        this.revokeBusin = revokeBusin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg;
    }

    public String getRuleList() {
        return ruleList;
    }

    public void setRuleList(String ruleList) {
        this.ruleList = ruleList;
    }

    public String getContIdentType() {
        return contIdentType;
    }

    public void setContIdentType(String contIdentType) {
        this.contIdentType = contIdentType;
    }

    public String getContIdentNo() {
        return contIdentNo;
    }

    public void setContIdentNo(String contIdentNo) {
        this.contIdentNo = contIdentNo;
    }

    public String getContCertValidDate() {
        return contCertValidDate;
    }

    public void setContCertValidDate(String contCertValidDate) {
        this.contCertValidDate = contCertValidDate;
    }

    public Long getContactorSerial() {
        return contactorSerial;
    }

    public void setContactorSerial(Long contactorSerial) {
        this.contactorSerial = contactorSerial;
    }

    @Override
    public String toString() {
        return "CustOperatorInfoRequest [id=" + id + ", contIdentType=" + contIdentType + ", name=" + name + ", contIdentNo=" + contIdentNo
                + ", contCertValidDate=" + contCertValidDate + ", sex=" + sex + ", zipCode=" + zipCode + ", address=" + address + ", phone=" + phone
                + ", faxNo=" + faxNo + ", mobileNo=" + mobileNo + ", email=" + email + ", identClass=" + identClass + ", transBusin=" + transBusin
                + ", revokeBusin=" + revokeBusin + ", status=" + status + ", lastStatus=" + lastStatus + ", regDate=" + regDate + ", modiDate="
                + modiDate + ", operCode=" + operCode + ", operOrg=" + operOrg + ", contactorSerial=" + contactorSerial + ", ruleList=" + ruleList
                + "]";
    }

}
