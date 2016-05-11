package com.betterjr.common.config;

import java.util.*;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.data.SaleSimpleRequest;
 
public abstract class ConfigServiceFace<D, T> extends BaseService {

    //获得所有的参数信息
    public abstract List<T> findAll();

    //根据交易申请，获得单笔约束记录
    public abstract T findOne(SaleSimpleRequest anRequest);
    
    //获得服务名称
    public abstract String getMyServiceName();
}
