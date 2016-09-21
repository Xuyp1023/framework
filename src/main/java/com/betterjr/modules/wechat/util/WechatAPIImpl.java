package com.betterjr.modules.wechat.util;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.lock.SimpleLock;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.JedisUtils;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.modules.wechat.constants.WechatConstants;
import com.betterjr.modules.wechat.data.ApiResult;
import com.betterjr.modules.wechat.data.MPAccount;
import com.betterjr.modules.wechat.data.api.AccessToken;
import com.betterjr.modules.wechat.data.api.FollowList;
import com.betterjr.modules.wechat.data.api.Follower;
import com.betterjr.modules.wechat.data.api.Follower2;
import com.betterjr.modules.wechat.data.api.Groups;
import com.betterjr.modules.wechat.data.api.JSTicket;
import com.betterjr.modules.wechat.data.api.Media;
import com.betterjr.modules.wechat.data.api.Menu;
import com.betterjr.modules.wechat.data.api.QRTicket;
import com.betterjr.modules.wechat.data.api.WechatPushTempField;
import com.betterjr.modules.wechat.data.api.WechatPushTemplate;
import com.betterjr.modules.wechat.data.faceapi.WechatAPI;
import com.betterjr.modules.wechat.data.message.MessageTemplate;

/**
 * 微信公众平台所有接口实现
 *
 * @author zhoucy
 */
public class WechatAPIImpl implements WechatAPI {

    private static final Logger log = LoggerFactory.getLogger(WechatAPIImpl.class);

    static int RETRY_COUNT = 3;
    private static JsonMapper jsonMapper = JsonMapper.buildNonNullMapper();

    private final MPAccount mpAct;

    public WechatAPIImpl(final MPAccount mpAct) {
        this.mpAct = mpAct;
    }

    /**
     * WechatAPI 实现方法
     *
     * @param mpAct
     *            微信公众号信息{@link MPAccount}
     * @return 对应的API
     */
    public static WechatAPIImpl create(final MPAccount mpAct) {
        return new WechatAPIImpl(mpAct);
    }

    private String mergeCgiBinUrl(final String url, final Object... values) {
        if (Collections3.isEmpty(values) == false) {

            return cgi_bin + String.format(url, values);
        }

        return cgi_bin + url;
    }

    /**
     * 强制刷新微信服务凭证
     */
    private synchronized void refreshAccessToken() {
        AccessToken at = JedisUtils.getObject(WechatConstants.wechatAccessTokenPrefix + mpAct.getMpId());
        if (at != null && at.isAvailable()) { // 如果有效，直接返回 否则去微信上取
            return;
        }
        final String url = mergeCgiBinUrl(get_at, mpAct.getAppId(), mpAct.getAppSecret());
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                at = jsonMapper.fromJson(ar.getJson(), AccessToken.class);
                //atmcMap.put(mpAct.getMpId(), at);
                if (at != null) {
                    JedisUtils.setObject(WechatConstants.wechatAccessTokenPrefix + mpAct.getMpId(), at, (int)at.getOrginExpireSec());
                }
            }

            if (at != null && at.isAvailable()) {
                return;
            }

