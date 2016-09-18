package com.betterjr.modules.wechat.util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.wechat.data.EventType;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.data.event.CustomServiceEvent;
import com.betterjr.modules.wechat.data.event.LocationEvent;
import com.betterjr.modules.wechat.data.event.MenuEvent;
import com.betterjr.modules.wechat.data.event.ScanCodeEvent;
import com.betterjr.modules.wechat.data.event.ScanEvent;
import com.betterjr.modules.wechat.data.event.SendLocationInfoEvent;
import com.betterjr.modules.wechat.data.event.SendPhotosEvent;
import com.betterjr.modules.wechat.data.message.Article;
import com.betterjr.modules.wechat.data.message.BasicMsg;
import com.betterjr.modules.wechat.data.message.ImageMsg;
import com.betterjr.modules.wechat.data.message.LinkMsg;
import com.betterjr.modules.wechat.data.message.LocationMsg;
import com.betterjr.modules.wechat.data.message.NewsMsg;
import com.betterjr.modules.wechat.data.message.TextMsg;
import com.betterjr.modules.wechat.data.message.VideoMsg;
import com.betterjr.modules.wechat.data.message.VoiceMsg;
import com.betterjr.modules.wechat.data.push.SentAllJobEvent;
import com.betterjr.modules.wechat.data.push.SentTmlJobEvent;
import com.betterjr.modules.wechat.dubboclient.CustWeChatDubboClientService;
import com.betterjr.modules.wechat.handler.QrcodeHandler;
import com.betterjr.modules.wechat.handler.QrcodeHandlerFactory;

/**
 * 微信消息,事件处理器(实际生产中需要重写)
 *
 * @author zhoucy
 */
public class WechatDefHandler implements WechatHandler {
    private final CustWeChatDubboClientService wechatDubboClientService;

    public WechatDefHandler(final CustWeChatDubboClientService anWechatDubboClientService){
        wechatDubboClientService = anWechatDubboClientService;
    }

    @Override
    public BasicMsg defMsg(final BasicMsg msg) {
        final TextMsg text_msg = new TextMsg(msg);
        text_msg.setContent(msg.getMsgType());
        return text_msg;
    }

    @Override
    public BasicMsg defEvent(final BasicEvent event) {
        final TextMsg text_msg = new TextMsg(event);
        text_msg.setContent(BetterStringUtils.join("\n", event.getEvent(), event.getEventKey()));
        return text_msg;
    }

    @Override
    public BasicMsg text(final TextMsg msg) {
        return msg;
    }

    @Override
    public BasicMsg image(final ImageMsg msg) {
        return msg;
    }

    @Override
    public BasicMsg voice(final VoiceMsg msg) {
        final TextMsg text_msg = new TextMsg(msg);
        text_msg.setContent(BetterStringUtils.join("\n", msg.getMediaId(), msg.getFormat(), msg.getRecognition()));
        return text_msg;
    }

    @Override
    public BasicMsg video(final VideoMsg msg) {
        final TextMsg text_msg = new TextMsg(msg);
        text_msg.setContent(BetterStringUtils.join("\n", msg.getMsgType(), msg.getMediaId(), msg.getThumbMediaId()));
        return text_msg;
    }

    @Override
    public BasicMsg shortVideo(final VideoMsg msg) {
        final TextMsg text_msg = new TextMsg(msg);
        text_msg.setContent(BetterStringUtils.join("\n", msg.getMsgType(), msg.getMediaId(), msg.getThumbMediaId()));
        return text_msg;
    }

    @Override
    public BasicMsg location(final LocationMsg msg) {
        final TextMsg text_msg = new TextMsg(msg);
        text_msg.setContent(BetterStringUtils.join("\n", msg.getX(), msg.getY(), String.valueOf(msg.getScale()), msg.getLabel()));
        return text_msg;
    }

