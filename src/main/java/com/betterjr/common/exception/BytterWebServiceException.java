package com.betterjr.common.exception;

import com.betterjr.common.data.WebServiceErrorCode;

public class BytterWebServiceException extends BytterException {

    private static final long serialVersionUID = 7407292215937177010L;

    private final WebServiceErrorCode errorCode;

    public BytterWebServiceException(WebServiceErrorCode anErrorCode) {

        super(anErrorCode.getCode(), anErrorCode.getMsg());
        errorCode = anErrorCode;
    }

    public BytterWebServiceException(WebServiceErrorCode anErrorCode, Throwable cause) {
        super(anErrorCode.getCode(), anErrorCode.getMsg());
        this.errorCode = anErrorCode;
    }

    public WebServiceErrorCode getErrorCode() {
        return this.errorCode;
    }
}
