package com.betterjr.modules.rule.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.betterjr.common.annotation.AnnotRuleService;
import com.betterjr.common.exception.BytterException;
import com.betterjr.modules.rule.annotation.*;
import com.betterjr.modules.rule.entity.BaseRuleFace;
import com.betterjr.modules.rule.entity.RuleFace;
import com.betterjr.modules.rule.entity.RuleObjectFace;

class RuleProxy implements InvocationHandler, Cloneable {

    private RuleObjectFace target;

    private static RuleDefinitionValidator ruleDefinitionValidator = new RuleDefinitionValidator();
    private Integer priority = 0;
    private String ruleNo = "";
    private String groupName = "";
    private List<ActionMethodOrderBean> actionList;
    private List<ActionMethodOrderBean> condList;

    public void setPriority(Integer anPriority) {
        priority = anPriority;
    }

    public void setRuleNo(String anRuleNo) {
        ruleNo = anRuleNo;
    }

    public void setGroupName(String anGroupName) {
        groupName = anGroupName;
    }

    public RuleProxy clone() {
        RuleProxy rp;
        try {
            rp = (RuleProxy) super.clone();
            rp.target = (RuleObjectFace) this.target.clone();
            return rp;
        }
        catch (CloneNotSupportedException e) {

            throw new BytterException(41009, "Clone Object RuleProxy, CloneNotSupportedException", e);
        }
    }

    public RuleProxy() {

    }

    public RuleProxy(final Object target) {
        this.target = (RuleObjectFace) target;
        this.actionList = getActionMethodBeans();
        this.condList = getConditionMethodBeans();
    }

    public BaseRuleFace cloneFace() {
        RuleProxy rpp = this.clone();

        BaseRuleFace rf = (BaseRuleFace) Proxy.newProxyInstance(BaseRuleFace.class.getClassLoader(),
                new Class[] { BaseRuleFace.class, Comparable.class }, rpp);

        return rf;
    }

    public static BaseRuleFace asRule(final Object rule) {
        ruleDefinitionValidator.validateRuleDefinition(rule);
        RuleProxy rp = new RuleProxy(rule);
        BaseRuleFace rf = (BaseRuleFace) Proxy.newProxyInstance(BaseRuleFace.class.getClassLoader(),
                new Class[] { BaseRuleFace.class, Comparable.class }, rp);

        return rf;
    }

    private Integer getValue(Object anObj) {
        if (anObj == null) {
            return new Integer(0);
        }
        else if (anObj instanceof Integer) {
            return (Integer) anObj;
        }
        else if (anObj instanceof String) {
            return new Integer((String) anObj);
        }
        else if (anObj.getClass() == int.class) {
            return new Integer((int) anObj);
        }
        else {
            return new Integer(anObj.toString());
        }
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (method.getName().equals("getRuleNo")) {

            return this.ruleNo;
        }
        else if (method.getName().equals("getRuleName")) {

            return this.ruleNo;
        }
        else if (method.getName().equals("getGroupName")) {

            return this.groupName;
        }
        else if (method.getName().equals("setGroupName")) {

            this.groupName = args[0].toString();
        }
        else if (method.getName().equals("setContext")) {

            QLExpressContext context = (QLExpressContext) args[0];
            this.target.setContext(context);
        }
        else if (method.getName().equals("getContext")) {

            return this.target.getContext();
        }
        else if (method.getName().equals("getDescription")) {

            return getRuleAnnotation().description();
        }
        else if (method.getName().equals("setRuleNo")) {

            this.ruleNo = args[0].toString();
        }
        else if (method.getName().equals("setPriority")) {

            this.priority = getValue(args[0]);
        }
        else if (method.getName().equals("getPriority")) {
            return getPriority();
        }
        else if (method.getName().equals("clone")) {
            
            return this.cloneFace();
        }
        else if (method.getName().equals("evaluate")) {
            Boolean bb = null;
            for (ActionMethodOrderBean actionMethodBean : condList) {
                bb = (Boolean) actionMethodBean.getMethod().invoke(target);
                if (bb == null || bb.booleanValue() == false) {

                    return Boolean.FALSE;
                }
            }
            return Boolean.TRUE;
        }
        else if (method.getName().equals("execute")) {
            Object obj = null;
            for (ActionMethodOrderBean actionMethodBean : actionList) {
                obj = actionMethodBean.getMethod().invoke(target);
            }
            return obj;
        }
        else if (method.getName().equals("equals")) {
            return target.equals(args[0]);
        }
        else if (method.getName().equals("hashCode")) {
            return target.hashCode();
        }
        else if (method.getName().equals("toString")) {
            return target.toString();
        }
        else if (method.getName().equals("compareTo")) {
            Method compareToMethod = getCompareToMethod();
            if (compareToMethod != null) {
                return compareToMethod.invoke(target, args);
            }
            else {
                RuleFace otherRule = (RuleFace) args[0];
                return compareTo(otherRule);
            }
        }
        return null;
    }

    private int compareTo(final RuleFace otherRule) {
        String otherName = otherRule.getRuleNo();
        int otherPriority = otherRule.getPriority();
        int priority = getPriority();

        if (priority < otherPriority) {
            return -1;
        }
        else if (priority > otherPriority) {
            return 1;
        }
        else {
            return this.ruleNo.compareTo(otherName);
        }
    }

    private int getPriority() {

        return this.priority;
    }

    private List<ActionMethodOrderBean> getConditionMethodBeans() {
        Method[] methods = getMethods();
        List<ActionMethodOrderBean> condBeans = new LinkedList<ActionMethodOrderBean>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RuleCondition.class)) {
                RuleCondition rc = method.getAnnotation(RuleCondition.class);
                condBeans.add(new ActionMethodOrderBean(method, rc.order(), rc.description()));
            }
        }
        Collections.sort(condBeans);

        return condBeans;
    }

    private List<ActionMethodOrderBean> getActionMethodBeans() {
        Method[] methods = getMethods();
        List<ActionMethodOrderBean> actionMethodBeans = new LinkedList<ActionMethodOrderBean>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RuleAction.class)) {
                RuleAction aa = method.getAnnotation(RuleAction.class);
                actionMethodBeans.add(new ActionMethodOrderBean(method, aa.order(), aa.description()));
            }
        }
        Collections.sort(actionMethodBeans);

        return actionMethodBeans;
    }

    private Method getCompareToMethod() {
        try {
            return target.getClass().getMethod("compareTo", RuleFace.class);
            /*
             * Method[] methods = getMethods(); for (Method method : methods) { if (method.getName().equals("compareTo")) { return method; } }
             */ }
        catch (Exception ex) {

        }
        return null;
    }

    private Method[] getMethods() {
        return target.getClass().getMethods();
    }

    private AnnotRuleService getRuleAnnotation() {
        return target.getClass().getAnnotation(AnnotRuleService.class);
    }

}
