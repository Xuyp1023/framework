package com.betterjr.common.exception;

public class SessionInvalidException extends BytterException {

    public SessionInvalidException(String message) {
        super(message);
    }

    public SessionInvalidException(int anCode, String message) {
        super(anCode, message);
    }

    public SessionInvalidException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }

    private static final long serialVersionUID = -8470816437235268803L;

}
