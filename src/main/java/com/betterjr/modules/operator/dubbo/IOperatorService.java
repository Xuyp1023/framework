package com.betterjr.modules.operator.dubbo;

import java.util.Map;

import com.betterjr.modules.account.entity.CustOperatorInfo;

/***
 * 操作员管理
 * @author hubl
 *
 */

public interface IOperatorService {

    /****
     * 新增操作员
     * @param anMap
     * @return
     */
    public String webAddCustOperator(Map<String, Object> anMap,String anCustList);


    /****
     * 编辑操作员
     * @param anMap
     * @return
     */
    public String webUpdateCustOperator(Map<String, Object> anMap,String anCustList);

    /****
     * 操作员分页查询
     * @param anMap
     * @param pageNum
     * @param pageSize
     * @return
     */
    public String webQueryCustOperator(Map<String,String> anMap,int pageNum,int pageSize);

    /****
     * 获取左侧菜单信息
     * @param menuId
     * @return
     */
    public String webFindSysMenuByMenuId(Integer anMenuId);

    /****
     * 获取根据角色获取菜单
     * @param menuId
     * @return
     */
    public String webFindSysMenuByMenuRole(String anRoleId);

    /****
     * 获取所有菜单信息
     * @return
     */
    public String webFindAllSysMenu();

    /****
     * 添加菜单角色信息
     * @param anRoleId 角色ID
     * @param anRoleName 角色名称
     * @param anMenuIdArr 菜单列表，前端传来 "，"分隔
     * @return
     */
    public String webAddMenuRole(String anRoleId,String anRoleName,String anMenuIdArr);

    /***
     * 查询操作员
     * @param operatorId
     * @return
     */
    public String webFindOperatorById(Long operatorId);
    /****
     * 获取当前登录机构下的所有操作员
     */
    public String webFindCustOperator();

    /****
     * 修改密码
     * @param anNewPasswd 新密码
     * @param anOkPasswd  确认密码
     * @param anPasswd 原密码
     * @return
     */
    public String webUpdatePassword(String anNewPasswd,String anOkPasswd,String anPasswd);

    /***
     * 密码重置
     * @param id
     * @param password
     * @param okPasswd
     * @return
     */
    public String webChangeUserPassword(Long anId, String anPassword, String anOkPasswd);

    /**
     * 根据客户信息，查找机构默认经办人信息
     * @param anOperOrg
     * @return
     */
    public CustOperatorInfo findCustClerkMan(String anOperOrg,String anClerkMan);

    /***
     * 查询机构绑定的客户信息
     * @return
     */
    public String webFindOperatorCustInfo();


    /**
     * 根据入参查询对应操作机构操作员
     */
    public String webFindCustOperatorByClerk(String anClerk);

}
