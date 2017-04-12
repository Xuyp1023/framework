// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年4月12日, liuwl, creation
// ============================================================================
package com.betterjr.modules.generator;

import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.modules.generator.service.SequenceService;

/**
 * @author liuwl
 *
 */
public final class SequenceFactory {
    private static final Generator generator = new PatternSequenceGenerator();


    public void init() {
        generator.setSequenceService(SpringContextHolder.getBean(SequenceService.class));
    }

    public static String generate(final String anSeqId, final String anOperOrg, final Long anCustNo, final String anPattern) {
        return generator.getValue(anSeqId, anOperOrg, anCustNo, anPattern).toString();
    }

    public static String generate(final String anSeqId, final String anOperOrg, final String anPattern) {
        return generator.getValue(anSeqId, anOperOrg, null, anPattern).toString();
    }

    public static String generate(final String anSeqId, final String anPattern) {
        return generator.getValue(anSeqId, null, null, anPattern).toString();
    }

}
