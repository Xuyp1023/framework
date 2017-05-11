// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年5月10日, liuwl, creation
// ============================================================================
package com.betterjr.modules.cert.dubbo.interfaces;

import java.util.Map;

/**
 * @author liuwl
 *
 */
public interface IVerifySignCertService {
    String webQueryCertList(Map<String, Object> anParam, int anFlag, int anPageNum, int anPageSize);

    String webSaveAddCert(Map<String, Object> anParam);

    String webSaveEditCert(Map<String, Object> anParam, Long anId);

    String webSaveEnableCert(Long anId);

    String webSaveDisableCert(Long anId);

    String webFindCert(Long anId);

    boolean verifyFile(Long anCustNo, Long anFileId, String anSignData);
}
