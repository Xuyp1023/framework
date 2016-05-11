package com.betterjr.modules.account.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.entity.UserType;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.modules.sys.entity.WorkUserInfo;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUSTINFO")
public class CustInfo implements BetterjrEntity, WorkUserInfo {

    /**
     * 客户编号
     */
    @Id
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 客户类型：0：机构；1：个人
     */
    @Column(name = "C_CUSTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "客户类型：0：机构", comments = "客户类型：0：机构；1：个人")
    private String custType;

    /**
     * 客户全称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户全称", comments = "客户全称")
    private String custName;

    /**
     * 客户简称
     */
    @Column(name = "C_SHORTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户简称", comments = "客户简称")
    private String shortName;

    /**
     * 客户昵称
     */
    @Column(name = "C_NICKNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户昵称", comments = "客户昵称")
    private String nickName;

    /**
     * 客户证件类型，个人证件类型0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照； 机构证件类型 0-技术监督局代码，1-营业执照，2-行政机关，3-社会团体，4-军队，5-武警，6-下属机构（具有主管单位批文号），7-基金会
     */
    @Column(name = "C_IDENTTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "客户证件类型", comments = "客户证件类型，个人证件类型0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照； 机构证件类型 0-技术监督局代码，1-营业执照，2-行政机关，3-社会团体，4-军队，5-武警，6-下属机构（具有主管单位批文号），7-基金会")
    private String identType;

    /**
     * 证件有效期
     */
    @Column(name = "D_VAILDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "证件有效期", comments = "证件有效期")
    private String validDate;

    /**
     * 国籍或地区
     */
    @Column(name = "C_NATIONAL", columnDefinition = "VARCHAR")
    @MetaData(value = "国籍或地区", comments = "国籍或地区")
    private String national;

    /**
     * 身份证明文件注册地址
     */
    @Column(name = "C_REGADDR", columnDefinition = "VARCHAR")
    @MetaData(value = "身份证明文件注册地址", comments = "身份证明文件注册地址")
    private String regAddr;

    /**
     * 客户状态；0正常，1冻结，9销户
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "客户状态", comments = "客户状态；0正常，1冻结，9销户")
    private String status;

    /**
     * 客户上次状态
     */
    @Column(name = "C_LAST_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "客户上次状态", comments = "客户上次状态")
    private String lastStatus;

    /**
     * 用户类型
     */
    @Column(name = "C_USER_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "用户类型", comments = "用户类型")
    private String userType;

    /**
     * 注册日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "注册日期", comments = "注册日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 客户证件号码
     */
    @Column(name = "C_IDENTNO", columnDefinition = "VARCHAR")
    @MetaData(value = "客户证件号码", comments = "客户证件号码")
    private String identNo;

    /**
     * 实名验证，0匿名客户，1实名客户
     */
    @Column(name = "C_IDENTVALID", columnDefinition = "VARCHAR")
    @MetaData(value = "实名验证", comments = "实名验证，0匿名客户，1实名客户")
    private Boolean identValid;

    /**
     * 审批级别
     */
    @Column(name = "C_ADUIT_LEVELS", columnDefinition = "VARCHAR")
    @MetaData(value = "审批级别", comments = "审批级别，0不审批，1复核，2 复核+审批")
    private String aduitLevels;

    private static final long serialVersionUID = 1439797394178L;

    public Long getCustNo() {
        return custNo;
    }

    public Long getId() {

        return this.custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType == null ? null : custType.trim();
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getIdentType() {
        return identType;
    }

    public void setIdentType(String identType) {
        this.identType = identType == null ? null : identType.trim();
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate == null ? null : validDate.trim();
    }

    public String getNational() {
        return national;
    }

    public void setNational(String national) {
        this.national = national == null ? null : national.trim();
    }

    public String getRegAddr() {
        return regAddr;
    }

    public void setRegAddr(String regAddr) {
        this.regAddr = regAddr == null ? null : regAddr.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus == null ? null : lastStatus.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
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

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo == null ? null : identNo.trim();
    }

    public Boolean getIdentValid() {
        return identValid;
    }

    public void setIdentValid(Boolean identValid) {
        this.identValid = identValid;
    }

    public String getRuleList() {

        return UserType.PERSON_USER.name();
    }

    public String getAduitLevels() {
        return this.aduitLevels;
    }

    public void setAduitLevels(String anAduitLevels) {
        this.aduitLevels = anAduitLevels;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(", custNo=").append(custNo);
        sb.append(", custType=").append(custType);
        sb.append(", custName=").append(custName);
        sb.append(", shortName=").append(shortName);
        sb.append(", nickName=").append(nickName);
        sb.append(", identType=").append(identType);
        sb.append(", validDate=").append(validDate);
        sb.append(", national=").append(national);
        sb.append(", regAddr=").append(regAddr);
        sb.append(", status=").append(status);
        sb.append(", lastStatus=").append(lastStatus);
        sb.append(", userType=").append(userType);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", identNo=").append(identNo);
        sb.append(", identValid=").append(identValid);
        sb.append(", aduitLevels=").append(aduitLevels);
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
        CustInfo other = (CustInfo) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getCustType() == null ? other.getCustType() == null : this.getCustType().equals(other.getCustType()))
                && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
                && (this.getShortName() == null ? other.getShortName() == null : this.getShortName().equals(other.getShortName()))
                && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
                && (this.getIdentType() == null ? other.getIdentType() == null : this.getIdentType().equals(other.getIdentType()))
                && (this.getValidDate() == null ? other.getValidDate() == null : this.getValidDate().equals(other.getValidDate()))
                && (this.getNational() == null ? other.getNational() == null : this.getNational().equals(other.getNational()))
                && (this.getRegAddr() == null ? other.getRegAddr() == null : this.getRegAddr().equals(other.getRegAddr()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getLastStatus() == null ? other.getLastStatus() == null : this.getLastStatus().equals(other.getLastStatus()))
                && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getIdentNo() == null ? other.getIdentNo() == null : this.getIdentNo().equals(other.getIdentNo()))
                && (this.getIdentValid() == null ? other.getIdentValid() == null : this.getIdentValid().equals(other.getIdentValid()))
                && (this.getAduitLevels() == null ? other.getAduitLevels() == null : this.getAduitLevels().equals(other.getAduitLevels()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getCustType() == null) ? 0 : getCustType().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getShortName() == null) ? 0 : getShortName().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getIdentType() == null) ? 0 : getIdentType().hashCode());
        result = prime * result + ((getValidDate() == null) ? 0 : getValidDate().hashCode());
        result = prime * result + ((getNational() == null) ? 0 : getNational().hashCode());
        result = prime * result + ((getRegAddr() == null) ? 0 : getRegAddr().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getLastStatus() == null) ? 0 : getLastStatus().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getIdentNo() == null) ? 0 : getIdentNo().hashCode());
        result = prime * result + ((getIdentValid() == null) ? 0 : getIdentValid().hashCode());
        result = prime * result + ((getAduitLevels() == null) ? 0 : getAduitLevels().hashCode());
       return result;
    }

    public CustInfo() {

    }

    public CustInfo(SaleAccoRequestInfo anRequest) {
        BeanMapper.copy(anRequest, this, "CertValidDate:ValidDate");
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setModiDate(BetterDateUtils.getNumDateTime());
        this.status = "0";
        this.lastStatus = this.status;
        this.userType = "0";
        this.identValid = true;
        this.aduitLevels = "1";
        this.custNo = SerialGenerator.getCustNo();
        anRequest.setCustNo(custNo);
    }

    @Override
    public String getName() {

        return this.custName;
    }
}