package com.betterjr.modules.account.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_CERTINFO")
public class CustCertInfo implements BetterjrEntity {
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

    private static final long serialVersionUID = 1439797394180L;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo == null ? null : identNo.trim();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName == null ? null : contName.trim();
    }

    public String getContIdentType() {
        return contIdentType;
    }

    public void setContIdentType(String contIdentType) {
        this.contIdentType = contIdentType == null ? null : contIdentType.trim();
    }

    public String getContIdentNo() {
        return contIdentNo;
    }

    public void setContIdentNo(String contIdentNo) {
        this.contIdentNo = contIdentNo == null ? null : contIdentNo.trim();
    }

    public String getContPhone() {
        return contPhone;
    }

    public void setContPhone(String contPhone) {
        this.contPhone = contPhone == null ? null : contPhone.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
    
    public void initOperatorInfo(CustOperatorInfo anOperInfo){
        this.contIdentNo = anOperInfo.getIdentNo();
        this.contIdentType = anOperInfo.getIdentType();
        this.contName = anOperInfo.getName();
        this.contPhone = anOperInfo.getPhone();
    }
    
    public void initCustInfo(CustInfo anCustInfo){
        this.custNo = anCustInfo.getCustNo();
        this.custName = anCustInfo.getCustName();
        this.identNo = anCustInfo.getIdentNo();
    }
    
    public String getVersionUid() {
        return versionUid;
    }

    public void setVersionUid(String versionUid) {
        this.versionUid = versionUid == null ? null : versionUid.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getOperNo() {
        return operNo;
    }

    public void setOperNo(String operNo) {
        this.operNo = operNo == null ? null : operNo.trim();
    }

    public String getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(String certInfo) {
        this.certInfo = certInfo == null ? null : certInfo.trim();
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate == null ? null : validDate.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getOperOrg() {
        return this.operOrg;
    }

    public void setOperOrg(String anOperOrg) {
        this.operOrg = anOperOrg;
    }

    public boolean validCertInfo(CustCertInfo other) {

        return (this.getSerialNo() == null ? other.getSerialNo() == null : this.getSerialNo().equals(other.getSerialNo()))
                && (this.getVersionUid() == null ? other.getVersionUid() == null : this.getVersionUid().equals(other.getVersionUid()))
                && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
                && (this.getCertInfo() == null ? other.getCertInfo() == null : this.getCertInfo().equals(other.getCertInfo()))
                && (this.getValidDate() == null ? other.getValidDate() == null : this.getValidDate().equals(other.getValidDate()))
                && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
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
        CustCertInfo other = (CustCertInfo) that;
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
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()));
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
        return result;
    }
}