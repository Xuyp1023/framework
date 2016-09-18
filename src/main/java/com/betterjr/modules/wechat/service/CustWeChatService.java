package com.betterjr.modules.wechat.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.Cryptos;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.JedisUtils;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.notification.INotificationSendService;
import com.betterjr.modules.notification.NotificationModel;
import com.betterjr.modules.notification.NotificationModel.Builder;
import com.betterjr.modules.sys.service.SysConfigService;
import com.betterjr.modules.wechat.constants.WechatConstants;
import com.betterjr.modules.wechat.dao.CustWeChatInfoMapper;
import com.betterjr.modules.wechat.data.EventType;
import com.betterjr.modules.wechat.data.MPAccount;
import com.betterjr.modules.wechat.data.api.AccessToken;
import com.betterjr.modules.wechat.data.api.WechatPushTempField;
import com.betterjr.modules.wechat.data.api.WechatPushTemplate;
import com.betterjr.modules.wechat.data.event.BasicEvent;
import com.betterjr.modules.wechat.entity.CustWeChatInfo;
import com.betterjr.modules.wechat.util.WechatAPIImpl;

@Service
public class CustWeChatService extends BaseService<CustWeChatInfoMapper, CustWeChatInfo> {

    private final MPAccount mpAccount = new MPAccount();

    @Resource
    private CustOperatorService custOperatorService;

    @Reference(interfaceClass = INotificationSendService.class)
    public INotificationSendService notificationSendService;

    @Resource
    private CustAccountService accountService;

    public MPAccount getMpAccount() {
        return this.mpAccount;
    }

    @PostConstruct
    public synchronized void init() {
        // 修改为实际的公众号信息,可以在开发者栏目中查看
        final String wechatConfig = SysConfigService.getString("WeChatConfig");

        final String orginWechatConfig = Cryptos.aesDecrypt(wechatConfig);

        BTAssert.isTrue(StringUtils.isNotBlank(orginWechatConfig), "微信参数初始化失败！");

        final Map<String, Object> wechatConfigMap = JsonMapper.parserJson(orginWechatConfig);
        BTAssert.notNull(wechatConfigMap, "微信参数初始化失败！");

        final String appId = (String) wechatConfigMap.get("AppId");
        final String appSecret = (String) wechatConfigMap.get("AppSecret");
        final String token = (String) wechatConfigMap.get("Token");
        final String aesKey = (String) wechatConfigMap.get("AESKey");
        final String mpId = (String) wechatConfigMap.get("MpId");

        BTAssert.isTrue(StringUtils.isNotBlank(appId), "微信参数初始化失败！");
        BTAssert.isTrue(StringUtils.isNotBlank(appSecret), "微信参数初始化失败！");
        BTAssert.isTrue(StringUtils.isNotBlank(token), "微信参数初始化失败！");
        BTAssert.isTrue(StringUtils.isNotBlank(aesKey), "微信参数初始化失败！");
        BTAssert.isTrue(StringUtils.isNotBlank(mpId), "微信参数初始化失败！");

        mpAccount.setAppId(appId);
        mpAccount.setAppSecret(appSecret);
        mpAccount.setToken(token);
        mpAccount.setAESKey(aesKey);
        mpAccount.setMpId(mpId);
    }

    /**
     * 验证微信登录信息
     *
     * @param anToken
     */
    public CustOperatorInfo saveLogin(final AccessToken anToken, final String[] anReturn) {
        String msg = null;
        if (anToken == null || StringUtils.isBlank(anToken.getOpenId())) {
            msg = "未关注企e金服微信公众号!";
        }
        final CustWeChatInfo weChatInfo = this.selectByPrimaryKey(anToken.getOpenId());
        if (weChatInfo == null) {
            msg = "未关注企e金服微信公众号!";
        }
        else if ("2".equals(weChatInfo.getBusinStatus())) {
            msg = "该微信账户已经被冻结，不能使用!";
        }
        else if ("1".equals(weChatInfo.getBusinStatus())) {
            final CustOperatorInfo operInfo = custOperatorService.selectByPrimaryKey(weChatInfo.getOperId());
            if (operInfo == null) {
                msg = "未能获得绑定的操作员信息";
            }
            else {
                return operInfo;
            }
        }
        else {
            msg = "未知原因导致账户不能使用";
        }
        anReturn[0] = msg;
        return null;
    }

