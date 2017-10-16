package com.betterjr.modules.account.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterDateUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_CONTACT")
public class CustContactInfo implements BetterjrEntity {
    /**
     * 客户编号
     */
    @Id
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 邮政编码
     */
    @Column(name = "C_ZIPCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "邮政编码", comments = "邮政编码")
    private String zipCode;

    /**
     * 城市地区代码
     */
    @Column(name = "C_CITYNO", columnDefinition = "VARCHAR")
    @MetaData(value = "城市地区代码", comments = "城市地区代码")
    private String cityNo;

    /**
     * 通讯地址
     */
    @Column(name = "C_ADDRESS", columnDefinition = "VARCHAR")
    @MetaData(value = "通讯地址", comments = "通讯地址")
    private String address;

    /**
     * 联系电话
     */
    @Column(name = "C_PHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "联系电话", comments = "联系电话")
    private String phone;

    /**
     * 传真号码
     */
    @Column(name = "C_FAXNO", columnDefinition = "VARCHAR")
    @MetaData(value = "传真号码", comments = "传真号码")
    private String faxNo;

    /**
     * 手机号码
     */
    @Column(name = "C_MOBILENO", columnDefinition = "VARCHAR")
    @MetaData(value = "手机号码", comments = "手机号码")
    private String mobileNo;

    /**
     * Email地址
     */
    @Column(name = "C_EMAIL", columnDefinition = "VARCHAR")
    @MetaData(value = "Email地址", comments = "Email地址")
    private String email;

    /**
     * 微信
     */
    @Column(name = "C_WEIXIN", columnDefinition = "VARCHAR")
    @MetaData(value = "微信", comments = "微信")
    private String weiXin;

    /**
     * QQ号码
     */
    @Column(name = "C_QQ", columnDefinition = "VARCHAR")
    @MetaData(value = "QQ号码", comments = "QQ号码")
    private String qq;

    /**
     * 网址
     */
    @Column(name = "C_WEBADDR", columnDefinition = "VARCHAR")
    @MetaData(value = "网址", comments = "网址")
    private String webAddr;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 家庭电话
     */
    @Column(name = "C_HOME_PHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "家庭电话", comments = "家庭电话")
    private String homePhone;

    /**
     * 单位电话号码
     */
    @Column(name = "C_OFFICE_PHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "单位电话号码", comments = "单位电话号码")
    private String officePhone;

    /**
     * 工作单位
     */
    @Column(name = "C_CORPNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "工作单位", comments = "工作单位")
    private String corpName;

    /**
     * 县/区代码
     */
    @Column(name = "C_COUNTY", columnDefinition = "VARCHAR")
    @MetaData(value = "县/区代码", comments = "县/区代码")
    private String county;

    private static final long serialVersionUID = 1440667936349L;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    public String getCityNo() {
        return cityNo;
    }

    public void setCityNo(String cityNo) {
        this.cityNo = cityNo == null ? null : cityNo.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo == null ? null : faxNo.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin == null ? null : weiXin.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getWebAddr() {
        return webAddr;
    }

    public void setWebAddr(String webAddr) {
        this.webAddr = webAddr == null ? null : webAddr.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone == null ? null : homePhone.trim();
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone == null ? null : officePhone.trim();
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName == null ? null : corpName.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", custNo=").append(custNo);
        sb.append(", zipCode=").append(zipCode);
        sb.append(", cityNo=").append(cityNo);
        sb.append(", address=").append(address);
        sb.append(", phone=").append(phone);
        sb.append(", faxNo=").append(faxNo);
        sb.append(", mobileNo=").append(mobileNo);
        sb.append(", email=").append(email);
        sb.append(", weiXin=").append(weiXin);
        sb.append(", qq=").append(qq);
        sb.append(", webAddr=").append(webAddr);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", homePhone=").append(homePhone);
        sb.append(", officePhone=").append(officePhone);
        sb.append(", corpName=").append(corpName);
        sb.append(", county=").append(county);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CustContactInfo other = (CustContactInfo) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getZipCode() == null ? other.getZipCode() == null
                        : this.getZipCode().equals(other.getZipCode()))
                && (this.getCityNo() == null ? other.getCityNo() == null : this.getCityNo().equals(other.getCityNo()))
                && (this.getAddress() == null ? other.getAddress() == null
                        : this.getAddress().equals(other.getAddress()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getFaxNo() == null ? other.getFaxNo() == null : this.getFaxNo().equals(other.getFaxNo()))
                && (this.getMobileNo() == null ? other.getMobileNo() == null
                        : this.getMobileNo().equals(other.getMobileNo()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getWeiXin() == null ? other.getWeiXin() == null : this.getWeiXin().equals(other.getWeiXin()))
                && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()))
                && (this.getWebAddr() == null ? other.getWebAddr() == null
                        : this.getWebAddr().equals(other.getWebAddr()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getHomePhone() == null ? other.getHomePhone() == null
                        : this.getHomePhone().equals(other.getHomePhone()))
                && (this.getOfficePhone() == null ? other.getOfficePhone() == null
                        : this.getOfficePhone().equals(other.getOfficePhone()))
                && (this.getCorpName() == null ? other.getCorpName() == null
                        : this.getCorpName().equals(other.getCorpName()))
                && (this.getCounty() == null ? other.getCounty() == null : this.getCounty().equals(other.getCounty()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getZipCode() == null) ? 0 : getZipCode().hashCode());
        result = prime * result + ((getCityNo() == null) ? 0 : getCityNo().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getFaxNo() == null) ? 0 : getFaxNo().hashCode());
        result = prime * result + ((getMobileNo() == null) ? 0 : getMobileNo().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getWeiXin() == null) ? 0 : getWeiXin().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        result = prime * result + ((getWebAddr() == null) ? 0 : getWebAddr().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getHomePhone() == null) ? 0 : getHomePhone().hashCode());
        result = prime * result + ((getOfficePhone() == null) ? 0 : getOfficePhone().hashCode());
        result = prime * result + ((getCorpName() == null) ? 0 : getCorpName().hashCode());
        result = prime * result + ((getCounty() == null) ? 0 : getCounty().hashCode());
        return result;
    }

    public CustContactInfo() {

    }

    public CustContactInfo(SaleAccoRequestInfo request) {
        BeanMapper.copy(request, this);
        this.setModiDate(BetterDateUtils.getNumDateTime());
    }
}