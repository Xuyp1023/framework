package com.betterjr.modules.account.dubbo.interfaces;

import java.util.List;

import com.betterjr.modules.account.entity.CustOperatorInfo;

public interface ICustOperatorService {

    CustOperatorInfo findCustOperatorByOperCode(String anOperOrg, String anOperCode);

    CustOperatorInfo findCustOperatorById(Long anId);

    List<CustOperatorInfo> queryOperatorByCustNo(Long anCustNo);

}