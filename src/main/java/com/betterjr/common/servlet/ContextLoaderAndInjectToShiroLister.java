package com.betterjr.common.servlet;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.betterjr.modules.sys.security.SystemAuthorizingRealm;

public class ContextLoaderAndInjectToShiroLister extends ContextLoaderListener {
	
	public static final String AuthorizingRealmInitPara="authorizingRealm";
	private static final String DubboClientServicePostfix="DubboClientService";
	
	/**
	 * Initialize the root web application context.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		//inject dubbo services into shiro realm
		WebApplicationContext context=this.getCurrentWebApplicationContext();
		
		String name = (String)context.getServletContext().getInitParameter(AuthorizingRealmInitPara);
		if(StringUtils.isBlank(name)){
			System.out.println("please init context-param in web.xml: "+AuthorizingRealmInitPara);
			System.exit(0);
		}
		
		AuthorizingRealm realm=(AuthorizingRealm)context.getBean(name);
		if(realm==null){
			System.out.println("shiro AuthorizingRealm bean name Not Equals with context-param "+AuthorizingRealmInitPara);
			System.exit(0);
		}
		
		Field[] fields=SystemAuthorizingRealm.class.getDeclaredFields();
		for(Field field:fields){
			Class fieldType=field.getType();
			String fieldTypeName=fieldType.getName();
			if(fieldTypeName.endsWith(DubboClientServicePostfix)){
				Object obj=context.getBean(fieldType);
				try {
					field.setAccessible(true);
					field.set(realm, obj);
				} catch (Exception e) {
					System.out.println("inject dubbo service into AuthorizingRealm failed");
					e.printStackTrace();
					System.exit(0);
				} 
			}
		}
	}

}
