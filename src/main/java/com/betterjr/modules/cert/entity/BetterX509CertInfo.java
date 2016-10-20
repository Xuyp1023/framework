package com.betterjr.modules.cert.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.mapper.CustTimeJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_cfg_certinfo")
public class BetterX509CertInfo implements BetterjrEntity {

    /**
     * 编号
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="编号", comments = "编号")
    private Long id;

    /**
     * 年限
     */
    @Column(name = "n_year",  columnDefinition="INTEGER" )
    @MetaData( value="年限", comments = "年限")
    private Integer year;

    /**
     * 证书名称
     */
    @Column(name = "c_name",  columnDefinition="VARCHAR" )
    @MetaData( value="证书名称", comments = "证书名称")
    private String commName;

    /**
     * 数字证书签发者
     */
    @Column(name = "c_publisher",  columnDefinition="VARCHAR" )
    @MetaData( value="数字证书签发者", comments = "数字证书签发者")
    private String signer;

    /**
     * 数字证书类型； 0 ：根证书，1：中级证书，2：SSL证书，3：最终用户证书
     */
    @Column(name = "c_cert_type",  columnDefinition="VARCHAR" )
    @MetaData( value="数字证书类型", comments = "数字证书类型； 0 ：根证书，1：中级证书，2：SSL证书，3：最终用户证书")
    private String certType;

    /**
     * 证书别名
     */
    @Column(name = "c_alias",  columnDefinition="VARCHAR" )
    @MetaData( value="证书别名", comments = "证书别名")
    private String certAlias;

    /**
     * 证书序列号
     */
    @Column(name = "c_serialno",  columnDefinition="VARCHAR" )
    @MetaData( value="证书序列号", comments = "证书序列号")
    private String serialNo;

    /**
     * 证书发行日期
     */
    @JsonSerialize(using=CustDateJsonSerializer.class)
    @Column(name = "d_createdate",  columnDefinition="VARCHAR" )
    @MetaData( value="证书发行日期", comments = "证书发行日期")
    private String createDate;

    /**
     * 证书有效期
     */
    @JsonSerialize(using=CustDateJsonSerializer.class)
    @Column(name = "d_validdate",  columnDefinition="VARCHAR" )
    @MetaData( value="证书有效期", comments = "证书有效期")
    private String validDate;

    /**
     * 省份
     */
    @Column(name = "c_province",  columnDefinition="VARCHAR" )
    @MetaData( value="省份", comments = "省份")
    private String provinceName;

    /**
     * 城市地区
     */
    @Column(name = "c_city",  columnDefinition="VARCHAR" )
    @MetaData( value="城市地区", comments = "城市地区")
    private String cityName;

    /**
     * 国家
     */
    @Column(name = "c_country",  columnDefinition="VARCHAR" )
    @MetaData( value="国家", comments = "国家")
    private String countryCode;

    /**
     * 部门
     */
    @Column(name = "c_department",  columnDefinition="VARCHAR" )
    @MetaData( value="部门", comments = "部门")
    private String orgUnitName;

    /**
     * 组织
     */
    @Column(name = "c_organization",  columnDefinition="VARCHAR" )
    @MetaData( value="组织", comments = "组织")
    private String orgName;

    /**
     * email地址
     */
    @Column(name = "c_email",  columnDefinition="VARCHAR" )
    @MetaData( value="email地址", comments = "email地址")
    private String email;

    /**
     * 状态；0：创建，1：发布，9：回收
     */
    @Column(name = "c_status",  columnDefinition="VARCHAR" )
    @MetaData( value="状态", comments = "状态；0：创建，1：发布，9：回收")
    private String certStatus;

    /**
     * 创建日期
     */
    @JsonSerialize(using=CustTimeJsonSerializer.class)
    @Column(name = "d_reg_date",  columnDefinition="VARCHAR" )
    @MetaData( value="创建日期", comments = "创建日期")
    @OrderBy("DESC")
    private String regDate;

    /**
     * 创建人(操作员)ID号
     */
    @Column(name = "l_reg_operid",  columnDefinition="INTEGER" )
    @MetaData( value="创建人(操作员)ID号", comments = "创建人(操作员)ID号")
    private Long regOperId;

    /**
     * 描述
     */
    @Column(name = "c_description",  columnDefinition="VARCHAR" )
    @MetaData( value="描述", comments = "描述")
    private String description;

    /**
     * 创建人(操作员)姓名
     */
    @Column(name = "c_reg_opername",  columnDefinition="VARCHAR" )
    @MetaData( value="创建人(操作员)姓名", comments = "创建人(操作员)姓名")
    private String regOperName;

    /**
     * 创建时间
     */
    @JsonSerialize(using=CustTimeJsonSerializer.class)
    @Column(name = "t_reg_time",  columnDefinition="VARCHAR" )
    @MetaData( value="创建时间", comments = "创建时间")
    @OrderBy("DESC")
    private String regTime;

    /**
     * 密码
     */
    @Column(name = "C_PASSWD", columnDefinition = "VARCHAR")
    @MetaData(value = "密码", comments = "密码")
    @JsonIgnore
    private String passwd;

