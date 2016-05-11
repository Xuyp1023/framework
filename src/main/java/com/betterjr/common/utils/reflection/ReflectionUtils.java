/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.betterjr.common.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.betterjr.common.mapper.BeanMapperHelper;
import com.betterjr.common.utils.Collections3;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.beans.*;

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 * 
 * @author henryzhou
 * @version 2016-01-15
 */
@SuppressWarnings("rawtypes")
public class ReflectionUtils {

    public static final String SETTER_PREFIX = "set";

    public static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 调用Getter方法. 支持多级，如：对象名.对象名.方法
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, ".")) {
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[] {}, BeanMapperHelper.objs);
        }
        return object;
    }
        
    public static Multimap listConvertToMuiltMap(List anList, String anProperName) {

        return (Multimap) listConvertToMap(anList, anProperName, true);
    }

    public static Map listConvertToMap(List anList, String anProperName) {

        return (Map) listConvertToMap(anList, anProperName, false);
    }
    
    private static Object listConvertToMap(List anList, String anProperName, boolean anMuitMap) {
        
        return listConvertToMap(anList, anProperName, anMuitMap , false);
    }
    
    public static Map listConvertToMapKeyObj(List anList, String anProperName) {
        
        return (Map) listConvertToMap(anList, anProperName, false, true);
    }
    
    private static Object listConvertToMap(List anList, String anProperName, boolean anMuitMap, boolean anUserObj) {
        if (Collections3.isEmpty(anList) == false) {
            Object obj = anList.get(0);
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(anProperName);
            try {
                Method mm = obj.getClass().getMethod(getterMethodName, new Class[] {});
                if (anMuitMap) {
                    Multimap map = HashMultimap.create();
                    for (Object subObj : anList) {
                        map.put(mm.invoke(subObj, BeanMapperHelper.objs), subObj);
                    }
                    return map;
                }
                else {
                    Map map = null;
                    if (anUserObj){
                        map = new LinkedHashMap();
                    }
                    else{
                        map = new LinkedCaseInsensitiveMap();
                    }
                    for (Object subObj : anList) {
                        Object mmX = mm.invoke(subObj, BeanMapperHelper.objs);
                        if (mmX != null ){
                            if (anUserObj){
                                map.put(mmX, subObj);
                            }
                            else{
                                map.put(mmX.toString(), subObj);
                            }
                        }
                    }
                    return map;
                }

            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
        if (anMuitMap){
            return HashMultimap.create(); 
        }
        else{
            return new HashMap();
        }

    }

    public static Map<String, Method> findReadMethods(Class anClass) {

        return findMethodMap(anClass, true);
    }

    public static Map<String, Method> findMethodMap(Class anClass, boolean anReader) {
        Map<String, Method> mapMethod = new LinkedCaseInsensitiveMap<>();
        String tmpName;
        List<Method> methodList = findClassReadMethods(anClass, anReader);
        for (Method mm : methodList) {
            if (mm == null) {
                continue;
            }

            tmpName = StringUtils.uncapitalize(mm.getName().substring(3));
            mapMethod.put(tmpName, mm);
        }

        return mapMethod;
    }

    public static List<Method> findClassReadMethods(Class anClass) {

        return findClassReadMethods(anClass, true);
    }

    public static List<Method> findClassReadMethods(Class anClass, boolean anReader) {
        PropertyUtilsBean utilsBean = new PropertyUtilsBean();
        List<Method> list = new ArrayList();
        Method mm;
        for (PropertyDescriptor pds : utilsBean.getPropertyDescriptors(anClass)) {
            if (anReader) {
                mm = utilsBean.getReadMethod(pds);
            }
            else {
                mm = utilsBean.getWriteMethod(pds);
            }
            if (mm != null) {
                list.add(mm);
            }
            else{
            //  System.out.println(pds);
            }
        }

        return list;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。 支持多级，如：对象名.对象名.方法
     */
    public static void invokeSetter(Object obj, String propertyName, Object value) {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++) {
            if (i < names.length - 1) {
                String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[] {}, BeanMapperHelper.objs);
            }
            else {
                String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] { value });
            }
        }
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        }
        catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        try {
            field.set(obj, value);
        }
        catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e.getMessage());
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用. 同时匹配方法名+参数类型，
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        }
        catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符， 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用. 只匹配函数名，如果有多个同名函数调用第一个。
     */
    public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        }
        catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * 
     * 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(fieldName, "fieldName can't be blank");
        return getAccessibleClassField(obj.getClass(), fieldName);
    }
    public static Field getClassField(final Class anClass, final String fieldName) {
        return getSubClassField(anClass, fieldName, false);
    }
    public static Field getAccessibleClassField(final Class anClass, final String fieldName) {
        return getSubClassField(anClass, fieldName, true);
    }
    private static Field getSubClassField(final Class anClass, final String fieldName, boolean anAccess) {
        for (Class<?> superClass = anClass; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                if (anAccess) {
                makeAccessible(field);
                }
                return field;
            }
            catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 匹配函数名+参数类型。
     * 
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            }
            catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 只匹配函数名。
     * 
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field
                .getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处 如无法找到, 返回Object.class. eg. public UserDao extends HibernateDao<User>
     *
     * @param clazz
     *            The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * 
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    public static Class<?> getUserClass(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;

    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        }
        else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        }
        else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    /**
     * 调用Getter方法.
     */
    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[] {}, BeanMapperHelper.objs);
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value) {
        invokeSetterMethod(obj, propertyName, value, null);
    }

    /**
     * 调用Setter方法.
     * 
     * @param propertyType
     *            用于查找Setter方法,为空时使用value的Class替代.
     */
    public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao extends HibernateDao<User>
     *
     * @param clazz
     *            The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * 
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz
     *            clazz The class to introspect
     * @param index
     *            the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }
    public static List<Method> findStaticMethodList(Class anClass ){
        List<Method> mmList = new ArrayList<Method>();
        for( Method  mm : anClass.getMethods()){
           if (mm.getDeclaringClass() != Object.class){
               int kk = mm.getModifiers();
               if (Modifier.isPublic(kk) && Modifier.isStatic(kk)){
                  mmList.add(mm); 
               }
           } 
        }
        return mmList;
    }
    public static List<Field> findAllField(Class anClass, boolean anAccess) {
        List<Field> fieldList = new ArrayList();
        anClass.getFields();
        for (Class<?> superClass = anClass; superClass != Object.class; superClass = superClass.getSuperclass()) {
            for (Field field : superClass.getDeclaredFields()) {
                if (anAccess) {
                    makeAccessible(field);
                }
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
