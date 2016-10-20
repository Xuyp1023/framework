// Copyright (c) 2014-2016 Betty. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年9月22日, liuwl, creation
// ============================================================================
package com.betterjr.modules.cert.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.cert.dao.CustCertRuleMapper;
import com.betterjr.modules.cert.entity.CustCertRule;

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

    /**
     *
     * @param anCertRule
     * @return
     */
    public CustCertRule addCustCertRule(final String anSerialNo, final String anRule, final String anCustName) {
        final CustCertRule certRule = new CustCertRule();

        certRule.initDefValue(anSerialNo, anRule, anCustName);
        this.insert(certRule);
        return certRule;
    }

    public void saveDelCertRuleBySerialNo(final String anSerialNo) {
        final List<CustCertRule> certRules = this.selectByProperty("serialNo", anSerialNo);

        for (final CustCertRule certRule: certRules) {
            this.delete(certRule);
        }
    }

    /**
     * @param anCertRule
     */
    public void saveDelCertRule(final CustCertRule anCertRule) {
        this.delete(anCertRule);
    }
}
