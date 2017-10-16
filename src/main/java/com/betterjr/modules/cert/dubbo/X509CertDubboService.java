package com.betterjr.modules.cert.dubbo;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.modules.cert.dubbo.interfaces.IX509CertService;
import com.betterjr.modules.cert.entity.BetterX509CertInfo;
import com.betterjr.modules.cert.service.BetterX509CertService;
import com.betterjr.modules.rule.service.RuleServiceDubboFilterInvoker;

@Service(interfaceClass = IX509CertService.class)
public class X509CertDubboService implements IX509CertService {

    @Resource
    private BetterX509CertService betterX509CertService;

    @Override
    public String webFindCertificateInfo(final Long anId) {
        return AjaxObject.newOk("查询证书成功", betterX509CertService.findX509CertInfo(anId)).toJson();
    }

    @Override
    public String webFindCertificateInfo(final Long anId, final String anSerialNo) {
        return AjaxObject.newOk("查询证书成功", betterX509CertService.findX509CertInfo(anId, anSerialNo)).toJson();
    }

    @Override
    public String webQuerySignerList() {
        return AjaxObject.newOk("查询签发人成功", betterX509CertService.findMiddleX509Cert()).toJson();
    }

    @Override
    public String webQueryCertificateInfo(final Map<String, Object> anParam, final int anFlag, final int anPageNum,
            final int anPageSize) {
        final Map<String, Object> param = RuleServiceDubboFilterInvoker.getInputObj();
        return AjaxObject.newOkWithPage("查询证书列表成功",
                betterX509CertService.queryX509CertInfo(param, anPageNum, anPageSize, anFlag)).toJson();
    }

    @Override
    public String webQueryUnusedCertificateInfo() {
        return AjaxObject.newOk("查询证书列表成功", betterX509CertService.queryUnusedX509CertInfo()).toJson();
    }

    @Override
    public String webAddCertificateInfo(final Map<String, Object> anParam) {
        final BetterX509CertInfo certInfo = RuleServiceDubboFilterInvoker.getInputObj();

        BTAssert.notNull(certInfo, "数字证书添加入参不允许为空！");
        certInfo.setCertType("3"); // 所有均为最终用户证书
        return AjaxObject.newOk("添加证书成功", betterX509CertService.saveX509CertFromWeb(certInfo)).toJson();
    }

    @Override
    public String webSaveCertificateInfo(final Long anId, final Map<String, Object> anParam) {
        // 通过 anId 来处理业务
        final BetterX509CertInfo certInfo = RuleServiceDubboFilterInvoker.getInputObj();
        BTAssert.notNull(certInfo, "数字证书添加入参不允许为空！");

        final BetterX509CertInfo tempCertInfo = betterX509CertService.findX509CertInfo(anId);
        BTAssert.notNull(tempCertInfo, "没有找到相应的数字证书记录");

        BTAssert.isTrue(StringUtils.equals("0", tempCertInfo.getCertStatus()), "已使用证书不允许修改！");

        tempCertInfo.modifyValue(certInfo);
        return AjaxObject.newOk("修改证书成功", betterX509CertService.saveX509CertFromWeb(tempCertInfo)).toJson();
    }

    @Override
    public String webMakeCertificateInfo(final Long anId, final String anSerialNo) {
        betterX509CertService.saveMakeCertificate(anId, anSerialNo);
        return AjaxObject.newOk("证书制作成功").toJson();
    }

    @Override
    public String webCancelCertificateInfo(final Long anId, final String anSerialNo) {
        betterX509CertService.saveCancelCertificate(anId, anSerialNo);
        return AjaxObject.newOk("证书作废成功").toJson();
    }

    @Override
    public String webRevokeCertificateInfo(final Long anId, final String anSerialNo, final String anReason) {
        betterX509CertService.saveRevokeCert(anId, anSerialNo, anReason);
        return AjaxObject.newOk("证书回收成功").toJson();
    }
}
