package com.betterjr.modules.rule.entity;

import com.betterjr.modules.rule.service.QLExpressContext;

public interface RuleObjectFace extends Cloneable {

    // 获取规则处理中的上下文信息
    public QLExpressContext getContext();

    // 设置规则处理中的上下文信息
    public void setContext(QLExpressContext anContext);

    public RuleObjectFace clone();
}
