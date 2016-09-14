package com.betterjr.modules.wechat.data.api;

/**
 * 微信模板消息
 *
 * @author zhoucy
 */
public class WechatPushTempField implements java.io.Serializable  {

    /**
     *
     */
    private static final long serialVersionUID = -6008755919656074511L;

    /**
     * 模板字段名称
     */
    private String name;
    /**
     * 显示颜色
     */
    private String color = "#000000";
    /**
     * 显示数据
     */
    private String value;

    public WechatPushTempField() {

    }

    /**
     * 默认颜色(蓝色)
     *
     * @param name
     *            参数名称
     * @param value
     *            参数值
     */
    public WechatPushTempField(final String name, final String value) {
        this.name = name;
        //this.color = "#119EF3";
        this.value = value;
    }

    /**
     * 带参数构造方法
     *
     * @param name
     *            参数名称
     * @param color
     *            字体颜色
     * @param value
     *            参数值
     */
    public WechatPushTempField(final String name, final String color, final String value) {
        this.name = name;
        this.color = color;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("name =").append(name);
        sb.append(", value =").append(value);
        sb.append(", color =").append(color);
        sb.append("]");
        return sb.toString();
    }

    /**
     * 组装模板数据
     */
    public String templateData() {
        final StringBuffer data = new StringBuffer("\"" + name + "\":{");
        data.append("\"value\":\"").append(value).append("\",");
        data.append("\"color\":\"").append(color).append("\"}");
        return data.toString();
    }

}