    public CustWeChatInfo saveWeChatInfo(final CustWeChatInfo anWeChatInfo) {

        return saveWeChatInfo(anWeChatInfo, null);
    }

    public List<CustWeChatInfo> findPendingWeChat() {
        final Map<String, Object> termMap = QueryTermBuilder.buildSingle("businStatus", "3");

        return queryCustWeChatPageInfo(true, 1, 10, termMap);
    }

    public List<CustWeChatInfo> queryCustWeChatPageInfo(final boolean anFirst, final int anPageNum, final int anPageSize, final Map anParams) {
        if (Collections3.isEmpty(anParams)) {
            return new ArrayList<CustWeChatInfo>(1);
        }
        else {
            return this.selectPropertyByPage(anParams, anPageNum, anPageSize, anFirst);
        }
    }

    /**
     * 扫描绑定时，保存操作员和微信账户的关系。
     *
     * @param anCustOperator
     * @param anOpenId
     * @return
     */
    public CustWeChatInfo saveBindingWeChatInfo(final CustOperatorInfo anCustOperator, final String anOpenId) {
        if (StringUtils.isNotBlank(anOpenId)) {
            final CustWeChatInfo wechatInfo = this.selectByPrimaryKey(anOpenId);
            if (wechatInfo != null) {
                wechatInfo.setOperId(anCustOperator.getId());
                wechatInfo.setOperName(anCustOperator.getName());
                wechatInfo.setOperOrg(anCustOperator.getOperOrg());
                wechatInfo.modifyValue(anCustOperator);
                this.updateByPrimaryKey(wechatInfo);

                return wechatInfo;
            }
        }

        return null;
    }

    public CustWeChatInfo saveMobileTradePass(final String anNewPasswd, final String anOkPasswd, final String anLoginPasswd,
            final CustPasswordType anPassType) {
        final CustOperatorInfo operator = UserUtils.getOperatorInfo();
        final String userKey = WechatConstants.wechatUserPrefix + operator.getId();
        final String openId = JedisUtils.get(userKey); // 取到userKey 对应的 operatorId

        BTAssert.notNull(openId, "扫描信息已过期！");

        // 保存关联关系
        final CustWeChatInfo wechatInfo = saveBindingWeChatInfo(operator, openId);

        BTAssert.notNull(wechatInfo, "微信账号绑定失败!");

        custOperatorService.saveBindingTradePassword(anPassType, anNewPasswd, anOkPasswd, anLoginPasswd);

        sendNotification(wechatInfo, operator);

        JedisUtils.delObject(userKey);
        return wechatInfo;
    }

    /**
     * @param anWechatInfo
     * @param anOperator
     */
    private void sendNotification(final CustWeChatInfo anWechatInfo, final CustOperatorInfo anOperator) {
        // 发送微信绑定结果通知
        final Long platformCustNo = Long.valueOf(Collections3.getFirst(DictUtils.getDictList("PlatformGroup")).getItemValue());
        final CustInfo platformCustomer = accountService.findCustInfo(platformCustNo);
        final CustOperatorInfo platformOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(platformCustomer.getCustNo()));

