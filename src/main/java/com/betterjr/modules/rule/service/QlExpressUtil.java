package com.betterjr.modules.rule.service;

import java.util.*;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.betterjr.common.annotation.AnnotRuleService;
import com.betterjr.common.exception.BettjerNestedException;
import com.betterjr.common.exception.BytterDeclareException;
import com.betterjr.common.utils.XmlUtils;
import com.betterjr.modules.rule.RuleCheckResult;
import com.betterjr.modules.rule.RuleContext;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.RuleFuncType;
import com.betterjr.modules.rule.entity.RuleFunction;
import com.ql.util.express.ExpressRunner;
import com.ql.util.express.ExpressUtil;
import com.ql.util.express.parse.ExpressPackage;

/**
 * 表达式工具类，主要实现Spring引用对象的动态加载。
 * 
 * @author zhoucy
 *
 */
@Component
public class QlExpressUtil implements ApplicationContextAware, InitializingBean {

    private static final ExpressRunner runner = new ExpressRunner(true, false);

    private static boolean isInitialRunner = false;
    private static ApplicationContext appContext;// spring上下文
    private List<String> defPackages = new ArrayList();

    public static ExpressRunner getRunner(){
        return runner;
    }
    public void setDefPackages(List<String> anDefPackages) {
        defPackages = anDefPackages;
    }

    @Autowired
    private BusinRuleService ruleService;

    public BusinRuleService getRuleService() {
        return ruleService;
    }

    public void setRuleService(BusinRuleService ruleService) {
        this.ruleService = ruleService;
    }

    public RuleBusiness findRuleBusin(String anName) {

        return this.ruleService.findRuleBusin(anName);
    }

    /**
     * 
     * @param statement 执行语句
     * @param context 上下文
     * @throws Exception
     */
    public RuleCheckResult execute(RuleBusiness rb, RuleContext context) throws Exception {

        return ruleService.execute(rb, runner, new QLExpressContext(context, appContext));
    }

    public static Object simpleInvoke(String qlExp, Map anMap) throws Exception {
        QLExpressContext context = new QLExpressContext(anMap, appContext);
        runner.setTrace(true);
        return runner.execute(qlExp, context, new ArrayList(), true, true, ExpressRunner.getLog());
    }
    public static Object invoke(String qlExp, String anWorkType, Object anObj, List anErrList) throws Exception{
        runner.setTrace(true);
        Map<String, Object> map = null;
        if (( anObj instanceof Map) && ("3".equals(anWorkType))){
           map = (Map)anObj; 
        }
        else{
           map = new HashMap();
           map.put("inputParam", anObj); 
        }
        QLExpressContext context = new QLExpressContext(map, appContext);
        
        return runner.execute(qlExp, context, anErrList, true, true, ExpressRunner.getLog());
    }
    
    public void initRunner() {
        if (isInitialRunner == true) {
            return;
        }
        synchronized (runner) {
            if (isInitialRunner == true) {
                return;
            }
            try {
                // runner.addMacro("test", "jsonTest.fromToXml(y)");
                // runner.addFunctionOfServiceMethod("test",
                // appContext.getBean("jsonTest"), "fromToXml", new String[] {
                // "Object" }, "this is test");
                // 设置默认的函数使用的包，默认包括：java.lang.*; java.util.*；可以增加在业务中使用到的表达式内容。
                if (this.defPackages != null) {
                    ExpressPackage epList = runner.getRootExpressPackage();
                    for (String tmpPackage : this.defPackages) {

                        epList.addPackage(tmpPackage);
                    }
                }
                final Map<String, Object> ruleClasses = appContext.getBeansWithAnnotation(AnnotRuleService.class);
                List<RuleFunction> funMap = this.ruleService.initRule(ruleClasses);
                mergeFunction(runner, funMap);

            }
            catch (Exception e) {
                throw new BettjerNestedException(40005, "初始化失败表达式", e);
            }
        }
        isInitialRunner = true;
    }

    private Class[] findParamList(String anStr) {
        List<String> arrList = XmlUtils.split(anStr);
        Class[] arrClass = new Class[arrList.size()];
        for (int i = 0, k = arrClass.length; i < k; i++) {
            arrClass[i] = ExpressUtil.getJavaClass(arrList.get(i));
        }
        return arrClass;
    }

    public void mergeFunction(ExpressRunner runner, List<RuleFunction> funMap) {
        RuleFuncType funcType;
        funMap.addAll(ruleService.findAllFunc());
        for (RuleFunction func : funMap) {
            funcType = RuleFuncType.checking(func.getFundType());
            try {
                switch (funcType) {
                case OBJECT:
                    Object obj = this.appContext.getBean(func.getClassName());
                    if (obj != null) {
                        runner.addFunctionOfServiceMethod(func.getFeatureName(), obj, func.getFuncName(), findParamList(func.getParamList()),
                                func.getErrorInfo());
                    }
                    else {
                        throw new BytterDeclareException(40003, "runner.addFunctionOfServiceMethod has error, spring javabean " + func.getClassName()
                                + ", not exits!");

                    }
                    break;

                case CLASS:
                    runner.addFunctionOfClassMethod(func.getFeatureName(), func.getClassName(), func.getFuncName(),
                            findParamList(func.getParamList()), func.getErrorInfo());
                    break;

                case MACRO:
                default:
                    runner.addMacro(func.getFeatureName(), func.getClassName());
                    break;
                }
            }
            catch (Exception e) {
                throw new BytterDeclareException(40002, "runner.addFunctionOfServiceMethod has error", e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public void setApplicationContext(ApplicationContext aContext) throws BeansException {
        appContext = aContext;
    }
}