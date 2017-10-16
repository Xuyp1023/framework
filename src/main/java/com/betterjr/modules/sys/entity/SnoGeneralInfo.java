package com.betterjr.modules.sys.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialBuildType;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_SNOGENERAL")
public class SnoGeneralInfo implements BetterjrEntity {

    /**
     * 产生字段，类.属性
     */
    @Id
    @Column(name = "C_OPERTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "产生字段", comments = "产生字段，类.属性")
    private String operType;

    /**
     * 最大序列号
     */
    @Column(name = "L_LASTNO", columnDefinition = "INTEGER")
    @MetaData(value = "最大序列号", comments = "最大序列号")
    private Long lastNo;

    /**
     * 最后更新日期
     */
    @Column(name = "D_LASTDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "最后更新日期", comments = "最后更新日期")
    private String workDate;

    /**
     * 使用的系统简称
     */
    @Column(name = "C_SYSNO", columnDefinition = "VARCHAR")
    @MetaData(value = "使用的系统简称", comments = "使用的系统简称")
    private String sysNo;

    /**
     * 产生类型，0每日累计，1单日累计,2每周累计，3每月累计，4每年累计
     */
    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "产生类型", comments = "产生类型，0每日累计，1单日累计,2每周累计，3每月累计，4每年累计")
    private String buildType;

    /**
     * 备注信息
     */
    @Column(name = "C_MSG", columnDefinition = "VARCHAR")
    @MetaData(value = "备注信息", comments = "备注信息")
    private String msg;

    /**
     * 数据长度，将根据不同的数据长度采取不同的策略；数据大于整数的将采取日期加当日序号的形式
     */
    @Column(name = "L_DATA_LEN", columnDefinition = "INTEGER")
    @MetaData(value = "数据长度", comments = "数据长度，将根据不同的数据长度采取不同的策略；数据大于整数的将采取日期加当日序号的形式")
    private Integer dataLen;

    private static final long serialVersionUID = 1439623644405L;

    @Transient
    private long oldNo = 0;

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType == null ? null : operType.trim();
    }

    public Long getLastNo() {
        return lastNo;
    }

    public void setLastNo(Long lastNo) {
        this.lastNo = lastNo;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate == null ? null : workDate.trim();
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo == null ? null : sysNo.trim();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public Integer getDataLen() {
        return dataLen;
    }

    public void setDataLen(Integer dataLen) {
        this.dataLen = dataLen;
    }

    public synchronized boolean hasSave() {

        return (this.oldNo != this.lastNo);
    }

    public void updateLastNo(Long anLastNo) {
        synchronized (lastNo) {
            if (lastNo < anLastNo) {
                lastNo = anLastNo;
            }
        }
    }

    public void updateOldNo(Long anOldNo) {
        oldNo = anOldNo;
    }

    public synchronized Long addValue() {
        this.lastNo = this.lastNo + 1;
        return this.lastNo;
    }

    public String getBuildType() {
        return this.buildType;
    }

    public void setBuildType(String anBuildType) {
        this.buildType = anBuildType;
    }

    @Override
    public SnoGeneralInfo clone() {
        SnoGeneralInfo info = new SnoGeneralInfo();
        info.dataLen = this.dataLen;
        info.lastNo = this.lastNo;
        info.msg = this.msg;
        info.oldNo = this.oldNo;
        info.operType = this.operType;
        info.sysNo = this.sysNo;
        info.workDate = this.workDate;
        info.buildType = this.buildType;

        return info;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", operType=").append(operType);
        sb.append(", lastNo=").append(lastNo);
        sb.append(", workDate=").append(workDate);
        sb.append(", sysNo=").append(sysNo);
        sb.append(", buildType=").append(buildType);
        sb.append(", msg=").append(msg);
        sb.append(", dataLen=").append(dataLen);
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
        SnoGeneralInfo other = (SnoGeneralInfo) that;
        return (this.getOperType() == null ? other.getOperType() == null
                : this.getOperType().equals(other.getOperType()))
                && (this.getLastNo() == null ? other.getLastNo() == null : this.getLastNo().equals(other.getLastNo()))
                && (this.getWorkDate() == null ? other.getWorkDate() == null
                        : this.getWorkDate().equals(other.getWorkDate()))
                && (this.getSysNo() == null ? other.getSysNo() == null : this.getSysNo().equals(other.getSysNo()))
                && (this.getBuildType() == null ? other.getBuildType() == null
                        : this.getBuildType().equals(other.getBuildType()))
                && (this.getMsg() == null ? other.getMsg() == null : this.getMsg().equals(other.getMsg()))
                && (this.getDataLen() == null ? other.getDataLen() == null
                        : this.getDataLen().equals(other.getDataLen()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOperType() == null) ? 0 : getOperType().hashCode());
        result = prime * result + ((getLastNo() == null) ? 0 : getLastNo().hashCode());
        result = prime * result + ((getWorkDate() == null) ? 0 : getWorkDate().hashCode());
        result = prime * result + ((getSysNo() == null) ? 0 : getSysNo().hashCode());
        result = prime * result + ((getBuildType() == null) ? 0 : getBuildType().hashCode());
        result = prime * result + ((getMsg() == null) ? 0 : getMsg().hashCode());
        result = prime * result + ((getDataLen() == null) ? 0 : getDataLen().hashCode());
        return result;
    }

    public String findMachValue() {
        SerialBuildType sbt = SerialBuildType.checking(this.buildType);
        return sbt.findMachValue();
    }
}