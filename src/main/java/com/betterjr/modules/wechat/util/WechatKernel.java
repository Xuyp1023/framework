package com.betterjr.modules.wechat.util;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.modules.wechat.aes.AesException;
import com.betterjr.modules.wechat.aes.SHA1;
import com.betterjr.modules.wechat.aes.WXBizMsgCrypt;
import com.betterjr.modules.wechat.data.ApiResult;
import com.betterjr.modules.wechat.data.EventType;
import com.betterjr.modules.wechat.data.MPAccount;
import com.betterjr.modules.wechat.data.MessageType;
import com.betterjr.modules.wechat.data.api.AccessToken;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.data.event.CustomServiceEvent;
import com.betterjr.modules.wechat.data.event.LocationEvent;
import com.betterjr.modules.wechat.data.event.MenuEvent;
import com.betterjr.modules.wechat.data.event.ScanCodeEvent;
import com.betterjr.modules.wechat.data.event.ScanEvent;
import com.betterjr.modules.wechat.data.event.SendLocationInfoEvent;
import com.betterjr.modules.wechat.data.event.SendPhotosEvent;
import com.betterjr.modules.wechat.data.message.BasicMsg;
import com.betterjr.modules.wechat.data.message.CustomerServiceMsg;
import com.betterjr.modules.wechat.data.message.ImageMsg;
import com.betterjr.modules.wechat.data.message.LinkMsg;
import com.betterjr.modules.wechat.data.message.LocationMsg;
import com.betterjr.modules.wechat.data.message.MusicMsg;
import com.betterjr.modules.wechat.data.message.NewsMsg;
import com.betterjr.modules.wechat.data.message.TextMsg;
import com.betterjr.modules.wechat.data.message.VideoMsg;
import com.betterjr.modules.wechat.data.message.VoiceMsg;
import com.betterjr.modules.wechat.data.push.SentAllJobEvent;
import com.betterjr.modules.wechat.data.push.SentTmlJobEvent;

/**
 * 微信消息内核
 *
 * @author zhoucy
 */

public class WechatKernel {

    static String user_auth2_info = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private static final Logger log = LoggerFactory.getLogger(WechatKernel.class);

    // XML解析准备
    private static SAXParserFactory factory = SAXParserFactory.newInstance();
    private SAXParser xmlParser;

    private final MessageHandler msgHandler = new MessageHandler();

    // 消息处理器
    private WechatHandler handler;
    // Request参数
    private Map<String, String> params;
    // 公众号信息
    private MPAccount mpAct;

    public WechatKernel() {
        try {
            xmlParser = factory.newSAXParser();
        }
        catch (final Exception e) {
            throw new BytterValidException(399901, "初始化SAXParserFactory出现异常", e);
        }
    }

    public WechatKernel(final MPAccount mpAct, final WechatHandler handler, final Map<String, String> anParams) {
        this();
        this.mpAct = mpAct;
        this.handler = handler;
        setParams(anParams);
    }

    /**
     * 设置微信服务器请求参数
     *
     * @param params
     *            请求参数
     */
    public void setParams(final Map<String, String> params) {
        this.params = params;
        // if (log.isDebugEnabled()) {
        final Set<Entry<String, String>> es = params.entrySet();
        log.info("微信服务器请求参数列表.");
        for (final Entry<String, String> e : es) {
            log.info(e.getKey() + " = " + e.getValue());
        }
        // }
    }

    /**
     * 设置公众号信息
     *
     * @param mpAct
     *            公众号信息
     */
    public void setMpAct(final MPAccount mpAct) {
        this.mpAct = mpAct;
    }

    /**
     * 设置微信消息处理器
     *
     * @param handler
     *            消息处理器
     */
    public void setWechatHandler(final WechatHandler handler) {
        this.handler = handler;
    }

    /**
     * 获取参数值
     *
     * @param param
     *            参数名
     * @return 参数值
     */
    protected String get(final String param) {

        return params.get(param);
    }

    /**
     * 微信服务器校验
     *
     * @return 微信服务器随机字符串
     */
    public String check() {
        final String sign = get("signature");
        final String ts = get("timestamp");
        final String nonce = get("nonce");

        if (sign == null || sign.length() > 128 || ts == null || ts.length() > 128 || nonce == null || nonce.length() > 128) {
            log.warn("接入微信服务器认证的加密参数为空或是长度大于128.");
            return "error";
        }

        try {
            final String validsign = SHA1.calculate(mpAct.getToken(), ts, nonce);
            if (log.isDebugEnabled()) {
                log.debug("接入微信服务器认证: %b. 加密字符串: %s", StringUtils.equals(validsign, sign), validsign);
            }

            if (sign.equals(validsign)) {
                return get("echostr");
            }

            return "error";
        }
        catch (final AesException e) {
            throw new BytterValidException(39901, "校验服务器认证出现异常", e);
        }
    }

