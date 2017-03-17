package com.betterjr.modules.role.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterDeclareException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.operator.service.SysOperatorRoleRelationService;
import com.betterjr.modules.role.dao.RoleMapper;
import com.betterjr.modules.role.entity.Role;
import com.betterjr.modules.sys.service.SysMenuRuleService;

/****
 * 角色管理
 *
 * @author hubl
 *
 */
@Service
public class RoleService extends BaseService<RoleMapper, Role> {

    @Autowired
    private SysOperatorRoleRelationService sysOperatorRoleService;
    @Autowired
    private SysMenuRuleService sysMenuRuleService;

    /***
     * 添加角色信息
     *
     * @param role
     * @return
     */
    public boolean addRole(final String anRoleName, final String anRoleType, final String anBusinStatus) {
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final Role anRole = new Role("", anRoleName, anRoleType, anBusinStatus, custOperator.getOperOrg(), "1");
        if (checkRoleName(anRole.getRoleName())) {
            throw new BytterDeclareException("角色名称已存在");
        }
        return this.insert(anRole) == 1;
    }

    /***
     * 修改角色信息
     *
     * @param role
     * @return
     */
    public boolean updateRole(final String anRoleId, final String anRoleName, final String anRoleType, final String anBusinStatus) {
        if (BetterStringUtils.isBlank(anRoleId)) {
            throw new BytterDeclareException("要修改的角色ID不存在");
        }
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final Role anRole = new Role(anRoleId, anRoleName, anRoleType, anBusinStatus, custOperator.getOperOrg(), "1");
        return this.updateByPrimaryKey(anRole) == 1;
    }

    /***
     * 检查角色名称是否存在
     *
     * @param roleName
     * @return
     */
    public boolean checkRoleName(final String roleName) {
        final List<Role> roleList = this.selectByProperty("roleName", roleName);
        if (roleList.size() > 0) {
            return true;
        }
        return false;
    }

    /***
     * 删除
     *
     * @param anRoleId
     * @return
     */
    public boolean delRole(final Long anRoleId) {
        final Role role = this.selectByPrimaryKey(anRoleId);
        // 绑定的操作员，菜单关系表都相应的删除
        sysOperatorRoleService.delSysOperatorRole(role.getId());
        sysMenuRuleService.delMenuRole(role.getId());
        return this.delete(role) == 1;
    }

    /***
     * 分页查询
     *
     * @param anMap
     * @param anPageNum
     * @param anPageSize
     * @return
     */
    public Page<Role> queryRole(final Map<String, Object> anMap, final int anPageNum, final int anPageSize) {
        final Map<String, Object> map = new HashMap<String, Object>();
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        map.put("operOrg", custOperator.getOperOrg());
        if (BetterStringUtils.isNotBlank((String) anMap.get("roleName"))) {
            map.put("roleName", anMap.get("roleName"));
        }

        return this.selectPropertyByPage(Role.class, map, anPageNum, anPageSize, "1".equals(anMap.get("flag")));
    }

    /***
     * 查询所有角色信息
     *
     * @return
     */
    public List<Role> findRole() {
        final Map<String, Object> roleMp = new HashMap<>();
        roleMp.put("businStatus", "1");
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        roleMp.put("operOrg", custOperator.getOperOrg());
        return this.selectByProperty(roleMp);
    }

    /***
     * 根据名称获取对象
     *
     * @param roleName
     * @return
     */
    public Role findRoleById(final Long roleId) {
        final Map<String, Object> roleMp = new HashMap<>();
        roleMp.put("id", roleId);
        roleMp.put("businStatus", "1");
        final List<Role> roleList = this.selectByProperty(roleMp);
        return Collections3.getFirst(roleList);
    }

    /****
     * 查询默认角色
     *
     * @return
     */
    public List<SimpleDataEntity> queryRoleDefault() {
        final List<SimpleDataEntity> result = new ArrayList<SimpleDataEntity>();
        final Map<String, Object> roleMp = new HashMap<>();
        roleMp.put("def", "0");
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        roleMp.put("operOrg", custOperator.getOperOrg());
        for (final Role role : this.selectByProperty(roleMp)) {
            result.add(new SimpleDataEntity(role.getRoleName(), role.getRoleType()));
        }
        logger.info(result.toString());
        return result;
    }

    /****
     * 添加系统角色
     *
     * @param anOperOrg
     * @return true 添加成功，false 该操作员的默认角色已添加
     */
    public boolean addDefaultRole(final String anOperOrg) {
        final Map<String, Object> anMap = new HashMap<>();
        anMap.put("def", "0");
        anMap.put("operOrg", anOperOrg);
        final List<Role> roleList = this.selectByProperty(anMap);
        if (roleList.size() <= 0) {
            // 默认添加三个角色信息，管理员，审批员，复核员，经办员
            //            Role anRole = new Role("", "管理员", "OPERATOR_ADMIN", "1", anOperOrg, "0");
            //            this.insert(anRole);
            Role anRole = new Role("", "审批员", "OPERATOR_ADUIT", "1", anOperOrg, "0");
            this.insert(anRole);
            anRole = new Role("", "复核员", "OPERATOR_CHECKER", "1", anOperOrg, "0");
            this.insert(anRole);
            anRole = new Role("", "经办员", "OPERATOR_USER", "1", anOperOrg, "0");
            this.insert(anRole);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param anRoleList
     * @return
     */
    public String findRoleName(final String anRoleList) {
        BTAssert.isTrue(BetterStringUtils.isNotBlank(anRoleList), "角色列表不能为空.");
        final String[] roles = anRoleList.split(",");
        final StringBuilder roleName = new StringBuilder();
        for (final String roleId: roles) {
            roleName.append(this.findRoleById(Long.valueOf(roleId)).getRoleName()).append(",");
        }
        roleName.deleteCharAt(roleName.length() - 1);
        return roleName.toString();
    }

}
