package com.betterjr.modules.rule.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.modules.rule.annotation.AnnotRuleProcess;
import com.betterjr.modules.rule.service.BusinRuleService;

import java.util.List;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_RULE_BUSIN")
public class RuleBusiness implements BetterjrEntity {
    /**
     * 业务编码
     */
    @Id
    @Column(name = "ID", columnDefinition = "INTEGER")
    @MetaData(value = "业务编码", comments = "业务编码")
    private Long id;

    /**
     * 业务名称；必须和申请业务绑定；系统通过该名称自动找到
     */
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "业务名称", comments = "业务名称；必须和申请业务绑定；系统通过该名称自动找到")
    private String businName;

    /**
     * 业务的类型，对象功能定义，功能定义，宏定义；如果使用宏定义，则直接写在C_CLASS中
     */
    @Column(name = "C_FUNC", columnDefinition = "VARCHAR")
    @MetaData(value = "入口功能", comments = "入口功能，类名+点+功能名称")
    private String enterFunc;

    /**
     * 业务的类型，对象功能定义，功能定义，宏定义；如果使用宏定义，则直接写在C_CLASS中
     */
    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "业务的类型", comments = "业务的类型，对象功能定义，功能定义，宏定义；如果使用宏定义，则直接写在C_CLASS中")
    private String businType;

    /**
     * 业务过程中执行内容；可以是单一的业务
     */
    @Column(name = "C_EXECUTE_CONTENT", columnDefinition = "VARCHAR")
    @MetaData(value = "业务过程中执行内容", comments = "业务过程中执行内容；可以是单一的业务")
    private String execContent;

    /**
     * 业务描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "业务描述", comments = "业务描述")
    private String description;

    /**
     * 规则调用的功能分组，例如：基金业务，票据业务，银证业务等
     */
    @Column(name = "C_GROUP", columnDefinition = "VARCHAR")
    @MetaData(value = "规则调用的功能分组", comments = "规则调用的功能分组，例如：基金业务，票据业务，银证业务等")
    private String businGroup;

    /**
     * 错误信息
     */
    @Column(name = "C_ERRORINFO", columnDefinition = "VARCHAR")
    @MetaData(value = "错误信息", comments = "错误信息")
    private String errorInfo;

    /**
     * 状态，0停用，1启用
     */
    @Column(name = "C_STATUS", columnDefinition = "VARCHAR")
    @MetaData(value = "状态", comments = "状态，0停用，1启用")
    private String status;

    /**
     * 登记日期
     */
    @Column(name = "D_REGDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "登记日期", comments = "登记日期")
    private String regDate;

    /**
     * 修改日期
     */
    @Column(name = "D_MODIDATE", columnDefinition = "VARCHAR")
    @MetaData(value = "修改日期", comments = "修改日期")
    private String modiDate;
 
    /**
     * 版本号，同一个业务只能有一个版本号激活
     */
    @Column(name = "C_VERSION", columnDefinition = "VARCHAR")
    @MetaData(value = "版本号", comments = "版本号，同一个业务只能有一个版本号激活")
    private String businVersion;

    private static final long serialVersionUID = 1442218275374L;

    @Transient
    private List rules;

    @Transient
    private List<WorkRuleValidator> validators;
    
    public List<WorkRuleValidator> getValidators() {
        return this.validators;
    }

    public void setValidators(List<WorkRuleValidator> anValidators) {
        this.validators = anValidators;
    }

    public String getEnterFunc() {
        return enterFunc;
    }

    public RuleBusiness() {
    }

    public RuleBusiness(AnnotRuleProcess anRuleProc, String anEnterName) {
        this.businName = anRuleProc.name();
        this.enterFunc = anEnterName;
        this.businGroup = anRuleProc.businGroup();
        this.description = anRuleProc.description();
        this.errorInfo = anRuleProc.errorInfo();
    }

    public void clearRule() {
        execContent = BusinRuleService.initStatement(execContent);
    }

    public void setEnterFunc(String enterFunc) {
        this.enterFunc = enterFunc;
    }

    public List getRules() {
        return rules;
    }

    public void setRules(List anRules) {
        rules = anRules;
    }

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

    public String getBusinType() {
        return businType;
    }

    public void setBusinType(String businType) {
        this.businType = businType == null ? null : businType.trim();
    }

    public String getExecContent() {
        return execContent;
    }

    public void setExecContent(String execContent) {
        this.execContent = execContent == null ? null : execContent.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getBusinGroup() {
        return businGroup;
    }

    public void setBusinGroup(String businGroup) {
        this.businGroup = businGroup == null ? null : businGroup.trim();
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo == null ? null : errorInfo.trim();
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
 
    public String getBusinVersion() {
        return businVersion;
    }

    public void setBusinVersion(String businVersion) {
        this.businVersion = businVersion == null ? null : businVersion.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", businName=").append(businName);
        sb.append(", enterFunc=").append(enterFunc);
        sb.append(", businType=").append(businType);
        sb.append(", execContent=").append(execContent);
        sb.append(", description=").append(description);
        sb.append(", businGroup=").append(businGroup);
        sb.append(", errorInfo=").append(errorInfo);
        sb.append(", status=").append(status);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", businVersion=").append(businVersion);
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
        RuleBusiness other = (RuleBusiness) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBusinName() == null ? other.getBusinName() == null : this.getBusinName().equals(other.getBusinName()))
                && (this.getEnterFunc() == null ? other.getEnterFunc() == null : this.getEnterFunc().equals(other.getEnterFunc()))
                && (this.getBusinType() == null ? other.getBusinType() == null : this.getBusinType().equals(other.getBusinType()))
                && (this.getExecContent() == null ? other.getExecContent() == null : this.getExecContent().equals(other.getExecContent()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getBusinGroup() == null ? other.getBusinGroup() == null : this.getBusinGroup().equals(other.getBusinGroup()))
                && (this.getErrorInfo() == null ? other.getErrorInfo() == null : this.getErrorInfo().equals(other.getErrorInfo()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getBusinVersion() == null ? other.getBusinVersion() == null : this.getBusinVersion().equals(other.getBusinVersion()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBusinName() == null) ? 0 : getBusinName().hashCode());
        result = prime * result + ((getBusinType() == null) ? 0 : getBusinType().hashCode());
        result = prime * result + ((getEnterFunc() == null) ? 0 : getEnterFunc().hashCode());
        result = prime * result + ((getExecContent() == null) ? 0 : getExecContent().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getBusinGroup() == null) ? 0 : getBusinGroup().hashCode());
        result = prime * result + ((getErrorInfo() == null) ? 0 : getErrorInfo().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getBusinVersion() == null) ? 0 : getBusinVersion().hashCode());
        return result;
    }
}