package com.betterjr.common.data;

public class KeyAndValueObject {
    private final Object key;
    private final Object value;

    public KeyAndValueObject(Object anKey, Object anValue) {
        this.key = anKey;
        this.value = anValue;
    }

    public Object getKey() {
        return this.key;
    }

    public Object getValue() {
        
        return this.value;
    }

    public String getStrKey() {
        if (key != null) {
            return key.toString();
        }
        else {
            return "";
        }
    }
}