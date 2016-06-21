package com.betterjr.modules.rule.service;

import com.betterjr.common.annotation.AnnotRuleService;
import com.betterjr.modules.rule.annotation.*;
import com.betterjr.modules.rule.entity.RuleObjectFace;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

class RuleDefinitionValidator {

    public void validateRuleDefinition(final Object rule) {
        checkRuleClass(rule);
        checkConditionMethod(rule);
        checkActionMethods(rule);
    }

    private void checkRuleClass(final Object rule) {
        if (!isRuleClassWellDefined(rule)) {
            throw new IllegalArgumentException(format("规则服务类 '%s' 没有注解为 '%s'", rule.getClass().getName(), AnnotRuleService.class.getName()));
        }
        else if ((rule instanceof RuleObjectFace) == false){

            throw new IllegalArgumentException(format("规则服务类 '%s' 必须实现接口RuleObjectFace", rule.getClass().getName()));
        }
    }

    private void checkConditionMethod(final Object rule) {
        List<Method> conditionMethods = getMethodsAnnotatedWith(RuleCondition.class, rule);
        if (conditionMethods.isEmpty()) {
            throw new IllegalArgumentException(format("规则 '%s' 必须注解条件方法 '%s'", rule.getClass().getName(), RuleCondition.class.getName()));
        }

        for (Method conditionMethod : conditionMethods) {

            if (!isConditionMethodWellDefined(conditionMethod)) {
                throw new IllegalArgumentException(
                        format("条件执行方法 '%s' 在规则执行类 '%s' 中，必须是公开的返回结果是Boolean,而且没有入参.", conditionMethod, rule.getClass().getName()));
            }
        }
    }

    private void checkActionMethods(final Object rule) {
        List<Method> actionMethods = getMethodsAnnotatedWith(RuleAction.class, rule);
        if (actionMethods.isEmpty()) {
            throw new IllegalArgumentException(format("规则 '%s' 必须有规则执行方法注解 '%s'", rule.getClass().getName(), RuleAction.class.getName()));
        }

        for (Method actionMethod : actionMethods) {
            if (!isActionMethodWellDefined(actionMethod)) {
                throw new IllegalArgumentException(format("规则执行方法 '%s' 在规则执行类 '%s' 中，必须是公开的无参数和返回结果.", actionMethod, rule.getClass().getName()));
            }
        }
    }

    private boolean isRuleClassWellDefined(final Object rule) {
        return rule.getClass().isAnnotationPresent(AnnotRuleService.class);
    }

    private boolean isConditionMethodWellDefined(final Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(Boolean.TYPE) && method.getParameterTypes().length == 0;
    }

    private boolean isActionMethodWellDefined(final Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0;
    }

    public boolean isPriorityMethodWellDefined(final Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(Integer.TYPE) && method.getParameterTypes().length == 0;
    }

    private List<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation, final Object rule) {
        Method[] methods = getMethods(rule);
        List<Method> annotatedMethods = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods;
    }

    private Method[] getMethods(final Object rule) {
        return rule.getClass().getMethods();
    }

}
