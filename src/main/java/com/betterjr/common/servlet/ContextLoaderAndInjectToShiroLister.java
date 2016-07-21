package com.betterjr.common.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.sys.dubboclient.DictToWebDubboClientService;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm;

public class ContextLoaderAndInjectToShiroLister extends ContextLoaderListener {
	
	public static final String AuthorizingRealmInitPara="authorizingRealm";
	public static final String DictDataFileName="jsParamFileName";
	private static final String DubboClientServicePostfix="DubboClientService";
	
	/**
	 * Initialize the root web application context.
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
        
		WebApplicationContext context=this.getCurrentWebApplicationContext();
		
        String shiroRealmName = (String)context.getServletContext().getInitParameter(AuthorizingRealmInitPara);
        if(BetterStringUtils.isBlank(shiroRealmName)){
            System.out.println("please init context-param in web.xml: "+AuthorizingRealmInitPara);
            System.exit(0);
        }
        
        String dictJs = (String)context.getServletContext().getInitParameter(DictDataFileName);
        if(BetterStringUtils.isBlank(dictJs)){
            System.out.println("please init context-param in web.xml: "+DictDataFileName);
            System.exit(0);
        }
		
		injectDubboIntoShiroRealm(context,shiroRealmName);
		
		createDictDataJS(context,dictJs);
	}
	
	/**
	 * init dict data js file
	 * @param anContext
	 * @param shiroRealmName
	 */
	private void createDictDataJS(WebApplicationContext anContext,String anDictJs){
	    
        String anFilePath = anContext.getServletContext().getRealPath(anDictJs);
        System.out.println("dict data js file:" + anFilePath);
        
	    DictToWebDubboClientService dictService=anContext.getBean(DictToWebDubboClientService.class);
	    String content=dictService.initWebCreateDictJS();
	    BufferedWriter outer = null;
        try {
            File ff = new File(anFilePath);
            if (ff.getParentFile().exists() == false) {
                ff.getParentFile().mkdirs();
            }
            outer = new BufferedWriter(new FileWriter(anFilePath));
            outer.write(content);
            outer.flush();
            outer.close();
        }
        catch (IOException ex) {

        }
        finally {
            IOUtils.closeQuietly(outer);
        }
	}

	/*
	 * inject dubbo services into shiro realm
	 */
    private void injectDubboIntoShiroRealm(WebApplicationContext anContext,String anShiroRealmName) {
		
		AuthorizingRealm realm=(AuthorizingRealm)anContext.getBean(anShiroRealmName);
		if(realm==null){
			System.out.println("shiro AuthorizingRealm bean name Not Equals with context-param "+AuthorizingRealmInitPara);
			System.exit(0);
		}
		
		Field[] fields=SystemAuthorizingRealm.class.getDeclaredFields();
		for(Field field:fields){
			Class fieldType=field.getType();
			String fieldTypeName=fieldType.getName();
			if(fieldTypeName.endsWith(DubboClientServicePostfix)){
				Object obj=anContext.getBean(fieldType);
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
