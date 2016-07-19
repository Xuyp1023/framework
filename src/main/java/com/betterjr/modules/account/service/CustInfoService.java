package com.betterjr.modules.account.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.account.dao.CustInfoMapper;
import com.betterjr.modules.account.entity.CustInfo;

/**
 * 
 * @author liuwl
 *
 */
@Service
public class CustInfoService extends BaseService<CustInfoMapper,CustInfo> {
    private static Logger logger = LoggerFactory.getLogger(CustInfoService.class);

    public List<CustInfo> queryCustInfoListByOperOrg(String anOperOrg) {
        assert anOperOrg != null;
        List<CustInfo> custInfos = this.selectByProperty("operOrg", anOperOrg);
        return custInfos;
    }
}