    /**
     * 与微信服务器交互处理过程
     *
     * @param is
     *            微信服务器推送消息
     * @return 响应消息
     */
    // TODO 是否考虑添加重复消息过滤功能
    public String handle(final InputStream is) {
        final String encrypt = get("encrypt_type");
        WXBizMsgCrypt pc = null;
        BasicMsg msg = null;
        String respmsg = "success";
        // 密文模式
        if (encrypt != null && "aes".equals(encrypt) && mpAct.getAESKey() != null) {
            try {
                pc = new WXBizMsgCrypt(mpAct.getToken(), mpAct.getAESKey(), mpAct.getAppId());

                final String ts = get("timestamp");
                final String nonce = get("nonce");
                final String msgsign = get("msg_signature");

                final String decmsg = pc.decryptMsg(msgsign, ts, nonce, is);
                xmlParser.parse(StreamTool.toStream(decmsg), msgHandler);
                msg = handleMsg();
                respmsg = pc.encryptMsg(responseXML(msg), ts, nonce);
            }
            catch (final Exception e) {
                throw new BytterValidException(399901, "使用密文模式出现异常", e);
            }
        }
        // 明文模式
        else {
            try {
                xmlParser.parse(is, msgHandler);
            }
            catch (final Exception e) {
                throw new BytterValidException(399901, "明文模式下解析消息出现异常", e);
            }
            msg = handleMsg();
            respmsg = responseXML(msg);
        }

        return respmsg;
    }

    /**
     * 微信消息处理
     *
     * @return 回复消息
     */
    protected BasicMsg handleMsg() {
        final String msgtype = msgHandler.getValues().get("msgType");
        if ("event".equals(msgtype)) {
            return handleEventMsg();
        }
        else {
            return handleNormalMsg();
        }
    }

    /**
     * 处理普通消息
     *
     * @return 回复消息
     */
    protected BasicMsg handleNormalMsg() {
        BasicMsg msg = null;
        MessageType mt;
        try {
            mt = MessageType.valueOf(msgHandler.getValues().get("msgType"));
        }
        catch (final Exception e) {
            log.error("处理微信普通消息时发现新的消息类型,请查阅官方更新文档.");
            mt = MessageType.def;
        }
        switch (mt) {
        case text:
            final TextMsg tm = new TextMsg(msgHandler.getValues());
            msg = handler.text(tm);
            break;
        case image:
            final ImageMsg im = new ImageMsg(msgHandler.getValues());
            msg = handler.image(im);
            break;
        case voice:
            final VoiceMsg vom = new VoiceMsg(msgHandler.getValues());
            msg = handler.voice(vom);
            break;
        case video:
            final VideoMsg vim = new VideoMsg(msgHandler.getValues());
            msg = handler.video(vim);
            break;
        case shortvideo:
            final VideoMsg shortvim = new VideoMsg(msgHandler.getValues());
            msg = handler.shortVideo(shortvim);
            break;
        case location:
            final LocationMsg locm = new LocationMsg(msgHandler.getValues());
            msg = handler.location(locm);
            break;
        case link:
            final LinkMsg lm = new LinkMsg(msgHandler.getValues());
            msg = handler.link(lm);
            break;
        default:
            final BasicMsg bm = new BasicMsg(msgHandler.getValues());
            msg = handler.defMsg(bm);
            break;
        }
        return msg;
    }

