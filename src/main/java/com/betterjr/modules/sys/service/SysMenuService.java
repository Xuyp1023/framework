package com.betterjr.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.sys.dao.SysMenuInfoMapper;
import com.betterjr.modules.sys.entity.SysMenuInfo;

@Service
public class SysMenuService extends BaseService<SysMenuInfoMapper, SysMenuInfo> {
   
    public List<Integer> findSubMenu(Integer anMenuId) {

        return findSubMenu(anMenuId, null);
    }

    public List<Integer> findSubMenu(Integer anMenuId, List<Integer> anList) {
        if (anList == null) {

            anList = new ArrayList<Integer>();
        }
        for (SysMenuInfo menuInfo : this.selectByProperty("parentId", anMenuId)) {
            if ("1".equals(menuInfo.getStatus())) {
                anList.add(menuInfo.getId());
                if (menuInfo.getEndNode() == false) {

                    anList = findSubMenu(menuInfo.getId(), anList);
                }
            }

        }

        return anList;
    }

    
    /****
     * 查询先定的菜单
     * @param menuIds
     * @return
     */
    public List findMenuList(List menuIds){
        List menuList = new ArrayList<SysMenuInfo>();
        Map anMap = new HashMap<String, Object>();
        List<SysMenuInfo> allList=new ArrayList<SysMenuInfo>();
        anMap.put("id", menuIds);
        allList = this.selectByProperty(anMap, "menuOrder");
        Map<Integer,SysMenuInfo> parentNoteMap = new HashMap<Integer,SysMenuInfo>();
        // 父节点
        List<PlatformBaseRuleType> userInnerRules = UserUtils.findInnerRules();
        logger.info("userInnerRules:"+userInnerRules);
        for(SysMenuInfo menuInfo : allList){
          if(0==menuInfo.getParentId()&&"1".equals(menuInfo.getStatus())  && menuInfo.hasValidMenu(userInnerRules)){
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
                if("1".equals(noteMenuInfo.getStatus()) && noteMenuInfo.hasValidMenu(userInnerRules)){
                    Map noteMenuMap = new HashMap<String, String>();
                    noteMenuMap.put("menuId", noteMenuInfo.getId());
                    noteMenuMap.put("title", noteMenuInfo.getMenuTitle());
                    noteMenuMap.put("url", noteMenuInfo.getMenuUrl());
                    noteMenuList.add(noteMenuMap);
                }
            }
            sysMenuMap.put("menuId", parentMenuInfo.getId());
            sysMenuMap.put("title", parentMenuInfo.getMenuTitle());
            sysMenuMap.put("children", noteMenuList);
            menuList.add(sysMenuMap);
            
        }
        return menuList;
    }

    /****
     * 查询所有菜单，增加选中标识
     * @param menuIds
     * @return
     */
    public List findAllMenuList(List menuIds){
        List menuList = new ArrayList<SysMenuInfo>();
        Map anMap = new HashMap<String, Object>();
        anMap.put("status", "1");
        List<SysMenuInfo> allList = this.selectByProperty(anMap, "menuOrder");
        Map<Integer,SysMenuInfo> parentNoteMap = new HashMap<Integer,SysMenuInfo>();
        // 父节点
        List<PlatformBaseRuleType> userInnerRules = UserUtils.findInnerRules();
        for(SysMenuInfo menuInfo : allList){
          if(0==menuInfo.getParentId()&&"1".equals(menuInfo.getStatus())   && menuInfo.hasValidMenu(userInnerRules)){
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
                if("1".equals(noteMenuInfo.getStatus()) && noteMenuInfo.hasValidMenu(userInnerRules)){
                    Map noteMenuMap = new HashMap<String, String>();
                    noteMenuMap.put("id", noteMenuInfo.getId());
                    noteMenuMap.put("text", noteMenuInfo.getMenuTitle());
                    noteMenuMap.put("checked", contains(noteMenuInfo.getId(),menuIds));
                    noteMenuList.add(noteMenuMap);
                }
            }
            sysMenuMap.put("id", parentMenuInfo.getId());
            sysMenuMap.put("text", parentMenuInfo.getMenuTitle());
            sysMenuMap.put("checked", contains(parentMenuInfo.getId(),menuIds));
            sysMenuMap.put("children", noteMenuList);
            menuList.add(sysMenuMap);
            
        }
        return menuList;
    }
    
    /***
     * 根据菜单id获取菜单信息
     * @param menuId
     * @return
     */
    public SysMenuInfo findMenuById(Integer menuId){
        return this.selectByPrimaryKey(menuId);
    }
    
    /****
     * 判断是否存在
     * @param menuId
     * @param menuIds
     * @return
     */
    public boolean contains(Integer menuId,List menuIds) {
        if(menuIds.contains(menuId.toString())){
            return true;
        }else{
            return false;
        }
   }
    
}
