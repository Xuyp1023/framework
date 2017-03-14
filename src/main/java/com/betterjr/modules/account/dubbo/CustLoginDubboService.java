package com.betterjr.modules.account.dubbo;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.dubbo.interfaces.ICustLoginService;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustLoginService;
import com.betterjr.modules.cert.entity.CustCertInfo;

@Service(interfaceClass=ICustLoginService.class)
public class CustLoginDubboService implements ICustLoginService {

    @Autowired
    private CustLoginService custLoginService;

    @Override
    public CustContextInfo saveFormLogin(final CustOperatorInfo anUser) {
        return custLoginService.saveFormLogin(anUser);
    }

    @Override
    public CustContextInfo saveTicketLogin(final String anTicket, final CustCertInfo anCertInfo) {
        return custLoginService.saveTicketLogin(anTicket, anCertInfo);
    }

}
