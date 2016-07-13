package com.betterjr.common.mq.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.betterjr.common.mq.codec.BtCodecType;

/**
 * @author liuwl
 */
public class BtMessage implements Serializable {
    private final String topic;
    
    private final BtCodecType codecType;
    
    private Serializable object;
    
    private Map<String, Serializable> param = new HashMap<>();
    
    public BtMessage(final String anTopic) {
        this(anTopic, BtCodecType.FST);
    }
    
    public BtMessage(final String anTopic, final Serializable anObject) {
        this(anTopic, BtCodecType.FST, anObject);
    }
    
    public BtMessage(final String anTopic, final BtCodecType anCodecType) {
        this(anTopic, anCodecType, null);
    }
    
    public BtMessage(final String anTopic, final BtCodecType anCodecType, final Serializable anObject) {
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
        this.object = object;
    }

    public BtCodecType getCodecType() {
        return codecType;
    }

    
    
}
