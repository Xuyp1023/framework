package com.betterjr.modules.account.dubbo.interfaces;

import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustLoginRecord;
import com.betterjr.modules.account.entity.CustOperatorInfo;

public interface ICustLoginService {

	CustContextInfo saveFormLogin(CustOperatorInfo anUser);

	CustContextInfo saveTokenLogin(String anToken);

}