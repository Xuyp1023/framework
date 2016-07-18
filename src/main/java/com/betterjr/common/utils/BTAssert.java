package com.betterjr.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.betterjr.common.exception.BytterException;
import com.betterjr.common.exception.BytterValidException;

/**
 * 断言判断的类，处理系统必须要的约定值，以后会不断扩充。
 * @author zhoucy
 *
 */
public class BTAssert extends Assert {
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
    
    public static void main(String[] args){
       isNotShorString("aaaa",5);
       //isNotSmallValue(91L);
    }
}