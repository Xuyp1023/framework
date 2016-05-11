package com.betterjr.modules.sys.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CFG_SNOGENERAL")
public class SnoGeneralInfo implements BetterjrEntity {
    /**
     * 流水号，用于数据更新
     */
    @Id
    @Column(name = "ID",  columnDefinition="INTEGER" )
    @MetaData( value="流水号", comments = "流水号，用于数据更新")
    private Integer id;

    /**
     * 产生字段，类.属性
     */
    @Column(name = "C_OPERTYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="产生字段", comments = "产生字段，类.属性")
    private String operType;

    /**
     * 最大序列号
     */
    @Column(name = "L_LASTNO",  columnDefinition="INTEGER" )
    @MetaData( value="最大序列号", comments = "最大序列号")
    private Long lastNo;

    /**
     * 最后更新日期
     */
    @Column(name = "D_LASTDATE",  columnDefinition="VARCHAR" )
    @MetaData( value="最后更新日期", comments = "最后更新日期")
    private String workDate;

    /**
     * 使用的系统简称
     */
    @Column(name = "C_SYSNO",  columnDefinition="VARCHAR" )
    @MetaData( value="使用的系统简称", comments = "使用的系统简称")
    private String sysNo;

    /**
     * 产生类型，0每日累计，1单日累计
     */
    @Column(name = "C_TYPE",  columnDefinition="VARCHAR" )
    @MetaData( value="产生类型", comments = "产生类型，0每日累计，1单日累计")
    private Boolean oneDay;

    /**
     * 备注信息
     */
    @Column(name = "C_MSG",  columnDefinition="VARCHAR" )
    @MetaData( value="备注信息", comments = "备注信息")
    private String msg;

    /**
     * 数据长度，将根据不同的数据长度采取不同的策略；数据大于整数的将采取日期加当日序号的形式
     */
    @Column(name = "L_DATA_LEN",  columnDefinition="INTEGER" )
    @MetaData( value="数据长度", comments = "数据长度，将根据不同的数据长度采取不同的策略；数据大于整数的将采取日期加当日序号的形式")
    private Integer dataLen;

    private static final long serialVersionUID = 1439623644405L;

    @Transient
    private long oldNo = 0;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Boolean getOneDay() {
        return oneDay;
    }

    public void setOneDay(Boolean oneDay) {
        this.oneDay = oneDay;
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

    public void updateLastNo(Long anLastNo){
    	synchronized (lastNo) {
    		 if (lastNo < anLastNo){
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
     public SnoGeneralInfo clone() {
         SnoGeneralInfo info = new SnoGeneralInfo();
         info.dataLen = this.dataLen;
         info.lastNo = this.lastNo;
         info.msg = this.msg;
         info.oldNo = this.oldNo;
         info.operType = this.operType;
         info.sysNo = this.sysNo;
         info.workDate = this.workDate;
         info.oneDay = this.oneDay;
         info.id = this.id;

         return info;
     }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operType=").append(operType);
        sb.append(", lastNo=").append(lastNo);
        sb.append(", workDate=").append(workDate);
        sb.append(", sysNo=").append(sysNo);
        sb.append(", oneDay=").append(oneDay);
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
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOperType() == null ? other.getOperType() == null : this.getOperType().equals(other.getOperType()))
            && (this.getLastNo() == null ? other.getLastNo() == null : this.getLastNo().equals(other.getLastNo()))
            && (this.getWorkDate() == null ? other.getWorkDate() == null : this.getWorkDate().equals(other.getWorkDate()))
            && (this.getSysNo() == null ? other.getSysNo() == null : this.getSysNo().equals(other.getSysNo()))
            && (this.getOneDay() == null ? other.getOneDay() == null : this.getOneDay().equals(other.getOneDay()))
            && (this.getMsg() == null ? other.getMsg() == null : this.getMsg().equals(other.getMsg()))
            && (this.getDataLen() == null ? other.getDataLen() == null : this.getDataLen().equals(other.getDataLen()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOperType() == null) ? 0 : getOperType().hashCode());
        result = prime * result + ((getLastNo() == null) ? 0 : getLastNo().hashCode());
        result = prime * result + ((getWorkDate() == null) ? 0 : getWorkDate().hashCode());
        result = prime * result + ((getSysNo() == null) ? 0 : getSysNo().hashCode());
        result = prime * result + ((getOneDay() == null) ? 0 : getOneDay().hashCode());
        result = prime * result + ((getMsg() == null) ? 0 : getMsg().hashCode());
        result = prime * result + ((getDataLen() == null) ? 0 : getDataLen().hashCode());
        return result;
    }
}