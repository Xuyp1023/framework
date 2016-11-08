package com.betterjr.modules.sys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.sys.dao.SysMenuRuleInfoMapper;
import com.betterjr.modules.sys.entity.SysMenuRuleInfo;

@Service
public class SysMenuRuleService extends BaseService<SysMenuRuleInfoMapper, SysMenuRuleInfo> {
    
    @Autowired
    private SysMenuService menuService;
    
    Map<Integer,SysMenuRuleInfo> menuMap = new HashMap<Integer,SysMenuRuleInfo>();
    
    public List<SysMenuRuleInfo> findAll(){
        List<SysMenuRuleInfo> list = new ArrayList<SysMenuRuleInfo>();
        list = this.findAll();
        return list;
    }

    /**
     * 获取角色对应菜单
     * @param ruleNames
     * @return
     */
    public List<String> findAllByRuleList(String ruleNames){
        List<String> menuList = new ArrayList<String>();
        List ruleNameList = Arrays.asList(ruleNames.split(";|,"));
        Map anMap = new HashMap<String, Object>();
        anMap.put("ruleName", ruleNameList);
        List<SysMenuRuleInfo> ruleList = this.selectByProperty(anMap);
        Map<Integer,SysMenuRuleInfo> ruleMap = new HashMap<Integer,SysMenuRuleInfo>();
        for (SysMenuRuleInfo ruleInfo : ruleList) {
            //去除重复
            if(ruleMap.get(ruleInfo.getMenuId())!=null){
                continue;
            }
            if (ruleInfo.getStatus().equals("1")) {
                menuList.add(ruleInfo.getMenuId() + "");
                ruleMap.put(ruleInfo.getMenuId(), ruleInfo);
            }
        }
        return menuList;
    }
    
    /**
     * 获取角色对应菜单
     * @param ruleNames
     * @return
     */
    public List<String> findAllByRuleAndMenu(List<String> anRuleIdList, Integer anMenuId){
        List<String> menuList = new ArrayList<String>();
        Map anMap = new HashMap<String, Object>();
        anMap.put("ruleId", anRuleIdList);
        List<Integer> menus=new ArrayList<Integer>();  
        if(-1==anMenuId){
            menus=this.menuService.findAllMenu();
        }else{
            menus=this.menuService.findSubMenu(anMenuId);
        }
        if (Collections3.isEmpty(menus) || Collections3.isEmpty(anRuleIdList)){
            
            return menuList;
        }
        anMap.put("menuId", menus);
        logger.info("findAllByRuleAndMenu ruleNames "+ anRuleIdList +", anMenuId = " + anMenuId +", " + anMap);
        List<SysMenuRuleInfo> ruleList = this.selectByProperty(anMap);
        Map<Integer,SysMenuRuleInfo> ruleMap = new HashMap<Integer,SysMenuRuleInfo>();
        for (SysMenuRuleInfo ruleInfo : ruleList) {
            //去除重复
            if(ruleMap.get(ruleInfo.getMenuId())!=null){
                continue;
            }
            if (ruleInfo.getStatus().equals("1")) {
                menuList.add(ruleInfo.getMenuId() + "");
                ruleMap.put(ruleInfo.getMenuId(), ruleInfo);
            }
        }
        
        return menuList;
    }
    
    /****
     * 添加菜单角色
     * @param roleId
     * @param roleName
     * @param menuId
     * @param menuName
     * @return
     */
    public boolean addMenuRole(String roleId,String roleName,String menuId,String menuName){
        SysMenuRuleInfo menuRole=new SysMenuRuleInfo();
        menuRole.initMenuRole(roleId, roleName, menuId, menuName);
        return this.insert(menuRole)>0;
    }
    
    /***
     * 根据角色删除关系表
     * @param anRoleId
     * @return
     */
    public boolean delMenuRole(Long anRoleId){
        return this.deleteByProperty("ruleId", anRoleId)>0;
    }
    
    /**
     * 获取角色对应指定的菜单
     * @param ruleNames
     * @return
     */
    public boolean checkMenuRole(List<String> anRuleIdList, Integer anMenuId){
        Map anMap = new HashMap<String, Object>();
        anMap.put("ruleId", anRuleIdList);
        anMap.put("menuId", anMenuId);
        logger.info("checkMenuRole ruleNames "+ anRuleIdList +", anMenuId = " + anMenuId +", " + anMap);
        List<SysMenuRuleInfo> ruleList = this.selectByProperty(anMap);
        if(Collections3.isEmpty(ruleList)){
            return false;
        }else{
            return true;
        }
    }
}
