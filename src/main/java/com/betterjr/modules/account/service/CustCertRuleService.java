// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月22日, liuwl, creation
// ============================================================================
package com.betterjr.modules.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.account.dao.CustCertRuleMapper;
import com.betterjr.modules.account.entity.CustCertRule;

/**
 * @author liuwl
 *
 */
@Service
public class CustCertRuleService extends BaseService<CustCertRuleMapper, CustCertRule> {

    /**
     * 按 serialNo 查找 RuleList
     * @param anSerialNo
     * @return
     */
    public List<CustCertRule> queryCertRuleListBySerialNo(final String anSerialNo) {
        return this.selectByProperty("serialNo", anSerialNo);
    }

    /**
     * 按 rule 查找 RuleList
     * @param anRule
     * @return
     */
    public List<CustCertRule> queryCertRuleListByRule(final String anRule) {
        return this.selectByProperty("rule", anRule);
    }
}
