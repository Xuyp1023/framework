package com.betterjr.modules.account.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterSecurityException;
import com.betterjr.common.security.CustKeyManager;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.PropertiesHolder;
import com.betterjr.common.web.Servlets;
import com.betterjr.modules.account.dao.CustLoginRecordMapper;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustLoginRecord;
import com.betterjr.modules.account.entity.CustOperatorInfo;
 
@Service
public class CustLoginService extends BaseService<CustLoginRecordMapper, CustLoginRecord> {
    private static final Logger logger = LoggerFactory.getLogger(CustLoginService.class);

    @Autowired
    private CustKeyManager custKeyManager;

    @Autowired
    private CustAccountService accountService;

    protected CustContextInfo mockCreateLogin(String anToken) {
        String token = Servlets.getSession().getId();
        CustContextInfo contextInfo = new CustContextInfo(token, null, null);
        CustContextInfo.putCustContextInfo(contextInfo);
        // CustOperatorInfo tmpInfo = operService.findCustOperatorByOperCode(
        // "3a3nGxa3QNGsvmd9VvpJQ7pxewbwhNwc27hyx63eEcxMp8b+Y7jFtA==FxOSPGFvQyrsz0CwcUjvjFQrepIzIzgiX7Je0hUhrAGgULe8udr9Gg==", "000001");
        // CustOperatorInfo tmpInfo = operService.findCustOperatorByOperCode("aXXAAAFWQWEQWEQWEEQWEXXXXxxxxx", "1234566");
        // CustOperatorInfo tmpInfo = operService.findCustOperatorByOperCode("aXXAAAFWQWEQWEQWEEQWEXXXXbbbbbb", "978441"); //77
        // CustOperatorInfo tmpInfo = operService.findCustOperatorByOperCode("aXXAAAFWQWEQWEQWEEQWEXXXXddpppp", "9857");// 91
        // contextInfo.setOperatorInfo(tmpInfo);
        // List<CustInfo> custList = findCustInfoByOperator(contextInfo.getOperatorInfo().getId(), null);
        // contextInfo.login(custList);

        // 增加交易账户信息
        // contextInfo.addTradeAccount(tradeAccountService.findTradeAccountByCustInfo(custList));
        // todo;登录信息和状态暂时不处理
        return contextInfo;
    }

    protected CustContextInfo mockLogin(String anToken) {
        String token = Servlets.getSession().getId();
        CustContextInfo contextInfo = new CustContextInfo(token, null, null);
        CustContextInfo.putCustContextInfo(contextInfo);
        CustOperatorInfo tmpInfo = new CustOperatorInfo();
        tmpInfo.setName("李三四");
        tmpInfo.setOperCode("119901");
        tmpInfo.setOperOrg("aaaXXXXXXXXXXXXXXXxxxxx");
        tmpInfo.initStatus();
        contextInfo.setOperatorInfo(tmpInfo);
        List<CustInfo> custList = new ArrayList();
        contextInfo.login(custList);

        // 增加交易账户信息
        //contextInfo.addTradeAccount(new ArrayList());

        // todo;登录信息和状态暂时不处理
        return contextInfo;
    }

    protected CustContextInfo mockLogin1(String anToken) {
        String token = Servlets.getSession().getId();
        CustContextInfo contextInfo = new CustContextInfo(token, null, null);
        CustContextInfo.putCustContextInfo(contextInfo);
        CustOperatorInfo tmpInfo = new CustOperatorInfo();
        tmpInfo.setName("欧尼");
        tmpInfo.setOperCode("9857");
        tmpInfo.setOperOrg("aXXAAAFWQWEQWEQWEEQWEXXXXddpppp");
        tmpInfo.initStatus();
        contextInfo.setOperatorInfo(tmpInfo);
        List<CustInfo> custList = new ArrayList();
        contextInfo.login(custList);

        // 增加交易账户信息
        //contextInfo.addTradeAccount(new ArrayList());

        // todo;登录信息和状态暂时不处理
        return contextInfo;
    }

    public CustContextInfo saveFormLogin(CustOperatorInfo anUser) {
        CustLoginRecord tmpRecord = CustLoginRecord.createByOperator(anUser, "1");
        saveLoginRecord(tmpRecord);
        return accountService.loginOperate(null, anUser);
    }

    /**
     * 保存登陆信息
     * @param anRecord
     */
    public void saveLoginRecord(CustLoginRecord anRecord){
        
       this.insert(anRecord); 
    }
    
    public CustLoginRecord findLastLoginRecord(String anOpeOrg){
       List<CustLoginRecord> custLoginList = this.selectPropertyByPage("operOrg", anOpeOrg, 1, 1, false);
       if (Collections3.isEmpty(custLoginList)){
           
           return null;  
       }
       else{
           
           return Collections3.getFirst(custLoginList);
       }
    }
    
    public CustContextInfo saveTokenLogin(String anToken) {
        anToken = BetterStringUtils.deleteWhitespace(anToken);
        /*
         * if ("123".equals(anToken)) { return mockLogin(anToken); } if ("1234".equals(anToken)) { // return mockCreateLogin(anToken); return
         * mockLogin1(anToken); }
         * 
         * if ("12345".equals(anToken)) { return mockLogin1(anToken); }
         */String msg = null;
        int errCode = 20401;
        if (StringUtils.isNotBlank(anToken)) {
            String[] arrStr = anToken.split(",");
            if (arrStr.length == 2) {

                String workToken = custKeyManager.decrypt(arrStr[0]);

                logger.info("this is client Send token=" + workToken);

                CustContextInfo contextInfo = CustContextInfo.findCustContextInfo(workToken);
                int loginTimeDiff = PropertiesHolder.getInt("operator.loginTimeDiff", 30);
                if (contextInfo == null) {
                    logger.warn("the request contextInfo is null, useToken =" + workToken);
                }
                // 验证上下文信息是否有效，登录是否超时，以及提供的证书是否由私钥签发；如果都通过，则去获取客户信息
                if (contextInfo != null && contextInfo.isValid() && contextInfo.isBeforeLoginValid(loginTimeDiff)
                        && contextInfo.verifySign(workToken, arrStr[1])) {
                    CustLoginRecord tmpRecord = CustLoginRecord.createByOperator(contextInfo.getOperatorInfo(), "0");
                    saveLoginRecord(tmpRecord);
                    return accountService.loginOperate(contextInfo, contextInfo.getOperatorInfo());
                }
                else {
                    errCode = 20402;
                    msg = "back login context has error!";
                }
            }
            else {
                errCode = 20403;
                msg = "request body is error";
            }
        }
        else {
            msg = "token is null";
        }
        System.out.println(errCode + ", " + msg);
        Servlets.getSession().invalidate();
        throw new AuthenticationException(new BytterSecurityException(errCode, msg));
    }

}
