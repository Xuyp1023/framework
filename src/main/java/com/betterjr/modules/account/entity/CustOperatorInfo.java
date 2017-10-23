package com.betterjr.modules.account.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.data.BaseRemoteEntity;
import com.betterjr.common.data.WorkUserInfo;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.mapper.CustDateJsonSerializer;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.IdcardUtils;
import com.betterjr.modules.account.data.CustContactInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_OPERATOR")
public class CustOperatorInfo implements BetterjrEntity, WorkUserInfo, BaseRemoteEntity {
    /**
     * 经办人编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "经办人编号", comments = "经办人编号")
    private Long id;

    /**
     * 经办人名字
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人名字", comments = "经办人名字")
    private String name;

    /**
     * 证件类型
     */
    @Column(name = "C_IDENTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "证件类型", comments = "证件类型")
    private String identType;

    /**
     * 证件号码
     */
    @Column(name = "C_IDENTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "证件号码", comments = "证件号码")
    private String identNo;

    /**
     * 证件有效期
     */
    @Column(name = "D_VAILDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "证件有效期", comments = "证件有效期")
    @JsonSerialize(using = CustDateJsonSerializer.class)
    private String validDate;

    /**
     * 性别
     */
    @Column(name = "C_SEX", columnDefinition = "VARCHAR")
    @MetaData(value = "性别", comments = "性别")
    private String sex;

    /**
     * 邮政编码
     */
    @Column(name = "C_ZIPCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "邮政编码", comments = "邮政编码")
    private String zipCode;

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
     * 经办人识别方式(1-书面委托 2-印鉴 3-密码 4-证件)
     */
    @Column(name = "C_IDENT_CLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人识别方式(1-书面委托 2-印鉴 3-密码 4-证件)", comments = "经办人识别方式(1-书面委托 2-印鉴 3-密码 4-证件)")
    private String identClass;

    /**
     * 允许办理的业务；业务代码使用逗号分隔；如果没有表示全部
     */
    @Column(name = "C_TRANS_BUSIN", columnDefinition = "VARCHAR")
    @MetaData(value = "允许办理的业务", comments = "允许办理的业务；业务代码使用逗号分隔；如果没有表示全部")
    private String transBusin;

    /**
     * 允许撤单的业务；业务代码使用逗号分隔；如果没有表示全部
     */
    @Column(name = "C_REVOKE_BUSIN", columnDefinition = "VARCHAR")
    @MetaData(value = "允许撤单的业务", comments = "允许撤单的业务；业务代码使用逗号分隔；如果没有表示全部")
    private String revokeBusin;

    /**
     * 默认经办人；1默认经办人，0非默认经办人
     */
    @Column(name = "C_DEFAULT", columnDefinition = "VARCHAR")
    @MetaData(value = "默认经办人", comments = "默认经办人；1默认经办人，0非默认经办人")
    private Boolean defOper;

    /**
     * 经办人状态；1正常，2暂停业务，3注销
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人状态", comments = "经办人状态；1正常，2暂停业务，3注销")
    private String status;

    /**
     * 经办人上次状态；1正常，2暂停业务，3注销
     */
    @Column(name = "C_LAST_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "经办人上次状态", comments = "经办人上次状态；1正常，2暂停业务，3注销")
    private String lastStatus;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 操作员编码
     */
    @Column(name = "C_CODE", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员编码", comments = "操作员编码")
    private String operCode;

    /**
     * 操作员所在机构，如果证书登录，则
     */
    @Column(name = "C_OPERORG", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员所在机构", comments = "操作员所在机构，如果证书登录，则")
    private String operOrg;

    /**
     * 经办人编号序列号，用于大成基金
     */
    @Column(name = "L_CONTACTOR_SERIAL", columnDefinition = "INTEGER")
    @MetaData(value = "经办人申请序列号", comments = "经办人编号序列号")
    private Long contactorSerial;

    /**
     * 经办人编号序列号，用于大成基金
     */
    @Column(name = "C_RULE_LIST", columnDefinition = "VARCHAR")
    @MetaData(value = "操作员角色", comments = "操作员角色")
    private String ruleList;

    /**
     * 人员办理业务类型；0：普通操作员，2:实际经办人
     */
    @Column(name = "C_CLERK", columnDefinition = "VARCHAR")
    @MetaData(value = "人员办理业务类型", comments = "人员办理业务类型；0：普通操作员，1对外经办人, 2:账户业务办理人员，3:交易业务办理人员")
    private String clerkMan = "0";

    private String contIdentType;

    /**
     * 证件号码
     */
    private String contIdentNo;

    /**
     * 证件有效期
     */
    private String contCertValidDate;

    @Transient
    private String accessType;
    @Transient
    private String custList;

    @Transient
    private String roleName;

    /**
     * 上传的批次号，对应fileinfo中的ID
     */
    @Column(name = "N_BATCHNO", columnDefinition = "INTEGER")
    @MetaData(value = "上传的批次号", comments = "上传的批次号，对应fileinfo中的ID")
    private Long batchNo;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String anRoleName) {
        roleName = anRoleName;
    }

    public String getClerkMan() {
        return this.clerkMan;
    }

    public void setClerkMan(final String anClerkMan) {
        this.clerkMan = anClerkMan;
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

    private static final long serialVersionUID = 1440482098183L;

    private boolean newUser = false;

    public boolean isNewUser() {
        return newUser;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name == null ? null : name.trim();
    }

    @Override
    public String getIdentType() {
        return identType;
    }

    public void setIdentType(final String identType) {
        this.identType = identType == null ? null : identType.trim();
        this.contIdentType = this.identType;
    }

    @Override
    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(final String identNo) {
        this.identNo = identNo == null ? null : identNo.trim();
        this.contIdentNo = identNo;
    }

    @Override
    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(final String validDate) {
        this.validDate = validDate == null ? null : validDate.trim();
        this.contCertValidDate = validDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(final String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode == null ? null : zipCode.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(final String faxNo) {
        this.faxNo = faxNo == null ? null : faxNo.trim();
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(final String mobileNo) {
        this.mobileNo = mobileNo == null ? null : mobileNo.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getIdentClass() {
        return identClass;
    }

    public void setIdentClass(final String identClass) {
        this.identClass = identClass == null ? null : identClass.trim();
    }

    public String getTransBusin() {
        return transBusin;
    }

    public void setTransBusin(final String transBusin) {
        this.transBusin = transBusin == null ? null : transBusin.trim();
    }

    public String getRevokeBusin() {
        return revokeBusin;
    }

    public void setRevokeBusin(final String revokeBusin) {
        this.revokeBusin = revokeBusin == null ? null : revokeBusin.trim();
    }

    public Boolean getDefOper() {
        if (defOper == null) {
            return Boolean.FALSE;
        }
        return defOper;
    }

    public void setDefOper(final Boolean defOper) {
        this.defOper = defOper;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(final String lastStatus) {
        this.lastStatus = lastStatus == null ? null : lastStatus.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(final String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(final String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(final String operCode) {
        this.operCode = operCode == null ? null : operCode.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(final String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public Long getContactorSerial() {
        return this.contactorSerial;
    }

    public void setContactorSerial(final Long anContactorSerial) {
        this.contactorSerial = anContactorSerial;
    }

    @Override
    public String getRuleList() {
        return this.ruleList;
    }

    public void setRuleList(final String anRuleList) {
        this.ruleList = anRuleList;
    }

    public String getAccessType() {
        return this.accessType;
    }

    public void setAccessType(final String anAccessType) {
        this.accessType = anAccessType;
    }

    public String getCustList() {
        return this.custList;
    }

    public void setCustList(final String anCustList) {
        this.custList = anCustList;
    }

    public Long getBatchNo() {
        return this.batchNo;
    }

    public void setBatchNo(final Long anBatchNo) {
        this.batchNo = anBatchNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", identType=").append(identType);
        sb.append(", identNo=").append(identNo);
        sb.append(", validDate=").append(validDate);
        sb.append(", sex=").append(sex);
        sb.append(", zipCode=").append(zipCode);
        sb.append(", address=").append(address);
        sb.append(", phone=").append(phone);
        sb.append(", faxNo=").append(faxNo);
        sb.append(", mobileNo=").append(mobileNo);
        sb.append(", email=").append(email);
        sb.append(", identClass=").append(identClass);
        sb.append(", transBusin=").append(transBusin);
        sb.append(", revokeBusin=").append(revokeBusin);
        sb.append(", defOper=").append(defOper);
        sb.append(", status=").append(status);
        sb.append(", lastStatus=").append(lastStatus);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", operCode=").append(operCode);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", contactorSerial=").append(contactorSerial);
        sb.append(", ruleList=").append(ruleList);
        sb.append(", custList=").append(custList);
        sb.append(", batchNo=").append(batchNo);
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
        final CustOperatorInfo other = (CustOperatorInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getIdentType() == null ? other.getIdentType() == null : this.getIdentType().equals(
                        other.getIdentType()))
                && (this.getIdentNo() == null ? other.getIdentNo() == null : this.getIdentNo().equals(
                        other.getIdentNo()))
                && (this.getValidDate() == null ? other.getValidDate() == null : this.getValidDate().equals(
                        other.getValidDate()))
                && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
                && (this.getZipCode() == null ? other.getZipCode() == null : this.getZipCode().equals(
                        other.getZipCode()))
                && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(
                        other.getAddress()))
                && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
                && (this.getFaxNo() == null ? other.getFaxNo() == null : this.getFaxNo().equals(other.getFaxNo()))
                && (this.getMobileNo() == null ? other.getMobileNo() == null : this.getMobileNo().equals(
                        other.getMobileNo()))
                && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
                && (this.getIdentClass() == null ? other.getIdentClass() == null : this.getIdentClass().equals(
                        other.getIdentClass()))
                && (this.getTransBusin() == null ? other.getTransBusin() == null : this.getTransBusin().equals(
                        other.getTransBusin()))
                && (this.getRevokeBusin() == null ? other.getRevokeBusin() == null : this.getRevokeBusin().equals(
                        other.getRevokeBusin()))
                && (this.getDefOper() == null ? other.getDefOper() == null : this.getDefOper().equals(
                        other.getDefOper()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getLastStatus() == null ? other.getLastStatus() == null : this.getLastStatus().equals(
                        other.getLastStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(
                        other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(
                        other.getModiDate()))
                && (this.getOperCode() == null ? other.getOperCode() == null : this.getOperCode().equals(
                        other.getOperCode()))
                && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(
                        other.getOperOrg()))
                && (this.getContactorSerial() == null ? other.getContactorSerial() == null : this.getContactorSerial()
                        .equals(other.getContactorSerial()))
                && (this.getRuleList() == null ? other.getRuleList() == null : this.getRuleList().equals(
                        other.getRuleList()))
                && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(
                        other.getBatchNo()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getId() == null ? 0 : getId().hashCode());
        result = prime * result + (getName() == null ? 0 : getName().hashCode());
        result = prime * result + (getIdentType() == null ? 0 : getIdentType().hashCode());
        result = prime * result + (getIdentNo() == null ? 0 : getIdentNo().hashCode());
        result = prime * result + (getValidDate() == null ? 0 : getValidDate().hashCode());
        result = prime * result + (getSex() == null ? 0 : getSex().hashCode());
        result = prime * result + (getZipCode() == null ? 0 : getZipCode().hashCode());
        result = prime * result + (getAddress() == null ? 0 : getAddress().hashCode());
        result = prime * result + (getPhone() == null ? 0 : getPhone().hashCode());
        result = prime * result + (getFaxNo() == null ? 0 : getFaxNo().hashCode());
        result = prime * result + (getMobileNo() == null ? 0 : getMobileNo().hashCode());
        result = prime * result + (getEmail() == null ? 0 : getEmail().hashCode());
        result = prime * result + (getIdentClass() == null ? 0 : getIdentClass().hashCode());
        result = prime * result + (getTransBusin() == null ? 0 : getTransBusin().hashCode());
        result = prime * result + (getRevokeBusin() == null ? 0 : getRevokeBusin().hashCode());
        result = prime * result + (getDefOper() == null ? 0 : getDefOper().hashCode());
        result = prime * result + (getStatus() == null ? 0 : getStatus().hashCode());
        result = prime * result + (getLastStatus() == null ? 0 : getLastStatus().hashCode());
        result = prime * result + (getRegDate() == null ? 0 : getRegDate().hashCode());
        result = prime * result + (getModiDate() == null ? 0 : getModiDate().hashCode());
        result = prime * result + (getOperCode() == null ? 0 : getOperCode().hashCode());
        result = prime * result + (getOperOrg() == null ? 0 : getOperOrg().hashCode());
        result = prime * result + (getContactorSerial() == null ? 0 : getContactorSerial().hashCode());
        result = prime * result + (getRuleList() == null ? 0 : getRuleList().hashCode());
        result = prime * result + (getBatchNo() == null ? 0 : getBatchNo().hashCode());
        return result;
    }

    public CustOperatorInfo() {

    }

    public CustOperatorInfo(final CustOperatorInfo anOperator) {
        BeanMapper.copy(anOperator, this);
    }

    public CustOperatorInfo(final CustContactInfo anContact, final String anOrg) {
        this.operOrg = anOrg;
        BeanMapper.copy(anContact, this);
    }

    public static void main(final String anStr[]) {

    }

    public CustOperatorInfo(final CustOperatorInfoRequest request) {
        BeanMapper.copy(request, this);
        final long id = SerialGenerator.getLongValue(SerialGenerator.OPERATOR_ID);
        this.setIdentNo(request.getContIdentNo());
        this.setIdentType(request.getContIdentType());
        this.setValidDate(request.getContCertValidDate());
        this.setId(id);
        this.setSex(IdcardUtils.getGenderByIdCard(request.getContIdentNo(), request.getContIdentType()));
        this.setStatus("1");
        this.setLastStatus("1");
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setModiDate(BetterDateUtils.getNumDateTime());
        if (this.getDefOper() == null) {
            this.setDefOper(false);
        }
    }

    private String nickName;

    @Override
    public String getNickName() {

        return this.name;
    }

    public void setNickName(final String nickName) {
        this.nickName = nickName;
    }

    public void setNewUser(final boolean newUser) {
        this.newUser = newUser;
    }

    public void initStatus() {
        this.defOper = false;
        this.status = "1";
        newUser = true;
    }
}