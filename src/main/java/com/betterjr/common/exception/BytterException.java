package com.betterjr.common.exception;

import java.util.List;

import com.alibaba.dubbo.rpc.RpcException;
import com.betterjr.common.utils.Collections3;

/**
 * 
 * 我司异常处理父类，所有的异常处理基于该类派生
 *
 */
public class BytterException extends BaseException {
    private static final long serialVersionUID = -1535405572500029548L;
    private int code;
    private List<String> errorList;

    public int getCode() {
        return code;
    }

    public BytterException() {

    }
    
    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        else {
            return new BytterException(e);
        }
    }

    public BytterException(String message) {
        super(message);
    }

    public BytterException(int anCode, String message) {
        super(String.valueOf(anCode),message);
        this.code = anCode;
    }
    
    public BytterException(int anCode, List<String> message) {
        super(String.valueOf(anCode),message.get(0));
        this.errorList=message;
        this.code = anCode;
    }

    public BytterException(Throwable cause) {
        super(cause);
    }
    
    public BytterException(String message, Throwable cause) {
        super(message, cause);
    }

    public BytterException(int anCode, String message, Throwable cause) {
        super(message, cause);
        this.code = anCode;
    }
    
    public static boolean isCauseBytterException(Exception btEx){
        if(btEx.getCause()!=null && btEx.getCause() instanceof BytterException){
            return true;
        }
        return false;
    }
    
    public List<String> getErrorList() {
        return errorList;
    }
}
