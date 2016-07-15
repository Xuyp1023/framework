package com.betterjr.common.mq.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.betterjr.common.mq.codec.MQCodecType;

/**
 * @author liuwl
 */
public class MQMessage implements Serializable {
    private final String topic;

    private final MQCodecType codecType;

    private Serializable object;

    private Map<String, Serializable> headers;

    public void addHead(String anName, Serializable anValue) {
        if (headers == null) {
            headers = new HashMap<>();
        }

        headers.put(anName, anValue);
    }

    public Object getHead(String anName) {
        if (this.headers == null) {
            return null;
        }

        return this.headers.get(anName);
    }

    public MQMessage(final String anTopic) {
        this(anTopic, MQCodecType.FST);
    }

    public MQMessage(final String anTopic, final Serializable anObject) {
        this(anTopic, MQCodecType.FST, anObject);
    }

    public MQMessage(final String anTopic, final MQCodecType anCodecType) {
        this(anTopic, anCodecType, null);
    }

    public MQMessage(final String anTopic, final MQCodecType anCodecType, final Serializable anObject) {
        this.topic = anTopic;
        this.codecType = anCodecType;
        this.object = anObject;
    }

    public String getTopic() {
        return topic;
    }

    public Serializable getObject() {
        return object;
    }

    public void setObject(Serializable anObject) {
        this.object = anObject;
    }

    public MQCodecType getCodecType() {
        return codecType;
    }

}
