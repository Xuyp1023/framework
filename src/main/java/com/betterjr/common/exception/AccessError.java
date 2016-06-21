package com.betterjr.common.exception;

/**
 * 系统错误处理类，例如：服务器不可用等的处理；开发人员定义错误代码
 * 
 *
 */
public class AccessError extends BytterException {
    private static final long serialVersionUID = -3895329719995376922L;

    public AccessError(String anMessage) {
        super(anMessage);
    }

    public AccessError(int anCode, String message) {
        super(anCode, message);
    }

    public AccessError(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }

}
