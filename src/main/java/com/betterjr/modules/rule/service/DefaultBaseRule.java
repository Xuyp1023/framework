package com.betterjr.modules.rule.service;

import com.betterjr.modules.rule.entity.RuleFace;
import com.betterjr.modules.rule.entity.RuleObjectFace;

public class DefaultBaseRule extends BasicRule {

    @Override
    public int compareTo(RuleFace anRule) {

        return 0;
    }

    @Override
    public boolean evaluate() {

        return false;
    }

    @Override
    public Object execute() {
        return new Object();
    }

    @Override
    public RuleObjectFace clone() {
        DefaultBaseRule obj = (DefaultBaseRule) super.clone();

        return obj;
    }

}
