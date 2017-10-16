package com.betterjr.modules.rule.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_RULE_BUSIN_STUB")
public class RuleBusinStub implements BetterjrEntity {
    /**
     * 编号
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "编号", comments = "编号")
    private Long id;

    /**
     * 业务名称；必须和申请业务绑定；系统通过该名称自动找到
     */
    @Column(name = "C_BUSIN_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "业务名称", comments = "业务名称；必须和申请业务绑定；系统通过该名称自动找到")
    private String businName;

    /**
     * 规则主键
     */
    @Column(name = "C_RULE_NO", columnDefinition = "VARCHAR")
    @MetaData(value = "规则主键", comments = "规则主键")
    private String ruleNo;

    /**
     * 规则优先级，越大优先级越高
     */
    @Column(name = "N_PRIORITY", columnDefinition = "VARCHAR")
    @MetaData(value = "规则优先级", comments = "规则优先级，越大优先级越高")
    private Integer rulePrior;

    /**
     * 状态，0停用，1启用
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0停用，1启用")
    private String status;

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

    private static final long serialVersionUID = 1442218275374L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinName() {
        return businName;
    }

    public void setBusinName(String businName) {
        this.businName = businName == null ? null : businName.trim();
    }

    public String getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo == null ? null : ruleNo.trim();
    }

    public Integer getRulePrior() {
        return rulePrior;
    }

    public void setRulePrior(Integer rulePrior) {
        this.rulePrior = rulePrior;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", businName=").append(businName);
        sb.append(", ruleNo=").append(ruleNo);
        sb.append(", rulePrior=").append(rulePrior);
        sb.append(", status=").append(status);
        sb.append(", regDate=").append(regDate);
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
        RuleBusinStub other = (RuleBusinStub) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBusinName() == null ? other.getBusinName() == null
                        : this.getBusinName().equals(other.getBusinName()))
                && (this.getRuleNo() == null ? other.getRuleNo() == null : this.getRuleNo().equals(other.getRuleNo()))
                && (this.getRulePrior() == null ? other.getRulePrior() == null
                        : this.getRulePrior().equals(other.getRulePrior()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBusinName() == null) ? 0 : getBusinName().hashCode());
        result = prime * result + ((getRuleNo() == null) ? 0 : getRuleNo().hashCode());
        result = prime * result + ((getRulePrior() == null) ? 0 : getRulePrior().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        return result;
    }
}