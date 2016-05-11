package com.betterjr.common.utils;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.modules.sys.entity.AreaInfo;
import com.betterjr.modules.sys.service.AreaInfoService;
import java.util.*;

public class AreaUtils {
    public static final String CACHE_AREA_MAP = "areaMap";
    private static AreaInfoService infoService = SpringContextHolder.getBean(AreaInfoService.class);

    public static List<SimpleDataEntity> findProvList() {
        Map<String, AreaInfo> map = findMap();
        List<SimpleDataEntity> list = new ArrayList();
        SimpleDataEntity data;
        for (AreaInfo info : map.values()) {
            data = new SimpleDataEntity(info.getAreaName(), info.getAreaCode());
            list.add(data);
        }

        return list;
    }

    public static List<SimpleDataEntity> findAreaList(String anProv) {
        List<SimpleDataEntity> list = new ArrayList();
        if (BetterStringUtils.isBlank(anProv) || anProv.trim().length() != 6) {
            return list;
        }
        anProv = anProv.substring(0, 2);

        Map<String, AreaInfo> map = findMap();
        SimpleDataEntity data;
        AreaInfo area = map.get(anProv);
        map = area.getCityMap();
        for (AreaInfo info : map.values()) {
            data = new SimpleDataEntity(info.getAreaName(), info.getAreaCode());
            list.add(data);
        }

        return list;
    }

    public static boolean checkExists(String anAreaCode){
        String tmpStr = findAreaName(anAreaCode);
        
        return tmpStr.equals(anAreaCode) == false;
    }
    
    public static String findAreaName(String anAreaCode) {
        if (BetterStringUtils.isBlank(anAreaCode)) {
            
            return "";
        }
        else if(anAreaCode.trim().length() != 6){
            
            return anAreaCode;
        }
        
        Map<String, AreaInfo> map = findMap();
        String prov;
        AreaInfo area;
        int i = 1;
        int k = AreaInfo.findLevel(anAreaCode);
        while (true) {
            prov = anAreaCode.substring(0, i * 2);
            area = map.get(prov);
            if (area != null) {
                if (i == k) {
                    return area.getAreaName();
                }
            }
            else {
                return anAreaCode;
            }
            map = area.getCityMap();
            i++;
        }
    }

    private synchronized static Map<String, AreaInfo> findMap() {
        Map<String, AreaInfo> map = (Map<String, AreaInfo>) CacheUtils.get(CACHE_AREA_MAP);
        if (map == null) {
            map = infoService.findAreaInfo();
            CacheUtils.put(CACHE_AREA_MAP, map);
        }
        return map;
    }
}
