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
import com.betterjr.common.utils.DictUtils;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_SALE_TRADEACCO")
public class SaleTradeAccountInfo implements BetterjrEntity {
    /**
     * 交易账户
     */
    @Id
    @Column(name = "C_TRADEACCO", columnDefinition = "VARCHAR")
    @MetaData(value = "交易账户", comments = "交易账户")
    private String tradeAccount;

    /**
     * 客户编号
     */
    @Column(name = "L_CUSTNO", columnDefinition = "INTEGER")
    @MetaData(value = "客户编号", comments = "客户编号")
    private Long custNo;

    /**
     * 网点编码
     */
    @Column(name = "C_NETNO", columnDefinition = "VARCHAR")
    @MetaData(value = "网点编码", comments = "网点编码")
    private String netNo;

    /**
     * 主交易账户； 0 主交易账户 1 非主交易账户
     */
    @Column(name = "C_MAIN", columnDefinition = "VARCHAR")
    @MetaData(value = "主交易账户", comments = "主交易账户； 0 主交易账户 1 非主交易账户")
    private Boolean mainAccount;

    /**
     * 来源业务代码；该业务属于开户还是做增卡来的
     */
    @Column(name = "C_REGCODE", columnDefinition = "VARCHAR")
    @MetaData(value = "来源业务代码", comments = "来源业务代码；该业务属于开户还是做增卡来的")
    private String regCode;

