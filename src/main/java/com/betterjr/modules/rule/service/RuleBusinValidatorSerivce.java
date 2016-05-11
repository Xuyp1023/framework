package com.betterjr.modules.rule.service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.reflection.ReflectionUtils;
import com.betterjr.modules.rule.dao.RuleBusinValidatorMapper;
import com.betterjr.modules.rule.entity.RuleBusinValidator;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class RuleBusinValidatorSerivce extends BaseService<RuleBusinValidatorMapper, RuleBusinValidator> {

    public Map<String, RuleBusinValidator> findBusinValidator(String anBusinName){
       List list = this.selectByProperty("businName", anBusinName);
       Map map = ReflectionUtils.listConvertToMap(list, "validName");
       return map;
    }
}
