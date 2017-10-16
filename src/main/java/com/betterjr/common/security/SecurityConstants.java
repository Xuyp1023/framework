package com.betterjr.common.security;

public interface SecurityConstants {

    /**
     * 数字客户端验证数字证书
     */
    public static String CERT_ATTR_CER = "javax.servlet.request.X509Certificate";

    public static String CUST_CERT_INFO = "betterjr.X509Certificate";

    public static String CUST_OPERATOR = "betterjr.CUST_OPERATOR";

    /**
    * 登录用户
    */
    public static String LOGIN_USER = "login_user";

    /**
     * 验证码
     */
    public static String CAPTCHA_KEY = "captcha_key";

    /**
     * 日志参数
     */
    public static String LOG_ARGUMENTS = "log_arguments";

    /**
     * ResponseBody注解返回的mapModel。
     */
    public static String MODEL_MAP = "model_map";
}
