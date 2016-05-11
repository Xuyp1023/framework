package com.betterjr.modules.sys.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.sys.dao.DictItemInfoMapper;
import com.betterjr.modules.sys.entity.DictItemInfo;

@Service
public class DictItemService extends BaseService<DictItemInfoMapper, DictItemInfo> {

    public List<DictItemInfo> findByGroup(int anParentId) {

        return this.selectByProperty("itemNo", anParentId);
    }
    
    public List<DictItemInfo> findOutScriptByGroup(int anParentId) {
        Map<String, Object> map = new HashMap();
        map.put("itemNo", anParentId);
        map.put("status", Boolean.TRUE);
        map.put("exportOut", Boolean.TRUE);
        return this.selectByProperty(map);
    }
}
