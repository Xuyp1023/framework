package com.betterjr.common.exception;

import java.util.List;

/**
 * 访问服务的异常类，开发人员自己定义错误代码。
 * 
 *
 */
public class BytterValidException extends BytterException {

    private static final long serialVersionUID = 3753937455939824776L;

    public BytterValidException(String message) {
        super(message);
    }

    public BytterValidException(int anCode, String message) {
        super(anCode, message);
    }

    public BytterValidException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }

    public BytterValidException(int anCode, List<String> message) {
        super(anCode, message);
    }

}
