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
    private final String name;
    private final String value;

    @JsonIgnore
    private String three;

    @JsonIgnore
    private List<String> data;

    @JsonIgnore
    private boolean useSplit = false;

    public SimpleDataEntity() {
        this.name = null;
        this.value = null;
        this.three = null;
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
        } else {
            this.value = arrStr[1];
            if (arrStr.length == 2) {
                this.three = null;
            } else {
                this.three = arrStr[2];
            }
        }
    }

    public String getThree() {
        return this.three;
    }

    public void setThree(String anThree) {
        this.three = anThree;
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
        if (data == null) {
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

    @Override
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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SimpleDataEntity other = (SimpleDataEntity) that;
        return (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
                && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()))
                && (this.getThree() == null ? other.getThree() == null : this.getThree().equals(other.getThree()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        result = prime * result + ((getThree() == null) ? 0 : getThree().hashCode());

        return result;
    }
}
