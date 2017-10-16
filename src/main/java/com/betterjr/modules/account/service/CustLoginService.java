package com.betterjr.modules.account.service;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.security.KeyReader;
import com.betterjr.common.security.SignHelper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.account.dao.CustLoginRecordMapper;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustLoginRecord;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.sys.utils.TicketUtils;

@Service
public class CustLoginService extends BaseService<CustLoginRecordMapper, CustLoginRecord> {
    private static final Logger logger = LoggerFactory.getLogger(CustLoginService.class);

    @Autowired
    private CustKeyManager custKeyManager;

    @Autowired
    private CustAccountService accountService;

    @Autowired
    private CustOperatorService custOperatorService;

    public CustContextInfo saveFormLogin(final CustOperatorInfo anUser) {
        final CustLoginRecord tmpRecord = CustLoginRecord.createByOperator(anUser, "1");
        saveLoginRecord(tmpRecord);
        final CustContextInfo contextInfo = null;
        return accountService.loginOperate(contextInfo, anUser);
    }

    /**
     * 保存登陆信息
     *
     * @param anRecord
     */
    public void saveLoginRecord(final CustLoginRecord anRecord) {
        this.insert(anRecord);
    }

    /**
     * 查询最后一次登陆信息
     *
     * @param anOpeOrg
     * @return
     */
    public CustLoginRecord findLastLoginRecord(final String anOpeOrg) {
        final List<CustLoginRecord> custLoginList = this.selectPropertyByPage("operOrg", anOpeOrg, 1, 1, false);
        return Collections3.getFirst(custLoginList);
    }

    /**
     * 使用ticket登陆
     *
     * @param anTicket
     * @param anCertInfo
     * @return
     */
    public CustContextInfo saveTicketLogin(final String anTicket, final CustCertInfo anCertInfo) {
        final String ticket = StringUtils.deleteWhitespace(anTicket);

        String msg = null;
        int errCode = 20401;
        if (StringUtils.isNotBlank(ticket)) {
            final String[] arrStr = ticket.split(",");
            if (arrStr.length == 2) {

                final String workToken = custKeyManager.decrypt(arrStr[0]);
                final String workTokenSign = arrStr[1];
                final X509Certificate certificate = (X509Certificate) KeyReader
                        .fromCerBase64String(anCertInfo.getCertInfo());

                if (SignHelper.verifySign(workToken, workTokenSign, certificate)) {
                    final Map<String, String> param = TicketUtils.getToken(workToken);

                    final String token = param.get("token");
                    final String operCode = param.get("operCode");
                    final String operName = param.get("operName");
                    final String corpId = param.get("corpId");
                    final String operOrg = param.get("operOrg");

                    final CustOperatorInfo operator = custOperatorService.findCustOperatorByOperCode(operOrg, operCode);
                    if (operator != null) {

                        final CustContextInfo contextInfo = accountService.loginOperateByToken(token, operator);

                        if (contextInfo == null) {
                            logger.warn("the request contextInfo is null, useToken =" + ticket);
                        }
                        // 验证上下文信息是否有效，登录是否超时，以及提供的证书是否由私钥签发；如果都通过，则去获取客户信息
                        if (contextInfo != null && contextInfo.isValid()) {
                            final CustLoginRecord tmpRecord = CustLoginRecord
                                    .createByOperator(contextInfo.getOperatorInfo(), "0");
                            saveLoginRecord(tmpRecord);
                            contextInfo.setOperatorInfo(operator);
                            return contextInfo;
                        } else {
                            errCode = 20402;
                            msg = "back login context has error!";
                        }
                    } else {
                        errCode = 20405;
                        msg = "back login context has error!";
                    }
                } else {
                    errCode = 20404;
                    msg = "back login context has error!";
                }

            } else {
                errCode = 20403;
                msg = "request body is error";
            }
        } else {
            msg = "token is null";
        }
        throw new AuthenticationException(new BytterSecurityException(errCode, msg));
    }

}
