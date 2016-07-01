package com.betterjr.modules.account.dubbo.interfaces;

import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorInfoRequest;
import com.betterjr.modules.account.entity.CustPassInfo;

public interface ICustOperatorService {

	CustOperatorInfo findCustOperatorByOperCode(String anOperOrg, String anOperCode);

}