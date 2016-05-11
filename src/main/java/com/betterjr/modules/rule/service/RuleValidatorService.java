package com.betterjr.modules.rule.service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterClassUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.SpringContextHolder;
import com.betterjr.common.utils.XmlUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.dao.RuleValidatorMapper;
import com.betterjr.modules.rule.entity.*;
import com.betterjr.modules.rule.validator.*;

import java.lang.reflect.Modifier;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;

@Service
public class RuleValidatorService extends BaseService<RuleValidatorMapper, RuleValidator> {
    private static Logger logger = LoggerFactory.getLogger(RuleValidatorService.class);

    @Autowired
    private RuleBusinValidatorSerivce businValidatorService;

    private boolean muiltError = false;
    private static String[] defValidators = new String[] { "mustValid", "typeValid", "minValid", "maxValid" };
    private static Map<String, DataValidatorFace> validatorMap = new LinkedCaseInsensitiveMap();
    private String validatorPath = "com.betterjr.modules.rule.validator";

    public String getValidatorPath() {
        return this.validatorPath;
    }

    public void setValidatorPath(String anValidatorPath) {
        this.validatorPath = anValidatorPath;
    }

    public void init() {
        List<Class<?>> list;
        DataValidatorFace dvFace;
        for (String tmpPath : XmlUtils.split(validatorPath)) {
            list = BetterClassUtils.getClassList(tmpPath, false, null);
            for (Class cc : list) {
                if (DataValidatorFace.class.isAssignableFrom(cc) && (cc.isInterface() == false) && (Modifier.isAbstract(cc.getModifiers()) == false)) {
                    try {
                        if (cc.getAnnotation(Service.class) != null) {
                            dvFace = (DataValidatorFace) SpringContextHolder.getBean(cc);
                        }
                        else {
                            dvFace = (DataValidatorFace) cc.newInstance();
                        }
                        validatorMap.put(dvFace.getValidatorName(), dvFace);
                    }
                    catch (InstantiationException | IllegalAccessException e) {

                        logger.warn("Create work Validator has error " + cc.getName(), e);
                    }
                }
            }
        }
    }

    public boolean isMuiltError() {
        return this.muiltError;
    }

    public void setMuiltError(boolean anMuiltError) {
        this.muiltError = anMuiltError;
    }

    public List<WorkRuleValidator> findValidator(String anBusinName) {
        Map<String, RuleBusinValidator> businValidator = businValidatorService.findBusinValidator(anBusinName);

        List<WorkRuleValidator> mapValidator = new LinkedList();
        if (businValidator != null) {
            Map<String, RuleValidator> map = ReflectionUtils.listConvertToMap(this.selectAll(), "validName");
            WorkRuleValidator wv;
            RuleValidator rv;
            for (RuleBusinValidator rbv : businValidator.values()) {
                rv = map.get(rbv.getValidName());
                wv = new WorkRuleValidator(rv, rbv);
                mapValidator.add(wv);
            }
        }
        return mapValidator;
    }

    private Map<String, Object> findRefValue(QLExpressContext anContext, String anRefValue) {
        if (BetterStringUtils.isBlank(anRefValue)) {

            return null;
        }
        Map<String, Object> map = new LinkedCaseInsensitiveMap(3);
        Object obj;
        for (String tmpStr : XmlUtils.split(anRefValue, ";")) {
            List<String> list = XmlUtils.split(tmpStr, ":");
            if (list.size() == 2) {
                obj = anContext.getObjValue(list.get(1));
                if (obj != null) {
                    map.put(list.get(0), obj);
                }
            }
        }

        return map;

    }

    public RuleCheckResult evaluate(QLExpressContext anContext, RuleCheckResult anResult, RuleBusiness ruleBusin) {
        Object obj;
        Object businObj;
        DataValidatorFace validFace;
        for (WorkRuleValidator validator : ruleBusin.getValidators()) {
            obj = anContext.getObjValue(validator.getFieldName());
            if (obj instanceof String) {

                obj = BetterStringUtils.trimToNull((String) obj);
            }
            boolean bb = true;

            // 基本的验证，包括是否可以Null,类型是否正确，最小值，最大值；只要有一个不通过，就终止验证。
            for (int i = 0; i < defValidators.length; i++) {
                validFace = validatorMap.get(defValidators[i]);
                if (validFace != null) {
                    if (validFace instanceof MinDataValidator) {
                        businObj = validator.getMinValue();
                    }
                    else if (validFace instanceof MaxDataValidator) {
                        businObj = validator.getMaxValues();
                    }
                    else {
                        businObj = null;
                    }
                    bb = validFace.evaluate(validator, anContext, anResult, ruleBusin, obj, businObj, null);
                    if (bb == false) {
                        break;
                    }
                }
            }

            // 处理依赖的验证器，并将自己的验证器追加在最后处理
            if (bb) {
                try {
                    DataValidContext.addValidContext(validator, anContext, anResult, ruleBusin, obj, null, validator.getMessage());
                    businObj = anContext.getBusinValue(validator.getBusinField());
                    DataValidContext.addBusinObj(businObj);
                    DataValidContext.addRefValue(findRefValue(anContext, validator.getRefValue()));
                    List<String> tmpList = XmlUtils.split(validator.getDepends());
                    if (BetterStringUtils.isNoneBlank(validator.getValidator())) {
                        tmpList.add(validator.getValidator().trim());
                    }
                    for (String tmpValidator : tmpList) {
                        validFace = validatorMap.get(tmpValidator);
                        if (validFace != null) {
                            if (obj != null) {
                                bb = validFace.evaluate(validator, anContext, anResult, ruleBusin, obj, businObj, validator.getMessage());
                                if (bb == false) {

                                    break;
                                }
                            }
                        }
                    }
                }
                finally {
                    DataValidContext.releaseValidContext();
                }
            }

            if (bb == false) {
                if (this.muiltError) {
                    continue;
                }
                else {
                    break;
                }
            }
        }

        return anResult;
    }
}