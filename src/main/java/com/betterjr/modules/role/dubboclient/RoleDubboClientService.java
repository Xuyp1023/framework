package com.betterjr.modules.role.dubboclient;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.role.dubbo.IRoleService;
@Service
public class RoleDubboClientService implements IRoleService {

    @Reference(interfaceClass=IRoleService.class)
    private IRoleService roleService;
    
    @Override
    public String webAddRole(String anRoleName, String anRoleType, String anBusinStatus) {
        return roleService.webAddRole(anRoleName, anRoleType, anBusinStatus);
    }

    @Override
    public String webUploadRole(String anRoleId, String anRoleName, String anRoleType, String anBusinStatus) {
        return roleService.webUploadRole(anRoleId, anRoleName, anRoleType, anBusinStatus);
    }

    @Override
    public String webDelRole(Long anRoleId) {
        return roleService.webDelRole(anRoleId);
    }

    @Override
    public String webQueryRole(Map<String, Object> anMap, int anPageNum, int anPageSize) {
        return roleService.webQueryRole(anMap, anPageNum, anPageSize);
    }

    @Override
    public String webFindRole() {
        return roleService.webFindRole();
    }

    @Override
    public String webQueryRoleDefault() {
        return roleService.webQueryRoleDefault();
    }

    @Override
    public String webAddDefRole() {
        return roleService.webAddDefRole();
    }

}
