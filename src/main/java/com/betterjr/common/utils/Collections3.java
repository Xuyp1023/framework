/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.betterjr.common.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.reflection.ReflectionUtils;

import java.util.*;

/**
 * Collections工具集. 在JDK的Collections和Guava的Collections2后, 命名为Collections3.
 * 
 * @author henryzhou
 * @version 2015-01-15
 */
@SuppressWarnings("rawtypes")
public class Collections3 {

    /**
     * 提取集合中的对象的两个属性(通过Getter函数), 组合成Map.
     * 
     * @param collection
     *            来源集合.
     * @param keyPropertyName
     *            要提取为Map中的Key值的属性名.
     * @param valuePropertyName
     *            要提取为Map中的Value值的属性名.
     */
    @SuppressWarnings("unchecked")
    public static Map extractToMap(final Collection collection, final String keyPropertyName, final String valuePropertyName) {
        Map map = new HashMap(collection.size());

        try {
            for (Object obj : collection) {
                map.put(PropertyUtils.getProperty(obj, keyPropertyName), PropertyUtils.getProperty(obj, valuePropertyName));
            }
        }
        catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }

        return map;
    }
    
    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成Map<属性，原对象>.
     * 
     * @param collection
     *            来源集合.
     * @param keyPropertyName
     *            要提取为Map中的Key值的属性名.
     */
    @SuppressWarnings("unchecked")
    public static Map extractToMap(final Collection collection, final String keyPropertyName) {
        Map map = new HashMap(collection.size());

        try {
            for (Object obj : collection) {
                map.put(PropertyUtils.getProperty(obj, keyPropertyName), obj);
            }
        }
        catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }

        return map;
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
     * 
     * @param collection
     *            来源集合.
     * @param propertyName
     *            要提取的属性名.
     */
    @SuppressWarnings("unchecked")
    public static List extractToList(final Collection collection, final String propertyName) {
        List list = new ArrayList(collection.size());

        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        }
        catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    /**
     * , 组合转成List
     * 
     * @param collection
     *            来源集合.
     */
    public static List extractToList(final Collection collection) {
        List list = new ArrayList(collection.size());

        try {
            for (Object obj : collection) {
                list.add(obj);
            }
        }
        catch (Exception e) {
            throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
        }

        return list;
    }

    public static String buildSignSourceString(Map<String, Object> map) {

        List<String> keyList = Collections3.extractToList(map.keySet());

        Collections.sort(keyList);

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < keyList.size(); index++) {
            String key = keyList.get(index);

            if (index > 0) {
                builder.append("&");
            }
            builder.append(key);
            builder.append("=");
            builder.append(map.get(key).toString());
        }

        return builder.toString();
    }

    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
     * 
     * @param collection
     *            来源集合.
     * @param propertyName
     *            要提取的属性名.
     * @param separator
     *            分隔符.
     */
    public static String extractToString(final Collection collection, final String propertyName, final String separator) {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 中间以 separator分隔。
     */
    public static String convertToString(final Collection collection, final String separator) {
        return StringUtils.join(collection, separator);
    }

    /**
     * 转换Collection所有元素(通过toString())为String, 每个元素的前面加入prefix，后面加入postfix，如 <div>mymessage</div>。
     */
    public static String convertToString(final Collection collection, final String prefix, final String postfix) {
        StringBuilder builder = new StringBuilder();
        for (Object o : collection) {
            builder.append(prefix).append(o).append(postfix);
        }
        return builder.toString();
    }

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Map collection) {

        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(String[] anArrStr) {

        if (anArrStr == null || anArrStr.length == 0) {
            return true;
        }
        for(String tmpStr : anArrStr){
           if (BetterStringUtils.isNotBlank(tmpStr)){
               return false;
           }
        }
        
        return true;
    }

    /**
     * 判断是否为空.
     */
    public static boolean isEmpty(Object[] collection) {

        return (collection == null || collection.length == 0);
    }
    
    /**
     * 取得Collection的第一个元素，如果collection为空返回null.
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        return collection.iterator().next();
    }

    public static <T> T getOnlyOne(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        if (collection.size() > 1) {
            return null;
        }
        return collection.iterator().next();
    }

    /**
     * 获取Collection的最后一个元素 ，如果collection为空返回null.
     */
    public static <T> T getLast(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        // 当类型为List时，直接取得最后一个元素 。
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            return list.get(list.size() - 1);
        }

        // 其他类型通过iterator滚动到最后一个元素.
        Iterator<T> iterator = collection.iterator();
        while (true) {
            T current = iterator.next();
            if (!iterator.hasNext()) {
                return current;
            }
        }
    }

    /*
     * public static String[] mergeArray(String[] anFirst, String[] anSecond) { Set<String> ss = new LinkedHashSet();
     * ss.addAll(Arrays.asList(anFirst)); ss.addAll(Arrays.asList(anSecond));
     * 
     * return ss.toArray(new String[ss.size()]); }
     */
    public static <T> T[] mergeArray(T[] anFirst, T[] anSecond) {
        Set ss = new LinkedHashSet();
        ss.addAll(Arrays.asList(anFirst));
        ss.addAll(Arrays.asList(anSecond));

        return (T[]) ss.toArray(anFirst);
    }

    /**
     * 返回a+b的新List.
     */
    public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
        List<T> result = new ArrayList<T>(a);
        result.addAll(b);
        return result;
    }

    /**
     * 返回a-b的新List.
     */
    public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
        List<T> list = new ArrayList<T>(a);
        for (T element : b) {
            list.remove(element);
        }

        return list;
    }

    /**
     * 返回a与b的交集的新List.
     */
    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList<T>();

        for (T element : a) {
            if (b.contains(element)) {
                list.add(element);
            }
        }
        return list;
    }

    /**
     * 过滤查询条件入参信息
     * 
     * @param anMap
     *            前端提供的查询条件
     * @param anTerms
     *            系统可以处理的查询条件入参
     * @return
     */
    public static Map filterMap(Map<String, Object> anMap, String[] anTerms) {
        Map<String, Object> termMap = new HashMap();
        if (isEmpty(anTerms) == false) {
            for (String tmpKey : anTerms) {
                Object obj = anMap.get(tmpKey);
                if (obj != null) {
                    if (obj instanceof String) {
                        if (BetterStringUtils.isBlank((String) obj)) {
                            continue;
                        }
                    }
                    termMap.put(tmpKey, obj);
                }
            }
        }

        return termMap;
    }

    /**
     * 增加模糊参数的处理
     * 
     * @param anMap
     *            已经过滤过的参数信息
     * @param anArrFuzzy
     *            需要模糊处理的参数
     * @return
     */
    public static Map fuzzyMap(Map<String, Object> anMap, String[] anArrFuzzy) {
        if (isEmpty(anArrFuzzy)) {
            return anMap;
        }
        for (String tmpKey : anArrFuzzy) {
            if (anMap.containsKey(tmpKey)) {
                Object obj = anMap.remove(tmpKey);
                anMap.put("LIKE".concat(tmpKey), "%" + (String) obj + "%");
            }
        }

        return anMap;
    }

    public static List arrayToList(Object anObj) {
        List list = new ArrayList();
        if (anObj != null) {
            if (anObj.getClass().isArray()) {
                int k = Array.getLength(anObj);
                for (int i = 0; i < k; i++) {
                    list.add(Array.get(anObj, i));
                }
            }
            else {
                list.add(anObj);
            }
        }
        return list;
    }

    /**
     * 判断一个对象是否是一个有效的集合，指集合中至少有一个对象
     * 
     * @param anObj
     * @return true有效的集合，false无效的集合
     */
    public static boolean isList(Object anObj) {
        if (anObj instanceof Collection) {
            return isEmpty((Collection) anObj) == false;
        }

        return false;
    }

    public static Collection removeNullValue(Collection anList) {
        if (isEmpty(anList)) {

            return anList;
        }
        anList.removeAll(Collections.singleton(null));
        return anList;

    }

    /**
     * 判断对象是否为空，如果为空直接返回真值，如果是Map，则使用Map的判断条件，<BR>
     * 如果是集合则使用集合的判断条件，如果是字符则使用字符的判断条件；<BR>
     * 如果是数组，则取出数组中的值来判断；如果数组中存在一个非空值，则该对象非空<BR>
     * 否则返回该对象非空
     * 
     * @param anObj
     * @return
     */
    public static boolean isEmptyObject(Object anObj) {
        if (anObj == null) {
            return true;
        }
        if (anObj instanceof Map) {
            return isEmpty((Map) anObj);
        }
        else if (anObj instanceof Collection) {
            return isEmpty((Collection) anObj);
        }
        else if (anObj instanceof String) {

            return BetterStringUtils.isBlank((String) anObj);
        }
        else {
            if (anObj.getClass().isArray()) {
                int k = Array.getLength(anObj);
                for (int i = 0; i < k; i++) {
                    if (isEmptyObject(Array.get(anObj, i)) == false) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 判读集合中是否存在空对象
     * @param anObj
     * @return
     */
    public static boolean hasEmptyObject(Object ... anObjs){
       for(Object obj : anObjs){
          if (isEmptyObject(obj)){
              
              return true;
          }
       }
       
       return false;
    }
}
