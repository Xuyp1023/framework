package com.betterjr.modules.sys.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.betterjr.common.entity.UserType;
import com.betterjr.common.utils.StaticThreadLocal;

public class TestRealm extends AuthorizingRealm{

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		System.out.println("testrealm=权限认证");
		AuthorizationInfo info=new SimpleAuthorizationInfo();
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		  UsernamePasswordToken upToken = (UsernamePasswordToken)token;
		  String username = upToken.getUsername();
		  char[] passwd=upToken.getPassword();
		  
		  UserType ut = UserType.OPERATOR_USER;
		  ShiroUser user=new ShiroUser(ut,0l,username,null,false,token);
		  
		  SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, passwd,"TestRealm");
		  System.out.println("testrealm="+info.toString());
		  return info;
	}

}