    @Override
    public BasicMsg link(final LinkMsg msg) {
        final NewsMsg news_msg = new NewsMsg(msg);
        final Article art = new Article();
        art.setTitle(msg.getTitle());
        art.setDescription(msg.getDescription());
        art.setPicUrl("http://www.qiejf.com/images/top1.jpg");
        art.setUrl(msg.getUrl());
        news_msg.setArticles(Arrays.asList(art));
        return news_msg;
    }

    @Override
    public BasicMsg eClick(final MenuEvent event) {
        final TextMsg text_msg = new TextMsg(event);
        text_msg.setContent(event.getEventKey());
        return text_msg;
    }

    @Override
    public void eView(final MenuEvent event) {
    }

    @Override
    public BasicMsg eSub(final BasicEvent anEvent) {
        wechatDubboClientService.saveWeChatInfo(anEvent, EventType.subscribe);
        if (StringUtils.startsWith(anEvent.getEventKey(), "qrscene_")) {// 带参数场景扫描
            final String eventKey = StringUtils.substring(anEvent.getEventKey(), 8);
            final QrcodeHandler handler = QrcodeHandlerFactory.createQrcodeHandler(eventKey);
            final BasicMsg basicMsg = handler.process(anEvent);
            return basicMsg;
        } else {
            final TextMsg textMsg = new TextMsg(anEvent);
            textMsg.setContent("欢迎关注前海拜特微信开发公众号!");
            return textMsg;
        }
    }

    @Override
    public void eUnSub(final BasicEvent anEvent){
        wechatDubboClientService.saveWeChatInfo(anEvent, EventType.unsubscribe);
    }

    @Override
    public BasicMsg eScan(final ScanEvent anEvent) {
        final QrcodeHandler handler = QrcodeHandlerFactory.createQrcodeHandler(anEvent.getEventKey());
        final BasicMsg basicMsg = handler.process(anEvent);
        return basicMsg;
    }

    @Override
    public void eLocation(final LocationEvent event) {

    }

    @Override
    public BasicMsg eScanCodePush(final ScanCodeEvent event) {
        final TextMsg text_msg = new TextMsg(event);
        text_msg.setContent(BetterStringUtils.join("\n", event.getEventKey(), event.getScanType(), event.getScanResult()));
        return text_msg;
    }

    @Override
    public BasicMsg eScanCodeWait(final ScanCodeEvent event) {
        return this.eScanCodePush(event);
    }

    @Override
    public BasicMsg ePicSysPhoto(final SendPhotosEvent event) {
        final TextMsg text_msg = new TextMsg(event);
        text_msg.setContent(BetterStringUtils.join("\n", event.getEventKey(), String.valueOf(event.getSendPicsInfo().getCount()),
                String.valueOf(event.getSendPicsInfo().getPicList()), String.valueOf(event.getSendPicsInfo().getPicList().get(0).getPicMd5Sum())));
        return text_msg;
    }

    @Override
    public BasicMsg ePicPhotoOrAlbum(final SendPhotosEvent event) {
        return this.ePicSysPhoto(event);
    }

    @Override
    public BasicMsg ePicWeixin(final SendPhotosEvent event) {
        return this.ePicSysPhoto(event);
    }

    @Override
    public BasicMsg eLocationSelect(final SendLocationInfoEvent event) {
        final TextMsg text_msg = new TextMsg(event);
        text_msg.setContent(BetterStringUtils.join("\n", event.getLocationX(), event.getLocationY(), event.getLabel(),
                String.valueOf(event.getScale()), event.getPoiname()));
        return text_msg;
    }

    @Override
    public void eSentTmplJobFinish(final SentTmlJobEvent event) {
    }

    @Override
    public void eSentAllJobFinish(final SentAllJobEvent event) {
    }

    @Override
    public void eCreateKfSession(final CustomServiceEvent event) {
    }

    @Override
    public void eCloseKfSession(final CustomServiceEvent event) {
    }

    @Override
    public void eSwitchKfSession(final CustomServiceEvent event) {
    }
}
