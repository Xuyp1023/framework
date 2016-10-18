package com.betterjr.modules.role.dubbo;

import java.util.Map;

/****
 * 角色管理
 * @author hubl
 *
 */
public interface IRoleService {

    /***
     * 添加角色
     * @param anMap
     * @return
     */
    public String webAddRole(String anRoleName,String anRoleType,String anBusinStatus);
    
    /****
     * 编辑角色
     * @param anMap
     * @return
     */
    public String webUploadRole(String anRoleId,String anRoleName,String anRoleType,String anBusinStatus);
    
    /***
     * 根据主键删除
     * @param anId
     * @return
     */
    public String webDelRole(Long anRoleId);
    
    /***
     * 分页查询角色信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    public String webQueryRole(Map<String, Object> anMap,int pageNum,int pageSize);
    
    /***
     * 查询所有角色信息
     * @return
     */
    public String webFindRole();
    
    /****
     * 查询默认角色
     * @return
     */
    public String webQueryRoleDefault();
    
    public String webAddDefRole();
}
