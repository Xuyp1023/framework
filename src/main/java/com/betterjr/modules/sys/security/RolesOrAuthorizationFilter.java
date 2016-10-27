// Copyright (c) 2014-2016 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年10月27日, liuwl, creation
// ============================================================================
package com.betterjr.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * @author liuwl
 *
 */
public class RolesOrAuthorizationFilter extends AuthorizationFilter {

    /* (non-Javadoc)
     * @see org.apache.shiro.web.filter.AccessControlFilter#isAccessAllowed(javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.Object)
     */
    @Override
    protected boolean isAccessAllowed(final ServletRequest anRequest, final ServletResponse anResponse, final Object anMappedValue) throws Exception {
        final Subject subject = getSubject(anRequest, anResponse);
        final String[] rolesArray = (String[]) anMappedValue;

        if (rolesArray == null || rolesArray.length == 0) { //没有角色限制，有权限访问
            return true;
        }
        for (final String element : rolesArray) {
            if (subject.hasRole(element)) { //若当前用户是rolesArray中的任何一个，则有权限访问
                return true;
            }
        }

        return false;
    }

}
