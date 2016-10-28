package com.betterjr.modules.sys.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.data.UserType;
import com.betterjr.common.data.WorkUserInfo;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.cert.entity.CustCertInfo;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {

    private static final long serialVersionUID = 8785516740145092469L;
    private final Long id;
    private final String loginName;
    private String ipAddress;
    private final WorkUserInfo user;
    private final UserType userType;
    private final long loginTime;
    private final Object data;
    private final boolean mobileLogin;
    private final CustCertInfo cretInfo;
    private final Map<String, Object> param;
    private List<PlatformBaseRuleType> innerRules = null;
    private List<SimpleDataEntity> userPassData = null;

    public void addParam(final String anKey, final Object anValue) {
        param.put(anKey, anValue);
    }

    public <T> T getParam(final String anKey) {
        return (T)param.get(anKey);
    }

    public void setInnerRules(final List<PlatformBaseRuleType> anInnerRuleList) {

        this.innerRules = anInnerRuleList;
    }

    public List<PlatformBaseRuleType> getInnerRules() {

        return this.innerRules;
    }

    private static boolean userRuleCheck(final ShiroUser anUser, final PlatformBaseRuleType anBaseRule) {
        if (anUser != null) {
            return anUser.innerRules.contains(anBaseRule);
        }
        return false;
    }

    public static boolean coreUser(final ShiroUser anUser) {

        return userRuleCheck(anUser, PlatformBaseRuleType.CORE_USER);
    }

    public static boolean sellerUser(final ShiroUser anUser) {

        return userRuleCheck(anUser, PlatformBaseRuleType.SELLER_USER);
    }

    public static boolean supplierUser(final ShiroUser anUser) {

        return userRuleCheck(anUser, PlatformBaseRuleType.SUPPLIER_USER);
    }

    public static boolean platformUser(final ShiroUser anUser) {

        return userRuleCheck(anUser, PlatformBaseRuleType.PLATFORM_USER);
    }

    public static boolean factorUser(final ShiroUser anUser) {

        return userRuleCheck(anUser, PlatformBaseRuleType.FACTOR_USER);
    }

    public <T> T getData() {
        return (T)data;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public UserType getUserType() {
        return userType;
    }

    public boolean checkPass(final String anType, final String anPass) {
        if (Collections3.isEmpty(userPassData) || BetterStringUtils.isBlank(anType) || BetterStringUtils.isBlank(anPass)) {

            return false;
        }

        for (final SimpleDataEntity sde : userPassData) {
            if (anType.equals(sde.getThree())) {
                final String passwd = SystemAuthorizingRealm.findEncrypt(anPass, sde.getValue());

                return passwd.equals(sde.getName());
            }
        }

        return false;
    }

    public String[] fingUserRule() {
        String[] arrUser = UserType.findUserRule(userType);
        if (BetterStringUtils.isNotBlank(this.user.getRuleList())) {
            final String[] arrRule = BetterStringUtils.split(this.user.getRuleList(), ";|,");
            arrUser = Collections3.mergeArray(arrUser, arrRule);
        }

        return arrUser;
    }

    /**
     * 构造函数
     *
     * @param id
     * @param loginName
     * @param email
     * @param createTime
     * @param status
     */
    public ShiroUser(final UserType anUserType, final Long id, final String loginName, final WorkUserInfo anUser, final CustCertInfo anCertInfo,
            final boolean anMobileLogin, final Object anData, final List<SimpleDataEntity> anUserPassData) {
        this.mobileLogin = anMobileLogin;
        this.userType = anUserType;
        this.id = id;
        this.loginName = loginName;
        this.user = anUser;
        this.loginTime = System.currentTimeMillis();
        this.data = anData;
        this.innerRules = anUserType.equals(UserType.NONE_USER) || anCertInfo == null ? new ArrayList<>() : PlatformBaseRuleType.checkList(anCertInfo.getCertRuleList());
        this.userPassData = anUserPassData;
        this.cretInfo = anCertInfo;
        this.param = new HashMap<>();
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

    /**
     * 返回 id 的值
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 返回 loginName 的值
     *
     * @return loginName
     */
    public String getLoginName() {
        return loginName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * 返回 user 的值，根据不同的情况返回不同的对象
     *
     * @return user
     */
    public WorkUserInfo getUser() {
        return user;
    }

    /**
     * 返回证书的值
     *
     * @return
     */
    public CustCertInfo getCretInfo() {
        return cretInfo;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("innerRuleList = ").append(innerRules);
        sb.append(", id=").append(id);
        sb.append(", loginName=").append(loginName);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append(", user=").append(user);
        sb.append(", userType=").append(userType);
        sb.append(", loginTime=").append(loginTime);
        sb.append(", data=").append(data);
        sb.append(", mobileLogin=").append(mobileLogin);
        sb.append("]");
        return sb.toString();
    }
}