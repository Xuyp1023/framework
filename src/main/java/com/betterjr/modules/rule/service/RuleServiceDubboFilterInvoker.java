package com.betterjr.modules.rule.service;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.RuleContext;
import com.betterjr.modules.rule.annotation.ParamName;
import com.betterjr.modules.rule.entity.RuleBusiness;

/**
 * ���ʽ��������ࡣ
 * 
 * @author zhoucy
 */
@Component
public class RuleServiceDubboFilterInvoker {
    protected static Logger logger = LoggerFactory.getLogger(RuleServiceDubboFilterInvoker.class);
    
    protected static ThreadLocal inputLocal=new ThreadLocal();
    
 
    @Autowired
    private QlExpressUtil qlExpress;
    
    /**
     * ���ʽ����ĳ�ʼ�����
     */
    @PostConstruct
    public void init() {
        qlExpress.initRunner();
    }

    
    public QlExpressUtil getQlExpress() {
        return qlExpress;
    }

    public void setQlExpress(QlExpressUtil qlExpress) {
        this.qlExpress = qlExpress;
    }

   

    public void afterReturn() {

    }

    public void after() {

    }

    public void afterThrowing() {

    }

   

    /**
     * �����ýӿڵĲ���д��context,�� ������-����ֵ ����ʽ�� �����Map���͵Ĳ�������ֱ�ӽ�map�����key-value���Ƶ�context
     * @param anInterface
     * @param anMethodName
     * @param anParaTypes
     * @param anObjects
     * @return
     */
    public static RuleContext constructContext(Class anInterface,String anMethodName,Class[] anParaTypes, Object[] anObjects) {
        RuleContext context =  new RuleContext();
        
        try {
			Method method=anInterface.getMethod(anMethodName, anParaTypes);
	        Parameter[] params = method.getParameters();
	        for (int i = 0, x = params.length; i < x; i++) {
	        		if(Map.class.isAssignableFrom(params[i].getType())){
	        			context.putAll((Map)anObjects[i]);
	        		}else{
	        			context.put(params[i].getName(), anObjects[i]);
	        		}
	        }
		} catch (NoSuchMethodException e) {
			logger.error(e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			logger.error(e.getLocalizedMessage(), e);
		}
        
        return context;
    }

    /*
     * ��������+������ �����������
     */
    public RuleBusiness checkProcess(String anClass, String anMethod) {
        String name = BusinRuleService.getProcessName(anClass, anMethod);
        if (BetterStringUtils.isNotBlank(name) && this.qlExpress != null) {
        return this.qlExpress.findRuleBusin(name);
        }
        else{
            return null;
        }
    }

    /*
     * �ӿڲ�����֤
     */
    public Result doAround(Invoker<?> invoker, Invocation invocation) throws Throwable {

        RuleBusiness rb = checkProcess(invoker.getInterface().getSimpleName(), invocation.getMethodName());
        if (rb == null) {
            return invoker.invoke(invocation);
        }

        RuleContext context = constructContext(invoker.getInterface(),invocation.getMethodName(),invocation.getParameterTypes(),invocation.getArguments());

        this.tranParaToObj(invoker, invocation, context);
        logger.info("Work Context Is :" + context.toString());

        Result result = null;
        try {
            RuleCheckResult checkResult = this.qlExpress.execute(rb, context);
 
            if (checkResult.isOk()) {
                result = invoker.invoke(invocation);
            }else{
            	throw new RpcException("method parameters valid failed!");
            }

            this.afterReturn(); // �൱�ں���֪ͨ
        }
        catch (Throwable e) {
            System.out.print(e);
            this.afterThrowing(); // �൱���쳣�׳���֪ͨ
            throw e;
        }
        finally {
            this.after(); // �൱������֪ͨ
            // market.set(null);
        }

        return result;
    }
    
    /*
     * ���ӿڲ���ת��Ϊһ�����󣬷���serviceʵ�ַ�ʹ��
     */
    public void tranParaToObj(Invoker<?> invoker, Invocation invocation,RuleContext context ) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
    	//tran paras to map
    	Map<String, String> paraMap=context;
    	RuleBusiness rb=this.checkProcess(invoker.getInterface().getSimpleName(), invocation.getMethodName());
    	//get data obj class
    	String clsName=rb.getEntity();
    	Class cls=Class.forName(clsName);
    	Object obj=this.qlExpress.getRuleService().buildRequest(cls.newInstance(), paraMap, rb.getBusinName());
    	inputLocal.set(obj);
    	logger.debug("ParaCheckByRuleFilter test:stored obj into thread local:");
    }
  
    
    public static Object getInputObj(){
    	return inputLocal.get();
    }
}