    /**
     * 处理事件消息
     *
     * @return 回复消息
     */
    protected BasicMsg handleEventMsg() {
        BasicMsg msg = null;
        EventType et;
        try {
            et = EventType.valueOf(msgHandler.getValues().get("event"));
        }
        catch (final Exception e) {
            log.error("处理微信事件消息时发现新的事件类型,请查阅官方更新文档.");
            et = EventType.def;
        }
        switch (et) {
        case subscribe:
            final BasicEvent sube = new BasicEvent(msgHandler.getValues());
            msg = handler.eSub(sube);
            break;
        case unsubscribe:
            final BasicEvent unsube = new BasicEvent(msgHandler.getValues());
            handler.eUnSub(unsube);
            break;
        case SCAN:
            final ScanEvent se = new ScanEvent(msgHandler.getValues());
            msg = handler.eScan(se);
            break;
        case LOCATION:
            final LocationEvent le = new LocationEvent(msgHandler.getValues());
            handler.eLocation(le);
            break;
        case CLICK:
            final MenuEvent cme = new MenuEvent(msgHandler.getValues());
            msg = handler.eClick(cme);
            break;
        case VIEW:
            final MenuEvent vme = new MenuEvent(msgHandler.getValues());
            handler.eView(vme);
            break;
        case scancode_push:
            final ScanCodeEvent sce = new ScanCodeEvent(msgHandler.getValues());
            msg = handler.eScanCodePush(sce);
            break;
        case scancode_waitmsg:
            final ScanCodeEvent scemsg = new ScanCodeEvent(msgHandler.getValues());
            msg = handler.eScanCodeWait(scemsg);
            break;
        case pic_sysphoto:
            final SendPhotosEvent spesys = new SendPhotosEvent(msgHandler.getValues());
            msg = handler.ePicSysPhoto(spesys);
            break;
        case pic_photo_or_album:
            final SendPhotosEvent spealb = new SendPhotosEvent(msgHandler.getValues());
            msg = handler.ePicPhotoOrAlbum(spealb);
            break;
        case pic_weixin:
            final SendPhotosEvent spewx = new SendPhotosEvent(msgHandler.getValues());
            msg = handler.ePicWeixin(spewx);
            break;
        case location_select:
            final SendLocationInfoEvent lse = new SendLocationInfoEvent(msgHandler.getValues());
            msg = handler.eLocationSelect(lse);
            break;
        case TEMPLATESENDJOBFINISH:
            final SentTmlJobEvent stje = new SentTmlJobEvent(msgHandler.getValues());
            handler.eSentTmplJobFinish(stje);
            break;
        case MASSSENDJOBFINISH:
            final SentAllJobEvent saje = new SentAllJobEvent(msgHandler.getValues());
            handler.eSentAllJobFinish(saje);
            break;
        case kf_create_session:
            final CustomServiceEvent sce_create = new CustomServiceEvent(msgHandler.getValues());
            handler.eCreateKfSession(sce_create);
            break;
        case kf_close_session:
            final CustomServiceEvent sce_close = new CustomServiceEvent(msgHandler.getValues());
            handler.eCloseKfSession(sce_close);
            break;
        case kf_switch_session:
            final CustomServiceEvent sce_switch = new CustomServiceEvent(msgHandler.getValues());
            handler.eSwitchKfSession(sce_switch);
            break;
        default:
            final BasicEvent be = new BasicEvent(msgHandler.getValues());
            msg = handler.defEvent(be);
            break;
        }
        return msg;
    }

    /**
     * 输出回复消息
     *
     * @param msg
     *            回复消息数据
     * @return XML消息
     */
    protected String responseXML(final BasicMsg msg) {
        String respmsg = "success";
        if (msg == null || StringUtils.isBlank(msg.getMsgType())) {
            return respmsg;
        }

        // 交换 fromUser 和 toUser
        final String fromUser = msg.getFromUserName();
        final String toUser = msg.getToUserName();
        msg.setFromUserName(toUser);
        msg.setToUserName(fromUser);

        final MessageType mt = MessageType.valueOf(msg.getMsgType());

        switch (mt) {
        case text:
            respmsg = XmlMsgBuilder.create().text((TextMsg) msg).build();
            break;
        case image:
            respmsg = XmlMsgBuilder.create().image((ImageMsg) msg).build();
            break;
        case voice:
            respmsg = XmlMsgBuilder.create().voice((VoiceMsg) msg).build();
            break;
        case music:
            respmsg = XmlMsgBuilder.create().music((MusicMsg) msg).build();
            break;
        case video:
            respmsg = XmlMsgBuilder.create().video((VideoMsg) msg).build();
            break;
        case news:
            respmsg = XmlMsgBuilder.create().news((NewsMsg) msg).build();
            break;
        case transfer_customer_service:
            respmsg = XmlMsgBuilder.create().transferCustomerService((CustomerServiceMsg) msg).build();
            break;
        default:
            break;
        }
        return respmsg;
    }

    public AccessToken findUserAuth2(final String anCode) {
        if (StringUtils.isNotBlank(anCode)) {
            final String apiUrl = String.format(user_auth2_info, this.mpAct.getAppId(), this.mpAct.getAppSecret(), anCode);
            AccessToken at = null;
            ApiResult ar = null;
            ar = ApiResult.create(HttpTool.get(apiUrl));
            if (ar.isSuccess()) {

                at = JsonMapper.buildNonEmptyMapper().fromJson(ar.getJson(), AccessToken.class);
            }
            log.info("this findUserAuth2 :" + ar);
            if (at != null && at.isAvailable()) {
                log.info("auth2 is " + at);
                return at;
            }

            log.error("Get mp[%s]access_token failed. There try %d items.", anCode, +1);
        }
        return new AccessToken();
    }

}
