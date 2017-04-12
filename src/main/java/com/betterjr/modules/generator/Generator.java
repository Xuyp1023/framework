// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.3 : 2017年4月11日, liuwl, creation
// ============================================================================
package com.betterjr.modules.generator;

import java.util.Map;

import com.betterjr.modules.generator.service.SequenceService;

public interface Generator {
    public Object getValue(String anSeqId, String anOperOrg, Long anCustNo, String anPattern);

    public void setSequenceService(SequenceService sequenceService);

    public void setParameters(Map<String, Object> paramMap);
}
