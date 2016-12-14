package com.betterjr.common.data;

/**
 * 远程调用结果信息
 * @author zhoucy
 *
 */
public class CheckDataResult {
    //处理结果
    private final boolean ok;
    //处理状态码
    private final int code;
    //处理返回消息
    private final String message;
    //处理后的错误信息
    private final Exception ex;
    
    public boolean isOk() {
        return this.ok;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Exception getEx() {
        return this.ex;
    }

    public CheckDataResult(boolean anOK) {
        this(anOK, " ");
    }

    public CheckDataResult(boolean anOK, String anMsg) {
        this(anOK, 0, anMsg);
    }

    public CheckDataResult(boolean anOK, int anCode) {
        this(anOK, anCode, " ");
    }

    public CheckDataResult(boolean anOK, int anCode, String anMsg) {
        this(anOK, anCode, anMsg, null);
    }

    public CheckDataResult(boolean anOK, int anCode, String anMsg, Exception anEx) {
        this.ok = anOK;
        this.code = anCode;
        this.message = anMsg;
        this.ex = anEx;
    }
}
