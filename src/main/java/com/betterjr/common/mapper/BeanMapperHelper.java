package com.betterjr.common.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterFieldNotFoundException;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.reflection.ReflectionUtils; 

public class BeanMapperHelper {

    private static Map<String, BeanMapperHelper> map = new HashMap();

    private Map<Method, Object> methodMap = new HashMap();
    public static final Object[] objs = new Object[] {};
    public static final Class[] nullClass = new Class[] {};
    //public static Map<Class, Map<String, Method>> getMethodMap = initMethdMap(true);
    public static Map<Class, Map<String, Method>> setMethodMap = initMethdMap(false);

    private static Map initMethdMap(boolean anReader) {
        // SaleRequestInfo.class, SaleAccoRequestInfo.class, SaleSimpleRequestObj.class 
        Class[] initMethodClasses = new Class[] {};
        Map<Class, Map<String, Method>> map = new HashMap();
        Map methodMap;
        for (Class cc : initMethodClasses) {
            methodMap = ReflectionUtils.findMethodMap(cc, anReader);

            map.put(cc, methodMap);
        }

        return map;
    }

    /**
     * 将申请的Map数据转换为需要的对象
     * 
     * @param anMap
     * @param anObj
     */
    public static void mapToClass(Map<String, SimpleDataEntity> anMap, Object anObj) {
        Map<String, Method> map = setMethodMap.get(anObj.getClass());
        if (map == null) {
            map = ReflectionUtils.findMethodMap(anObj.getClass(), false);
            synchronized (setMethodMap) {
                setMethodMap.put(anObj.getClass(), map);
            }
        }
        Method mm;
        Object obj;
        SimpleDataEntity sde;
        for (Map.Entry<String, SimpleDataEntity> ent : anMap.entrySet()) {
            mm = map.get(ent.getKey());
            if (mm != null) {
                sde = ent.getValue();
                obj = parseSimpleObject(sde.getValue(), sde.getName(), mm.getParameterTypes()[0]);
                try {
                    mm.invoke(anObj, obj);
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                }
            }
        }
    }
    
    public static void invoke(Object source, Object destObj, Map anFieldDefMap) {
        
    }
    
    public static void invoke(Object source, Object destObj, String anFieldMap) {
        if (source == null || destObj == null) {
            return;
        }
        String mapKey = source.getClass().getSimpleName() + "," + anFieldMap + "," + destObj.getClass().getSimpleName();
        BeanMapperHelper mapperHelp = map.get(mapKey);
        if (mapperHelp == null) {
            mapperHelp = new BeanMapperHelper();
            mapperHelp.init(source.getClass(), destObj.getClass(), anFieldMap);
            map.put(mapKey, mapperHelp);
        }
        mapperHelp.invokeMethod(source, destObj, false);
    }

    /**
     * 将字符串转换 为简单的对象，包括：日期、数字等
     * 
     * @param tmpStr
     * @param anField
     * @param anPatten
     * @param anClass
     * @return
     */
    public static Object parseSimpleObject(String tmpStr, String anPatten, Class anClass) {
        if (StringUtils.isNotBlank(tmpStr)) {
            if (Date.class.isAssignableFrom(anClass)) {
                try {
                    java.util.Date dd;
                    if (BetterStringUtils.isBlank(anPatten)) {
                        dd = BetterDateUtils.parseDate(tmpStr);
                    }
                    else {
                        dd = BetterDateUtils.parseDate(tmpStr, anPatten);
                    }
                    if (anClass.equals(Date.class)) {

                        return dd;
                    }
                    Constructor cc = anClass.getConstructor(long.class);
                    return cc.newInstance(dd.getTime());
                }
                catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | ParseException e) {
                    e.printStackTrace();

                    return null;
                }
            }
            else if (String.class.isAssignableFrom(anClass)) {

                return tmpStr;
            }
            String mName = "parse" + anClass.getSimpleName();
            Method method;
            try {
                if (anClass == Integer.class) {
                    mName = "parseInt";
                }
                else if (anClass == BigDecimal.class) {

                    return new BigDecimal(tmpStr);
                }
                method = anClass.getMethod(mName, String.class);
                return method.invoke(null, tmpStr);
            }
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static Object invokeGetterMethod(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        Method mm;
        try {
            mm = obj.getClass().getMethod(getterMethodName, nullClass);
            return mm.invoke(obj, objs);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new BytterFieldNotFoundException(60001, "fieldNot Find", e);
        }
        catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, Object> invokeMap(Object source, String anFieldMap) {
        Map<String, Object> result = new HashMap();
        if (source == null) {

            return result;
        }

        String mapKey = source.getClass().getSimpleName() + "," + anFieldMap;
        BeanMapperHelper mapperHelp = map.get(mapKey);
        if (mapperHelp == null) {
            mapperHelp = new BeanMapperHelper();
            mapperHelp.init(source.getClass(), Map.class, anFieldMap);
            map.put(mapKey, mapperHelp);
        }
        mapperHelp.invokeMethod(source, result, true);

        return result;
    }

    private void invokeMethod(Object anSource, Object anDest, boolean anMap) {
        Method destM;
        Map workMap = null;
        if (anMap) {
            workMap = (Map) anDest;
        }
        for (Map.Entry<Method, Object> ent : this.methodMap.entrySet()) {
            Object obj;
            try {
                obj = ent.getKey().invoke(anSource, objs);
                if (anMap) {
                    workMap.put(ent.getValue(), obj);
                }
                else {
                    destM = (Method) ent.getValue();
                    destM.invoke(anDest, obj);
                }
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
                BytterException.unchecked(e);
            }
        }
    }

    private void init(Class anSource, Class anDest, String anFieldMap) {
        String sName;
        String dName;
        Method sourceMethod;
        String tmpMethodName;
        for (String tmpStr : BetterStringUtils.split(anFieldMap, ";")) {
            try {
                if (Map.class.isAssignableFrom(anDest)) {
                    tmpMethodName = ReflectionUtils.GETTER_PREFIX + StringUtils.capitalize(BetterStringUtils.trim(tmpStr));
                    sourceMethod = anSource.getMethod(tmpMethodName, new Class[] {});
                    methodMap.put(sourceMethod, BetterStringUtils.trim(tmpStr));
                }
                else {
                    String[] subTmpArr = BetterStringUtils.split(tmpStr, ":");
                    sName = BetterStringUtils.trim(subTmpArr[0]);
                    if (subTmpArr.length > 1){
                        dName = BetterStringUtils.trim(subTmpArr[1]);
                    }
                    else{
                        dName = sName;
                    }
                    
                    if (subTmpArr.length == 2) { // && (sName.equals(dName) == false)
                        tmpMethodName = ReflectionUtils.GETTER_PREFIX + StringUtils.capitalize(sName);
                        sourceMethod = anSource.getMethod(tmpMethodName, new Class[] {});
                        tmpMethodName = ReflectionUtils.SETTER_PREFIX + StringUtils.capitalize(dName);
                        Method destMethod = anDest.getMethod(tmpMethodName, sourceMethod.getReturnType());
                        methodMap.put(sourceMethod, destMethod);
                    }
                }
            }
            catch (NoSuchMethodException | SecurityException e) {
                System.out.println(e);
                throw BytterException.unchecked(e);
            }
        }
    }
}
