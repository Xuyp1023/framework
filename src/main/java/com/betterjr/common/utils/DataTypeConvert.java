package com.betterjr.common.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.exception.DataTypeConvertException;

public class DataTypeConvert implements Serializable {

    private static final long serialVersionUID = -1937753360101249835L;

    protected static final Logger log = LoggerFactory.getLogger(DataTypeConvert.class);

    protected static final Map primitiveDefaults = createPrimitiveDefaults();

    private static final SimpleDateFormat[] dateFormater = createDateFormater();

    private static final DataTypeConvert converter = new DataTypeConvert();

    private static final Map<String, Class> defineClass = registerTypeClass();

    private DataTypeConvert() {}

    public static Map createPrimitiveDefaults() {
        Map map = new HashMap();
        map.put(Integer.TYPE, new Integer(0));
        map.put(Short.TYPE, new Short((short) 0));
        map.put(Byte.TYPE, new Byte((byte) 0));
        map.put(Float.TYPE, new Float(0));
        map.put(Double.TYPE, new Double(0));
        map.put(Long.TYPE, new Long(0));
        map.put(Boolean.TYPE, Boolean.FALSE);
        map.put(Character.TYPE, new Character('\u0000'));
        return map;
    }

    private static Map registerTypeClass() {
        Map<String, Class> map = new HashMapIgnoreCase<String, Class>();
        Class[] arrClass = new Class[] { Boolean.TYPE, Boolean.class, Byte.TYPE, Byte.class, Short.TYPE, Short.class,
                Integer.TYPE, Integer.class, Long.TYPE, Long.class, Float.TYPE, Float.class, BigDecimal.class,
                BigInteger.class, Double.TYPE, Double.class, Character.TYPE, Character.class, String.class,
                java.util.Date.class, java.sql.Date.class, java.sql.Timestamp.class, java.sql.Time.class,
                java.util.Calendar.class };
        final String lang = "java.lang.";
        final String math = "java.math.";
        final Class dateClass = java.util.Date.class;
        String tmpStr = null;
        String preFix = null;
        for (Class cc : arrClass) {
            tmpStr = cc.getName();
            if (tmpStr.startsWith(lang)) {
                preFix = "G";
            } else if (tmpStr.startsWith(math)) {
                preFix = "M";
            } else if (dateClass.isAssignableFrom(cc)) {
                if (dateClass == cc) {
                    preFix = "T";
                } else {
                    preFix = "S";
                }
            } else if (cc == java.util.Calendar.class) {
                preFix = "T";
            } else {
                preFix = "";
            }
            tmpStr = cc.getSimpleName();
            map.put(preFix.concat(tmpStr), cc);
            String keyValue = null;
            for (int i = 1; i < 4; i++) {
                if (preFix.equals("M")) {
                    keyValue = preFix.concat(tmpStr.substring(3, i + 3));
                } else {
                    keyValue = preFix.concat(tmpStr.substring(0, i));
                }
                map.put(keyValue, cc);
            }
        }
        map.put("V", String.class);
        map.put("T", java.util.Date.class);
        map.put("Str", String.class);
        map.put("String", String.class);
        map.put("Ttt", java.sql.Timestamp.class);
        map.put("TT", java.sql.Timestamp.class);
        map.put("3T", java.sql.Timestamp.class);
        map.put("N", Double.TYPE);
        map.put("GN", Double.TYPE);
        map.put("bigD", java.math.BigDecimal.class);
        map.put("bigI", java.math.BigInteger.class);

        return map;
    }

