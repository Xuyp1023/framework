package com.betterjr.modules.rule.entity;

import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;

public class WorkRuleInfo extends RuleInfo {
    private static final long serialVersionUID = -4319487487442832609L;
    private Long id;

    public Long getId() {
        return id;
    }

    private Integer priority;

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer anPriority) {
        priority = anPriority;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public WorkRuleInfo(RuleInfo anRule, RuleBusinStub anStub) {
        BeanMapper.copy(anRule, this);
        this.id = anStub.getId();
        this.priority = anStub.getRulePrior();
        this.status = anStub.getStatus();
    }

    public WorkRuleInfo(RuleInfo anRule, int workOrder) {
        BeanMapper.copy(anRule, this);
        this.priority = workOrder;
        this.status = "1";
    }

    public boolean checkValid() {
        String tmpDate = BetterDateUtils.getDate("yyyyMMdd");
        String[] arrStr = new String[] { this.getEffectDate(), this.getValidDate() };
        for (int i = 0; i < arrStr.length; i++) {
            String tmpStr = arrStr[i];
            if (BetterStringUtils.isNotBlank(tmpStr)) {
                if (tmpDate.compareTo(tmpStr) > 0) {
                    if (i == 0) {
                        return false;
                    }
                }
                else if (tmpDate.compareTo(tmpStr) > 0) {
                    if (i == 1) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public int compareTo(final RuleFace rule) {
        if (this.priority < rule.getPriority()) {
            return -1;
        }
        else if (this.priority > rule.getPriority()) {
            return 1;
        }
        else {
            return this.getRuleNo().compareTo(rule.getRuleNo());
        }
    }
}
