package com.betterjr.modules.cert.dubbo;

import java.security.cert.X509Certificate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.cert.dubbo.interfaces.ICustCertService;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.cert.service.BetterX509CertService;
import com.betterjr.modules.cert.service.CustCertService;

@Service(interfaceClass=ICustCertService.class)
public class CustCertDubboService implements ICustCertService {

    @Autowired
    private CustCertService custCertService;

    @Autowired
    private BetterX509CertService x509CertService;

    @Override
    public CustCertInfo checkValidity(final X509Certificate anX509) {
        return custCertService.checkValidity(anX509);
    }

    @Override
    public CustCertInfo findFirstCertInfoByOperOrg(final String anOperOrg) {
        return custCertService.findFirstCertInfoByOperOrg(anOperOrg);
    }

    @Override
    public String webFindCustCertificate(final Long anId) {
        return null;
    }

    @Override
    public String webQueryCustCertificate(final Map<String, Object> anParam, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String webAddCustCertificate(final Map<String, Object> anParam) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String webSaveCustCertificate(final Map<String, Object> anParam) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String webPublishCustCertificate(final Long anId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String webCancelCustCertificate(final Long anId) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#revokeCustCertificate(java.lang.Long)
     */
    @Override
    public String webRevokeCustCertificate(final Long anId) {
        // TODO Auto-generated method stub
        return null;
    }

}
