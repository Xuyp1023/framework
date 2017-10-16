package com.betterjr.modules.rule.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.rule.dao.RuleBusinValidatorMapper;
import com.betterjr.modules.rule.entity.RuleBusinValidator;

@Service
public class RuleBusinValidatorSerivce extends BaseService<RuleBusinValidatorMapper, RuleBusinValidator> {

    public Map<String, RuleBusinValidator> findBusinValidator(String anBusinName) {
        List list = this.selectByProperty("businName", anBusinName);
        Map map = ReflectionUtils.listConvertToMap(list, "validName");
        return map;
    }
}
