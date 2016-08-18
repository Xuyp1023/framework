package com.betterjr.modules.remote.entity;

import java.util.List;
import java.util.Map;

/**
 * 定义远程调用返回结果超类信息
 * 
 * @author zhoucy
 *
 */
public class RemoteResultInfo<T> implements java.io.Serializable {

    private static final long serialVersionUID = -8395523440216243887L;

    /**
     * 返回状态，0000表示成功；根据不同的错误码做处理
     */
    private final boolean sucess;

    /**
     * 返回错误信息，成功无错误信息
     */
    private final String message;

    private final List data;

    /**
     * 解析后的原始数据
     */
    private final List<Map<String, Object>> orignData;

    public RemoteResultInfo(List<Map<String, Object>> anOrign, List anData, boolean anStatus, String anMsg) {

        this.orignData = anOrign;
        this.data = anData;
        this.sucess = anStatus;
        this.message = anMsg;

    }

    public Map<String, Object> getOrign() {

        return orignData.get(0);
    }

    public List<Map<String, Object>> getOrignData() {
        return orignData;
    }

    public List getData() {
        return data;
    }

    public Object getResult() {

        return this.data.get(0);
    }

    public boolean isSucess() {

        return sucess;
    }

    public String getMessage() {

        return message;
    }

    public boolean isOneData() {

        return this.data.size() == 1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("sucess = ").append(sucess);
        sb.append(", message=").append(message).append("\r\n");
        
        for (Object obj : this.data) {
            sb.append(obj.toString());
        }
        sb.append("]");
        return sb.toString();

    }
}