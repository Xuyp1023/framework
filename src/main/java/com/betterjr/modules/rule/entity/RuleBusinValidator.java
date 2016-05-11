package com.betterjr.modules.rule.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_RULE_BUSIN_VALIDATOR")
public class RuleBusinValidator implements BetterjrEntity {
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
     * 验证名称
     */
    @Column(name = "C_VALID_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "验证名称", comments = "验证名称")
    private String validName;

    /**
     * 规则优先级，越大优先级越高
     */
    @Column(name = "N_PRIORITY", columnDefinition = "INTEGER")
    @MetaData(value = "规则优先级", comments = "规则优先级，越大优先级越高")
    @OrderBy
    private Integer validPrior;

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

    /**
     * 显示名称
     */
    @Column(name = "C_SHOW_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "显示名称", comments = "显示名称")
    private String showName;

    /**
     * 字段名称
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "字段名称", comments = "字段名称")
    private String fieldName;

    /**
     * 0不是必须内容，1必须内容
     */
    @Column(name = "C_MUSTITEM", columnDefinition = "VARCHAR")
    @MetaData(value = "0不是必须内容", comments = "0不是必须内容，1必须内容")
    private String mustItem;
    
    private static final long serialVersionUID = -4810839603379461453L;

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

    public String getValidName() {
        return validName;
    }

    public void setValidName(String validName) {
        this.validName = validName == null ? null : validName.trim();
    }

    public Integer getValidPrior() {
        return validPrior;
    }

    public void setValidPrior(Integer validPrior) {
        this.validPrior = validPrior;
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

    public String getShowName() {
        return this.showName;
    }

    public void setShowName(String anShowName) {
        this.showName = anShowName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String anFieldName) {
        this.fieldName = anFieldName;
    }

    public String getMustItem() {
        return this.mustItem;
    }

    public void setMustItem(String anMustItem) {
        this.mustItem = anMustItem;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", businName=").append(businName);
        sb.append(", validName=").append(validName);
        sb.append(", showName=").append(showName);
        sb.append(", fieldName=").append(fieldName);
        sb.append(", mustItem=").append(mustItem);
        sb.append(", validPrior=").append(validPrior);
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
        RuleBusinValidator other = (RuleBusinValidator) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBusinName() == null ? other.getBusinName() == null : this.getBusinName().equals(other.getBusinName()))
                && (this.getValidName() == null ? other.getValidName() == null : this.getValidName().equals(other.getValidName()))
                && (this.getShowName() == null ? other.getShowName() == null : this.getShowName().equals(other.getShowName()))
                && (this.getFieldName() == null ? other.getFieldName() == null : this.getFieldName().equals(other.getFieldName()))
                && (this.getMustItem() == null ? other.getMustItem() == null : this.getMustItem().equals(other.getMustItem()))
                && (this.getValidPrior() == null ? other.getValidPrior() == null : this.getValidPrior().equals(other.getValidPrior()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBusinName() == null) ? 0 : getBusinName().hashCode());
        result = prime * result + ((getValidName() == null) ? 0 : getValidName().hashCode());
        result = prime * result + ((getShowName() == null) ? 0 : getShowName().hashCode());
        result = prime * result + ((getFieldName() == null) ? 0 : getFieldName().hashCode());
        result = prime * result + ((getMustItem() == null) ? 0 : getMustItem().hashCode());
        result = prime * result + ((getValidPrior() == null) ? 0 : getValidPrior().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        return result;
    }
}