    public static SimpleDateFormat[] createDateFormater() {
        String[] arrStr = new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM-dd",
                "HH:mm:ss", "HH:mm" };
        List list = new ArrayList();
        String[] arrSplit = new String[] { "-", "/", ".", "" };
        for (int i = 0; i < arrSplit.length; i++) {
            for (int j = 0; j < arrStr.length; j++) {
                list.add(new SimpleDateFormat(arrStr[j].replaceAll("-", arrSplit[i])));
            }
        }
        SimpleDateFormat[] dateF = new SimpleDateFormat[list.size()];
        return (SimpleDateFormat[]) list.toArray(dateF);
    }

    /**
     * 判断一个类是否是另外一个类的子类
     * 
     * @param anClass
     *            Class
     * @param anParent
     *            Class
     * @return boolean
     */
    public static boolean isMemberClass(Class anClass, Class anParent) {
        if (anClass == null) {
            return false;
        }
        if (anClass == anParent) {
            return true;
        }
        if (anClass.getSuperclass() == anParent) {
            return true;
        }
        if (anClass == Object.class) {
            return false;
        }
        return isMemberClass(anClass.getSuperclass(), anParent);
    }

    /**
     * convert
     * 
     * @param value
     *            Object //需要转换的对象；在转换过程中，可能涉及到数据精度的丢失
     * @param target
     *            Class //转换的目标数据类型
     * @return Object //转换后的对象
     */
    public static Object convert(final Object value, final Class target) {
        return converter.convertT(value, target, null);
    }

    public static Object convert(final Object value, String target) {
        return convert(value, target, null);
    }

    public static Class findDefineClass(String anStr) {
        return converter.findDefineClassT(anStr);
    }

    public Class findDefineClassT(String anStr) {
        String classN = null;
        Class cc = null;
        if (anStr.indexOf("/") > 0) {
            classN = anStr.replace('/', '.');
        } else if (anStr.indexOf("\\") > 0) {
            classN = anStr.replace('\\', '.');
        } else {
            classN = anStr;
        }

        if (classN.indexOf(".") < 0) {
            cc = defineClass.get(classN);
        } else {
            try {
                cc = Class.forName(classN);
            }
            catch (ClassNotFoundException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        if (cc == null) {
            throw new DataTypeConvertException("指定的数据类型不存在；类型字符是：".concat(classN));
        } else {
            return cc;
        }
    }

    public static Object convert(final Object value, String target, final String anPatten) {
        Class cc = converter.findDefineClassT(target);
        return converter.convertT(value, cc, anPatten);
    }

    public static Object convert(final Object value, final Class target, final String anPatten) {
        return converter.convertT(value, target, anPatten);
    }

    public Object convertT(Object value, Class target, String anPatten) {
        if ((target.isInstance(value)) || (value == null)) {
            if ((value == null) && target.isPrimitive()) {
                return primitiveDefaults.get(target);
            } else {
                return value;
            }
        } else if (target.isPrimitive()) { // 目标对象是一个简单的数据类型；需要封装成相应的数据对象类型
            target = findCasingClassT(target);
        }

        // log.info("转换的数据:" + value + ", 转换的数据类型是" + value.getClass() +",
        // 目标数据类型:" + target);
        if (Number.class.isAssignableFrom(target)) {
            if (Number.class.isInstance(value) || (value instanceof Character)) {
                Number nn;
                if (value instanceof Character) {
                    nn = new Integer(((Character) value).charValue());
                } else {
                    nn = (Number) value;
                }
                return toNumberValue(target, nn);
            }
            if (value instanceof String) {
                String tmpStr = (String) value;
                if ((anPatten == null) || StringUtils.isBlank(anPatten)) {
                    if (tmpStr.indexOf(".") > -1) {
                        return toNumberValue(target, new Double(tmpStr));
                    } else {
                        return toNumberValue(target, new Long(parseLongValue(tmpStr)));
                    }
                } else {
                    DecimalFormat df = new DecimalFormat(anPatten);
                    try {
                        return toNumberValue(target, df.parse(tmpStr));
                    }
                    catch (ParseException e) {
                        throw new DataTypeConvertException(
                                "错误的数字格式，和指定的模式不匹配；字符是：".concat(tmpStr).concat(", 模式是：").concat(anPatten));
                    }
                }
            } else if (value instanceof java.util.Date) {
                Long nn = new Long(((java.util.Date) value).getTime());
                return toNumberValue(target, nn);
            } else if (value instanceof java.util.Calendar) {
                return toNumberValue(target, new Long(((java.util.Calendar) value).getTimeInMillis()));
            } else {
                return value;
            }
        } else if (target == Boolean.class || target == Boolean.TYPE) {
            if (Number.class.isInstance(value)) {
                return ((Number) value).intValue() == 1 ? Boolean.TRUE : Boolean.FALSE;
            } else if (value instanceof String) {
                String tmpStr = ((String) value).toLowerCase();
                if (tmpStr.equals("true") || tmpStr.equals("1") || tmpStr.equals("t")) {
                    tmpStr = "true";
                } else {
                    tmpStr = "false";
                }

                return Boolean.valueOf(tmpStr);
            } else {
                return Boolean.TRUE;
            }
        } else if (target == String.class) {
            if ((anPatten == null) || StringUtils.isBlank(anPatten)) {
                return value.toString();
            } else if (value instanceof java.util.Date) {
                return DateFormatUtils.format((java.util.Date) value, anPatten);
            } else if (value instanceof Number) {
                Number new_name = (Number) value;
                DecimalFormat nf = new DecimalFormat(anPatten);
                return nf.format(new_name);
            } else if (value instanceof Calendar) {
                return DateFormatUtils.format(((Calendar) value).getTime(), anPatten);
            } else {
                return value.toString();
            }
        } else if (java.util.Date.class.isAssignableFrom(target) || (target == Calendar.class)) {
            long ll = 0;
            if (value instanceof java.util.Date) {
                ll = ((java.util.Date) value).getTime();
            } else if (value instanceof String) {

                ll = convertStrToDate((String) value, anPatten);

            } else if (Number.class.isInstance(value)) {
                ll = ((Number) value).longValue();
            }

            if (target == Calendar.class) {
                Calendar cc = Calendar.getInstance();
                cc.setTime(new java.util.Date(ll));
                return cc;
            } else {
                return toDate(target, ll);
            }
        } else {
            return value;
        }
    }

    public long convertStrToDate(final String anStr) {
        return convertStrToDate(anStr, null);
    }

    public SimpleDateFormat findDateFormater(final String anPatten) {
        for (SimpleDateFormat sdf : dateFormater) {
            if (sdf.toPattern().equals(anPatten)) {
                // logger.info(sdf);
                return sdf;
            }
        }
        log.info("not find " + anPatten);
        return new SimpleDateFormat(anPatten);
    }

    public long convertStrToDate(final String anStr, final String anPatten) {
        if (StringUtils.isNotBlank(anPatten)) {
            try {
                SimpleDateFormat sdf = findDateFormater(anPatten);
                return sdf.parse(anStr).getTime();
            }
            catch (ParseException e) {
                throw new DataTypeConvertException(
                        "错误的日期格式，和指定的模式不匹配；字符是：".concat(anStr).concat(", 模式是：").concat(anPatten));
            }
        }
        for (int i = 0, k = dateFormater.length; i < k; i++) {
            try {
                java.util.Date dd = dateFormater[i].parse(anStr);
                return dd.getTime();
            }
            catch (Exception ex) {}
        }
        long ll = parseLongValue(anStr);
        if (ll == Long.MIN_VALUE) { // 是字符串的东西
            throw new DataTypeConvertException("错误的日期格式，请与开发人员联系；字符是：".concat(anStr));
        }

        return ll;
    }

    public long parseLongValue(final String anValue) {
        for (int i = 0; i < 3; i++) {
            try {
                if (i == 0) {
                    return Long.parseLong(anValue);
                }
                if (i == 1) {
                    return Long.parseLong(anValue, 16);
                }
                if (i == 2) {
                    return Long.parseLong(anValue, 8);
                }
            }
            catch (NumberFormatException ex) {
                // log.error ( "不能将提供的字符串“" + anValue + "”,解析成长整型数字" );
            }
        }
        return Long.MIN_VALUE;
    }

    private java.util.Date toDate(final Class target, long anL) {
        if (target == java.util.Date.class) {
            return new java.util.Date(anL);
        } else if (target == java.sql.Date.class) {
            return new java.sql.Date(anL);
        } else if (target == java.sql.Time.class) {
            return new java.sql.Time(anL);
        } else if (target == java.sql.Timestamp.class) {
            return new java.sql.Timestamp(anL);
        } else {
            return new java.util.Date(anL);
        }
    }

    private Number toNumberValue(final Class target, final Number nn) {
        if ((target == Integer.class) || (target == Integer.TYPE)) {
            return new Integer(nn.intValue());
        }
        if ((target == Long.class) || (target == Long.TYPE)) {
            return new Long(nn.longValue());
        }
        if ((target == Short.class) || (target == Short.TYPE)) {
            return new Short(nn.shortValue());
        }
        if ((target == Float.class) || (target == Float.TYPE)) {
            return new Float(nn.floatValue());
        }
        if ((target == Double.class) || (target == Double.TYPE)) {
            return new Double(nn.doubleValue());
        }
        if ((target == Byte.class) || (target == Byte.TYPE)) {
            return new Byte(nn.byteValue());
        }
        if (target == BigDecimal.class) {
            return new BigDecimal(nn.doubleValue());
        }
        if (target == BigInteger.class) {
            return new BigInteger(Integer.toString(nn.intValue()));
        } else {
            return new Integer(0);
        }
    }

    /**
     * 判断是否是数据类型是否兼容。
     * 
     * @param value
     *            Object
     * @param type
     *            Class
     * @return boolean
     */
    public boolean isCompatibleType(final Object value, final Class type) {
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
            return true;

        } else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
            return true;

        } else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
            return true;

        } else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
            return true;

        } else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
            return true;

        } else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
            return true;

        } else if (type.equals(Character.TYPE) && Character.class.isInstance(value)) {
            return true;

        } else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
            return true;

        } else {
            return false;
        }
    }

    public static Class findCasingClass(Class anClass) {
        return converter.findCasingClassT(anClass);
    }

    public Class findCasingClassT(final Class anClass) {
        if (anClass.isPrimitive()) {
            if (anClass == Integer.TYPE) {
                return Integer.class;
            }
            if (anClass == Long.TYPE) {
                return Long.class;
            }
            if (anClass == Double.TYPE) {
                return Double.class;
            }
            if (anClass == Float.TYPE) {
                return Float.class;
            }
            if (anClass == Boolean.TYPE) {
                return Boolean.class;
            }
            if (anClass == Character.TYPE) {
                return Character.class;
            }
            if (anClass == Byte.TYPE) {
                return Byte.class;
            }
            if (anClass == Short.TYPE) {
                return Short.class;
            }
            if (anClass == Void.TYPE) {
                return Void.class;
            }
            return anClass;
        } else {
            return anClass;
        }
    }

}
