package com.betterjr.modules.sys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.sys.dao.SysMenuInfoMapper;
import com.betterjr.modules.sys.dao.SysMenuRuleInfoMapper;
import com.betterjr.modules.sys.entity.SysMenuInfo;
import com.betterjr.modules.sys.entity.SysMenuRuleInfo;

@Service
public class SysMenuRuleService extends BaseService<SysMenuRuleInfoMapper, SysMenuRuleInfo> {
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
    
}
