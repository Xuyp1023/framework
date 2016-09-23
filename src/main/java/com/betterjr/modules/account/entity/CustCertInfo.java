package com.betterjr.modules.account.entity;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.data.BetterBaseEntity;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_CERTINFO")
public class CustCertInfo extends BetterBaseEntity implements BetterjrEntity {

    /**
     * 证书序列号
     */
    @Id
    @Column(name = "C_SERIALNO", columnDefinition = "VARCHAR")
    @MetaData(value = "证书序列号", comments = "证书序列号")
    private String serialNo;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 证件号码
     */
    @Column(name = "C_IDENTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "证件号码", comments = "证件号码")
    private String identNo;

    /**
     * 客户名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户名称", comments = "客户名称")
    private String custName;

    /**
     * 经办人姓名
     */
    @Column(name = "C_CONTACT", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人姓名", comments = "经办人姓名")
    private String contName;

    /**
     * 经办人证件类型
     */
    @Column(name = "C_CONTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人证件类型", comments = "经办人证件类型")
    private String contIdentType;

    /**
     * 经办人证件号码
     */
    @Column(name = "C_CONTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人证件号码", comments = "经办人证件号码")
    private String contIdentNo;

    /**
     * 经办人联系电话
     */
    @Column(name = "C_CONTPHONE", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人联系电话", comments = "经办人联系电话")
    private String contPhone;

    /**
     * 证书状态，0正常，1暂停，2注销，9未启用。只有状态为9的情况下才允许更新token的值
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "证书状态", comments = "证书状态，0正常，1暂停，2注销，9未启用。只有状态为9的情况下才允许更新token的值")
    private String status;

    /**
     * 公钥唯一编号
     */
    @Column(name = "C_VERSIONUID", columnDefinition = "VARCHAR")
    @MetaData(value = "公钥唯一编号", comments = "公钥唯一编号")
    private String versionUid;

    /**
     * 主题证书的，SubjectDN
     */
    @Column(name = "C_SUBJECT", columnDefinition = "VARCHAR")
    @MetaData(value = "主题证书的", comments = "主题证书的，SubjectDN")
    private String subject;

    /**
     * 操作员编码；更新这个证书的人员信息
     */
    @Column(name = "C_OPERNO", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员编码", comments = "操作员编码；更新这个证书的人员信息")
    private String operNo;

    /**
     * 证书信息，Base64编码
     */
    @Column(name = "C_CERTINFO", columnDefinition = "VARCHAR")
    @MetaData(value = "证书信息", comments = "证书信息，Base64编码")
    @JsonIgnore
    private String certInfo;

    /**
     * 证书有效期
     */
    @Column(name = "D_VALIDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "证书有效期", comments = "证书有效期")
    private String validDate;

    /**
     * 证书发行日期
     */
    @Column(name = "D_CREATEDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "证书发行日期", comments = "证书发行日期")
    private String createDate;

    /**
     * 唯一验证客户标示，根据客户服务器特征，唯一产生的服务器ID信息，如果更换服务器特征，则需要更新该标示！
     */
    @Column(name = "C_TOKEN", columnDefinition = "VARCHAR")
    @MetaData(value = "唯一验证客户标示", comments = "唯一验证客户标示，根据客户服务器特征，唯一产生的服务器ID信息，如果更换服务器特征，则需要更新该标示！")
    private String token;

    /**
     * 操作员所在机构，如果证书登录，则
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "操作员所在机构，如果证书登录，则")
    private String operOrg;

    /**
     * 使用证书的角色，内置角色，颁发证书的指定，例如：CORE_USER，SUPPLIER_USER，FACTOR_USER，SELLER_USER
     */
    @Column(name = "C_RULE_LIST", columnDefinition = "VARCHAR")
    @MetaData(value = " 使用证书的角色", comments = "内置角色，颁发证书的指定，例如：CORE_USER，SUPPLIER_USER，FACTOR_USER，SELLER_USER")
    private String ruleList;

    /**
     * 数字证书内部序号
     */
    @Column(name = "L_CERTID", columnDefinition = "INTEGER")
    @MetaData(value = "数字证书内部序号", comments = "数字证书内部序号")
    private Long certId;

    /**
     * 创建人(操作员)ID号
     */
    @Column(name = "L_REG_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "创建人(操作员)ID号", comments = "创建人(操作员)ID号")
    @JsonIgnore
    private Long regOperId;

    /**
     * 创建人(操作员)姓名
     */
    @Column(name = "C_REG_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建人(操作员)姓名", comments = "创建人(操作员)姓名")
    private String regOperName;

    /**
     * 创建日期
     */
    @Column(name = "D_REG_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "创建日期", comments = "创建日期")
    private String regDate;

    /**
     * 创建时间
     */
    @Column(name = "T_REG_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "创建时间", comments = "创建时间")
    private String regTime;

    /**
     * 修改人(操作员)ID号
     */
    @Column(name = "L_MODI_OPERID", columnDefinition = "INTEGER")
    @MetaData(value = "修改人(操作员)ID号", comments = "修改人(操作员)ID号")
    @JsonIgnore
    private Long modiOperId;

    /**
     * 修改人(操作员)姓名
     */
    @Column(name = "C_MODI_OPERNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改人(操作员)姓名", comments = "修改人(操作员)姓名")
    private String modiOperName;

    /**
     * 修改日期
     */
    @Column(name = "D_MODI_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 修改时间
     */
    @Column(name = "T_MODI_TIME", columnDefinition = "VARCHAR")
    @MetaData(value = "修改时间", comments = "修改时间")
    private String modiTime;

    /**
     * 发布日期
     */
    @Column(name = "C_PUBLISH_DATE", columnDefinition = "VARCHAR")
    @MetaData(value = "发布日期", comments = "发布日期")
    private String publishDate;

    /**
     * 发布方式
     */
    @Column(name = "C_PUBLISH_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "发布方式", comments = "发布方式")
    private String publishMode;

    /**
     * 描述
     */
    @Column(name = "c_description", columnDefinition = "VARCHAR")
    @MetaData(value = "描述", comments = "描述")
    private String description;

    /**
     * email地址
     */
    @Column(name = "c_email",  columnDefinition="VARCHAR" )
    @MetaData( value="email地址", comments = "email地址")
    private String email;

    @Transient
    private List<CustCertRule> certRuleList;

    private static final long serialVersionUID = 1439797394180L;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(final String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(final Long custNo) {
        this.custNo = custNo;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(final String identNo) {
        this.identNo = identNo == null ? null : identNo.trim();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(final String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getContName() {
        return contName;
    }

    public void setContName(final String contName) {
        this.contName = contName == null ? null : contName.trim();
    }

    public String getContIdentType() {
        return contIdentType;
    }

    public void setContIdentType(final String contIdentType) {
        this.contIdentType = contIdentType == null ? null : contIdentType.trim();
    }

    public String getContIdentNo() {
        return contIdentNo;
    }

    public void setContIdentNo(final String contIdentNo) {
        this.contIdentNo = contIdentNo == null ? null : contIdentNo.trim();
    }

    public String getContPhone() {
        return contPhone;
    }

    public void setContPhone(final String contPhone) {
        this.contPhone = contPhone == null ? null : contPhone.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status == null ? null : status.trim();
    }

    public void initOperatorInfo(final CustOperatorInfo anOperInfo) {
        this.contIdentNo = anOperInfo.getIdentNo();
        this.contIdentType = anOperInfo.getIdentType();
        this.contName = anOperInfo.getName();
        this.contPhone = anOperInfo.getPhone();
    }

    public void initCustInfo(final CustInfo anCustInfo) {
        this.custNo = anCustInfo.getCustNo();
        this.custName = anCustInfo.getCustName();
        this.identNo = anCustInfo.getIdentNo();
    }

    public String getVersionUid() {
        return versionUid;
    }

    public void setVersionUid(final String versionUid) {
        this.versionUid = versionUid == null ? null : versionUid.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(final String operNo) {
        this.operNo = operNo == null ? null : operNo.trim();
    }

    public String getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(final String certInfo) {
        this.certInfo = certInfo == null ? null : certInfo.trim();
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(final String validDate) {
        this.validDate = validDate == null ? null : validDate.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(final String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String anDescription) {
        this.description = anDescription;
    }

    public boolean validCertInfo(final CustCertInfo other) {

        return (this.getSerialNo() == null ? other.getSerialNo() == null : this.getSerialNo().equals(other.getSerialNo()))
                && (this.getVersionUid() == null ? other.getVersionUid() == null : this.getVersionUid().equals(other.getVersionUid()))
                && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
                && (this.getCertInfo() == null ? other.getCertInfo() == null : this.getCertInfo().equals(other.getCertInfo()))
                && (this.getValidDate() == null ? other.getValidDate() == null : this.getValidDate().equals(other.getValidDate()))
                && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()));
    }

    @Override
    public Long getRegOperId() {
        return this.regOperId;
    }

    @Override
    public void setRegOperId(final Long anRegOperId) {
        this.regOperId = anRegOperId;
    }

    @Override
    public String getRegOperName() {
        return this.regOperName;
    }

    @Override
    public void setRegOperName(final String anRegOperName) {
        this.regOperName = anRegOperName;
    }

    @Override
    public String getRegDate() {
        return this.regDate;
    }

    @Override
    public void setRegDate(final String anRegDate) {
        this.regDate = anRegDate;
    }

    @Override
    public String getRegTime() {
        return this.regTime;
    }

    @Override
    public void setRegTime(final String anRegTime) {
        this.regTime = anRegTime;
    }

    public Long getModiOperId() {
        return this.modiOperId;
    }

    @Override
    public void setModiOperId(final Long anModiOperId) {
        this.modiOperId = anModiOperId;
    }

    public String getModiOperName() {
        return this.modiOperName;
    }

    @Override
    public void setModiOperName(final String anModiOperName) {
        this.modiOperName = anModiOperName;
    }

    public String getModiDate() {
        return this.modiDate;
    }

    @Override
    public void setModiDate(final String anModiDate) {
        this.modiDate = anModiDate;
    }

    public String getModiTime() {
        return this.modiTime;
    }

    @Override
    public void setModiTime(final String anModiTime) {
        this.modiTime = anModiTime;
    }

    public String getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(final String anPublishDate) {
        this.publishDate = anPublishDate;
    }

    public String getPublishMode() {
        return this.publishMode;
    }

    public void setPublishMode(final String anPublishMode) {
        this.publishMode = anPublishMode;
    }

    public String getRuleList() {
        return this.ruleList;
    }

    public void setRuleList(final String anRuleList) {
        this.ruleList = anRuleList;
    }

    public Long getCertId() {
        return this.certId;
    }

    public void setCertId(final Long anCertId) {
        this.certId = anCertId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String anEmail) {
        this.email = anEmail;
    }

    public List<CustCertRule> getCertRuleList() {
        return certRuleList;
    }

    public void setCertRuleList(final List<CustCertRule> anCertRuleList) {
        certRuleList = anCertRuleList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("ruleList = ").append(ruleList);
        sb.append(", serialNo=").append(serialNo);
        sb.append(", custNo=").append(custNo);
        sb.append(", identNo=").append(identNo);
        sb.append(", custName=").append(custName);
        sb.append(", contName=").append(contName);
        sb.append(", contIdentType=").append(contIdentType);
        sb.append(", contIdentNo=").append(contIdentNo);
        sb.append(", contPhone=").append(contPhone);
        sb.append(", status=").append(status);
        sb.append(", versionUid=").append(versionUid);
        sb.append(", subject=").append(subject);
        sb.append(", operNo=").append(operNo);
        sb.append(", certInfo=").append(certInfo);
        sb.append(", validDate=").append(validDate);
        sb.append(", createDate=").append(createDate);
        sb.append(", token=").append(token);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", certId=").append(certId);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", publishDate=").append(publishDate);
        sb.append(", publishMode=").append(publishMode);
        sb.append(", email=").append(email);
        sb.append(", description=").append(description);
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
        final CustCertInfo other = (CustCertInfo) that;
        return (this.getSerialNo() == null ? other.getSerialNo() == null : this.getSerialNo().equals(other.getSerialNo()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getIdentNo() == null ? other.getIdentNo() == null : this.getIdentNo().equals(other.getIdentNo()))
                && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
                && (this.getContName() == null ? other.getContName() == null : this.getContName().equals(other.getContName()))
                && (this.getContIdentType() == null ? other.getContIdentType() == null : this.getContIdentType().equals(other.getContIdentType()))
                && (this.getContIdentNo() == null ? other.getContIdentNo() == null : this.getContIdentNo().equals(other.getContIdentNo()))
                && (this.getContPhone() == null ? other.getContPhone() == null : this.getContPhone().equals(other.getContPhone()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getVersionUid() == null ? other.getVersionUid() == null : this.getVersionUid().equals(other.getVersionUid()))
                && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
                && (this.getOperNo() == null ? other.getOperNo() == null : this.getOperNo().equals(other.getOperNo()))
                && (this.getCertInfo() == null ? other.getCertInfo() == null : this.getCertInfo().equals(other.getCertInfo()))
                && (this.getValidDate() == null ? other.getValidDate() == null : this.getValidDate().equals(other.getValidDate()))
                && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
                && (this.getToken() == null ? other.getToken() == null : this.getToken().equals(other.getToken()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
                && (this.getCertId() == null ? other.getCertId() == null : this.getCertId().equals(other.getCertId()))
                && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
                && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
                && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
                && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
                && (this.getPublishDate() == null ? other.getPublishDate() == null : this.getPublishDate().equals(other.getPublishDate()))
                && (this.getPublishMode() == null ? other.getPublishMode() == null : this.getPublishMode().equals(other.getPublishMode()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getRuleList() == null ? other.getRuleList() == null : this.getRuleList().equals(other.getRuleList()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSerialNo() == null) ? 0 : getSerialNo().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getIdentNo() == null) ? 0 : getIdentNo().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getContName() == null) ? 0 : getContName().hashCode());
        result = prime * result + ((getContIdentType() == null) ? 0 : getContIdentType().hashCode());
        result = prime * result + ((getContIdentNo() == null) ? 0 : getContIdentNo().hashCode());
        result = prime * result + ((getContPhone() == null) ? 0 : getContPhone().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getVersionUid() == null) ? 0 : getVersionUid().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getOperNo() == null) ? 0 : getOperNo().hashCode());
        result = prime * result + ((getCertInfo() == null) ? 0 : getCertInfo().hashCode());
        result = prime * result + ((getValidDate() == null) ? 0 : getValidDate().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getToken() == null) ? 0 : getToken().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getRuleList() == null) ? 0 : getRuleList().hashCode());
        result = prime * result + ((getCertId() == null) ? 0 : getCertId().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getPublishDate() == null) ? 0 : getPublishDate().hashCode());
        result = prime * result + ((getPublishMode() == null) ? 0 : getPublishMode().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());

        return result;
    }

    public void publishModifyValue(final String anToken, final String anPublishMode){
        this.token = anToken;
        this.publishMode = anPublishMode;
        this.publishDate = BetterDateUtils.getNumDate();
    }

    /**
     * 检查是否可以下载数字证书，默认是10天；数字证书发布10天后，就不能下载；数字证书只有在状态为未启用和刚发布的情况下才可以下载
     * @return
     */
    public boolean invalidDownload(){
        if (BetterStringUtils.isBlank(this.publishDate)){
            return false;
        }
        final String tmpDate = BetterDateUtils.addStrDays(this.publishDate, 10);
        return (BetterDateUtils.getNumDate().compareTo(tmpDate) >0) && (" 2, 3, 9".indexOf(this.status) > 0);
    }
}