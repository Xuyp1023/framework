// Copyright (c) 2014-2016 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年10月12日, liuwl, creation
// ============================================================================
package com.betterjr.modules.cert.dubboclient;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.cert.dubbo.interfaces.IX509CertService;

/**
 * @author liuwl
 *
 */
@Service
public class X509CertDubboClientService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Reference(interfaceClass = IX509CertService.class)
    private IX509CertService betterX509CertService;

    /**
     * 查询数字证书
     */
    public String queryCertificateInfo(final Map<String, Object> anParam, final int anFlag, final int anPageNum, final int anPageSize) {
        return betterX509CertService.webQueryCertificateInfo(anParam, anFlag, anPageNum, anPageSize);
    }

    /**
     * 新增数字证书
     */
    public String addCertificateInfo(final Map<String, Object> anParam) {
        return betterX509CertService.webAddCertificateInfo(anParam);
    }

    /**
     * 修改数字证书
     */
    public String modifyCertificateInfo(final Long anId, final Map<String, Object> anParam) {
        return betterX509CertService.webSaveCertificateInfo(anId, anParam);
    }

    /**
     * 查询数字证书详情
     */
    public String findCertificateInfo(final Long anId, final String anSerialNo) {
        return betterX509CertService.webFindCertificateInfo(anId, anSerialNo);
    }

    /**
     * 作废数字证书
     */
    public String cancelCertificateInfo(final Long anId, final String anSerialNo) {
        return betterX509CertService.webCancelCertificateInfo(anId, anSerialNo);
    }

    /**
     * 回收数字证书
     */
    public String revokeCertificateInfo(final Long anId, final String anSerialNo, final String anReason) {
        return betterX509CertService.webRevokeCertificateInfo(anId, anSerialNo, anReason);
    }

    /**
     * 查询签发者列表
     */
    public String querySignerList() {
        return betterX509CertService.webQuerySignerList();
    }
}
