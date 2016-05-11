package com.betterjr.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "T_CFG_SNOGENERAL")
public class SnoGeneralInfo implements Serializable {
    /**
     * This attribute maps to the column C_OPERTYPE in the T_CFG_SNOGENERAL
     * table. 产生字段，类.属性
     */

    @Column(name = "C_OPERTYPE")
    private String operType;

    /**
     * This attribute maps to the column L_LASTNO in the T_CFG_SNOGENERAL table.
     * 最大序列号
     */
    @Column(name = "L_LASTNO")
    private long lastNo = 0l;

    /**
     * This attribute maps to the column D_LASTDATE in the T_CFG_SNOGENERAL
     * table. 最后更新日期
     */
    @Column(name = "D_LASTDATE")
    private String workDate;

    /**
     * This attribute maps to the column C_SYSNO in the T_CFG_SNOGENERAL table.
     * 使用的系统简称
     */
    @Column(name = "C_SYSNO")
    private String sysNo;

    /**
     * This attribute maps to the column C_TYPE in the T_CFG_SNOGENERAL table.
     * 产生类型，0每日累计，1单日累计
     */
    @Column(name = "C_TYPE")
    private String workType;

    /**
     * This attribute maps to the column C_MSG in the T_CFG_SNOGENERAL table.
     * 备注信息
     */
    @Column(name = "C_MSG")
    private String msg;

    /**
     * This attribute maps to the column L_LEN in the T_CFG_SNOGENERAL table.
     * null
     */
    @Column(name = "L_LENGTH")
    private short dataLength;

    /**
     * This field corresponds to the database table T_CFG_SNOGENERAL
     */
    private static final long serialVersionUID = 1438071623307L;

    @Transient
    private long oldNo = 0;

    /**
     * 
     * <p>
     * 判断是否更新到数据中
     * </p>
     */
    public synchronized boolean hasSave() {

        return (this.oldNo != this.lastNo);
    }

    public void updateOldNo(long anOldNo) {
        oldNo = anOldNo;
    }

    public synchronized long addValue() {
        this.lastNo = this.lastNo + 1;
        return this.lastNo;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String anOperType) {
        operType = anOperType;
    }

    public long getLastNo() {
        return lastNo;
    }

    public void setLastNo(long anLastNo) {
        lastNo = anLastNo;
        this.oldNo = anLastNo;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String anWorkDate) {
        workDate = anWorkDate;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String anSysNo) {
        sysNo = anSysNo;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String anWorkType) {
        workType = anWorkType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String anMsg) {
        msg = anMsg;
    }

    public short getDataLength() {
        return dataLength;
    }

    public void setDataLength(short anDataLength) {
        dataLength = anDataLength;
    }

    public SnoGeneralInfo clone() {
        SnoGeneralInfo info = new SnoGeneralInfo();
        info.dataLength = this.dataLength;
        info.lastNo = this.lastNo;
        info.msg = this.msg;
        info.oldNo = this.oldNo;
        info.operType = this.operType;
        info.sysNo = this.sysNo;
        info.workDate = this.workDate;
        info.workType = this.workType;

        return info;
    }

    /**
     * This method corresponds to the database table T_CFG_SNOGENERAL
     */
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
        sb.append(", workType=").append(workType);
        sb.append(", msg=").append(msg);
        sb.append(", dataLength=").append(dataLength);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}