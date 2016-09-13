package com.betterjr.modules.account.dubbo.interfaces;

import java.util.List;
import java.util.Map;

import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.modules.account.entity.CustPassInfo;

public interface ICustPassService {


	CustPassInfo getOperaterPassByCustNo(Long anPassID, CustPasswordType anPassType);

	public List<SimpleDataEntity> findPassAndSalt(Long anPassID, String[] anPassTypes);
}