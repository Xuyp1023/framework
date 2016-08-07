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
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterDateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Access(AccessType.FIELD)
@Entity
@Table(name = "t_custinfo")
public class CustInfo implements BetterjrEntity {
    /**
     * 客户编号
     */
    @Id
    @Column(name = "L_CUSTNO",  columnDefinition="INTEGER" )
    @MetaData( value="客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 数据版本号
     */
    @JsonIgnore
    @Column(name = "N_VERSION",  columnDefinition="INTEGER" )
    @MetaData( value="数据版本号", comments = "数据版本号")
    private Long version;

    /**
     * 客户类型：0：机构；1：个人
     */
    @Column(name = "C_CUSTTYPE",  columnDefinition="CHAR" )
    @MetaData( value="客户类型：0：机构", comments = "客户类型：0：机构；1：个人")
    private String custType;

    /**
     * 客户全称
     */
    @Column(name = "C_CUSTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户全称", comments = "客户全称")
    private String custName;

    /**
     * 客户简称
     */
    @Column(name = "C_SHORTNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户简称", comments = "客户简称")
    private String shortName;

    /**
     * 客户昵称
     */
    @Column(name = "C_NICKNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="客户昵称", comments = "客户昵称")
    private String nickName;

    /**
     * 客户证件类型，个人证件类型0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照； 机构证件类型 0-技术监督局代码，1-营业执照，2-行政机关，3-社会团体，4-军队，5-武警，6-下属机构（具有主管单位批文号），7-基金会
     */
    @Column(name = "C_IDENTTYPE",  columnDefinition="CHAR" )
    @MetaData( value="客户证件类型", comments = "客户证件类型，个人证件类型0-身份证，1-护照，2-军官证，3-士兵证，4-回乡证，5-户口本，6-外国护照； 机构证件类型 0-技术监督局代码，1-营业执照，2-行政机关，3-社会团体，4-军队，5-武警，6-下属机构（具有主管单位批文号），7-基金会")
    private String identType;

    /**
     * 证件有效期
     */
    @Column(name = "D_VAILDDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="证件有效期", comments = "证件有效期")
    private String vaildDate;

    /**
     * 国籍或地区
     */
    @Column(name = "C_NATIONAL",  columnDefinition="VARCHAR" )
    @MetaData( value="国籍或地区", comments = "国籍或地区")
    private String national;

    /**
     * 身份证明文件注册地址
     */
    @Column(name = "C_REGADDR",  columnDefinition="VARCHAR" )
    @MetaData( value="身份证明文件注册地址", comments = "身份证明文件注册地址")
    private String regAddr;

    /**
     * 用户类型
     */
    @Column(name = "C_USER_TYPE",  columnDefinition="CHAR" )
    @MetaData( value="用户类型", comments = "用户类型")
    private String userType;

    /**
     * 客户证件号码
     */
    @Column(name = "C_IDENTNO",  columnDefinition="VARCHAR" )
    @MetaData( value="客户证件号码", comments = "客户证件号码")
    private String identNo;

    /**
     * 实名验证，0匿名客户，1实名客户
     */
    @Column(name = "C_IDENTVALID",  columnDefinition="CHAR" )
    @MetaData( value="实名验证", comments = "实名验证，0匿名客户，1实名客户")
    private Boolean identValid;

    /**
     * 审批级别，0不审批，1复核，2 复核+审批
     */
    @JsonIgnore
    @Column(name = "C_ADUIT_LEVELS",  columnDefinition="CHAR" )
    @MetaData( value="审批级别", comments = "审批级别，0不审批，1复核，2 复核+审批")
    private String aduitLevels;

    /**
     * 附件信息
     */
    @JsonIgnore
    @Column(name = "N_BATCHNO",  columnDefinition="INTEGER" )
    @MetaData( value="附件信息", comments = "附件信息")
    private Long batchNo;

    /**
     * 创建人(操作员)ID号
     */
    @JsonIgnore
    @Column(name = "L_REG_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="创建人(操作员)ID号", comments = "创建人(操作员)ID号")
    private Long regOperId;

    /**
     * 创建人(操作员)姓名
     */
    @JsonIgnore
    @Column(name = "C_REG_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="创建人(操作员)姓名", comments = "创建人(操作员)姓名")
    private String regOperName;

    /**
     * 创建日期
     */
    @JsonIgnore
    @Column(name = "D_REG_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="创建日期", comments = "创建日期")
    private String regDate;

    /**
     * 创建时间
     */
    @JsonIgnore
    @Column(name = "T_REG_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="创建时间", comments = "创建时间")
    private String regTime;

    /**
     * 修改人(操作员)ID号
     */
    @JsonIgnore
    @Column(name = "L_MODI_OPERID",  columnDefinition="INTEGER" )
    @MetaData( value="修改人(操作员)ID号", comments = "修改人(操作员)ID号")
    private Long modiOperId;

    /**
     * 修改人(操作员)姓名
     */
    @JsonIgnore
    @Column(name = "C_MODI_OPERNAME",  columnDefinition="VARCHAR" )
    @MetaData( value="修改人(操作员)姓名", comments = "修改人(操作员)姓名")
    private String modiOperName;

    /**
     * 修改日期
     */
    @JsonIgnore
    @Column(name = "D_MODI_DATE",  columnDefinition="VARCHAR" )
    @MetaData( value="修改日期", comments = "修改日期")
    private String modiDate;

    /**
     * 修改时间
     */
    @JsonIgnore
    @Column(name = "T_MODI_TIME",  columnDefinition="VARCHAR" )
    @MetaData( value="修改时间", comments = "修改时间")
    private String modiTime;

    /**
     * 操作机构
     */
    @JsonIgnore
    @Column(name = "C_OPERORG",  columnDefinition="VARCHAR" )
    @MetaData( value="操作机构", comments = "操作机构")
    private String operOrg;

    /**
     * 客户状态；0正常，1冻结，9销户
     */
    @JsonIgnore
    @Column(name = "C_BUSIN_STATUS",  columnDefinition="CHAR" )
    @MetaData( value="客户状态", comments = "客户状态；0正常，1冻结，9销户")
    private String businStatus;

    /**
     * 最后更新状态
     */
    @JsonIgnore
    @Column(name = "C_LAST_STATUS",  columnDefinition="CHAR" )
    @MetaData( value="最后更新状态", comments = "最后更新状态")
    private String lastStatus;

    private static final long serialVersionUID = 1468216953792L;

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    public String getVaildDate() {
        return vaildDate;
    }

    public void setVaildDate(String vaildDate) {
        this.vaildDate = vaildDate == null ? null : vaildDate.trim();
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
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

    public String getAduitLevels() {
        return aduitLevels;
    }

    public void setAduitLevels(String aduitLevels) {
        this.aduitLevels = aduitLevels == null ? null : aduitLevels.trim();
    }

    public Long getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(Long batchNo) {
        this.batchNo = batchNo;
    }

    public Long getRegOperId() {
        return regOperId;
    }

    public void setRegOperId(Long regOperId) {
        this.regOperId = regOperId;
    }

    public String getRegOperName() {
        return regOperName;
    }

    public void setRegOperName(String regOperName) {
        this.regOperName = regOperName == null ? null : regOperName.trim();
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate == null ? null : regDate.trim();
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime == null ? null : regTime.trim();
    }

    public Long getModiOperId() {
        return modiOperId;
    }

    public void setModiOperId(Long modiOperId) {
        this.modiOperId = modiOperId;
    }

    public String getModiOperName() {
        return modiOperName;
    }

    public void setModiOperName(String modiOperName) {
        this.modiOperName = modiOperName == null ? null : modiOperName.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    public String getModiTime() {
        return modiTime;
    }

    public void setModiTime(String modiTime) {
        this.modiTime = modiTime == null ? null : modiTime.trim();
    }

    public String getOperOrg() {
        return operOrg;
    }

    public void setOperOrg(String operOrg) {
        this.operOrg = operOrg == null ? null : operOrg.trim();
    }

    public String getBusinStatus() {
        return businStatus;
    }

    public void setBusinStatus(String businStatus) {
        this.businStatus = businStatus == null ? null : businStatus.trim();
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus == null ? null : lastStatus.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", custNo=").append(custNo);
        sb.append(", version=").append(version);
        sb.append(", custType=").append(custType);
        sb.append(", custName=").append(custName);
        sb.append(", shortName=").append(shortName);
        sb.append(", nickName=").append(nickName);
        sb.append(", identType=").append(identType);
        sb.append(", vaildDate=").append(vaildDate);
        sb.append(", national=").append(national);
        sb.append(", regAddr=").append(regAddr);
        sb.append(", userType=").append(userType);
        sb.append(", identNo=").append(identNo);
        sb.append(", identValid=").append(identValid);
        sb.append(", aduitLevels=").append(aduitLevels);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", regOperId=").append(regOperId);
        sb.append(", regOperName=").append(regOperName);
        sb.append(", regDate=").append(regDate);
        sb.append(", regTime=").append(regTime);
        sb.append(", modiOperId=").append(modiOperId);
        sb.append(", modiOperName=").append(modiOperName);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", modiTime=").append(modiTime);
        sb.append(", operOrg=").append(operOrg);
        sb.append(", businStatus=").append(businStatus);
        sb.append(", lastStatus=").append(lastStatus);
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
        CustInfo other = (CustInfo) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getCustType() == null ? other.getCustType() == null : this.getCustType().equals(other.getCustType()))
            && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
            && (this.getShortName() == null ? other.getShortName() == null : this.getShortName().equals(other.getShortName()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getIdentType() == null ? other.getIdentType() == null : this.getIdentType().equals(other.getIdentType()))
            && (this.getVaildDate() == null ? other.getVaildDate() == null : this.getVaildDate().equals(other.getVaildDate()))
            && (this.getNational() == null ? other.getNational() == null : this.getNational().equals(other.getNational()))
            && (this.getRegAddr() == null ? other.getRegAddr() == null : this.getRegAddr().equals(other.getRegAddr()))
            && (this.getUserType() == null ? other.getUserType() == null : this.getUserType().equals(other.getUserType()))
            && (this.getIdentNo() == null ? other.getIdentNo() == null : this.getIdentNo().equals(other.getIdentNo()))
            && (this.getIdentValid() == null ? other.getIdentValid() == null : this.getIdentValid().equals(other.getIdentValid()))
            && (this.getAduitLevels() == null ? other.getAduitLevels() == null : this.getAduitLevels().equals(other.getAduitLevels()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getRegOperId() == null ? other.getRegOperId() == null : this.getRegOperId().equals(other.getRegOperId()))
            && (this.getRegOperName() == null ? other.getRegOperName() == null : this.getRegOperName().equals(other.getRegOperName()))
            && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
            && (this.getRegTime() == null ? other.getRegTime() == null : this.getRegTime().equals(other.getRegTime()))
            && (this.getModiOperId() == null ? other.getModiOperId() == null : this.getModiOperId().equals(other.getModiOperId()))
            && (this.getModiOperName() == null ? other.getModiOperName() == null : this.getModiOperName().equals(other.getModiOperName()))
            && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
            && (this.getModiTime() == null ? other.getModiTime() == null : this.getModiTime().equals(other.getModiTime()))
            && (this.getOperOrg() == null ? other.getOperOrg() == null : this.getOperOrg().equals(other.getOperOrg()))
            && (this.getBusinStatus() == null ? other.getBusinStatus() == null : this.getBusinStatus().equals(other.getBusinStatus()))
            && (this.getLastStatus() == null ? other.getLastStatus() == null : this.getLastStatus().equals(other.getLastStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getCustType() == null) ? 0 : getCustType().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getShortName() == null) ? 0 : getShortName().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getIdentType() == null) ? 0 : getIdentType().hashCode());
        result = prime * result + ((getVaildDate() == null) ? 0 : getVaildDate().hashCode());
        result = prime * result + ((getNational() == null) ? 0 : getNational().hashCode());
        result = prime * result + ((getRegAddr() == null) ? 0 : getRegAddr().hashCode());
        result = prime * result + ((getUserType() == null) ? 0 : getUserType().hashCode());
        result = prime * result + ((getIdentNo() == null) ? 0 : getIdentNo().hashCode());
        result = prime * result + ((getIdentValid() == null) ? 0 : getIdentValid().hashCode());
        result = prime * result + ((getAduitLevels() == null) ? 0 : getAduitLevels().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getRegOperId() == null) ? 0 : getRegOperId().hashCode());
        result = prime * result + ((getRegOperName() == null) ? 0 : getRegOperName().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getRegTime() == null) ? 0 : getRegTime().hashCode());
        result = prime * result + ((getModiOperId() == null) ? 0 : getModiOperId().hashCode());
        result = prime * result + ((getModiOperName() == null) ? 0 : getModiOperName().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getModiTime() == null) ? 0 : getModiTime().hashCode());
        result = prime * result + ((getOperOrg() == null) ? 0 : getOperOrg().hashCode());
        result = prime * result + ((getBusinStatus() == null) ? 0 : getBusinStatus().hashCode());
        result = prime * result + ((getLastStatus() == null) ? 0 : getLastStatus().hashCode());
        return result;
    }
    

    public CustInfo() {

    }

    public CustInfo(SaleAccoRequestInfo anRequest) {
        BeanMapper.copy(anRequest, this, "CertValidDate:ValidDate");
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setModiDate(BetterDateUtils.getNumDateTime());
        this.businStatus = "0";
        this.lastStatus = this.businStatus;
        this.userType = "0";
        this.identValid = true;
        this.aduitLevels = "1";
        this.custNo = SerialGenerator.getCustNo();
        anRequest.setCustNo(custNo);
    }
}