package com.betterjr.modules.account.dubboclient;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.dubbo.interfaces.ICustLoginService;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.cert.entity.CustCertInfo;

@Service
public class CustLoginDubboClientService implements ICustLoginService {

    @Reference(interfaceClass = ICustLoginService.class)
    private ICustLoginService custLoginService;

    @Override
    public CustContextInfo saveFormLogin(final CustOperatorInfo anUser) {
        return custLoginService.saveFormLogin(anUser);
    }

    @Override
    public CustContextInfo saveTicketLogin(final String anTicket, final CustCertInfo anCertInfo) {
        return custLoginService.saveTicketLogin(anTicket, anCertInfo);
    }

}
