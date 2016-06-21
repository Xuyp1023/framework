package com.betterjr.common.data;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;

 

import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;

import java.util.*;

/**
 * C 字符型(String) A 数字字符型(Integer)，T时间日期型，D日期型， 限于0—9 N 数值型(Double)，其长度不包含小数点，B逻辑型(Boolean), BD(BigDecimal), LONG长整型(Long) 可参与数值计算 TEXT 不定长文本 BINARY 二进制数据
 * 
 * @author zhoucy
 *
 */
public enum DataTypeInfo {
    C, N, A, D, BD, T, B, O, LONG, TEXT, BINARY;
    private static final DecimalFormat dfArr[] = new DecimalFormat[] { new DecimalFormat("##0"), new DecimalFormat("##0.0"),
            new DecimalFormat("##0.00"), new DecimalFormat("##0.000"), new DecimalFormat("##0.0000"), new DecimalFormat("##0.00000") };

    private static Map<DataTypeInfo, Class> workClassMap = new HashMap();

    static {
        workClassMap.put(C, String.class);
        workClassMap.put(N, Double.class);
        workClassMap.put(A, Integer.class);
        workClassMap.put(D, Date.class);
        workClassMap.put(T, Timestamp.class);
        workClassMap.put(B, Boolean.class);
        workClassMap.put(BD, BigDecimal.class);
        workClassMap.put(LONG, Long.class);
        workClassMap.put(TEXT, String.class);
        workClassMap.put(BINARY, byte[].class);
        workClassMap.put(O, Void.class);
    }

    public static boolean simpleObject(Object anObj){
       if (anObj != null){
           DataTypeInfo dt = findDataType(anObj.getClass());
           return dt != null;
       }
       
        return true;
    }
    
    public static DataTypeInfo findDataType(Class anClass) {
        for (Map.Entry<DataTypeInfo, Class> ent : workClassMap.entrySet()) {
            if (ent.getValue().equals(anClass)) {
                return ent.getKey();
            }
        }
        return null;
    }
    public static Class getClass(DataTypeInfo cc) {

        return workClassMap.get(cc);
    }

    public static Class getClass(String anWorkType) {
        DataTypeInfo cc = checking(anWorkType);

        return workClassMap.get(cc);
    }

    public static DataTypeInfo checking(String anWorkType) {
        try {
            if (BetterStringUtils.isNotBlank(anWorkType)) {
                return DataTypeInfo.valueOf(anWorkType.trim().toUpperCase());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 检查远程调用的数据的有效性，无效抛出异常
     * 
     * @param anObj
     * @return
     */
    public boolean validData(Object anObj) {

        return true;
    }

    public static synchronized String formatData(Object anObj, int anScale) {
        if (anObj == null) {
            return "";
        }
        if (anObj instanceof Number) {
            if (anScale >= 0 && anScale < 6) {
                DecimalFormat df = dfArr[anScale];

                return df.format(anObj);
            }
            else {
                return anObj.toString();
            }
        }
        else if (anObj instanceof java.util.Date) {

            return BetterDateUtils.formatNumberDate((java.util.Date) anObj);
        }

        return anObj.toString();
    }
    public static void main(String[] args) {
        Object[] objs = new Object[] { "aaaa", new Long(1), new Integer(12), new BigDecimal(123), new Double(1234), new Date(),
                new Timestamp(System.currentTimeMillis()), new byte[]{12,12,1}, new ArrayList()};
        for (Object obj : objs){
           System.out.println(obj +" = "+simpleObject(obj)); 
        }
    }
}
