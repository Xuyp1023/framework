package com.betterjr.modules.cert.dubboclient;

import java.security.cert.X509Certificate;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.cert.dubbo.interfaces.ICustCertService;
import com.betterjr.modules.cert.entity.CustCertInfo;

@Service
public class CustCertDubboClientService implements ICustCertService {

    @Reference(interfaceClass=ICustCertService.class)
    private ICustCertService custCertService;

    @Override
    public CustCertInfo checkValidity(final X509Certificate anX509) {
        return custCertService.checkValidity(anX509);
    }
    @Override
    public CustCertInfo findFirstCertInfoByOperOrg(final String anOperOrg) {
        return custCertService.findFirstCertInfoByOperOrg(anOperOrg);
    }
    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#webFindCustCertificate(java.lang.Long)
     */
    @Override
    public String webFindCustCertificate(final Long anId) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#webQueryCustCertificate(java.util.Map, int, int, int)
     */
    @Override
    public String webQueryCustCertificate(final Map<String, Object> anParam, final int anFlag, final int anPageNum, final int anPageSize) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#webAddCustCertificate(java.util.Map)
     */
    @Override
    public String webAddCustCertificate(final Map<String, Object> anParam) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#webSaveCustCertificate(java.util.Map)
     */
    @Override
    public String webSaveCustCertificate(final Map<String, Object> anParam) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#webPublishCustCertificate(java.lang.Long)
     */
    @Override
    public String webPublishCustCertificate(final Long anId) {
        // TODO Auto-generated method stub
        return null;
    }
    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#webCancelCustCertificate(java.lang.Long)
     */
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
