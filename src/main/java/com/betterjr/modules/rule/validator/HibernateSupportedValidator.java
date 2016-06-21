package com.betterjr.modules.rule.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 支持标准的validation方式
 * 
 * @author zhoucy
 *
 */
@Service
public class HibernateSupportedValidator implements DataValidatorFace {

    @Autowired
    private LocalValidatorFactoryBean validatorFactory;

    public LocalValidatorFactoryBean getValidatorFactory() {
        return this.validatorFactory;
    }

    public void setValidatorFactory(LocalValidatorFactoryBean anValidatorFactory) {
        this.validatorFactory = anValidatorFactory;
    }

    @Override
    public synchronized boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult,
            RuleBusiness anRuleBusin, Object anValue, Object anBusinValue, String anMessage) {

        Set violations;
        Class groups = findClass(anValidator.getWorkPattern());
        if (groups == null) {
            violations = validatorFactory.getValidator().validate(anValue);
        }
        else {
            violations = validatorFactory.getValidator().validate(anValue, groups);
        }

        // 没有异常，表示OK
        if (Collections3.isEmpty(violations)) {
            return true;
        }

        // 添加异常信息
        for (Object obj : violations) {
            if (obj instanceof ConstraintViolation) {
                ConstraintViolation vv = (ConstraintViolation) obj;
                anResult.addMessage("{" + vv.getPropertyPath() + "} " + vv.getMessage());
            }
        }

        return false;

    }

    private Class findClass(String anName) {
        if (BetterStringUtils.isNotBlank(anName)) {
            try {
                return Class.forName(anName);
            }
            catch (ClassNotFoundException e) {
            }
        }

        return null;
    }

    public String getValidatorName() {

        return "validator";
    }
}
