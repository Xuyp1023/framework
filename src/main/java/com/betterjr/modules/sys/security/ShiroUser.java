package com.betterjr.modules.sys.security;

import java.io.Serializable;

import com.betterjr.common.entity.UserType;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.sys.entity.WorkUserInfo;

 
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
    private Object data;
    private boolean mobileLogin;

    public Object getData() {
        return data;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public UserType getUserType() {
        return userType;
    }

    public String[] fingUserRule() {
        String[] arrUser = UserType.findUserRule(userType);
        if (BetterStringUtils.isNotBlank(this.user.getRuleList())) {
            String[] arrRule = BetterStringUtils.split(this.user.getRuleList(), ";|,");
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
    public ShiroUser(UserType anUserType, Long id, String loginName, WorkUserInfo anUser, boolean anMobileLogin, Object anData) {
        this.mobileLogin = anMobileLogin;
        this.userType = anUserType;
        this.id = id;
        this.loginName = loginName;
        this.user = anUser;
        this.loginTime = System.currentTimeMillis();
        this.data = anData;
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

    public void setIpAddress(String ipAddress) {
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
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return loginName;
    }
}