package com.betterjr.modules.cert.dubbo;

import java.security.cert.X509Certificate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.cert.dubbo.interfaces.ICustCertService;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.cert.service.BetterX509CertService;
import com.betterjr.modules.cert.service.CustCertService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

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
    public String webFindCustCertificate(final String anSerialNo) {
        return AjaxObject.newOk("证书详情查询成功", custCertService.findBySerialNo(anSerialNo)).toJson();
    }

    @Override
    public String webQueryCustCertificate(final Map<String, Object> anParam, final int anFlag, final int anPageNum, final int anPageSize) {
        final Map<String, Object> param = RuleServiceDubboFilterInvoker.getInputObj();

        return AjaxObject.newOkWithPage("查询证书信息成功", custCertService.queryCustCertInfo(param, anPageNum, anPageSize, String.valueOf(anFlag))).toJson();
    }

    @Override
    public String webAddCustCertificate(final Map<String, Object> anParam) {
        final CustCertInfo certInfo = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("添加证书信息成功！", custCertService.saveCustCertInfo(certInfo)).toJson();
    }

    @Override
    public String webSaveCustCertificate(final String anSerialNo, final Map<String, Object> anParam) {
        final CustCertInfo certInfo = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOk("修改证书信息成功！", custCertService.saveCustCertInfo(certInfo)).toJson();
    }

    @Override
    public String webPublishCustCertificate(final String anSerialNo) {
        return null;
    }

    @Override
    public String webCancelCustCertificate(final String anSerialNo) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#revokeCustCertificate(java.lang.Long)
     */
    @Override
    public String webRevokeCustCertificate(final String anSerialNo) {
        // TODO Auto-generated method stub
        return null;
    }

}
