package com.betterjr.modules.cert.dubboclient;

import java.security.cert.X509Certificate;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.cert.dubbo.interfaces.ICustCertService;
import com.betterjr.modules.cert.entity.CustCertInfo;

@Service
public class CustCertDubboClientService {

    @Reference(interfaceClass = ICustCertService.class)
    private ICustCertService custCertService;

    public CustCertInfo checkValidity(final X509Certificate anX509) {
        return custCertService.checkValidity(anX509);
    }

    public CustCertInfo findFirstCertInfoByOperOrg(final String anOperOrg) {
        return custCertService.findFirstCertInfoByOperOrg(anOperOrg);
    }

    public String findCustCertificate(final String anSerialNo) {
        return custCertService.webFindCustCertificate(anSerialNo);
    }

    public String queryCustCertificate(final Map<String, Object> anParam, final int anFlag, final int anPageNum, final int anPageSize) {
        return custCertService.webQueryCustCertificate(anParam, anFlag, anPageNum, anPageSize);
    }

    public String addCustCertificate(final Map<String, Object> anParam) {
        return custCertService.webAddCustCertificate(anParam);
    }

    public String modifyCustCertificate(final String anSerialNo, final Map<String, Object> anParam) {
        return custCertService.webSaveCustCertificate(anSerialNo, anParam);
    }

    public String publishCustCertificate(final String anSerialNo, final String anPublishMode) {
        return custCertService.webPublishCustCertificate(anSerialNo, anPublishMode);
    }

    public String cancelCustCertificate(final String anSerialNo, final String anReason) {
        return custCertService.webCancelCustCertificate(anSerialNo, anReason);
    }

    public String revokeCustCertificate(final String anSerialNo, final String anReason) {
        return custCertService.webRevokeCustCertificate(anSerialNo, anReason);
    }

}
