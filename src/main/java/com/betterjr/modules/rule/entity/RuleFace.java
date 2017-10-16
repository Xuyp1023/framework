package com.betterjr.modules.rule.entity;

public interface RuleFace extends Comparable<RuleFace> {

    // 规则名称
    String getRuleNo();

    // 规则名称
    void setRuleNo(String anValue);

    // 规则名称
    String getRuleName();

    // 规则组信息，业务过程中，相同组的规则中只要有一个规则执行成功，该组其他规则不再执行
    String getGroupName();

    // 规则描述
    String getDescription();

    // 规则在过程中的优先级
    Integer getPriority();

    // 规则在过程中的优先级
    void setPriority(Integer anValue);

    // 规则之间的比较
    @Override
    int compareTo(RuleFace rule);

}