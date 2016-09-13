package com.betterjr.modules.wechat.data.event;

import java.util.List;
import java.util.Map;

import com.betterjr.common.mapper.JsonMapper;

/**
 * 用户拍照/相册发图事件
 * 
 * @author zhoucy
 */
public class SendPhotosEvent extends MenuEvent {

    /**
     * 发送的图片信息
     */
    private SendPicsInfo sendPicsInfo;

    public SendPhotosEvent() {

    }

    public SendPhotosEvent(Map<String, String> values) {
        super(values);
        List<PicItem> items;
        items = JsonMapper.jacksonToCollection(values.get("picList"), PicItem.class);
        this.sendPicsInfo = new SendPicsInfo(Integer.parseInt(values.get("count")), items);
    }

    public SendPicsInfo getSendPicsInfo() {
        return sendPicsInfo;
    }

    public void setSendPicsInfo(SendPicsInfo sendPicsInfo) {
        this.sendPicsInfo = sendPicsInfo;
    }

    @Override
    public String toString() {
        return "ScanSysPhotoEvent [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime=" + createTime + ", msgType="
                + msgType + ", event=" + event + ", eventKey=" + eventKey + ", sendPicsInfo=" + sendPicsInfo + "]";
    }

}
