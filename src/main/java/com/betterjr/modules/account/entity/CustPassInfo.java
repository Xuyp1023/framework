package com.betterjr.modules.account.entity;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.PropertiesHolder;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm.HashPassword;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_PASS")
public class CustPassInfo implements BetterjrEntity {
    /**
     * 客户编号，或者操作员编码
     */
    @Id
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号，或者操作员编码")
    private Long custNo;

    /**
     * 密码错误次数
     */
    @Column(name = "N_ERRCOUNT", columnDefinition = "INTEGER")
    @MetaData(value = "密码错误次数", comments = "密码错误次数")
    private Integer errCount;

    /**
     * 密码类型；0查询密码，1交易密码，6操作员操作密码
     */
    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "密码类型", comments = "密码类型；0查询密码，1交易密码，6操作员操作密码")
    private String passType;

    /**
     * 密码
     */
    @Column(name = "C_PASSWD", columnDefinition = "VARCHAR")
    @MetaData(value = "密码", comments = "密码")
    private String passwd;

    /**
     * 加盐
     */
    @Column(name = "C_SALT", columnDefinition = "VARCHAR")
    @MetaData(value = "加盐", comments = "加盐")
    private String passSalt;

    /**
     * 密码锁定标志0 未锁定 1 已锁定 2完全锁定；完全锁定必须柜台解锁
     */
    @Column(name = "C_LOCK", columnDefinition = "VARCHAR")
    @MetaData(value = "密码锁定标志0 未锁定 1 已锁定 2完全锁定", comments = "密码锁定标志0 未锁定 1 已锁定 2完全锁定；完全锁定必须柜台解锁")
    private String lockStatus;

    /**
     * 操作方式
     */
    @Column(name = "D_OPERWAY", columnDefinition = "VARCHAR")
    @MetaData(value = "操作方式", comments = "操作方式")
    private String operWay;

    /**
     * 锁定日期
     */
    @Column(name = "D_LOCKDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "锁定日期", comments = "锁定日期")
    private String lockDate;

    /**
     * 锁定时间
     */
    @Column(name = "T_LOCKTIME", columnDefinition = "VARCHAR")
    @MetaData(value = "锁定时间", comments = "锁定时间")
    private String lockTime;

    /**
     * 密码到期日期
     */
    @Column(name = "D_ENDDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "密码到期日期", comments = "密码到期日期")
    private String endDate;

    /**
     * 密码最后更新日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "密码最后更新日期", comments = "密码最后更新日期")
    private String modiDate;

    private static final long serialVersionUID = 1439797394184L;

    /**
     * 确认密码
     */
    @Transient
    private String confirmation;

    public CustPassInfo() {
    }
    
    public void modifyPassword(String anSalt, String anPassword){
        this.passwd = anPassword;
        this.passSalt = anSalt;
        this.setModiDate(BetterDateUtils.getNumDateTime()); 
    }

    public CustPassInfo(CustOperatorInfo operator, String password) {
        this.setPassType("6");
        this.setCustNo(operator.getId());
        HashPassword hashPassword = new SystemAuthorizingRealm().encrypt(password);
        this.setPasswd(hashPassword.password);
        this.setPassSalt(hashPassword.salt);
        this.setErrCount(0);
        this.setLockStatus("0");
        this.setOperWay("0");
        this.setModiDate(BetterDateUtils.getNumDateTime());
        this.setEndDate("20501231");
    }

    public CustPassInfo(CustPasswordType anPassType, int anYear, Long anUserNo, String anSalt, String anPassword) {
        this.setPassType(anPassType.getPassType());
        this.setCustNo(anUserNo);
        this.setPasswd(anPassword);
        this.setPassSalt(anSalt);
        this.setErrCount(0);
        this.setLockStatus("0");
        this.setOperWay("0");
        this.setModiDate(BetterDateUtils.getNumDateTime());
        this.setEndDate(BetterDateUtils.formatNumberDate(BetterDateUtils.addYears(new Date() , anYear)) );
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String anConfirmation) {
        confirmation = anConfirmation;
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public Integer getErrCount() {
        return errCount;
    }

    public void setErrCount(Integer errCount) {
        this.errCount = errCount;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType == null ? null : passType.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    public String getPassSalt() {
        return passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt == null ? null : passSalt.trim();
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus == null ? null : lockStatus.trim();
    }

    public String getOperWay() {
        return operWay;
    }

    public void setOperWay(String operWay) {
        this.operWay = operWay == null ? null : operWay.trim();
    }

    public String getLockDate() {
        return lockDate;
    }

    public void setLockDate(String lockDate) {
        this.lockDate = lockDate == null ? null : lockDate.trim();
    }

    public String getLockTime() {
        return lockTime;
    }

    public void setLockTime(String lockTime) {
        this.lockTime = lockTime == null ? null : lockTime.trim();
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    public String getModiDate() {
        return modiDate;
    }

    public void setModiDate(String modiDate) {
        this.modiDate = modiDate == null ? null : modiDate.trim();
    }

    /**
     * 
     * 检查密码是否锁定
     * 
     * @return 锁定返回True， 否则返回False
     * @throws 异常情况
     */
    public boolean validLockType() {
        if (this.lockStatus.equals("0")) {

            return false;
        }
        else if (this.lockStatus.equals("1")) {
            long lockTime = PropertiesHolder.getLong("operator.lockTime", 2 * 3600 * 1000);
            long workTime = BetterDateUtils.parseDate(this.getLockDate() + " " + this.getLockTime()).getTime();

            return (lockTime + workTime) > System.currentTimeMillis();
        }
        else {

            return true;
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", custNo=").append(custNo);
        sb.append(", errCount=").append(errCount);
        sb.append(", passType=").append(passType);
        sb.append(", passwd=").append(passwd);
        sb.append(", confirmation=").append(confirmation);
        sb.append(", passSalt=").append(passSalt);
        sb.append(", lockStatus=").append(lockStatus);
        sb.append(", operWay=").append(operWay);
        sb.append(", lockDate=").append(lockDate);
        sb.append(", lockTime=").append(lockTime);
        sb.append(", endDate=").append(endDate);
        sb.append(", modiDate=").append(modiDate);
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
        CustPassInfo other = (CustPassInfo) that;
        return (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getErrCount() == null ? other.getErrCount() == null : this.getErrCount().equals(other.getErrCount()))
                && (this.getPassType() == null ? other.getPassType() == null : this.getPassType().equals(other.getPassType()))
                && (this.getPasswd() == null ? other.getPasswd() == null : this.getPasswd().equals(other.getPasswd()))
                && (this.getConfirmation() == null ? other.getConfirmation() == null : this.getConfirmation().equals(other.getConfirmation()))
                && (this.getPassSalt() == null ? other.getPassSalt() == null : this.getPassSalt().equals(other.getPassSalt()))
                && (this.getLockStatus() == null ? other.getLockStatus() == null : this.getLockStatus().equals(other.getLockStatus()))
                && (this.getOperWay() == null ? other.getOperWay() == null : this.getOperWay().equals(other.getOperWay()))
                && (this.getLockDate() == null ? other.getLockDate() == null : this.getLockDate().equals(other.getLockDate()))
                && (this.getLockTime() == null ? other.getLockTime() == null : this.getLockTime().equals(other.getLockTime()))
                && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getErrCount() == null) ? 0 : getErrCount().hashCode());
        result = prime * result + ((getPassType() == null) ? 0 : getPassType().hashCode());
        result = prime * result + ((getPasswd() == null) ? 0 : getPasswd().hashCode());
        result = prime * result + ((getPassSalt() == null) ? 0 : getPassSalt().hashCode());
        result = prime * result + ((getLockStatus() == null) ? 0 : getLockStatus().hashCode());
        result = prime * result + ((getOperWay() == null) ? 0 : getOperWay().hashCode());
        result = prime * result + ((getLockDate() == null) ? 0 : getLockDate().hashCode());
        result = prime * result + ((getLockTime() == null) ? 0 : getLockTime().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        return result;
    }
}