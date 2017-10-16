package com.betterjr.modules.rule.validator;

import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

public interface DataValidatorFace {

    /**
     * 定义验证规则的接口
     * @param 验证规则信息
     * @param 处理所需要的上下文信息
     * @param 处理结果信息
     * @param 处理的业务过程
     * @param 入参的数据
     * @param 业务规则数据
     * @return
     */
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult,
            RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage);

    public String getValidatorName();
}
