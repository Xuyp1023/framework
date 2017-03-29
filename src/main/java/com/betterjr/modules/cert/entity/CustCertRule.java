package com.betterjr.modules.cert.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.selectkey.SerialGenerator;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_CUST_CERTINFO_RULE")
public class CustCertRule implements BetterjrEntity {
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 证书序列号
     */
    @Column(name = "C_SERIALNO", columnDefinition = "VARCHAR")
    @MetaData(value = "证书序列号", comments = "证书序列号")
    private String serialNo;

    /**
     * 客户名称
     */
    @Column(name = "C_CUSTNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "客户名称", comments = "客户名称")
    private String custName;

    /**
     * 使用证书的角色，内置角色，颁发证书的指定，例如：CORE_USER，SUPPLIER_USER，FACTOR_USER，SELLER_USER
     */
    @Column(name = "C_RULE", columnDefinition = "VARCHAR")
    @MetaData(value = " 使用证书的角色", comments = "内置角色，颁发证书的指定，例如：CORE_USER，SUPPLIER_USER，FACTOR_USER，SELLER_USER")
    private String rule;

    private static final long serialVersionUID = 1439797394180L;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(final String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(final String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    public String getRule() {
        return this.rule;
    }

    public void setRule(final String anRule) {
        this.rule = anRule;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("rule = ").append(rule);
        sb.append(", serialNo=").append(serialNo);
        sb.append(", custName=").append(custName);
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
        final CustCertRule other = (CustCertRule) that;
        return (this.getSerialNo() == null ? other.getSerialNo() == null : this.getSerialNo().equals(other.getSerialNo()))
                && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCustName() == null ? other.getCustName() == null : this.getCustName().equals(other.getCustName()))
                && (this.getRule() == null ? other.getRule() == null : this.getRule().equals(other.getRule()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSerialNo() == null) ? 0 : getSerialNo().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCustName() == null) ? 0 : getCustName().hashCode());
        result = prime * result + ((getRule() == null) ? 0 : getRule().hashCode());

        return result;
    }

    /**
     * @param anRule
     * @param anSerialNo
     *
     */
    public void initDefValue(final String anSerialNo, final String anRule, final String anCustName) {
        this.id = SerialGenerator.getLongValue("CustCertRule.id");

        this.serialNo = anSerialNo;
        this.rule = anRule;
        this.custName = anCustName;
    }

}