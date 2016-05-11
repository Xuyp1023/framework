package com.betterjr.modules.sys.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.sys.dao.*;
import com.betterjr.modules.sys.entity.*;

@Service
public class AreaInfoService extends BaseService<AreaInfoMapper, AreaInfo> {

    
    public Map<String, AreaInfo> findAreaInfo() {
        List<AreaInfo> list = this.selectByProperty("status", "1");
        Map<String, AreaInfo> map = new LinkedHashMap<String, AreaInfo>();
        String tmpKey;
        Map<String, AreaInfo> cityMap = null;
        Map<String, AreaInfo> subCityMap = null;
        String areaCode;
        for (AreaInfo area : list) {
            areaCode = area.getAreaCode();
            if (areaCode.endsWith("0000")) {
                tmpKey = areaCode.substring(0, 2);
                map.put(tmpKey, area);
                cityMap = new LinkedHashMap<String, AreaInfo>();
                area.setCityMap(cityMap);
            }
            else if (areaCode.endsWith("00")) {
                tmpKey = areaCode.substring(0, 4);
                cityMap.put(tmpKey, area);
                subCityMap = new LinkedHashMap<String, AreaInfo>();
                area.setCityMap(subCityMap);
            }
            else {
                subCityMap.put(areaCode, area);
            }
        }
        return map;
    }
}