    /**
     * 数字证书回收的原因
     */
    @Column(name = "c_reason",  columnDefinition="VARCHAR" )
    @MetaData( value="数字证书回收的原因", comments = "数字证书回收的原因")
    private String revokeReason;

    /**
     * 数字证书内容
     */
    @Column(name = "c_data",  columnDefinition="Byte[]" )
    @MetaData( value="数字证书内容", comments = "数字证书内容")
    @JsonIgnore
    private byte[] data;

    /**
     * 数字证书私钥信息
     */
    @Column(name = "c_private_key",  columnDefinition="Byte[]" )
    @MetaData( value="数字证书私钥信息", comments = "数字证书私钥信息")
    @JsonIgnore
    private byte[] privateKey;

    /**
     * 数字证书公钥信息
     */
    @Column(name = "c_public_key",  columnDefinition="Byte[]" )
    @MetaData( value="数字证书公钥信息", comments = "数字证书公钥信息")
    @JsonIgnore
    private byte[] publicKey;

    @Transient
    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String anSubject) {
        subject = anSubject;
    }

    private static final long serialVersionUID = -7927551280143276694L;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer anYear) {
        year = anYear;
    }

    public String getCommName() {
        return commName;
    }

    public void setCommName(final String commName) {
        this.commName = commName == null ? null : commName.trim();
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(final String signer) {
        this.signer = signer == null ? null : signer.trim();
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(final String certType) {
        this.certType = certType == null ? null : certType.trim();
    }

    public String getCertAlias() {
        return certAlias;
    }

    public void setCertAlias(final String certAlias) {
        this.certAlias = certAlias == null ? null : certAlias.trim();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(final String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(final String validDate) {
        this.validDate = validDate == null ? null : validDate.trim();
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(final String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode == null ? null : countryCode.trim();
    }

    public String getOrgUnitName() {
        return orgUnitName;
    }

    public void setOrgUnitName(final String orgUnitName) {
        this.orgUnitName = orgUnitName == null ? null : orgUnitName.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(final String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(final String certStatus) {
        this.certStatus = certStatus == null ? null : certStatus.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public Long getRegOperId() {
        return regOperId;
    }

    public void setRegOperId(final Long regOperId) {
        this.regOperId = regOperId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(final String regOperName) {
        this.regOperName = regOperName == null ? null : regOperName.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(final String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public byte[] getData() {

        return this.data;
    }

    public void setData(final byte[] anData){

        this.data = anData;
    }

    public String getRevokeReason() {
        return this.revokeReason;
    }

    public void setRevokeReason(final String anRevokeReason) {
        this.revokeReason = anRevokeReason;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(final String anPasswd) {
        this.passwd = anPasswd;
    }

    public byte[] getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(final byte[] anPrivateKey) {
        this.privateKey = anPrivateKey;
    }

    public byte[] getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(final byte[] anPublicKey) {
        this.publicKey = anPublicKey;
    }

    public String findOperOrg(){
        if (BetterStringUtils.isNotBlank(this.orgName)){
            return this.orgName.concat(".").concat(orgUnitName);
        }
        else{
            return this.commName.concat(".").concat(orgUnitName);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", year=").append(year);
        sb.append(", commName=").append(commName);
        sb.append(", signer=").append(signer);
        sb.append(", certType=").append(certType);
        sb.append(", certAlias=").append(certAlias);
        sb.append(", serialNo=").append(serialNo);
        sb.append(", createDate=").append(createDate);
        sb.append(", validDate=").append(validDate);
        sb.append(", provinceName=").append(provinceName);
        sb.append(", cityName=").append(cityName);
        sb.append(", countryCode=").append(countryCode);
        sb.append(", orgUnitName=").append(orgUnitName);
        sb.append(", orgName=").append(orgName);
        sb.append(", email=").append(email);
        sb.append(", certStatus=").append(certStatus);
        sb.append(", regDate=").append(regDate);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", description=").append(description);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regTime=").append(regTime);
        sb.append(", passwd=").append(passwd);
        sb.append(", revokeReason=").append(revokeReason);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        final BetterX509CertInfo other = (BetterX509CertInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getYear() == null ? other.getYear() == null : this.getYear().equals(other.getYear()))
                && (this.getCommName() == null ? other.getCommName() == null : this.getCommName().equals(other.getCommName()))
                && (this.getSigner() == null ? other.getSigner() == null : this.getSigner().equals(other.getSigner()))
                && (this.getCertType() == null ? other.getCertType() == null : this.getCertType().equals(other.getCertType()))
                && (this.getCertAlias() == null ? other.getCertAlias() == null : this.getCertAlias().equals(other.getCertAlias()))
                && (this.getSerialNo() == null ? other.getSerialNo() == null : this.getSerialNo().equals(other.getSerialNo()))
                && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
                && (this.getValidDate() == null ? other.getValidDate() == null : this.getValidDate().equals(other.getValidDate()))
                && (this.getProvinceName() == null ? other.getProvinceName() == null : this.getProvinceName().equals(other.getProvinceName()))
                && (this.getCityName() == null ? other.getCityName() == null : this.getCityName().equals(other.getCityName()))
                && (this.getCountryCode() == null ? other.getCountryCode() == null : this.getCountryCode().equals(other.getCountryCode()))
                && (this.getOrgUnitName() == null ? other.getOrgUnitName() == null : this.getOrgUnitName().equals(other.getOrgUnitName()))
                && (this.getOrgName() == null ? other.getOrgName() == null : this.getOrgName().equals(other.getOrgName()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getCertStatus() == null ? other.getCertStatus() == null : this.getCertStatus().equals(other.getCertStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
                && (this.getPasswd() == null ? other.getPasswd() == null : this.getPasswd().equals(other.getPasswd()))
                && (this.getRevokeReason() == null ? other.getRevokeReason() == null : this.getRevokeReason().equals(other.getRevokeReason()))
                && (this.getPublicKey() == null ? other.getPublicKey() == null : this.getPublicKey().equals(other.getPublicKey()))
                && (this.getPrivateKey() == null ? other.getPrivateKey() == null : this.getPrivateKey().equals(other.getPrivateKey()))
                && (this.getData() == null ? other.getData() == null : this.getData().equals(other.getData()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getYear() == null) ? 0 : getYear().hashCode());
        result = prime * result + ((getCommName() == null) ? 0 : getCommName().hashCode());
        result = prime * result + ((getSigner() == null) ? 0 : getSigner().hashCode());
        result = prime * result + ((getCertType() == null) ? 0 : getCertType().hashCode());
        result = prime * result + ((getCertAlias() == null) ? 0 : getCertAlias().hashCode());
        result = prime * result + ((getSerialNo() == null) ? 0 : getSerialNo().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getValidDate() == null) ? 0 : getValidDate().hashCode());
        result = prime * result + ((getProvinceName() == null) ? 0 : getProvinceName().hashCode());
        result = prime * result + ((getCityName() == null) ? 0 : getCityName().hashCode());
        result = prime * result + ((getCountryCode() == null) ? 0 : getCountryCode().hashCode());
        result = prime * result + ((getOrgUnitName() == null) ? 0 : getOrgUnitName().hashCode());
        result = prime * result + ((getOrgName() == null) ? 0 : getOrgName().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getCertStatus() == null) ? 0 : getCertStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
        result = prime * result + ((getPasswd() == null) ? 0 : getPasswd().hashCode());
        result = prime * result + ((getRevokeReason() == null) ? 0 : getRevokeReason().hashCode());
        result = prime * result + ((getPublicKey() == null) ? 0 : getPublicKey().hashCode());
        result = prime * result + ((getPrivateKey() == null) ? 0 : getPrivateKey().hashCode());
        return result;
    }

    public void modifyValue(final CustOperatorInfo anOperator, final BetterX509CertInfo anCertInfo){
        this.regDate = anCertInfo.getRegDate();
        this.regTime = anCertInfo.getRegTime();
        this.certStatus = anCertInfo.getCertStatus();
        this.id = anCertInfo.getId();
        if (anOperator != null){
            this.regOperId = anOperator.getId();
            this.regOperName = anOperator.getName();
        }
    }

    /**
     * @param anCertInfo
     */
    public void modifyValue(final BetterX509CertInfo anCertInfo) {
        this.certAlias = anCertInfo.getCertAlias();
        this.cityName = anCertInfo.getCityName();
        this.commName = anCertInfo.getCommName();
        this.countryCode = anCertInfo.getCountryCode();
        this.provinceName = anCertInfo.getProvinceName();
        this.description = anCertInfo.getDescription();
        this.email = anCertInfo.getEmail();
        this.orgName = anCertInfo.getOrgName();
        this.orgUnitName = anCertInfo.getOrgUnitName();
        this.signer = anCertInfo.getSigner();
        this.year = anCertInfo.getYear();
        this.description = anCertInfo.getDescription();
    }

    /**
     * 只有序列号匹配，数字证书是最终数字证书以及数字证书状态为发布的证书才允许下载
     * @param anSerialNo 证书序列号
     * @return
     */
    public boolean validDownload(final String anSerialNo){

        return "3".equals(this.getCertType()) && "1".equals(this.getCertStatus()) && this.getSerialNo().equals(anSerialNo);
    }

    public void calcValidDate() {
        final Date now = BetterDateUtils.getNow();
        this.setCreateDate(BetterDateUtils.formatNumberDate(now));
        this.setValidDate(BetterDateUtils.formatNumberDate(BetterDateUtils.addYears(now, this.getYear())));
    }

    public void initDefValue(final CustOperatorInfo anOperator){
        this.regDate = BetterDateUtils.getNumDate();
        this.regTime = BetterDateUtils.getNumTime();
        this.certStatus = "0";
        this.id = SerialGenerator.getLongValue("X509CertInfo.id");
        if (anOperator != null){
            this.regOperId = anOperator.getId();
            this.regOperName = anOperator.getName();
        }

        if (BetterStringUtils.isBlank(this.serialNo)){
            this.serialNo = Long.toUnsignedString(System.currentTimeMillis() * 10000 + SerialGenerator.randomInt(10000));
        }
    }

}