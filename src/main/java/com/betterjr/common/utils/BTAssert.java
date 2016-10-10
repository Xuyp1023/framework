package com.betterjr.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.exception.BytterValidException;

/**
 * 断言判断的类，处理系统必须要的约定值，以后会不断扩充。
 * @author zhoucy
 *
 */
public class BTAssert   {
    private static final Logger logger = LoggerFactory.getLogger(BTAssert.class);
    private static int ASSERT_ERROR_CODE = 90001;

    /**
     * 判断数字是否太小，包括空值
     * 
     * @param anValue
     *            判断的值
     */
    public static void isNotSmallValue(Number anValue) {

        isNotSmallValue(anValue, null, null, null);
    }
    
    public static void isTrue(boolean expression) {
        
        isTrue(expression, "业务异常 - 业务处理必须正常");
    }
    
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            
            createBytterExcepObject(BytterTradeException.class, message);
        }
    }
    /**
     * 判断数字是否太小，包括空值;如果太小，则抛出异常
     * 
     * @param anValue
     *            判断的值
     * @param anMessage
     *            出现异常提示的消息
     */
    public static void isNotSmallValue(Number anValue, String anMessage) {

        isNotSmallValue(anValue, anMessage, null);
    }

    /**
     * 判断数字是否太小，包括空值;如果太小，则抛出异常
     * 
     * @param anValue
     *            判断的值
     * @param anMessage
     *            出现异常提示的消息
     * @param anEx
     *            异常类信息
     */
    public static void isNotSmallValue(Number anValue, Class anEx) {
        isNotSmallValue(anValue, null, anEx);
    }

    public static void isNotSmallValue(Number anValue, String anMessage, Class anEx) {

        isNotSmallValue(anValue, null, anMessage, anEx);
    }

    /**
     * 判断数字是否太小，包括空值;如果太小，则抛出异常
     * 
     * @param anValue
     *            判断的值
     * @param anCompValue
     *            比較值，可以為空
     * @param anMessage
     *            出现异常提示的消息
     * @param anEx
     *            异常类信息
     */
    public static void isNotSmallValue(Number anValue, Integer anCompValue, String anMessage) {
        isNotSmallValue(anValue, anCompValue, anMessage, null);
    }

    public static void isNotSmallValue(Number anValue, Integer anCompValue, String anMessage, Class anEx) {
        boolean inValid = false;
        if (anEx == null) {
            anEx = BytterValidException.class;
        }

        if (anCompValue == null) {
            inValid = MathExtend.smallValue(anValue);
        }
        else {
            inValid = MathExtend.smallValue(anValue, anCompValue);
        }

        if (inValid) {
            if (BetterStringUtils.isBlank(anMessage)) {
                if (anValue == null) {
                    anMessage = "数值不能为空！";
                }
                else {
                    anMessage = "数值太小，不满足约定最小值！";
                }
            }
            createBytterExcepObject(anEx, anMessage);
        }
    }

    private static void createBytterExcepObject(Class anEx, String anMessage) {
        if (BytterException.class.isAssignableFrom(anEx)) {
            try {
                Constructor<BytterException> conStructor = anEx.getConstructor(int.class, String.class);
                BytterException bx = conStructor.newInstance(ASSERT_ERROR_CODE, anMessage);
                throw bx;
            }
            catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                logger.error("create BytterException has error!", e);
            }
        }
    }

    /**
     * 判断字符是否太短，如果太短则抛出异常，包括空字符
     * @param anValue 需要判断的字符
     */
    public static void isNotShorString(String anValue) {
        isNotShorString(anValue, null, null);
    }

    /**
     * 判断字符是否太短，如果太短则抛出异常，包括空字符
     * @param anValue 需要判断的字符
     * @param anMessage 如果字符太短，提示的消息信息
     */
    public static void isNotShorString(String anValue, String anMessage) {

        isNotShorString(anValue, null, anMessage);
    }

    /**
     * 判断字符是否太短，如果太短则抛出异常，包括空字符
     * @param anValue 需要判断的字符
     * @param anLength 比较的字符长度，只有比该长度小的值，才抛出消息
     * @param anMessage 提示的消息
     */
    public static void isNotShorString(String anValue, Integer anLength) {
        isNotShorString(anValue, anLength, null);
    }
    
    public static void isNotShorString(String anValue, Integer anLength, String anMessage) {

        isNotShorString(anValue, anLength, anMessage, null);
    }

    /**
     * 判断字符是否太短，如果太短则抛出异常，包括空字符
     * @param anValue 需要判断的字符
     * @param anLength 比较的字符长度，只有比该长度小的值，才抛出消息
     * @param anMessage 提示的消息
     * @param anEx 抛出的异常类信息，如果不指定异常类，则统一为验证异常类
     */
    public static void isNotShorString(String anValue, Integer anLength, String anMessage, Class anEx) {
        boolean inValid = false;
        if (anEx == null) {
            anEx = BytterValidException.class;
        }
        
        if (anLength == null) {
            inValid = BetterStringUtils.defShortString(anValue);
        }
        else {
            inValid = BetterStringUtils.shortString(anValue, anLength);
        }

        if (inValid) {
            if (BetterStringUtils.isBlank(anMessage)) {
                if (anValue == null) {
                    anMessage = "字符参数不能为空！";
                }
                else {
                    anMessage = "字符参数不能太短，不满足约定的字符长度！";
                }
            }
            createBytterExcepObject(anEx, anMessage);
        }
    }
    
    public static void isNull(Object object) {
        
        isNull(object, "业务参数必须为空");
    }
    
    public static void isNull(Object object, String message) {
        if (object != null) {
            
            createBytterExcepObject(BytterTradeException.class, message); 
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            
            createBytterExcepObject(BytterTradeException.class, message); 
        }
    }
    
    public static void notNull(Object object) {
        
        notNull(object, "业务参数不能为空");
    }
    
    public static void hasLength(String text, String message) {
        if (StringUtils.hasLength(text) == false ) {
            
            createBytterExcepObject(BytterTradeException.class, message); 
        }
    }
    
    public static void hasLength(String text) {
        
        hasLength(text, "字符长度不够，不能为空或者空格");
    }
    
    public static void hasText(String text, String message) {
        if (!StringUtils.hasText(text)) {
            
            createBytterExcepObject(BytterTradeException.class, message); 
        }
    }
    
    public static void hasText(String text) {
        
        hasText(text, "字符必須要有內容，不能为空或者空格");
    }

    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
                textToSearch.contains(substring)) {
            createBytterExcepObject(BytterTradeException.class, message);
        }
    }
    
    public static void doesNotContain(String textToSearch, String substring) {
        
        doesNotContain(textToSearch, substring, "字符串 ["+textToSearch+"] 不能包含 字符串 [" + substring + "]");
    }
    
    public static void notEmpty(Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            createBytterExcepObject(BytterTradeException.class, message);
        }
    }
    
    public static void notEmpty(Object[] array) {
        
        notEmpty(array, "数据数组不能为空: 必须至少包含一条数据");
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    createBytterExcepObject(BytterTradeException.class, message);
                }
            }
        }
    }

    public static void noNullElements(Object[] array) {
        
        noNullElements(array, "数组不能包括任何空的数据，不能有一条空的数据");
    }
    
    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            
            createBytterExcepObject(BytterTradeException.class, message);
        }
    }

    public static void notEmpty(Collection<?> collection) {
        
        notEmpty(collection, "数据列表不能为空，至少包括一条数据");
    }
    
    public static void notEmpty(Map<?, ?> map, String message) {
        if (CollectionUtils.isEmpty(map)) {
            createBytterExcepObject(BytterTradeException.class, message);
        }
    }
    
    public static void notEmpty(Map<?, ?> map) {
        
        notEmpty(map, "数据映射集合不能为空，至少包括条数据");
    }
    
    public static void notLessThan(int value,int target, String message) {
        if (value>=target) {
            
            createBytterExcepObject(BytterTradeException.class, message);
        }
    }
    
    public static void isInstanceOf(Class<?> clazz, Object obj) {
        
        isInstanceOf(clazz, obj, "");
    }    
    
    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "检查JAVA对象类型为空");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(
                    (StringUtils.hasLength(message) ? message + " " : "") +
                    "对象的类 [" + (obj != null ? obj.getClass().getName() : "空") +
                    "] 必须是 【" + type+"】的一个实例");
        }
    }
    
    public static void isAssignable(Class<?> superType, Class<?> subType) {
        isAssignable(superType, subType, "");
    }
    
    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        notNull(superType, "检查JAVA对象类型为空");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException((StringUtils.hasLength(message) ? message + " " : "") +
                    subType + " 不是继承类  " + superType);
        }
    }
    /***
     * 判断字符是否为数值类型
     * @param number
     * @param message
     */
    public static void isNumber(String number,String message){
        if (!number.matches("\\d+(.\\d+)?[fF]?")) {
            createBytterExcepObject(BytterTradeException.class,message);
        }
    }
    
    public static void main(String[] args){
//       isNotShorString("aaaa",5);
        isNumber("fdfd","不是数值");
       //isNotSmallValue(91L);
    }
}