            log.error("Get mp[%s]access_token failed. There try %d items.", mpAct.getMpId(), i + 1);

        }

        throw new BytterValidException(ar.getJson());
    }

    private synchronized void refreshJSTicket() {
        JSTicket jst = JedisUtils.getObject(WechatConstants.wechatJSTicketPrefix + mpAct.getMpId());
        if (jst != null && jst.isAvailable()) {
            return;
        }
        final String url = mergeCgiBinUrl(js_ticket + getAccessToken());
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                jst = jsonMapper.fromJson(ar.getJson(), JSTicket.class);
                if (jst != null) {
                    JedisUtils.setObject(WechatConstants.wechatJSTicketPrefix + mpAct.getMpId(), jst,
                            (int) jst.getOrginExpireSec());
                }
            }
            if (jst != null && jst.isAvailable()) {
                return;
            }

            log.error("Get mp[%s] JSSDK ticket failed. There try %d items.", mpAct.getMpId(), i + 1);

        }

        throw new BytterValidException(ar.getJson());
    }

    public void invalidAccessToken() {
        JedisUtils.delObject(WechatConstants.wechatAccessTokenPrefix + mpAct.getMpId());
    }

    @Override
    public String getAccessToken() {
        AccessToken at = null;
        if (StringUtils.isBlank(mpAct.getMpId()) == false) {
            at = JedisUtils.getObject(WechatConstants.wechatAccessTokenPrefix + mpAct.getMpId());
        }
        if (at == null || !at.isAvailable()) {
            // ++ 分布式加锁
            new SimpleLock(WechatConstants.wechatAccessTokenLockPrefix + mpAct.getMpId()).wrap(this::refreshAccessToken);
            at = JedisUtils.getObject(WechatConstants.wechatAccessTokenPrefix + mpAct.getMpId());
        }
        return at.getAccessToken();
    }

    @Override
    public List<String> getServerIps() {
        final String url = mergeCgiBinUrl(cb_ips + getAccessToken());
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                return JsonMapper.jacksonToCollection(jsonMapper.toJson(ar.get("ip_list")), String.class);
            }

            log.error("Get mp[%s] server ips failed. There try %d items.", mpAct.getMpId(), i + 1);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public String getShortUrl(final String longUrl) {
        final String url = mergeCgiBinUrl(short_url + getAccessToken());
        final String data = "{\"action\":\"long2short\",\"long_url\":\"" + longUrl + "\"}";
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return String.valueOf(ar.get("short_url"));
            }

            log.error("Create mp[%s] short url failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    public void invalidJSTicket() {
        JedisUtils.delObject(WechatConstants.wechatJSTicketPrefix + mpAct.getMpId());
    }

    @Override
    public String getJSTicket() {
        JSTicket jst = null;
        if (StringUtils.isBlank(mpAct.getMpId()) == false) {
            jst = JedisUtils.getObject(WechatConstants.wechatJSTicketPrefix + mpAct.getMpId());
        }
        if (jst == null || !jst.isAvailable()) {
            // ++ 分布式加锁
            new SimpleLock(WechatConstants.wechatJSTicketLockPrefix + mpAct.getMpId()).wrap(this::refreshJSTicket);
            jst = JedisUtils.getObject(WechatConstants.wechatJSTicketPrefix + mpAct.getMpId());
        }
        return jst.getTicket();
    }

    @Override
    public List<Menu> getMenu() {
        final String url = mergeCgiBinUrl(query_menu + getAccessToken());
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                final Map<String, Object> button = jsonMapper.fromJson(jsonMapper.toJson(ar.get("menu")), Map.class);
                return JsonMapper.jacksonToCollection(jsonMapper.toJson(button.get("button")), Menu.class);
            }

            // 菜单为空
            if (ar.getErrCode().intValue() == 46003) {
                return null;
            }

            log.error("Get mp[%s] custom menu failed. There try %d items.", mpAct.getAppId(), i + 1);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public boolean createMenu(final Menu... menu) {
        final String url = mergeCgiBinUrl(create_menu + getAccessToken());
        final Map<String, Object> body = new HashMap<String, Object>();
        body.put("button", menu);
        final String data = jsonMapper.toJson(body);
        log.info("create menu data :" + data);
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return true;
            }

            log.error("Create mp[%s] custom menu failed. There try %d items.", mpAct.getAppId(), i + 1);

        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public boolean delMenu() {
        final String url = mergeCgiBinUrl(del_menu + getAccessToken());
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                return true;
            }

            log.error("Delete mp[%s] custom menu failed. There try %d items.", mpAct.getMpId(), i + 1);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public Media upMedia(final String type, final File media) {
        final String url = mergeCgiBinUrl(upload_media, getAccessToken(), type);
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.upload(url, media));
            if (ar.isSuccess()) {
                return jsonMapper.fromJson(ar.getJson(), Media.class);
            }

            log.error("Upload mp[%s] media failed. There try %d items.", mpAct.getMpId(), i + 1);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public File dlMedia(final String mediaId) {
        final String url = mergeCgiBinUrl(get_media, getAccessToken(), mediaId);
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            final Object tmp = HttpTool.download(url);
            if (tmp instanceof File) {
                return (File) tmp;
            }

            ar = ApiResult.create((String) tmp);
            log.error("Download mp[%s] media failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public int createGroup(final String name) {
        final String url = mergeCgiBinUrl(create_groups + getAccessToken());
        final String data = "{\"group\":{\"name\":\"" + name + "\"}}";
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                final Groups g = jsonMapper.fromJson(jsonMapper.toJson(ar.get("group")), Groups.class);
                return g.getId();
            }

            log.error("Create mp[%s] group name[%s] failed. There try %d items.", mpAct.getMpId(), name, i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public List<Groups> getGroups() {
        final String url = mergeCgiBinUrl(get_groups + getAccessToken());
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                return JsonMapper.jacksonToCollection(jsonMapper.toJson(ar.get("groups")), Groups.class);
            }

            log.error("Get mp[%s] groups failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public int getGroup(final String openId) {
        final String url = mergeCgiBinUrl(get_member_group + getAccessToken());
        final String data = "{\"openid\":\"" + openId + "\"}";
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return Integer.parseInt(String.valueOf(ar.get("groupid")));
            }

            log.error("Get mp[%s] openId[%s] groups failed. There try %d items.", mpAct.getMpId(), openId, i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public boolean renGroups(final int id, final String name) {
        final String url = mergeCgiBinUrl(update_group + getAccessToken());
        final String data = "{\"group\":{\"id\":" + id + ",\"name\":\"" + name + "\"}}";
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return true;
            }

            log.error("Rename mp[%s] groups[%d-%s] failed. There try %d items.", mpAct.getMpId(), id, name, i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public boolean move2Group(final String openId, final int groupId) {
        final String url = mergeCgiBinUrl(update_member_group + getAccessToken());
        final String data = "{\"openid\":\"" + openId + "\",\"to_groupid\":" + groupId + "}";
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return true;
            }

            log.error("Move mp[%s] openId[%s] to groups[%d] failed. There try %d items.", mpAct.getMpId(), openId, groupId, i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public boolean batchMove2Group(final Collection<String> openIds, final int groupId) {
        final String url = mergeCgiBinUrl(update_members_group + getAccessToken());
        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("openid_list", jsonMapper.toJson(openIds));
        data.put("to_groupid", groupId);
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, jsonMapper.toJson(data)));
            if (ar.isSuccess()) {
                return true;
            }

            log.error("Move mp[%s] openIds to groups[%d] failed. There try %d items.", mpAct.getMpId(), groupId, i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public boolean delGroup(final int id) {
        final String url = mergeCgiBinUrl(delete_groups + getAccessToken());
        final String data = "{\"group\":{\"id\":" + id + "}}";
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return true;
            }

            log.error("Delete mp[%s] groups[%d] failed. There try %d items.", mpAct.getMpId(), id, i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public QRTicket createQR(final Object sceneId, final int expireSeconds) {
        final String url = mergeCgiBinUrl(create_qrcode + getAccessToken());
        ApiResult ar = null;
        final Map data = new HashMap();
        Map scene;
        // 临时二维码
        if (expireSeconds > 0) {
            data.put("action_name", "QR_SCENE");
            data.put("expire_seconds", expireSeconds);

            scene = QueryTermBuilder.buildSingle("scene_id", Integer.parseInt(sceneId.toString()));
        }
        // 永久二维码
        else if (sceneId instanceof Number) {
            data.put("action_name", "QR_LIMIT_SCENE");
            scene = QueryTermBuilder.buildSingle("scene_id", Integer.parseInt(sceneId.toString()));
        }
        // 永久字符串二维码
        else {
            data.put("action_name", "QR_LIMIT_STR_SCENE");
            scene = QueryTermBuilder.buildSingle("scene_str", sceneId.toString());
        }
        data.put("action_info", QueryTermBuilder.buildSingle("scene", scene));
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, jsonMapper.toJson(data)));
            if (ar.isSuccess()) {
                return jsonMapper.fromJson(jsonMapper.toJson(ar.getContent()), QRTicket.class);
            }

            log.info("Create mp[%s] scene[%s] qrcode failed. There try %d items.", mpAct.getMpId(), data.get("action_name"), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public File getQR(final String ticket) {
        final String url = mergeCgiBinUrl(show_qrcode + ticket);
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            final Object tmp = HttpTool.get(url);
            if (tmp instanceof File) {
                return (File) tmp;
            }

            ar = ApiResult.create((String) tmp);
            log.error("Download mp[%s] qrcode image failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public boolean updateRemark(final String openId, final String remark) {
        final String url = mergeCgiBinUrl(user_remark + getAccessToken());
        ApiResult ar = null;
        final String data = "{\"openid\":\"" + openId + "\",\"remark\":\"" + remark + "\"}";
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return true;
            }

            log.error("Update mp[%s] user[%s] remark[%s] failed. There try %d items.", mpAct.getMpId(), openId, remark, i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public FollowList getFollowerList(final String nextOpenId) {
        final String url = mergeCgiBinUrl(user_list, getAccessToken(), StringUtils.defaultString(nextOpenId));
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                final FollowList fl = jsonMapper.fromJson(ar.getJson(), FollowList.class);
                final Map<String, Object> openid = (Map<String, Object>) ar.get("data");
                fl.setOpenIds(JsonMapper.jacksonToCollection(jsonMapper.toJson(openid.get("openid")), String.class));
                return fl;
            }

            log.info("Get mp[%s] follow list failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public Follower getFollower(final String openId, final String lang) {
        final String url = mergeCgiBinUrl(user_info, getAccessToken(), openId, StringUtils.defaultIfBlank(lang, "zh_CN"));
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                return jsonMapper.fromJson(ar.getJson(), Follower.class);
            }

            log.error("Get mp[%s] follower[%s] information failed. There try %d items.", mpAct.getMpId(), openId, i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public List<Follower> getFollowers(final Collection<Follower2> users) {
        final String url = mergeCgiBinUrl(batch_user_info + getAccessToken());
        ApiResult ar = null;
        final String data = jsonMapper.toJson(QueryTermBuilder.buildSingle("user_list", users));
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return JsonMapper.jacksonToCollection(jsonMapper.toJson(ar.get("user_info_list")), Follower.class);
            }

            log.error("Get mp[%s] followers information failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public boolean setIndustry(final int id1, final int id2) {
        final String url = mergeCgiBinUrl(set_industry + getAccessToken());
        ApiResult ar = null;
        final String data = "{\"industry_id1\":\"" + id1 + "\",\"industry_id2\":\"" + id2 + "\"}";
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return true;
            }

            log.error("Set mp[%s] template industry failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public String getTemplateId(final String tmlShortId) {
        final String url = mergeCgiBinUrl(add_template + getAccessToken());
        ApiResult ar = null;
        final String data = "{\"template_id_short\":\"" + tmlShortId + "\"}";
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(url, data));
            if (ar.isSuccess()) {
                return String.valueOf(ar.get("template_id"));
            }

            log.error("Get mp[%s] template id failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public long sendTemplateMsg(final String openId, final String tmlId, final String topColor, final String url,  final WechatPushTempField... tmls) {

        return sendTemplateMsg(openId, tmlId, topColor, url, Arrays.asList(tmls));
    }

    /**
     * 发送模板消息
     * @param anOpenId
     * @param anWechatPushTemp
     * @return
     */
    public long sendTemplateMessage(final String anOpenId, final WechatPushTemplate anWechatPushTemp){
        if (anWechatPushTemp != null){

            return sendTemplateMsg(anOpenId, anWechatPushTemp.getTempId(), anWechatPushTemp.getTempId(), anWechatPushTemp.getInvokeUrl(), anWechatPushTemp.getFields());
        }

        return -1;
    }

    public long sendTemplateMsg(final String openId, final String tmlId, final String topColor, final String url, final Collection<WechatPushTempField> anTmls) {
        final String apiurl = mergeCgiBinUrl(send_template + getAccessToken());
        ApiResult ar = null;
        final String data = JsonMsgBuilder.create().template(openId, tmlId, topColor, url, anTmls).build();
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.post(apiurl, data));
            if (ar.isSuccess()) {
                return Long.valueOf(ar.get("msgid").toString());
            }

            log.error("Send mp[%s] template message failed. There try %d items.", mpAct.getMpId(), i);
        }

        throw new BytterValidException(ar.getJson());
    }

    @Override
    public List<MessageTemplate> findTemplateList() {
        final String url = mergeCgiBinUrl(list_template + getAccessToken());
        ApiResult ar = null;
        for (int i = 0; i < RETRY_COUNT; i++) {
            ar = ApiResult.create(HttpTool.get(url));
            if (ar.isSuccess()) {
                // Map<String, Object> button = jsonMapper.fromJson(jsonMapper.toJson(ar.get("template_list")), Map.class);
                return JsonMapper.jacksonToCollection(jsonMapper.toJson(ar.get("template_list")), MessageTemplate.class);
            }

            log.error("Get mp[%s] custom template failed. There try %d items.", mpAct.getAppId(), i + 1);
        }

        throw new BytterValidException(ar.getJson());
    }

}
