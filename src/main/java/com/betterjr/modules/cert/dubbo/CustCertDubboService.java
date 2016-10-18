package com.betterjr.modules.cert.dubbo;

import java.security.cert.X509Certificate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
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

        return AjaxObject.newOk("添加证书信息成功！", custCertService.addCustCertInfo(certInfo)).toJson();
    }

    @Override
    public String webSaveCustCertificate(final String anSerialNo, final Map<String, Object> anParam) {
        final CustCertInfo certInfo = RuleServiceDubboFilterInvoker.getInputObj();
        final String serialNo = certInfo.getSerialNo();

        final CustCertInfo tempCertInfo = custCertService.findBySerialNo(serialNo);
        BTAssert.notNull(tempCertInfo, "没有找到对应的客户证书信息！");

        BTAssert.isTrue(BetterStringUtils.equals(tempCertInfo.getStatus(), "0"), "证书当前状态不允许修改！");

        return AjaxObject.newOk("修改证书信息成功！", custCertService.saveCustCertInfo(certInfo)).toJson();
    }

    @Override
    public String webPublishCustCertificate(final String anSerialNo, final String anPublishMode) {
        return AjaxObject.newOk("发布证书成功！", custCertService.savePublishCert(anSerialNo, anPublishMode)).toJson();
    }

    @Override
    public String webCancelCustCertificate(final String anSerialNo, final String anReason) {
        final CustCertInfo certInfo = custCertService.findBySerialNo(anSerialNo);

        BTAssert.notNull(certInfo, "没有找到相应的客户证书！");
        BTAssert.isTrue(BetterStringUtils.equals(certInfo.getStatus(), "3"),"客户证书已使用不允许作废！");

        custCertService.saveCancelCustCert(anSerialNo, anReason);
        return AjaxObject.newOk("作废证书成功！").toJson();
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#revokeCustCertificate(java.lang.Long)
     */
    @Override
    public String webRevokeCustCertificate(final String anSerialNo, final String anReason) {
        // 回收涉及

        return null;
    }

    /* (non-Javadoc)
     * @see com.betterjr.modules.cert.dubbo.interfaces.ICustCertService#webDownloadCert(java.lang.String)
     */
    @Override
    public byte[] webDownloadCert(final String anToken) {
        return custCertService.saveDownloadCert(anToken);
    }


}
