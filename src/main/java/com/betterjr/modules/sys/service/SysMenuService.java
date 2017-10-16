package com.betterjr.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.sys.dao.SysMenuInfoMapper;
import com.betterjr.modules.sys.entity.SysMenuInfo;

@Service
public class SysMenuService extends BaseService<SysMenuInfoMapper, SysMenuInfo> {

    @Autowired
    private SysMenuRuleService sysMenuRuleService;

    /***
     * 查询所有有效菜单
     * @param anMenuId
     * @return
     */
    public List<Integer> findAllMenu() {
        List<Integer> anList = new ArrayList<Integer>();
        for (SysMenuInfo menuInfo : this.selectByProperty("status", "1")) {
            anList.add(menuInfo.getId());
        }
        return anList;
    }

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
    public List findMenuList(List menuIds) {
        List menuList = new ArrayList<SysMenuInfo>();
        Map anMap = new HashMap<String, Object>();
        List<SysMenuInfo> allList = new ArrayList<SysMenuInfo>();
        anMap.put("id", menuIds);
        allList = this.selectByProperty(anMap, "menuOrder");
        Map<Integer, SysMenuInfo> parentNoteMap = new LinkedHashMap<Integer, SysMenuInfo>();
        // 父节点
        List<PlatformBaseRuleType> userInnerRules = UserUtils.findInnerRules();
        logger.info("userInnerRules:" + userInnerRules);
        for (SysMenuInfo menuInfo : allList) {
            if ((menuInfo.getEndNode() == false) && "1".equals(menuInfo.getStatus())
                    && menuInfo.hasValidMenu(userInnerRules)) {
                parentNoteMap.put(menuInfo.getId(), menuInfo);
            }
        }
        // 子节点
        for (Integer parentId : parentNoteMap.keySet()) {
            Map<String, Object> sysMenuMap = new LinkedHashMap<String, Object>();
            SysMenuInfo parentMenuInfo = parentNoteMap.get(parentId);
            List<SysMenuInfo> noteList = this.selectByProperty("parentId", parentId, "menuOrder");
            List<Map<String, String>> noteMenuList = new ArrayList<Map<String, String>>();
            for (SysMenuInfo noteMenuInfo : noteList) {
                if ("1".equals(noteMenuInfo.getStatus()) && noteMenuInfo.hasValidMenu(userInnerRules)) {
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
    public List findAllMenuList(List menuIds) {
        List menuList = new ArrayList<SysMenuInfo>();
        Map anMap = new HashMap<String, Object>();
        anMap.put("status", "1");
        List<SysMenuInfo> allList = this.selectByProperty(anMap, "menuOrder");
        Map<Integer, SysMenuInfo> parentNoteMap = new LinkedHashMap<Integer, SysMenuInfo>();
        // 父节点
        List<PlatformBaseRuleType> userInnerRules = UserUtils.findInnerRules();
        for (SysMenuInfo menuInfo : allList) {
            if ((menuInfo.getEndNode() == false) && "1".equals(menuInfo.getStatus())
                    && menuInfo.hasValidMenu(userInnerRules) && menuInfo.getParentId() == 0) {
                parentNoteMap.put(menuInfo.getId(), menuInfo);
            }
        }
        // 子节点
        for (Integer parentId : parentNoteMap.keySet()) {
            Map<String, Object> sysMenuMap = new LinkedHashMap<String, Object>();
            SysMenuInfo parentMenuInfo = parentNoteMap.get(parentId);
            List<SysMenuInfo> noteList = this.selectByProperty("parentId", parentId, "menuOrder");
            List<Map<String, Object>> sysNoMenuList = new ArrayList<Map<String, Object>>();
            for (SysMenuInfo noteMenuInfo : noteList) {
                Map<String, Object> sysNoMenuMap = new LinkedHashMap<String, Object>();
                List<Map<String, String>> noteMenuList = new ArrayList<Map<String, String>>();
                if ((noteMenuInfo.getEndNode() == false) && "1".equals(noteMenuInfo.getStatus())
                        && noteMenuInfo.hasValidMenu(userInnerRules)) {
                    List<SysMenuInfo> noList = this.selectByProperty("parentId", noteMenuInfo.getId(), "menuOrder");
                    for (SysMenuInfo noMenuInfo : noList) {
                        if ("1".equals(noMenuInfo.getStatus()) && noMenuInfo.hasValidMenu(userInnerRules)) {
                            Map noteMenuMap = new HashMap<String, String>();
                            noteMenuMap.put("id", noMenuInfo.getId());
                            noteMenuMap.put("text", noMenuInfo.getMenuTitle());
                            if (noMenuInfo.getEndNode()) {
                                noteMenuMap.put("checked", contains(noMenuInfo.getId(), menuIds));
                            }
                            noteMenuList.add(noteMenuMap);
                        }
                    }
                    sysNoMenuMap.put("id", noteMenuInfo.getId());
                    sysNoMenuMap.put("text", noteMenuInfo.getMenuTitle());
                    sysNoMenuMap.put("children", noteMenuList);
                    sysNoMenuList.add(sysNoMenuMap);
                }
            }
            sysMenuMap.put("id", parentMenuInfo.getId());
            sysMenuMap.put("text", parentMenuInfo.getMenuTitle());
            if (parentMenuInfo.getEndNode()) {
                sysMenuMap.put("checked", contains(parentMenuInfo.getId(), menuIds));
            }
            sysMenuMap.put("children", sysNoMenuList);
            menuList.add(sysMenuMap);
        }
        return menuList;
    }

    /***
     * 根据菜单id获取菜单信息
     * @param menuId
     * @return
     */
    public SysMenuInfo findMenuById(Integer menuId) {
        return this.selectByPrimaryKey(menuId);
    }

    /****
     * 判断是否存在
     * @param menuId
     * @param menuIds
     * @return
     */
    public boolean contains(Integer menuId, List menuIds) {
        if (menuIds.contains(menuId.toString())) {
            return true;
        } else {
            return false;
        }
    }

    /****
     * 查询选定的菜单
     * @param menuIds
     * @return
     */
    public List findMenuList(List<String> anRuleIdList, List menuIds) {
        List menuList = new ArrayList<SysMenuInfo>();
        Map anMap = new HashMap<String, Object>();
        List<SysMenuInfo> allList = new ArrayList<SysMenuInfo>();
        anMap.put("id", menuIds);
        allList = this.selectByProperty(anMap, "menuOrder");
        Map<Integer, SysMenuInfo> parentNoteMap = new LinkedHashMap<Integer, SysMenuInfo>();
        // 父节点
        List<PlatformBaseRuleType> userInnerRules = UserUtils.findInnerRules();
        logger.info("userInnerRules:" + userInnerRules);
        for (SysMenuInfo menuInfo : allList) {
            if ((menuInfo.getEndNode() == false) && "1".equals(menuInfo.getStatus())
                    && menuInfo.hasValidMenu(userInnerRules)) {
                parentNoteMap.put(menuInfo.getId(), menuInfo);
            }
        }
        // 子节点
        for (Integer parentId : parentNoteMap.keySet()) {
            Map<String, Object> sysMenuMap = new LinkedHashMap<String, Object>();
            SysMenuInfo parentMenuInfo = parentNoteMap.get(parentId);
            List<SysMenuInfo> noteList = this.selectByProperty("parentId", parentId, "menuOrder");
            List<Map<String, String>> noteMenuList = new ArrayList<Map<String, String>>();
            for (SysMenuInfo noteMenuInfo : noteList) {
                if ("1".equals(noteMenuInfo.getStatus()) && noteMenuInfo.hasValidMenu(userInnerRules)) {
                    // 判断该角色子菜单有没有权限
                    if (sysMenuRuleService.checkMenuRole(anRuleIdList, noteMenuInfo.getId())) {
                        Map noteMenuMap = new HashMap<String, String>();
                        noteMenuMap.put("menuId", noteMenuInfo.getId());
                        noteMenuMap.put("title", noteMenuInfo.getMenuTitle());
                        noteMenuMap.put("url", noteMenuInfo.getMenuUrl());
                        noteMenuList.add(noteMenuMap);
                    }
                }
            }
            sysMenuMap.put("menuId", parentMenuInfo.getId());
            sysMenuMap.put("title", parentMenuInfo.getMenuTitle());
            sysMenuMap.put("children", noteMenuList);
            menuList.add(sysMenuMap);

        }
        return menuList;
    }

}
