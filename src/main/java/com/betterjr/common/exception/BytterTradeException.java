package com.betterjr.common.exception;

/**
 * 访问服务的异常类，开发人员自己定义错误代码。
 * 
 *
 */
public class BytterTradeException extends BytterException {

    private static final long serialVersionUID = 3753937455939824776L;

    public BytterTradeException(String message) {
        super(message);
    }

    public BytterTradeException(int anCode, String message) {
        super(anCode, message);
    }

    public BytterTradeException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}
