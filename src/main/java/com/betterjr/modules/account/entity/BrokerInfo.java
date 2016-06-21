package com.betterjr.modules.account.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_BROKER")
public class BrokerInfo implements BetterjrEntity {
    /**
     * 经纪人编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="经纪人编号", comments = "经纪人编号")
    private Integer id;

    /**
     * 名字
     */
    @Column(name = "C_NAME",  columnDefinition="VARCHAR" )
    @MetaData( value="名字", comments = "名字")
    private String name;

    /**
     * 邮政编码
     */
    @Column(name = "C_ZIPCODE",  columnDefinition="VARCHAR" )
    @MetaData( value="邮政编码", comments = "邮政编码")
    private String zipCode;

    /**
     * 通讯地址
     */
    @Column(name = "C_ADDRESS",  columnDefinition="VARCHAR" )
    @MetaData( value="通讯地址", comments = "通讯地址")
    private String address;

    /**
     * 联系电话
     */
    @Column(name = "C_PHONE",  columnDefinition="VARCHAR" )
    @MetaData( value="联系电话", comments = "联系电话")
    private String phone;

    /**
     * 传真号码
     */
    @Column(name = "C_FAXNO",  columnDefinition="VARCHAR" )
    @MetaData( value="传真号码", comments = "传真号码")
    private String faxNo;

    /**
     * 手机号码
     */
    @Column(name = "C_MOBILENO",  columnDefinition="VARCHAR" )
    @MetaData( value="手机号码", comments = "手机号码")
    private String mobileNo;

    /**
     * Email地址
     */
    @Column(name = "C_EMAIL",  columnDefinition="VARCHAR" )
    @MetaData( value="Email地址", comments = "Email地址")
    private String email;

    /**
     * 经纪人状态
     */
    @Column(name = "C_STATUS",  columnDefinition="VARCHAR" )
    @MetaData( value="经纪人状态", comments = "经纪人状态")
    private String status;

    /**
     * 经纪人类型：0客户经理，1理财经理，2推荐人
     */
    @Column(name = "C_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="经纪人类型：0客户经理", comments = "经纪人类型：0客户经理，1理财经理，2推荐人")
    private String brokerType;

    /**
     * 注册日期
     */
    @Column(name = "D_REGDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="注册日期", comments = "注册日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 微信
     */
    @Column(name = "C_WEIXIN",  columnDefinition="VARCHAR" )
    @MetaData( value="微信", comments = "微信")
    private String weiXin;

    /**
     * QQ号码
     */
    @Column(name = "C_QQ",  columnDefinition="VARCHAR" )
    @MetaData( value="QQ号码", comments = "QQ号码")
    private String qq;

    private static final long serialVersionUID = 1440667936347L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(String brokerType) {
        this.brokerType = brokerType == null ? null : brokerType.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", zipCode=").append(zipCode);
        sb.append(", address=").append(address);
        sb.append(", phone=").append(phone);
        sb.append(", faxNo=").append(faxNo);
        sb.append(", mobileNo=").append(mobileNo);
        sb.append(", email=").append(email);
        sb.append(", status=").append(status);
        sb.append(", brokerType=").append(brokerType);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", weiXin=").append(weiXin);
        sb.append(", qq=").append(qq);
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
        BrokerInfo other = (BrokerInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getZipCode() == null ? other.getZipCode() == null : this.getZipCode().equals(other.getZipCode()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getFaxNo() == null ? other.getFaxNo() == null : this.getFaxNo().equals(other.getFaxNo()))
            && (this.getMobileNo() == null ? other.getMobileNo() == null : this.getMobileNo().equals(other.getMobileNo()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getBrokerType() == null ? other.getBrokerType() == null : this.getBrokerType().equals(other.getBrokerType()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getWeiXin() == null ? other.getWeiXin() == null : this.getWeiXin().equals(other.getWeiXin()))
            && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getZipCode() == null) ? 0 : getZipCode().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getFaxNo() == null) ? 0 : getFaxNo().hashCode());
        result = prime * result + ((getMobileNo() == null) ? 0 : getMobileNo().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getBrokerType() == null) ? 0 : getBrokerType().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getWeiXin() == null) ? 0 : getWeiXin().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        return result;
    }
}