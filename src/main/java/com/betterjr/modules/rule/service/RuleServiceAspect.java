package com.betterjr.modules.rule.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.RuleContext;
import com.betterjr.modules.rule.annotation.ParamName;
import com.betterjr.modules.rule.entity.RuleBusiness;

/**
 * 服务切面类，通过该类拦截所有的服务，然后做规则检查。
 * 
 * @author zhoucy
 */
public class RuleServiceAspect {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    // 标记对象
    private static ThreadLocal<Object> market = new ThreadLocal();

    @Autowired
    private QlExpressUtil qlExpress;

    public QlExpressUtil getQlExpress() {
        return qlExpress;
    }

    public void init() {
        qlExpress.initRunner();
    }

    /**
     * 
     * 在前端调用时，清除定义标志，避免后续服务调用不走规则
     */
    public static void clearMarket() {

        market.set(null);
    }

    public static RuleCheckResult getCheckResult() {
        Object obj = market.get();
        if (obj instanceof RuleCheckResult) {

            return (RuleCheckResult) obj;
        }
        else {

            return new RuleCheckResult();
        }
    }

    public void setQlExpress(QlExpressUtil qlExpress) {
        this.qlExpress = qlExpress;
    }

    public void before(JoinPoint call) {
    }

    public void afterReturn() {

    }

    public void after() {

    }

    public void afterThrowing() {

    }

    // 检查参数是否自己实现了规则的上下文，如果自己实现；则就不在业务处理过程中实现。
    public static RuleContext checkParams(Class[] anClasses, Object[] anObjects) {
        Class cc;
        for (int i = 0, k = anClasses.length; i < k; i++) {
            cc = anClasses[i];
            if (RuleContext.class.isAssignableFrom(cc)) {

                return (RuleContext) anObjects[i];
            }
            else if (Map.class.isAssignableFrom(cc)) {

                return new RuleContext((Map) anObjects[i]);
            }
        }

        return null;
    }

    public static RuleContext constructContext(Method anM, Class[] anClasses, Object[] anObjects) {
        RuleContext context = checkParams(anClasses, anObjects);
        if (context == null) {
            context = new RuleContext();
        }
        Annotation[][] arrDoubleAnnot = anM.getParameterAnnotations();
        boolean hasDef = false;
        Parameter[] params = anM.getParameters();
        for (int i = 0, x = arrDoubleAnnot.length; i < x; i++) {
            Annotation[] arrAnnot = arrDoubleAnnot[i];
            hasDef = false;
            for (int k = 0, y = arrAnnot.length; k < y; k++) {
                if (arrAnnot[k] instanceof ParamName) {
                    ParamName pn = (ParamName) arrAnnot[k];
                    context.put(pn.value(), anObjects[i]);
                    hasDef = true;
                    break;
                }
            }

            // 如果没有定义，取参数名称
            // 注意：保留参数名这一选项由编译开关javac
            // -parameters打开，默认是关闭的；在Eclipse中，可以通过Compiler选项中的最后一个Store
            // Information...打开
            if (hasDef == false) {
                context.put(params[i].getName(), anObjects[i]);
            }
        }

        return context;
    }

    public RuleBusiness checkProcess(String anClass, String anMethod) {

        String name = BusinRuleService.getProcessName(anClass, anMethod);
        if (BetterStringUtils.isNotBlank(name) && this.qlExpress != null) {
        return this.qlExpress.findRuleBusin(name);
        }
        else{
            return null;
        }
    }

    public Object doAround(ProceedingJoinPoint call) throws Throwable {

        // 获取目标对象上正在执行的方法名
        MethodSignature ms = (MethodSignature) call.getSignature();
        System.out.println(ms);
        // 标记是否处理过，如果已经处理过，则不再重新处理服务
        if (market.get() != null || AopUtils.isAopProxy(call.getTarget())) {

            return call.proceed(call.getArgs());
        }
        else {
            market.set(new Object());
        }

        RuleBusiness rb = checkProcess(call.getTarget().getClass().getSimpleName(), ms.getMethod().getName());
        if (rb == null) {

            return call.proceed(call.getArgs());
        }

        RuleContext context = constructContext(ms.getMethod(), ms.getParameterTypes(), call.getArgs());

        context.put(BusinRuleService.METHOD, ms.getMethod());
        // context.put(BusinRuleService.BUSIN_INFO, rb);

        logger.info("Work Context Is :" + context.toString());

        Object result = null;
        try {
            RuleCheckResult checkResult = this.qlExpress.execute(rb, context);
            market.set(checkResult);

            if (checkResult.isOk()) {
                result = call.proceed(call.getArgs());
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
}