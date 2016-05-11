package com.betterjr.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.sys.dao.SysMenuInfoMapper;
import com.betterjr.modules.sys.entity.SysMenuInfo;

@Service
public class SysMenuService extends BaseService<SysMenuInfoMapper, SysMenuInfo> {
   
    public List findMenuList(List menuIds){
        List menuList = new ArrayList<SysMenuInfo>();
        Map anMap = new HashMap<String, Object>();
        anMap.put("id", menuIds);
        List<SysMenuInfo> allList = this.selectByProperty(anMap, "menuOrder");
        
        Map<Integer,SysMenuInfo> parentNoteMap = new HashMap<Integer,SysMenuInfo>();
        //父节点
        for(SysMenuInfo menuInfo : allList){
          if(0==menuInfo.getParentId()&&"1".equals(menuInfo.getStatus())){
              parentNoteMap.put(menuInfo.getId(), menuInfo);
          }
        }
        //子节点
        for(Integer parentId : parentNoteMap.keySet()){
            Map<String, Object> sysMenuMap = new LinkedHashMap<String,Object>();
            SysMenuInfo parentMenuInfo = parentNoteMap.get(parentId);
            List<SysMenuInfo> noteList = this.selectByProperty("parentId", parentId);
            List<Map<String, String>> noteMenuList = new ArrayList<Map<String,String>>();
            for(SysMenuInfo noteMenuInfo : noteList){
                if("1".equals(noteMenuInfo.getStatus())){
                    Map noteMenuMap = new HashMap<String, String>();
                    noteMenuMap.put("title", noteMenuInfo.getMenuTitle());
                    noteMenuMap.put("url", noteMenuInfo.getMenuUrl());
                    noteMenuList.add(noteMenuMap);
                }
            }
            sysMenuMap.put("title", parentMenuInfo.getMenuTitle());
            sysMenuMap.put("children", noteMenuList);
            menuList.add(sysMenuMap);
            
        }
        return menuList;
    }
    
}
