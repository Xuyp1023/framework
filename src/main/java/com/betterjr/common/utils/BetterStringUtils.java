package com.betterjr.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.betterjr.common.service.SpringContextHolder;
import com.google.common.collect.Lists;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * 
 * @author zhoucy
 */
public class BetterStringUtils extends org.apache.commons.lang3.StringUtils {

    private static final char SEPARATOR = '_';
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 转换为字节数组
     * 
     * @param str
     * @return
     */
    public static byte[] getBytes(String str) {
        if (str != null) {
            try {
                return str.getBytes(CHARSET_NAME);
            }
            catch (UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static <T> T nvlCheck(T source, T dest) {
        if (source == null) {
            return dest;
        } else {
            return source;
        }
    }

    // 检查联系方式，包括手机和电话
    public static boolean checkPhone(String anPhone) {
        if (isBlank(anPhone)) {

            return true;
        }
        if (anPhone.matches("\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d(3)-\\d(8)")) {
            return true;
        } else if (anPhone.matches("^[1][3,5]+\\d{9}")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMobileNo(String anMobile) {
        if (StringUtils.isBlank(anMobile)) {
            return true;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[^4,\\D]))\\d{8}$");
        Matcher m = p.matcher(anMobile);
        return m.matches();
    }

    // 验证邮政编码
    public static boolean checkPost(String anPost) {
        if (isBlank(anPost)) {
            return true;
        }
        if (anPost.matches("[1-9]\\d{5}(?!\\d)")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmail(String anEmail) {
        if (isBlank(anEmail)) {
            return true;
        }
        return anEmail.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,3}$");
    }

    /**
     * 转换为字节数组
     * 
     * @param str
     * @return
     */
    public static String toString(byte[] bytes) {
        try {
            return new String(bytes, CHARSET_NAME);
        }
        catch (UnsupportedEncodingException e) {
            return EMPTY;
        }
    }

    /**
     * 是否包含字符串
     * 
     * @param str
     *            验证字符串
     * @param strs
     *            字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strs) {
        if (str != null) {
            for (String s : strs) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     * 
     * @param html
     * @return
     */
    public static String replaceMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     * 
     * @param txt
     * @return
     */
    public static String toHtml(String txt) {
        if (txt == null) {
            return "";
        }
        return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
    }

    /**
     * 缩略字符串（不区分中英文字符）
     * 
     * @param str
     *            目标字符串
     * @param length
     *            截取长度
     * @return
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String abbr2(String param, int length) {
        if (param == null) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        int n = 0;
        char temp;
        boolean isCode = false; // 是不是HTML代码
        boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHTML = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHTML) {
                isHTML = false;
            }
            try {
                if (!isCode && !isHTML) {
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (n <= length - 3) {
                result.append(temp);
            } else {
                result.append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去掉不需要结素标记的HTML标记
        temp_result = temp_result.replaceAll(
                "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
                "");
        // 去掉成对的HTML标记
        temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
        // 用正则表达式取出标记
        Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
        Matcher m = p.matcher(temp_result);
        List<String> endHTML = Lists.newArrayList();
        while (m.find()) {
            endHTML.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHTML.size() - 1; i >= 0; i--) {
            result.append("</");
            result.append(endHTML.get(i));
            result.append(">");
        }
        return result.toString();
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return Double.valueOf(trim(val.toString()));
        }
        catch (Exception e) {
            return 0D;
        }
    }

    /**
     * 转换为Float类型
     */
    public static Float toFloat(Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(Object val) {
        return toLong(val).intValue();
    }

    public static String join(final List<String> anList) {

        return join(anList, ",");
    }

    public static String join(final List<String> anList, final char separator) {
        if (Collections3.isEmpty(anList)) {

            return "";
        }

        final StringBuilder buf = new StringBuilder(anList.size() * 16);
        for (int i = 0, k = anList.size(); i < k; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            buf.append(anList.get(i));
        }

        return buf.toString();
    }

    /**
     * 使用给定的分隔符, 将一个数组拼接成字符串
     * 
     * @param sp
     *            分隔符
     * @param array
     *            要拼接的数组
     * @return 拼接好的字符串
     */
    public static <T> String join(String sp, T... array) {
        return join(sp, array).toString();
    }

    /**
     * 将一个数组转换成字符串
     * <p>
     * 每个元素之间，都会用一个给定的字符分隔
     * 
     * @param c
     *            分隔符
     * @param objs
     *            数组
     * @return 拼合后的字符串
     */
    public static <T> StringBuilder join(Object c, T[] objs) {
        StringBuilder sb = new StringBuilder();
        if (null == objs || 0 == objs.length) return sb;

        sb.append(objs[0]);
        for (int i = 1; i < objs.length; i++)
            sb.append(c).append(objs[i]);

        return sb;
    }

    /**
     * 获得i18n字符串
     */
    public static String getMessage(String code, Object[] args) {
        LocaleResolver localLocaleResolver = SpringContextHolder.getBean(LocaleResolver.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Locale localLocale = localLocaleResolver.resolveLocale(request);
        return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
    }

    /**
     * 驼峰命名法工具
     * 
     * @return toCamelCase("hello_world") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") =
     *         "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     * 
     * @return toCamelCase("hello_world") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") =
     *         "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     * 
     * @return toCamelCase("hello_world") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") =
     *         "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 如果不为空，则设置值
     * 
     * @param target
     * @param source
     */
    public static void setValueIfNotBlank(String target, String source) {
        if (isNotBlank(source)) {
            target = source;
        }
    }

    public static String getSysPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp = path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
        String separator = System.getProperty("file.separator");
        String resultPath = temp.replaceAll("/", separator + separator);
        return resultPath;
    }

    public static String getClassPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp = path.replaceFirst("file:/", "");
        String separator = System.getProperty("file.separator");
        String resultPath = temp.replaceAll("/", separator + separator);
        return resultPath;
    }

    public static String getSystempPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     * 
     * @param objectString
     *            对象串 例如：row.user.id 返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] vals = split(objectString, ".");
        for (int i = 0; i < vals.length; i++) {
            val.append("." + vals[i]);
            result.append("!" + (val.substring(1)) + "?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }

    public static Boolean defBoolean(Boolean anB, Boolean anDef) {
        if (anB == null) {
            return anDef;
        } else {
            return anB;
        }
    }

    public static String defCmfCustType(String anCustType, String anIdentType) {
        if ("0".equalsIgnoreCase(anCustType)) {
            return "1".concat(anIdentType);
        } else {
            return anIdentType;
        }
    }

    public static List<String> splitTrim(String anStr) {
        return splitTrim(anStr, ",");
    }

    public static List<String> splitTrim(String anStr, String anSeparatorChars) {
        List<String> tmpList = new ArrayList();
        if (isBlank(anStr)) {
            return tmpList;
        }
        if (isBlank(anSeparatorChars)) {
            anSeparatorChars = ",";
        }
        for (String tmpStr : split(anStr, anSeparatorChars)) {
            if (isNotBlank(tmpStr)) {
                tmpList.add(tmpStr.trim());
            }
        }
        return tmpList;
    }

    public static Map<String, String> parseParamsMap(String anStr) {

        return parseParamsMap(anStr, '&', '=');
    }

    /**
     * 将字符串按照参数分隔符号和键值分隔符号解析为Map数组。
     * @param anStr
     * @param anSplitChar
     * @param anPartChar
     * @return
     */
    public static Map<String, String> parseParamsMap(String anStr, char anSplitChar, char anPartChar) {
        Map<String, String> paramsMap = new HashMap();

        if (isNotBlank(anStr)) {
            char[] workCharArr = anStr.toCharArray();
            boolean usePart = false, endSplit = false;
            String workKey = null, workData;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < workCharArr.length; i++) {
                if (usePart == false && anPartChar == workCharArr[i]) {
                    usePart = true;
                    workKey = sb.toString();
                    sb.setLength(0);
                    endSplit = false;
                } else if (anSplitChar == workCharArr[i] && usePart == true) {
                    usePart = false;
                    workData = sb.toString();
                    sb.setLength(0);
                    paramsMap.put(workKey, workData);
                    endSplit = true;
                } else {
                    sb.append(workCharArr[i]);
                }
            }
            if (endSplit == false) {
                paramsMap.put(workKey, sb.toString());
            }
        }

        return paramsMap;
    }

    /**
     * 判断一个对象是否是字符串
     * 
     * @param anObj
     *            ，如果是空字符串，也不认为是一个有效的字符串
     * @return true 是字符串，false不是字符串或者是一个空字符串
     */
    public static boolean isString(Object anObj) {
        if (anObj instanceof String) {

            return isNotBlank((String) anObj);
        }

        return false;
    }

    public static String createRandomCharAndNum(int length) {
        String val = "";
        SecureRandom random = new SecureRandom();
        String chars = "abcdefhjkmnprstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得字母
                val += chars.charAt((random.nextInt(chars.length())));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 系统默认的短字符是3个，即小于或等于三个字符串，则认为是短字符串。
     * @param anStr
     * @return
     */
    public static boolean defShortString(String anStr) {

        return shortString(anStr, 3);
    }

    /**
     * 判断字符串的长度是否小于或等于指定长度，如果是，则为真
     * @param anStr
     * @param anLength
     * @return
     */
    public static boolean shortString(String anStr, int anLength) {
        if (isBlank(anStr)) {
            return true;
        }
        if (anStr.trim().length() <= anLength) {
            return true;
        }

        return false;
    }

    /**
     * 全角字符转半角的函数
     * 
     * @param src
     * @return
     */
    public static String toSemiangle(String anStr) {
        char[] c = anStr.toCharArray();
        for (int index = 0; index < c.length; index++) {
            if (c[index] == 12288) {// 全角空格
                c[index] = (char) 32;
            } else if (c[index] > 65280 && c[index] < 65375) {// 其他全角字符
                c[index] = (char) (c[index] - 65248);
            }
        }

        return String.valueOf(c);
    }

    public static void main(String[] args) {
        String tmpStr = "data=sdke9SqdtKzfcUiYpwJ/Re9do9ylwBDCFya+=smLJeck0QGDlWy4ywXAK9N3U1VJe8yEdweAii7SdCX+GEi9JD60PCSosggcVU/j1vFqMa1UKmf0TiSJD2lxg5sK2Kze0T1cxZl2FINrzaz6qmtAL1i+P706OLOGQ4/3oywNUZDyE5JIzaYLwROelLC1cUmBAbt/Qw0hdLuQwRVTuPtsEXOSy7t97ZhWo9u/MQze/meyNHREHXCI8EkVjw8v2n5pPz+z4ZxrF85j3CXaRNDnsORUE7kAwKIV98yBI/KyO95Ub7PiqaE4PwNpvphojwNVw+G8YHlMdMHPIHAO2abh+Aw==&sign=dkSLs22CVmfR/tWbiM+JV/BgUMzdsiV2jjeWhYbwswxyUpDf+HenUMBeEQjGvxHDmYaavSEJTXqpUyZDMIv8RVkKX4Ss0nY2hO0Czkp8m/ZKJVOMdwNcWjNYIC7Va/7kg1PN7ERMHadfkfdD+cxszCDKp4dClAa1UaDJs/ZDAvCBM1NKstJEjF9cFdktROgJmqrz8EFM6LloG+uo9FsHfwtY8mtHMtIE+TKANVyL3q6MUtXFNUhSWBZNo18BQLqtSBTOETKnBMEZbRCQauhbALw5h8Dvo7TG8IRXUmeMr8lU8NCrSEkRuY6quPN4yxxr9+d+EoHhEpvMfZNCRGkb5w==";
        Map<String, String> tmpMap = parseParamsMap(tmpStr);
        for (Map.Entry<String, String> ent : tmpMap.entrySet()) {

            System.out.println(ent.getKey() + " = " + ent.getValue());
        }
    }
}
