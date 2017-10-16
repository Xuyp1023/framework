package com.betterjr.modules.account.dubbo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.modules.account.dubbo.interfaces.ICustPassService;
import com.betterjr.modules.account.entity.CustPassInfo;
import com.betterjr.modules.account.service.CustPassService;

@Service(interfaceClass = ICustPassService.class)
public class CustPassDubboService implements ICustPassService {

    @Autowired
    private CustPassService custPassService;

    @Override
    public CustPassInfo getOperaterPassByCustNo(Long anPassID, CustPasswordType anPassType) {
        // TODO Auto-generated method stub
        return custPassService.getOperaterPassByCustNo(anPassID, anPassType);
    }

    @Override
    public List<SimpleDataEntity> findPassAndSalt(Long anPassID, String[] anPassTypes) {
        // TODO Auto-generated method stub
        return custPassService.findPassAndSalt(anPassID, anPassTypes);
    }

}
