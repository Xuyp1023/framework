package com.betterjr.common.utils;

import java.math.BigDecimal;

import com.betterjr.common.data.DataTypeInfo;

public class MathExtend {
    // 默认除法运算精度
    private static final int DEFAULT_DIV_SCALE = 10;
    public static BigDecimal value100 = new BigDecimal(100);

    // public static final MathContext NORMAL_MC = new MathContext(2, RoundingMode.HALF_EVEN);

    /**
     * 提供精确的加法运算。
     * 
     * @param v1
     * @param v2
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static BigDecimal add(BigDecimal anFirst, BigDecimal anSecond) {
        if (anFirst == null) {

            return anSecond;
        }
        if (anSecond == null) {

            return anFirst;
        }

        return anFirst.add(anSecond);
    }

    public static BigDecimal maxDouble(BigDecimal anFirst, BigDecimal anSecond) {
        if (anFirst == null || anFirst.compareTo(BigDecimal.ZERO) == 0) {

            return anSecond;
        }
        if (anSecond == null || anSecond.compareTo(BigDecimal.ZERO) == 0) {
            return anFirst;
        }
        return anFirst.max(anSecond);
    }

    public static boolean compareToZero(BigDecimal anBD) {

        return anBD != null && (anBD.compareTo(BigDecimal.ZERO) > 0);
    }

    public static BigDecimal minDouble(BigDecimal anFirst, BigDecimal anSecond) {
        if (anFirst == null || anFirst.compareTo(BigDecimal.ZERO) == 0) {
            return anSecond;
        }
        if (anSecond == null || anFirst.compareTo(BigDecimal.ZERO) == 0) {
            return anFirst;
        }
        return anFirst.min(anSecond);
    }

    /**
     * 提供精确的加法运算
     * 
     * @param v1
     * @param v2
     * @return 两个参数数学加和，以字符串格式返回
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 提供精确的减法运算。
     * 
     * @param v1
     * @param v2
     * @return 两个参数的差
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static Double getDoubeValue(BigDecimal anBD) {
        if (anBD == null) {
            return null;
        }
        else {
            return new Double(anBD.doubleValue());
        }
    }

    /**
     * 提供精确的减法运算
     * 
     * @param v1
     * @param v2
     * @return 两个参数数学差，以字符串格式返回
     */
    public static String subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }

    /**
     * 提供精确的乘法运算。
     * 
     * @param v1
     * @param v2
     * @return 两个参数的积
     */
    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     * 
     * @param v1
     * @param v2
     * @return 两个参数的数学积，以字符串格式返回
     */
    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
     * 
     * @param v1
     * @param v2
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2) {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
     * 
     * @param v1
     * @param v2
     * @param scale
     *            表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale) {
        return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     * 
     * @param v1
     * @param v2
     * @param scale
     *            表示需要精确到小数点以后几位
     * @param round_mode
     *            表示用户指定的舍入模式
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, round_mode).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
     * 
     * @param v1
     * @param v2
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2) {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
     * 
     * @param v1
     * @param v2
     * @param scale
     *            表示需要精确到小数点以后几位
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2, int scale) {
        return divide(v1, v2, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     * 
     * @param v1
     * @param v2
     * @param scale
     *            表示需要精确到小数点以后几位
     * @param round_mode
     *            表示用户指定的舍入模式
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, round_mode).toString();
    }

    /**
     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        return round(v, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 提供精确的小数位四舍五入处理
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @param round_mode
     *            指定的舍入模式
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        return b.setScale(scale, round_mode).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果，以字符串格式返回
     */
    public static String round(String v, int scale) {
        return round(v, scale, BigDecimal.ROUND_HALF_EVEN);
    }

    public static BigDecimal round(BigDecimal v) {
        return round(v, 2);
    }

    public static BigDecimal round(BigDecimal v, int scale) {
        if (v != null) {
            return v.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
        }
        else {
            return v;
        }
    }

    /**
     * 提供精确的小数位四舍五入处理
     * 
     * @param v
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @param round_mode
     *            指定的舍入模式
     * @return 四舍五入后的结果，以字符串格式返回
     */
    public static String round(String v, int scale, int round_mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, round_mode).toString();
    }

    /**
     * 比较数据是否在区间
     * 
     * @param 最小的数据
     * @param 需要比较的数据
     * @param 最大的数据
     * @return
     */
    public static boolean compareRange(Double anFirst, Double anMid, Double anEnd) {
        if (anMid == null) {
            return false;
        }
        if (anFirst == null && anEnd == null) {
            return true;
        }
        if (anFirst == null) {
            return anMid.compareTo(anEnd) <= 0;
        }

        if (anEnd == null) {
            return anFirst.compareTo(anMid) <= 0;
        }
        if (anFirst.compareTo(anEnd) > 0) {
            Double dd = anFirst;
            anFirst = anEnd;
            anEnd = dd;
        }

        return anFirst.compareTo(anMid) <= 0 && anMid.compareTo(anEnd) <= 0;
    }

    public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {

        return divide(v1, v2, 2);
    }

    public static BigDecimal divide(BigDecimal v1, BigDecimal v2, int anScale) {
        if (v1 == null || v2 == null || v2.equals(BigDecimal.ZERO)) {

            return BigDecimal.ZERO;
        }

        return v1.divide(v2, anScale, BigDecimal.ROUND_HALF_EVEN).setScale(anScale);
    }

    public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
        if (v1 == null) {
            v1 = BigDecimal.ZERO;
        }
        if (v2 == null) {
            v2 = BigDecimal.ZERO;
        }

        return v1.subtract(v2);
    }

    public static String convertToPercent(String anValue, int anScale) {
        if (BetterStringUtils.isBlank(anValue)) {
            return "";
        }

        BigDecimal bb = new BigDecimal(anValue);

        return bb.multiply(value100).setScale(anScale).toPlainString();
    }

    public static String convertIncomeRatio(String anValue) {

        return convertToPercent(anValue, 4);
    }

    public static int compareTo(Object anObj, Object anOther, int anScope) {
        BigDecimal bd1 = new BigDecimal(DataTypeInfo.formatData(anObj, anScope));
        BigDecimal bd2 = new BigDecimal(DataTypeInfo.formatData(anOther, anScope));

        return bd1.compareTo(bd2);
    }

    public static BigDecimal defaultOne(BigDecimal anValue) {

        return defaultValue(anValue, BigDecimal.ONE);
    }

    public static BigDecimal defaultZero(BigDecimal anValue) {

        return defaultValue(anValue, BigDecimal.ZERO);
    }

    public static BigDecimal defaultValue(BigDecimal anValue, BigDecimal anDef) {
        if (anValue == null) {
            return anDef;
        }
        else {
            return anValue;
        }
    }

    public static BigDecimal multiply(BigDecimal v1, BigDecimal v2) {
        if (v1 != null && v2 != null) {
            return v1.multiply(v2);
        }
        return BigDecimal.ZERO;
    }
}