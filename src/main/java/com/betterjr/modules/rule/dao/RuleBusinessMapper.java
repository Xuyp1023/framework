package com.betterjr.modules.rule.dao;

import java.util.List;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.betterjr.common.annotation.BetterjrMapper;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.modules.rule.entity.RuleBusinStub;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.RuleFunction;
import com.betterjr.modules.rule.entity.RuleInfo;

@BetterjrMapper
public interface RuleBusinessMapper extends Mapper<RuleBusiness> {

    // 查询所有的功能定义
    @Select("select a.* from t_rule_fun a where a.C_STATUS = '1' order by a.c_group, a.c_type")
    @ResultType(RuleFunction.class)
    public List<RuleFunction> findAllRuleFun();

    // 查询业务过程定义的规则
    @Select("select * from T_RULE_BUSIN_STUB a where a.c_busin_name =  #{businName} order by a.n_priority")
    @ResultType(RuleBusinStub.class)
    public List<RuleBusinStub> findRulesByBusinName(String anBusinName);

    // 查询定义的规则
    @Select("select a.* from  t_rule_info a order by a.c_rule_no")
    @ResultType(RuleInfo.class)
    public List<RuleInfo> findAllBusinRule();

}