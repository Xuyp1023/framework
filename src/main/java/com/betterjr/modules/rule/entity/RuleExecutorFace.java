package com.betterjr.modules.rule.entity;

public interface RuleExecutorFace {

    // 评估执行条件
    boolean evaluate();

    // 执行内容
    Object execute();

}
