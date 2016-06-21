package com.betterjr.common.exception;

/**
 * 
 * 我司异常处理父类，所有的异常处理基于该类派生
 *
 */
public class BytterSecurityException extends RuntimeException {
    private static final long serialVersionUID = -1535405572500029548L;
    private int code;

    public int getCode() {
        return code;
    }

    public BytterSecurityException() {

    }

    public BytterSecurityException(String message) {
        super(message);
    }

    public BytterSecurityException(int anCode, String message) {
        super(message);
        this.code = anCode;
    }

    public BytterSecurityException(Throwable cause) {
        super(cause);
    }

    public BytterSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public BytterSecurityException(int anCode, String message, Throwable cause) {
        super(message, cause);
        this.code = anCode;
    }
}
