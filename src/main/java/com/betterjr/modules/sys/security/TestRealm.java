package com.betterjr.modules.sys.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.betterjr.common.data.UserType;

public class TestRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        // TODO Auto-generated method stub
        System.out.println("testrealm=权限认证");
        final AuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token)
            throws AuthenticationException {
        // TODO Auto-generated method stub
        final UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        final String username = upToken.getUsername();
        final char[] passwd = upToken.getPassword();

        final UserType ut = UserType.OPERATOR_USER;
        final ShiroUser user = new ShiroUser(ut, Long.valueOf(0l), username, null, null, null, false, token, null);

        final SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, passwd, "TestRealm");
        System.out.println("testrealm=" + info.toString());
        return info;
    }

}
