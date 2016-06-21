package com.betterjr.modules.rule.entity;

import com.betterjr.common.annotation.*;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.modules.rule.service.BusinRuleService;

import javax.persistence.*;

@Access(AccessType.FIELD)
@Entity
@Table(name = "T_RULE_INFO")
public class RuleInfo implements BetterjrEntity, RuleFace {
    /**
     * 规则主键
     */
    @Id
    @Column(name = "C_RULE_NO", columnDefinition = "VARCHAR")
    @MetaData(value = "规则主键", comments = "规则主键")
    private String ruleNo;

    /**
     * 规则名称
     */
    @Column(name = "C_RULE_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "规则名称", comments = "规则名称")
    private String ruleName;

    /**
     * 分组类型
     */
    @Column(name = "C_GROUP_TYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "分组类型", comments = "分组类型")
    private String groupType;

    /**
     * 分组的名称，在一个规则中相同组只会执行一个规则
     */
    @Column(name = "C_GROUP_NAME", columnDefinition = "VARCHAR")
    @MetaData(value = "分组的名称", comments = "分组的名称，在一个规则中相同组只会执行一个规则")
    private String groupName;

    /**
     * 规则进入条件
     */
    @Column(name = "C_MATCH_CONDITION", columnDefinition = "VARCHAR")
    @MetaData(value = "规则进入条件", comments = "规则进入条件")
    private String matchCond;

    /**
     * 规则执行内容
     */
    @Column(name = "C_EXECUTE_CONTENT", columnDefinition = "VARCHAR")
    @MetaData(value = "规则执行内容", comments = "规则执行内容")
    private String execContent;

    /**
     * 生效日期
     */
    @Column(name = "D_EFFECTIVE", columnDefinition = "VARCHAR")
    @MetaData(value = "生效日期", comments = "生效日期")
    private String effectDate;

    /**
     * 过期日期
     */
    @Column(name = "D_EXPIRES", columnDefinition = "VARCHAR")
    @MetaData(value = "过期日期", comments = "过期日期")
    private String validDate;

    /**
     * 实现语言，可以是JAVASCRIPT,groovy,QLExpress
     */
    @Column(name = "C_DIALECT", columnDefinition = "VARCHAR")
    @MetaData(value = "实现语言", comments = "实现语言，可以是JAVASCRIPT,groovy,QLExpress")
    private String ruleDialect;

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

    /**
     * 业务描述
     */
    @Column(name = "C_DESCRIPTION", columnDefinition = "VARCHAR")
    @MetaData(value = "业务描述", comments = "业务描述")
    private String description;

    /**
     * 参数处理模式
     */
    @Column(name = "C_MODE", columnDefinition = "VARCHAR")
    @MetaData(value = "参数处理模式", comments = "参数处理模式")
    private String workMode;

    /**
     * 参数控制
     */
    @Column(name = "C_CTRLTYPE", columnDefinition = "VARCHAR")
    @MetaData(value = "参数控制", comments = "参数控制")
    private String itemType;

    public String getDescription() {
        return description;
    }

    public Integer getPriority() {
        return 0;
    }

    public void setPriority(Integer anPriority) {

    }

    public void setDescription(String anDescription) {
        description = anDescription;
    }

    public String getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo == null ? null : ruleNo.trim();
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType == null ? null : groupType.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getMatchCond() {
        return matchCond;
    }

    public void setMatchCond(String matchCond) {
        this.matchCond = matchCond == null ? null : matchCond.trim();
    }

    public String getExecContent() {
        return execContent;
    }

    public void setExecContent(String execContent) {
        this.execContent = execContent == null ? null : execContent.trim();
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate == null ? null : effectDate.trim();
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate == null ? null : validDate.trim();
    }

    public String getRuleDialect() {
        return ruleDialect;
    }

    public void setRuleDialect(String ruleDialect) {
        this.ruleDialect = ruleDialect == null ? null : ruleDialect.trim();
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

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void clearRule() {
        matchCond = BusinRuleService.initStatement(matchCond);
        execContent = BusinRuleService.initStatement(execContent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ruleNo=").append(ruleNo);
        sb.append(", ruleName=").append(ruleName);
        sb.append(", groupType=").append(groupType);
        sb.append(", groupName=").append(groupName);
        sb.append(", matchCond=").append(matchCond);
        sb.append(", execContent=").append(execContent);
        sb.append(", effectDate=").append(effectDate);
        sb.append(", validDate=").append(validDate);
        sb.append(", ruleDialect=").append(ruleDialect);
        sb.append(", regDate=").append(regDate);
        sb.append(", modiDate=").append(modiDate);
        sb.append(", description=").append(description);
        sb.append(", workMode=").append(workMode);
        sb.append(", itemType=").append(itemType);
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
        RuleInfo other = (RuleInfo) that;
        return (this.getRuleNo() == null ? other.getRuleNo() == null : this.getRuleNo().equals(other.getRuleNo()))
                && (this.getRuleName() == null ? other.getRuleName() == null : this.getRuleName().equals(other.getRuleName()))
                && (this.getGroupType() == null ? other.getGroupType() == null : this.getGroupType().equals(other.getGroupType()))
                && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
                && (this.getMatchCond() == null ? other.getMatchCond() == null : this.getMatchCond().equals(other.getMatchCond()))
                && (this.getExecContent() == null ? other.getExecContent() == null : this.getExecContent().equals(other.getExecContent()))
                && (this.getEffectDate() == null ? other.getEffectDate() == null : this.getEffectDate().equals(other.getEffectDate()))
                && (this.getValidDate() == null ? other.getValidDate() == null : this.getValidDate().equals(other.getValidDate()))
                && (this.getRuleDialect() == null ? other.getRuleDialect() == null : this.getRuleDialect().equals(other.getRuleDialect()))
                && (this.getRegDate() == null ? other.getRegDate() == null : this.getRegDate().equals(other.getRegDate()))
                && (this.getModiDate() == null ? other.getModiDate() == null : this.getModiDate().equals(other.getModiDate()))
                && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
                && (this.getWorkMode() == null ? other.getWorkMode() == null : this.getWorkMode().equals(other.getWorkMode()))
                && (this.getItemType() == null ? other.getItemType() == null : this.getItemType().equals(other.getItemType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRuleNo() == null) ? 0 : getRuleNo().hashCode());
        result = prime * result + ((getRuleName() == null) ? 0 : getRuleName().hashCode());
        result = prime * result + ((getGroupType() == null) ? 0 : getGroupType().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getMatchCond() == null) ? 0 : getMatchCond().hashCode());
        result = prime * result + ((getExecContent() == null) ? 0 : getExecContent().hashCode());
        result = prime * result + ((getEffectDate() == null) ? 0 : getEffectDate().hashCode());
        result = prime * result + ((getValidDate() == null) ? 0 : getValidDate().hashCode());
        result = prime * result + ((getRuleDialect() == null) ? 0 : getRuleDialect().hashCode());
        result = prime * result + ((getRegDate() == null) ? 0 : getRegDate().hashCode());
        result = prime * result + ((getModiDate() == null) ? 0 : getModiDate().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getWorkMode() == null) ? 0 : getWorkMode().hashCode());
        result = prime * result + ((getItemType() == null) ? 0 : getItemType().hashCode());
        return result;
    }

    @Override
    public int compareTo(RuleFace anRule) {

        return 0;
    }
}