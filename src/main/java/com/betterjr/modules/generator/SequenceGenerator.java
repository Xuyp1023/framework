// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.3 : 2017年4月11日, liuwl, creation
// ============================================================================
package com.betterjr.modules.generator;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.modules.generator.service.SequenceService;

public class SequenceGenerator implements Generator {
    private SequenceService sequenceService;

    public SequenceGenerator() {
    }

    @Override
    public Object getValue(final String anSeqId, final String anOperOrg, final Long anCustNo, final String anPattern) throws BytterException {
        BTAssert.notNull(sequenceService, "序列化生产服务未找到！");
        String domainId = "DEFAULT";
        Long custNo = 0L;
        if (StringUtils.isNotBlank(anOperOrg)) {
            domainId = anOperOrg;
        }
        if (anCustNo != null) {
            custNo = anCustNo;
        }
        return sequenceService.saveGetSequence(anSeqId, domainId, custNo);
    }

    @Override
    public void setParameters(final Map<String, Object> params) {
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.generator.Generator#setSequenceService(com.betterjr.modules.generator.service.SequenceService)
     */
    @Override
    public void setSequenceService(final SequenceService anSequenceService) {
        this.sequenceService = anSequenceService;
    }
}