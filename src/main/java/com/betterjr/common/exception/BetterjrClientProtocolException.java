package com.betterjr.common.exception;

public class BetterjrClientProtocolException extends AccessException {

    private static final long serialVersionUID = 4109056985709772547L;

    public BetterjrClientProtocolException(String message) {
        super(message);
    }

    public BetterjrClientProtocolException(int anCode, String message) {
        super(anCode, message);
    }

    public BetterjrClientProtocolException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}
