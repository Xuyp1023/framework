package com.betterjr.modules.wechat.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 * 采用驱动式处理微信消息
 * 
 * @author zhoucy
 */
public class MessageHandler extends DefaultHandler2 {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private static final String[] DEF_TOKENS = new String[] { "xml", "ScanCodeInfo", "SendLocationInfo", "SendPicsInfo", "PicList", "item" };
    // 节点属性值
    private String attrVal;

    private Map<String, String> values = new ConcurrentHashMap<String, String>();

    private StringBuffer sb = new StringBuffer();

    public Map<String, String> getValues() {
        return values;
    }

    @Override
    public void startDocument() throws SAXException {
        values.clear();
        sb.setLength(0);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("PicList".equals(qName)) {
            sb.append("[");
            return;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (log.isDebugEnabled()) {
            boolean bbq = false;
            for (String tmpStr : DEF_TOKENS) {
                if (tmpStr.equals(qName) == false) {
                    bbq = true;
                    break;
                }
            }
            if (bbq) {
                log.debug("Current node vaule: [%s-%s]", qName, attrVal);
            }
        }

        // 暂存为map集合
        if ("ToUserName".equalsIgnoreCase(qName)) {
            values.put("toUserName", attrVal);
            return;
        }
        if ("FromUserName".equalsIgnoreCase(qName)) {
            values.put("fromUserName", attrVal);
            return;
        }
        if ("CreateTime".equalsIgnoreCase(qName)) {
            values.put("createTime", attrVal);
            return;
        }
        if ("MsgType".equalsIgnoreCase(qName)) {
            values.put("msgType", attrVal);
            return;
        }
        if ("Content".equalsIgnoreCase(qName)) {
            values.put("content", attrVal);
            return;
        }
        if ("PicUrl".equalsIgnoreCase(qName)) {
            values.put("picUrl", attrVal);
            return;
        }
        if ("MediaId".equalsIgnoreCase(qName)) {
            values.put("mediaId", attrVal);
            return;
        }
        if ("Format".equalsIgnoreCase(qName)) {
            values.put("format", attrVal);
            return;
        }
        if ("Recognition".equalsIgnoreCase(qName)) {
            values.put("recognition", attrVal);
            return;
        }
        if ("ThumbMediaId".equalsIgnoreCase(qName)) {
            values.put("thumbMediaId", attrVal);
            return;
        }
        if ("Location_X".equalsIgnoreCase(qName)) {
            values.put("locationX", attrVal);
            return;
        }
        if ("Location_Y".equalsIgnoreCase(qName)) {
            values.put("locationY", attrVal);
            return;
        }
        if ("Scale".equalsIgnoreCase(qName)) {
            values.put("scale", attrVal);
            return;
        }
        if ("Label".equalsIgnoreCase(qName)) {
            values.put("label", attrVal);
            return;
        }
        if ("Title".equalsIgnoreCase(qName)) {
            values.put("title", attrVal);
            return;
        }
        if ("Description".equalsIgnoreCase(qName)) {
            values.put("description", attrVal);
            return;
        }
        if ("Url".equalsIgnoreCase(qName)) {
            values.put("url", attrVal);
            return;
        }
        if ("MsgId".equalsIgnoreCase(qName)) {
            values.put("msgId", attrVal);
            return;
        }
        if ("Event".equalsIgnoreCase(qName)) {
            values.put("event", attrVal);
            return;
        }
        if ("EventKey".equalsIgnoreCase(qName)) {
            values.put("eventKey", attrVal);
            return;
        }
        if ("ScanType".equalsIgnoreCase(qName)) {
            values.put("scanType", attrVal);
            return;
        }
        if ("ScanResult".equalsIgnoreCase(qName)) {
            values.put("scanResult", attrVal);
            return;
        }
        if ("Poiname".equalsIgnoreCase(qName)) {
            values.put("poiname", attrVal);
            return;
        }
        if ("Count".equalsIgnoreCase(qName)) {
            values.put("count", attrVal);
            return;
        }
        if ("PicMd5Sum".equalsIgnoreCase(qName)) {
            sb.append("{\"picMd5Sum\":\"").append(attrVal).append("\"},");
            return;
        }
        if ("PicList".equalsIgnoreCase(qName)) {
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("]");
            values.put("picList", sb.toString());
            return;
        }
        if ("Status".equalsIgnoreCase(qName)) {
            values.put("status", attrVal);
            return;
        }
        if ("TotalCount".equalsIgnoreCase(qName)) {
            values.put("totalCount", attrVal);
            return;
        }
        if ("FilterCount".equalsIgnoreCase(qName)) {
            values.put("filterCount", attrVal);
            return;
        }
        if ("SentCount".equalsIgnoreCase(qName)) {
            values.put("sentCount", attrVal);
            return;
        }
        if ("ErrorCount".equalsIgnoreCase(qName)) {
            values.put("errorCount", attrVal);
            return;
        }
        if ("KfAccount".equalsIgnoreCase(qName)) {
            values.put("kfAccount", attrVal);
            return;
        }
        if ("FromKfAccount".equalsIgnoreCase(qName)) {
            values.put("fromKfAccount", attrVal);
            return;
        }
        if ("ToKfAccount".equalsIgnoreCase(qName)) {
            values.put("toKfAccount", attrVal);
            return;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.attrVal = new String(ch, start, length);
    }

}
