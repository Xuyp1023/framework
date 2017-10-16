package com.betterjr.modules.rule.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.modules.rule.annotation.AnnotRuleFunc;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_RULE_FUN")
public class RuleFunction implements BetterjrEntity {
    /**
     * 规则调用的功能定义名称
     */
    @Id
    @Column(name = "C_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "规则调用的功能定义名称", comments = "规则调用的功能定义名称")
    private String featureName;

    /**
     * 功能定义的类型，对象功能定义，功能定义，宏定义；如果使用宏定义，则直接写在C_CLASS中
     */
    @Column(name = "C_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "功能定义的类型", comments = "功能定义的类型，对象功能定义，功能定义，宏定义；如果使用宏定义，则直接写在C_CLASS中")
    private String fundType;

    /**
     * 规则调用的实现对象；如果是spring的对象；则使用spring中定义的ID
     */
    @Column(name = "C_CLASS", columnDefinition = "VARCHAR")
    @MetaData(value = "规则调用的实现对象", comments = "规则调用的实现对象；如果是spring的对象；则使用spring中定义的ID")
    private String className;

    /**
     * 规则调用的函数
     */
    @Column(name = "C_FUNCNAME", columnDefinition = "VARCHAR")
    @MetaData(value = "规则调用的函数", comments = "规则调用的函数")
    private String funcName;

    /**
     * 规则调用函数的参数类型列表，使用分号分隔
     */
    @Column(name = "C_PARAMS", columnDefinition = "VARCHAR")
    @MetaData(value = "规则调用函数的参数类型列表", comments = "规则调用函数的参数类型列表，使用分号分隔")
    private String paramList;

    /**
     * 规则调用的功能分组，例如：基金业务，票据业务，银证业务等
     */
    @Column(name = "C_GROUP", columnDefinition = "VARCHAR")
    @MetaData(value = "规则调用的功能分组", comments = "规则调用的功能分组，例如：基金业务，票据业务，银证业务等")
    private String groupName;

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
     * 业务描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "业务描述", comments = "业务描述")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String anDescription) {
        description = anDescription;
    }

    private static final long serialVersionUID = 1442218275374L;

    private boolean annot = false;

    public RuleFunction() {

    }

    public RuleFunction(AnnotRuleFunc anRuleFunc, String anName, String anParam, String anParamName) {
        this.featureName = anRuleFunc.name();
        if (anRuleFunc.fundType() == RuleFuncType.MACRO) {
            this.funcName = anName;
        } else {
            this.funcName = anName;
            this.fundType = anRuleFunc.fundType().name();
        }
        this.groupName = anRuleFunc.groupName();
        this.errorInfo = anRuleFunc.errorInfo();
        this.paramList = anParam;
        this.status = "1";
        annot = true;
    }

    public boolean isAnnot() {
        return annot;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName == null ? null : featureName.trim();
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType == null ? null : fundType.trim();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName == null ? null : funcName.trim();
    }

    public String getParamList() {
        return paramList;
    }

    public void setParamList(String paramList) {
        this.paramList = paramList == null ? null : paramList.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", featureName=").append(featureName);
        sb.append(", fundType=").append(fundType);
        sb.append(", className=").append(className);
        sb.append(", funcName=").append(funcName);
        sb.append(", paramList=").append(paramList);
        sb.append(", groupName=").append(groupName);
        sb.append(", errorInfo=").append(errorInfo);
        sb.append(", status=").append(status);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", description=").append(description);
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
        RuleFunction other = (RuleFunction) that;
        return (this.getFeatureName() == null ? other.getFeatureName() == null
                : this.getFeatureName().equals(other.getFeatureName()))
                && (this.getFundType() == null ? other.getFundType() == null
                        : this.getFundType().equals(other.getFundType()))
                && (this.getClassName() == null ? other.getClassName() == null
                        : this.getClassName().equals(other.getClassName()))
                && (this.getFuncName() == null ? other.getFuncName() == null
                        : this.getFuncName().equals(other.getFuncName()))
                && (this.getParamList() == null ? other.getParamList() == null
                        : this.getParamList().equals(other.getParamList()))
                && (this.getGroupName() == null ? other.getGroupName() == null
                        : this.getGroupName().equals(other.getGroupName()))
                && (this.getErrorInfo() == null ? other.getErrorInfo() == null
                        : this.getErrorInfo().equals(other.getErrorInfo()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getRegDate() == null ? other.getRegDate() == null
                        : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null
                        : this.getModiDate().equals(other.getModiDate()))
                && (this.getDescription() == null ? other.getDescription() == null
                        : this.getDescription().equals(other.getDescription()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFeatureName() == null) ? 0 : getFeatureName().hashCode());
        result = prime * result + ((getFundType() == null) ? 0 : getFundType().hashCode());
        result = prime * result + ((getClassName() == null) ? 0 : getClassName().hashCode());
        result = prime * result + ((getFuncName() == null) ? 0 : getFuncName().hashCode());
        result = prime * result + ((getParamList() == null) ? 0 : getParamList().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getErrorInfo() == null) ? 0 : getErrorInfo().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        return result;
    }
}