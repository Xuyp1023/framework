package com.betterjr.modules.rule.validator;

import com.betterjr.common.data.DataTypeInfo;
import com.betterjr.common.utils.TypeCaseHelper;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.QLExpressContext;

/**
 * 数据类型转换；入参和定义类型之间是否存在转换关系
 * @author zhoucy
 *
 */
public class TypeDataValidator extends BaseDataValidator implements DataValidatorFace {

    @Override
    public boolean evaluate(WorkRuleValidator anValidator, QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness anRuleBusin,
            Object anValue, Object anBusinValue, String anMessage) {


        if (anValue == null){
            
            return true;
        }
        
        // 转换数据类型
        Class dt = DataTypeInfo.getClass(anValidator.getDataType());

        // 无类型定义，直接返回
        if (Void.class.isAssignableFrom(dt)) {
            return true;
        }

        String fmt = null;
        fmt = anValidator.getWorkPattern();
        boolean bb = true;
        try {
            anValue = TypeCaseHelper.convert(anValue, dt.getSimpleName(), fmt);
        }
        catch (Exception ex) {
            bb = false;
        }

        return returnValue(bb, anResult, anValidator.dataFormatMessage());
    }

    @Override
    public String getValidatorName() {

        return "typeValid";
    }

}
