package com.betterjr.modules.sys.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.sys.dao.DictItemInfoMapper;
import com.betterjr.modules.sys.entity.DictItemInfo;

@Service
public class DictItemService extends BaseService<DictItemInfoMapper, DictItemInfo> {

    public List<DictItemInfo> findByGroup(int anParentId) {

        return this.selectByProperty("itemNo", anParentId);
    }
    
    public DictItemInfo saveDictItem(Integer anItemNo, String anKey, String anCode){
       Map<String, Object > termMap = new HashMap();
       termMap.put("itemNo", anItemNo);
       termMap.put("itemValue", anKey);
       List<DictItemInfo> data = this.selectByProperty(termMap);
       DictItemInfo itemInfo = Collections3.getFirst(data);  
       if ( itemInfo == null ){
          itemInfo = new DictItemInfo(anKey, anCode, anItemNo);
          this.insert(itemInfo);
       }
       else{
          itemInfo.setItemCode(anCode);
          this.updateByPrimaryKey(itemInfo);
       }
       return itemInfo;
    }
    
    public List<DictItemInfo> findOutScriptByGroup(int anParentId) {
        Map<String, Object> map = new HashMap();
        map.put("itemNo", anParentId);
        map.put("status", Boolean.TRUE);
        map.put("exportOut", Boolean.TRUE);
        return this.selectByProperty(map);
    }
}
