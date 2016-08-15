package com.betterjr.common.mq.exception;

import com.betterjr.common.exception.BytterException;

/**
 * 
 * @author liuwl
 *
 */
public class BetterMqException extends BytterException {
    private static final long serialVersionUID = 7632348289407345304L;

    public BetterMqException(String message) {
        super(message);
    }

    public BetterMqException(int anCode, String message) {
        super(anCode, message);
    }

    public BetterMqException(Throwable cause) {
        super(cause);
    }

    public BetterMqException(String message, Throwable cause) {
        super(message, cause);
    }

    public BetterMqException(int anCode, String message, Throwable cause) {
        super(anCode, message, cause);
    }
}