        // 发送微信绑定消息
        final Builder builder = NotificationModel.newBuilder("微信账号绑定状态通知", platformCustomer, platformOperator);
        builder.setEntity(anWechatInfo);
        builder.addReceiveOperator(anOperator.getId()); // 接收人
        notificationSendService.sendNotification(builder.build());
    }

    public CustWeChatInfo saveWeChatInfo(final CustWeChatInfo anWeChatInfo, final String anStatus) {
        BTAssert.notNull(anWeChatInfo, "的客户微信信息必须存在");
        logger.info("saveWeChatInfo from WeChatInfo:" + anWeChatInfo);
        final CustWeChatInfo weChatInfo = this.selectByPrimaryKey(anWeChatInfo.getOpenId());
        if (weChatInfo != null) {
            anWeChatInfo.modifyValue((CustOperatorInfo) null, weChatInfo);
            anWeChatInfo.setAppId(weChatInfo.getAppId());
            if (StringUtils.isNotBlank(anStatus)) {
                if ("1".equals(anStatus)) {
                    BTAssert.isTrue("1".equals(anWeChatInfo.getSubscribeStatus()), "只有在已订阅情况下，才能修改微信客户的状态为正常");
                }
                anWeChatInfo.setBusinStatus(anStatus);
            }
            this.updateByPrimaryKey(anWeChatInfo);
        }

        return anWeChatInfo;
    }

    /**
     * 根据微信订阅事件，保存微信订阅状态信息
     *
     * @param anWeChatEvent
     * @param anEventType
     * @return
     */
    public CustWeChatInfo saveWeChatInfo(final BasicEvent anWeChatEvent, final EventType anEventType) {
        BTAssert.notNull(anWeChatEvent, "的客户微信信息必须存在");

        logger.info("saveWeChatInfo anWeChatEvent is " + anWeChatEvent);
        CustWeChatInfo weChatInfo = this.selectByPrimaryKey(anWeChatEvent.getFromUserName());

        String subscribeStatus;
        if (weChatInfo == null && anEventType == EventType.subscribe) {
            subscribeStatus = "1";
            weChatInfo = new CustWeChatInfo(anWeChatEvent, subscribeStatus);
            weChatInfo.setBusinStatus("3");
            weChatInfo.initValue((CustOperatorInfo) null);
            this.insert(weChatInfo);

            return weChatInfo;
        }
        else if (EventType.unsubscribe == anEventType) {
            BTAssert.notNull(weChatInfo, "没有找到微信账户信息");
            subscribeStatus = "0";
            weChatInfo.setBusinStatus("0");
            weChatInfo.setUnSubscribeTime(BetterDateUtils.getNumDateTime());
        }
        else if (EventType.subscribe == anEventType) {
            BTAssert.notNull(weChatInfo, "没有找到微信账户信息");
            subscribeStatus = "1";
            weChatInfo.setUnSubscribeTime("");
            // 如果不是暂停的情况下，则允许修改状态，如果是设置为暂停；则不修改状态
            if ("2".equals(weChatInfo.getBusinStatus()) == false) {
                weChatInfo.setBusinStatus("3");
            }
        }
        else {

            return weChatInfo;
        }
        BTAssert.notNull(weChatInfo, "没有找到微信账户信息");
        weChatInfo.setSubscribeStatus(subscribeStatus);
        weChatInfo.modifyValue((CustOperatorInfo) null);
        this.updateByPrimaryKey(weChatInfo);

        return weChatInfo;
    }

    /**
     * 给指定操作员发送微信消息
     *
     * @param anId
     *            操作员或客户编号
     * @param anMsg
     *            需要发送的消息，是一个WechatPushTemplate类的 JSON序列化，
     * @return 返回值是long类型，根据该ID，可以获取发送结果.
     */
    public long sendWechatMessage(final Long anId, final String anMsg) {
        try {
            final WechatPushTemplate wpt = JsonMapper.buildNonEmptyMapper().readValue(anMsg, WechatPushTemplate.class);
            checkWechatPushTemplate(wpt);
            final CustWeChatInfo wechatInfo = Collections3.getFirst(this.selectByProperty("operId", anId));
            if (CustWeChatInfo.checkSendStatus(wechatInfo)) {
                final WechatAPIImpl wechatApi = WechatAPIImpl.create(mpAccount);
                return wechatApi.sendTemplateMessage(wechatInfo.getOpenId(), wpt);
            }

        }
        catch (final IOException e) {
        }
        return 0L;
    }

    /**
     * @param anWpt
     */
    private void checkWechatPushTemplate(final WechatPushTemplate anWpt) {
        BTAssert.notNull(anWpt, "消息不能为空！");

        BTAssert.isTrue(StringUtils.isNotBlank(anWpt.getTempId()), "消息模板ID不允许为空！");

        final Set<WechatPushTempField> fields = anWpt.getFields();
        for (final WechatPushTempField field : fields) {
            checkWechatPushTempField(field);
        }
    }

    /**
     * @param anField
     */
    private void checkWechatPushTempField(final WechatPushTempField anField) {
        final String[] fieldNames = new String[] { "first", "remark", "keyword1", "keyword2", "keyword3", "keyword4", "keyword5" };
        BTAssert.isTrue(Arrays.asList(fieldNames).contains(anField.getName()), anField.getName() + " 模板字段名不正确！");
    }

    /**
     * @param anFromUserName
     * @return
     */
    public CustWeChatInfo findWeChatInfo(final String anFromUserName) {
        return this.selectByPrimaryKey(anFromUserName);
    }

    /**
     * @param anId
     * @return
     */
    public boolean checkWeChatInfoByOperId(final Long anId) {
        return Collections3.isEmpty(this.selectByProperty("operId", anId));
    }

}