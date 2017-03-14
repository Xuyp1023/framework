package com.betterjr.modules.account.dubbo.interfaces;

import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.cert.entity.CustCertInfo;

public interface ICustLoginService {

    CustContextInfo saveFormLogin(CustOperatorInfo anUser);

    CustContextInfo saveTicketLogin(String anTicket, CustCertInfo anCertInfo);

}