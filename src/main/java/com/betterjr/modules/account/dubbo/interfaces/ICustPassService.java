package com.betterjr.modules.account.dubbo.interfaces;

import java.util.List;
import java.util.Map;

import com.betterjr.modules.account.entity.CustPassInfo;

public interface ICustPassService {


	CustPassInfo getOperaterPassByCustNo(Long anPassID);

}