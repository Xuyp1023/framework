package com.betterjr.modules.account.entity;

import com.betterjr.mapper.entity.ReferClass;

@ReferClass(CustOperatorInfo.class)
public class CustOperatorInfoRequest implements java.io.Serializable {

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
     *
     * 操作员密码
     */
    private String password;
    /**
     * 默认经办人；1默认经办人，0非默认经办人
     */
    private String defOper;
    
    /***
     * 客户列表 
     */
    private String custList;

    public CustOperatorInfoRequest() {

    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDefOper() {
        return defOper;
    }

    public void setDefOper(final String defOper) {
        this.defOper = defOper;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(final String sex) {
        this.sex = sex;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(final String faxNo) {
        this.faxNo = faxNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(final String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getIdentClass() {
        return identClass;
    }

    public void setIdentClass(final String identClass) {
        this.identClass = identClass;
    }

    public String getTransBusin() {
        return transBusin;
    }

    public void setTransBusin(final String transBusin) {
        this.transBusin = transBusin;
    }

    public String getRevokeBusin() {
        return revokeBusin;
    }

    public void setRevokeBusin(final String revokeBusin) {
        this.revokeBusin = revokeBusin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(final String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate;
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(final String modiDate) {
        this.modiDate = modiDate;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(final String operCode) {
        this.operCode = operCode;
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(final String operOrg) {
        this.operOrg = operOrg;
    }

    public String getRuleList() {
        return ruleList;
    }

    public void setRuleList(final String ruleList) {
        this.ruleList = ruleList;
    }

    public String getContIdentType() {
        return contIdentType;
    }

    public void setContIdentType(final String contIdentType) {
        this.contIdentType = contIdentType;
    }

    public String getContIdentNo() {
        return contIdentNo;
    }

    public void setContIdentNo(final String contIdentNo) {
        this.contIdentNo = contIdentNo;
    }

    public String getContCertValidDate() {
        return contCertValidDate;
    }

    public void setContCertValidDate(final String contCertValidDate) {
        this.contCertValidDate = contCertValidDate;
    }

    public Long getContactorSerial() {
        return contactorSerial;
    }

    public void setContactorSerial(final Long contactorSerial) {
        this.contactorSerial = contactorSerial;
    }

    public String getCustList() {
        return this.custList;
    }

    public void setCustList(String anCustList) {
        this.custList = anCustList;
    }

    @Override
    public String toString() {
        return "CustOperatorInfoRequest [id=" + id + ", contIdentType=" + contIdentType + ", name=" + name + ", contIdentNo=" + contIdentNo
                + ", contCertValidDate=" + contCertValidDate + ", sex=" + sex + ", zipCode=" + zipCode + ", address=" + address + ", phone=" + phone
                + ", faxNo=" + faxNo + ", mobileNo=" + mobileNo + ", email=" + email + ", identClass=" + identClass + ", transBusin=" + transBusin
                + ", revokeBusin=" + revokeBusin + ", status=" + status + ", lastStatus=" + lastStatus + ", regDate=" + regDate + ", modiDate="
                + modiDate + ", operCode=" + operCode + ", operOrg=" + operOrg + ", contactorSerial=" + contactorSerial + ", ruleList=" + ruleList+ ", custList=" + custList
                + "]";
    }

    public void initAdminOperator(final String anOperOrg, final String anName, final String anPassword) {
        this.operOrg = anOperOrg;
        this.ruleList = "SUPPLIER_USER,SELLER_USER,CORE_USER,FACTOR_USER,PLATFORM_USER";
        this.operCode = "admin";
        this.password = anPassword;
        this.name = anName;
        this.defOper = "1";
    }

}
