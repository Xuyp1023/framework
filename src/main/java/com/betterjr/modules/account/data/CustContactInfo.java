package com.betterjr.modules.account.data;

/**
 * 操作员信息， 需要通过拜特资金管理系统传到平台
 * 
 * @author zhoucy
 *
 */
public class CustContactInfo implements java.io.Serializable {

    private static final long serialVersionUID = -5753311961672334535L;

    /**
     * 经办人名字
     */
    private String name;

    /**
     * 证件类型
     */
    private String identType;

    /**
     * 证件号码
     */
    private String identNo;

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
     * 用户编码
     */
    private String operCode;

    public CustContactInfo() {

    }

    public CustContactInfo(String anName, String anCode) {
        this.name = anName;
        this.operCode = anCode;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String anOperCode) {
        operCode = anOperCode;
    }

    /**
     * 默认经办人；1默认经办人，0非默认经办人
     */
    private Boolean defOper;

    public String getName() {
        return name;
    }

    public void setName(String anName) {
        name = anName;
    }

    public String getIdentType() {
        return identType;
    }

    public void setIdentType(String anIdentType) {
        identType = anIdentType;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String anIdentNo) {
        identNo = anIdentNo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String anZipCode) {
        zipCode = anZipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String anAddress) {
        address = anAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String anPhone) {
        phone = anPhone;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String anFaxNo) {
        faxNo = anFaxNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String anMobileNo) {
        mobileNo = anMobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String anEmail) {
        email = anEmail;
    }

    public Boolean getDefOper() {
        return defOper;
    }

    public void setDefOper(Boolean anDefOper) {
        defOper = anDefOper;
    }

}