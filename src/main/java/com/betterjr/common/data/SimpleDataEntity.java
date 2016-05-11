package com.betterjr.common.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.betterjr.common.utils.XmlUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("simpleData")
public class SimpleDataEntity implements Serializable {
    private static final long serialVersionUID = 3689286741663664469L;
    private   String name;
    private   String value;

    @JsonIgnore
    private   String three;
    
    @JsonIgnore
    private List<String> data;

    @JsonIgnore
    private boolean useSplit = false;
    
    public SimpleDataEntity(){
        
    }
    public SimpleDataEntity(String anName, String anValue) {
        this.name = anName;
        this.value = anValue;
        this.three = null;
    }

    public SimpleDataEntity(String anStr) {
        String[] arrStr = anStr.split("@");
        this.name = arrStr[0];
        if (arrStr.length == 1) {
            this.value = this.name;
            this.three = null;
        }
        else {
            this.value = arrStr[1];
            if (arrStr.length == 2) {
                this.three = null;
            }
            else {
                this.three = arrStr[2];
            }
        }
    }

    public String getThree() {
        return this.three;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isUseSplit() {
        return useSplit;
    }

    public SimpleDataEntity(String anStr, boolean anSplitValue) {
        this(anStr);
        if (anSplitValue) {
            this.data = XmlUtils.split(value);
        }
        useSplit = anSplitValue;
    }

    public List<String> findSplitValueData() {
        List<String> list = new LinkedList<>();
        if (data == null){
            return list;
        }
        list.addAll(data);

        return list;
    }

    public String splitLastValue() {

        return data.get(data.size() - 1);
    }

    public String splitFirstValue() {

        return data.get(0);
    }

    public boolean onlyOne() {
        return this.name.equals(value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append(", name=").append(name);
        sb.append(", value=").append(value);
        sb.append(", three=").append(three);
        sb.append(", data=").append(data);
        sb.append("]");
        return sb.toString();
    }
}