    /**
     * 开户日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "开户日期", comments = "开户日期")
    private String regDate;

    /**
     * 最后更新日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "最后更新日期", comments = "最后更新日期")
    private String modiDate;

    /**
     * 登记状态
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "登记状态", comments = "登记状态")
    private String status;

    /**
     * 上次登记状态
     */
    @Column(name = "C_LAST_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "上次登记状态", comments = "上次登记状态")
    private String lastStatus;

    /**
     * 清算状态
     */
    @Column(name = "C_CLEAR_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "清算状态", comments = "清算状态")
    private String clearStatus;

    /**
     * 特殊标志
     */
    @Column(name = "C_SPECIALFLAG", columnDefinition = "VARCHAR")
    @MetaData(value = "特殊标志", comments = "特殊标志")
    private String specialFlag;

    /**
     * 实体渠道 0 非柜台渠道 1 柜台渠道
     */
    @Column(name = "C_COUNTER", columnDefinition = "VARCHAR")
    @MetaData(value = "实体渠道 0 非柜台渠道 1 柜台渠道", comments = "实体渠道 0 非柜台渠道 1 柜台渠道")
    private Boolean conter;

    /**
     * 客户允许的交易手段；0柜台，1电话，2网上交易，3传真，4手机
     */
    @Column(name = "C_ACCEPT", columnDefinition = "VARCHAR")
    @MetaData(value = "客户允许的交易手段", comments = "客户允许的交易手段；0柜台，1电话，2网上交易，3传真，4手机")
    private String acceptMode;

    private static final long serialVersionUID = 1440667936392L;

    public String getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(String tradeAccount) {
        this.tradeAccount = tradeAccount == null ? null : tradeAccount.trim();
    }

    public Long getCustNo() {
        return custNo;
    }

    public void setCustNo(Long custNo) {
        this.custNo = custNo;
    }

    public String getNetNo() {
        return netNo;
    }

    public void setNetNo(String netNo) {
        this.netNo = netNo == null ? null : netNo.trim();
    }

    public Boolean getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(Boolean mainAccount) {
        this.mainAccount = mainAccount;
    }

    public String getRegCode() {
        return regCode;
    }

    public void setRegCode(String regCode) {
        this.regCode = regCode == null ? null : regCode.trim();
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

    public String getClearStatus() {
        return clearStatus;
    }

    public void setClearStatus(String clearStatus) {
        this.clearStatus = clearStatus == null ? null : clearStatus.trim();
    }

    public String getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(String specialFlag) {
        this.specialFlag = specialFlag == null ? null : specialFlag.trim();
    }

    public Boolean getConter() {
        return conter;
    }

    public void setConter(Boolean conter) {
        this.conter = conter;
    }

    public String getAcceptMode() {
        return acceptMode;
    }

    public void setAcceptMode(String acceptMode) {
        this.acceptMode = acceptMode == null ? null : acceptMode.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tradeAccount=").append(tradeAccount);
        sb.append(", custNo=").append(custNo);
        sb.append(", netNo=").append(netNo);
        sb.append(", mainAccount=").append(mainAccount);
        sb.append(", regCode=").append(regCode);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", status=").append(status);
        sb.append(", lastStatus=").append(lastStatus);
        sb.append(", clearStatus=").append(clearStatus);
        sb.append(", specialFlag=").append(specialFlag);
        sb.append(", conter=").append(conter);
        sb.append(", acceptMode=").append(acceptMode);
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
        SaleTradeAccountInfo other = (SaleTradeAccountInfo) that;
        return (this.getTradeAccount() == null ? other.getTradeAccount() == null
                : this.getTradeAccount().equals(other.getTradeAccount()))
                && (this.getCustNo() == null ? other.getCustNo() == null : this.getCustNo().equals(other.getCustNo()))
                && (this.getNetNo() == null ? other.getNetNo() == null : this.getNetNo().equals(other.getNetNo()))
                && (this.getMainAccount() == null ? other.getMainAccount() == null
                        : this.getMainAccount().equals(other.getMainAccount()))
                && (this.getRegCode() == null ? other.getRegCode() == null
                        : this.getRegCode().equals(other.getRegCode()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getLastStatus() == null ? other.getLastStatus() == null
                        : this.getLastStatus().equals(other.getLastStatus()))
                && (this.getClearStatus() == null ? other.getClearStatus() == null
                        : this.getClearStatus().equals(other.getClearStatus()))
                && (this.getSpecialFlag() == null ? other.getSpecialFlag() == null
                        : this.getSpecialFlag().equals(other.getSpecialFlag()))
                && (this.getConter() == null ? other.getConter() == null : this.getConter().equals(other.getConter()))
                && (this.getAcceptMode() == null ? other.getAcceptMode() == null
                        : this.getAcceptMode().equals(other.getAcceptMode()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTradeAccount() == null) ? 0 : getTradeAccount().hashCode());
        result = prime * result + ((getCustNo() == null) ? 0 : getCustNo().hashCode());
        result = prime * result + ((getNetNo() == null) ? 0 : getNetNo().hashCode());
        result = prime * result + ((getMainAccount() == null) ? 0 : getMainAccount().hashCode());
        result = prime * result + ((getRegCode() == null) ? 0 : getRegCode().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getLastStatus() == null) ? 0 : getLastStatus().hashCode());
        result = prime * result + ((getClearStatus() == null) ? 0 : getClearStatus().hashCode());
        result = prime * result + ((getSpecialFlag() == null) ? 0 : getSpecialFlag().hashCode());
        result = prime * result + ((getConter() == null) ? 0 : getConter().hashCode());
        result = prime * result + ((getAcceptMode() == null) ? 0 : getAcceptMode().hashCode());
        return result;
    }

    public SaleTradeAccountInfo() {

    }

    public SaleTradeAccountInfo(SaleAccoRequestInfo request) {
        BeanMapper.copy(request, this);
        this.setRegDate(BetterDateUtils.getNumDate());
        this.setModiDate(BetterDateUtils.getNumDateTime());
        this.setRegCode(request.getBusinFlag());
        this.setMainAccount(Boolean.TRUE);
        this.setStatus("8");
        this.setLastStatus(this.status);
        this.setClearStatus("0");
        this.setConter(false);
        this.setAcceptMode(
                DictUtils.getDictCode("SaleDefAcceptModes", request.getAcceptMode(), request.getAcceptMode()));
        this.setTradeAccount(SerialGenerator.getTradeAcco(request.getNetNo()));
        request.setTradeAccount(this.getTradeAccount());

    }
}