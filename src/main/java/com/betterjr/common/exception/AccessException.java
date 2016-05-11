package com.betterjr.common.exception;

/**
 * 访问服务的异常类，开发人员自己定义错误代码。
 * 
 *
 */
public class AccessException extends BytterException {

    private static final long serialVersionUID = 3753937455939824776L;

    public AccessException(String message) {
        super(message);
    }

    public AccessException(int anCode, String message) {
        super(anCode, message);
    }

    public AccessException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}
