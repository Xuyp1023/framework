package com.betterjr.modules.sys.security;

import java.io.Serializable;
import java.util.List;

import com.betterjr.common.data.PlatformBaseRuleType;
import com.betterjr.common.data.SimpleDataEntity;
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
    private List<PlatformBaseRuleType> innerRules = null;
    private List<SimpleDataEntity> userPassData = null;

    public void setInnerRules(List<PlatformBaseRuleType> anInnerRuleList) {
        
        this.innerRules = anInnerRuleList;
    }

    public List<PlatformBaseRuleType> getInnerRules() {
        
        return this.innerRules;
    }

    private static boolean userRuleCheck(ShiroUser anUser, PlatformBaseRuleType anBaseRule){
        if (anUser != null){
            return anUser.innerRules.contains( anBaseRule );
         }
         return false;
    }
    
    public static boolean coreUser(ShiroUser anUser){
        
        return userRuleCheck(anUser, PlatformBaseRuleType.CORE_USER);
    }
    
    public static boolean sellerUser(ShiroUser anUser){
        
        return userRuleCheck(anUser, PlatformBaseRuleType.SELLER_USER);
    }
    
    public static boolean supplierUser(ShiroUser anUser){
        
        return userRuleCheck(anUser, PlatformBaseRuleType.SUPPLIER_USER);
    }
    
    public static boolean platformUser(ShiroUser anUser){
        
        return userRuleCheck(anUser, PlatformBaseRuleType.PLATFORM_USER);
    }
    
    public static boolean factorUser(ShiroUser anUser){
         
        return userRuleCheck(anUser, PlatformBaseRuleType.FACTOR_USER);
    }
    public Object getData() {
        return data;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public UserType getUserType() {
        return userType;
    }

    public boolean checkPass(String anType, String anPass) {
        if (Collections3.isEmpty(userPassData) || BetterStringUtils.isBlank(anType) || BetterStringUtils.isBlank(anPass)) {
            
            return false;
        }
        
        for (SimpleDataEntity sde : userPassData) {
            if (anType.equals(sde.getThree())) {
                String passwd = SystemAuthorizingRealm.findEncrypt(anPass, sde.getValue());
                
                return passwd.equals(sde.getName());
            }
        }
        
        return false;
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
    public ShiroUser(UserType anUserType, Long id, String loginName, WorkUserInfo anUser, String anInnerRuleList, boolean anMobileLogin,
            Object anData, List<SimpleDataEntity> anUserPassData) {
        this.mobileLogin = anMobileLogin;
        this.userType = anUserType;
        this.id = id;
        this.loginName = loginName;
        this.user = anUser;
        this.loginTime = System.currentTimeMillis();
        this.data = anData;
        this.innerRules = PlatformBaseRuleType.checkList(anInnerRuleList);
        this.userPassData = anUserPassData;
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
        StringBuilder sb = new StringBuilder();
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