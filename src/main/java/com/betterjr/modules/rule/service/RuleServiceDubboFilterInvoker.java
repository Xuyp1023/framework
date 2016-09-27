package com.betterjr.modules.rule.service;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.RuleContext;
import com.betterjr.modules.rule.annotation.ParamName;
import com.betterjr.modules.rule.entity.RuleBusiness;

/**
 * 表达式引擎服务类。
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
     * 表达式引擎的初始化入口
     */
//    @PostConstruct
//    public void init() {
//        qlExpress.initRunner();
//    }

    
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
     * 将调用接口的参数写入context,以 参数名-参数值 的形式， 如果是Map类型的参数，则直接将map里面的key-value复制到context
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
     * 根据类名+方法名 查找相关配置
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
     * 接口参数验证
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
                String errormsg="method:("+invoker.getInterface()+"."+invocation.getMethodName()+") parameters valid failed!" ;
                List<String> errorList=checkResult.getErrorList();
                if(errorList!=null){
                    errorList.add(errormsg);
                }else{
                    errorList=new ArrayList<String>();
                    errorList.add(errormsg);
                }
                logger.error(errorList.toString());
            	throw new BytterValidException(90002,errorList);
            }

            this.afterReturn(); // 相当于后置通知
        }
        catch (Throwable e) {
            System.out.print(e);
            this.afterThrowing(); // 相当于异常抛出后通知
            throw e;
        }
        finally {
            this.after(); // 相当于最终通知
            // market.set(null);
        }

        return result;
    }
    
    /*
     * 将接口参数转换为一个对象，方便service实现方使用
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
  
    
    public static <T> T getInputObj(){
    	return (T)inputLocal.get();
    }
    
    public static void clearThreadLocal(){
        inputLocal.remove();
    }
}