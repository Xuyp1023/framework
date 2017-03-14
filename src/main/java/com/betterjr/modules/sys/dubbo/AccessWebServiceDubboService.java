// Copyright (c) 2014-2017 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2017年3月6日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sys.dubbo;

import java.security.cert.X509Certificate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.exception.AccessError;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.security.KeyReader;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.cert.service.CustCertService;
import com.betterjr.modules.sys.dubbo.interfaces.IAccessWebService;
import com.betterjr.modules.sys.utils.TicketUtils;

/**
 * @author liuwl
 *
 */
@Service(interfaceClass = IAccessWebService.class)
public class AccessWebServiceDubboService implements IAccessWebService {
    private static final Logger logger = LoggerFactory.getLogger(AccessWebServiceDubboService.class);

    @Autowired
    private CustCertService certService;

    @Autowired
    private CustKeyManager custKeyManager;

    @Autowired
    private CustOperatorService custOperatorService;


    /* (non-Javadoc)
     * @see com.betterjr.modules.sys.dubbo.interfaces.IAccessWebService#login(java.util.Map)
     */
    @Override
    public String ticket(final Map anMap) {

        final String anData = (String) anMap.get("data");
        final String anSign = (String) anMap.get("sign");
        final String certSerialNo = (String) anMap.get("cert");

        final CustCertInfo certInfo = certService.findBySerialNo(certSerialNo);
        final X509Certificate cert = (X509Certificate) KeyReader.fromCerBase64String(certInfo.getCertInfo());

        final String data = this.custKeyManager.decrypt(anData);

        if (SignHelper.verifySign(data, anSign, cert)) {
            @SuppressWarnings("unchecked")
            final Map<String, String> param = JsonMapper.buildNormalMapper().fromJson(data, Map.class);

            if (param == null) {
                logger.error("access ticket 参数错误");
                return null;
            }

            final String token = param.get("token");
            final String operCode = param.get("operCode");
            final String operName = param.get("operName");
            final String corpId = param.get("corpId");

            final CustCertInfo baseCertInfo = this.certService.findCertByToken(token);
            if (baseCertInfo == null) {
                logger.error("access ticket token错误: token=" + token);
                return null;
            }

            final CustOperatorInfo operator = custOperatorService.findCustOperatorByOperCode(baseCertInfo.getOperOrg(), operCode);
            if (operator == null) {
                logger.error("access ticket operCode错误: token=" + operCode);
                return null;
            }

            // 验证证书 以及 用户
            if (certInfo.validCertInfo(baseCertInfo) && BetterStringUtils.equals(operator.getStatus(), "1")) {
                final String ticket = SignHelper.randomBase64(40);

                param.put("operOrg", baseCertInfo.getOperOrg());
                TicketUtils.putTicket(ticket, param);

                return SignHelper.encrypt(ticket, cert.getPublicKey());
            }
        }
        logger.error("access ticket 验签错误: data=" + data + " sign=" + anSign + " serialNo=" + certInfo.getSerialNo());
        return null;
    }


    /* (non-Javadoc)
     * @see com.betterjr.modules.sys.dubbo.interfaces.IAccessWebService#firstLogin(java.util.Map)
     */
    @Override
    public String firstLogin(final Map anMap) {
        final String anMark = (String) anMap.get("mark");
        final String anSign = (String) anMap.get("sign");
        final String certSerialNo = (String) anMap.get("cert");

        final CustCertInfo certInfo = certService.findBySerialNo(certSerialNo);
        final X509Certificate cert = (X509Certificate) KeyReader.fromCerBase64String(certInfo.getCertInfo());

        String token = this.custKeyManager.decrypt(anMark);
        if (SignHelper.verifySign(token, anSign, cert)) {
            token = token + SignHelper.randomBase64(40);
            certInfo.setToken(token);
            final int workCount = certService.updateToken(certInfo);
            if (workCount == 1) {
                return SignHelper.encrypt(token, cert.getPublicKey());
            }
            else {
                throw new AccessError(200001, "this X509Certificate has used");
            }
        }

        return null;
    }

}
