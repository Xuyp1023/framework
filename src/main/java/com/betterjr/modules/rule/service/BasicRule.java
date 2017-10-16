package com.betterjr.modules.rule.service;

import com.betterjr.common.exception.BytterException;
import com.betterjr.modules.rule.entity.BaseRuleFace;
import com.betterjr.modules.rule.entity.RuleFace;
import com.betterjr.modules.rule.entity.RuleObjectFace;

/**
 * 规则定义，主要用于注入的规则处理
 * 
 * @author zhoucy
 *
 */
public abstract class BasicRule implements BaseRuleFace {

    /**
     * 规则简称，和业务过程关联时使用，即：在定义业务过程中的规则使用简称
     */
    protected String ruleNo = "ruleNo";

    /**
     * 规则名称
     */
    protected String name = "rule";

    protected String groupName;
    /**
     * 规则描述
     */
    protected String description;

    protected Integer priority;

    protected QLExpressContext context;

    @Override
    public QLExpressContext getContext() {
        return context;
    }

    @Override
    public void setContext(QLExpressContext anContext) {
        context = anContext;
    }

    public BasicRule() {

    }

    public void setGroupName(String anValue) {

        this.groupName = anValue;
    }

    @Override
    public String getGroupName() {

        return this.groupName;
    }

    public BasicRule(final String name) {}

    public BasicRule(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public abstract boolean evaluate();

    @Override
    public abstract Object execute();

    @Override
    public String getRuleName() {
        return name;
    }

    @Override
    public void setRuleNo(String anRuleNo) {
        ruleNo = anRuleNo;
    }

    @Override
    public String getRuleNo() {

        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getPriority() {
        return priority;
    }

    @Override
    public void setPriority(Integer anValue) {

        this.priority = anValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicRule basicRule = (BasicRule) o;

        if (priority != basicRule.priority) return false;
        if (!name.equals(basicRule.name)) return false;
        return !(description != null ? !description.equals(basicRule.description) : basicRule.description != null);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + priority;
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public RuleObjectFace clone() {
        RuleObjectFace obj;
        try {
            obj = (RuleObjectFace) super.clone();
            return obj;
        }
        catch (CloneNotSupportedException e) {
            throw new BytterException(41009, "CloneNotSupportedException", e);
        }
    }

    @Override
    public int compareTo(final RuleFace otherRule) {
        String otherName = otherRule.getRuleNo();
        int otherPriority = otherRule.getPriority();
        int priority = getPriority();

        if (priority < otherPriority) {
            return -1;
        } else if (priority > otherPriority) {
            return 1;
        } else {
            return this.ruleNo.compareTo(otherName);
        }
    }

}
