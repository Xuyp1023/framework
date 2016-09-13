package com.betterjr.modules.account.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.modules.account.dao.CustPassInfoMapper;
import com.betterjr.modules.account.data.CustPassRequest;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustPassInfo;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm.HashPassword;

@Service
public class CustPassService extends BaseService<CustPassInfoMapper, CustPassInfo> {

    private int passValidLimit = 1;
    
    public List<CustPassInfo> getCustPassByCustNo(Long anPassID, CustPasswordType anType) {

        return selectByProperty(QueryTermBuilder.newInstance().put("custNo", anPassID).put("passType", anType.getPassType()).build());
    }

    public CustPassInfo getOperaterPassByCustNo(Long anPassID, CustPasswordType anPassType) {

        return Collections3.getFirst(getCustPassByCustNo(anPassID, anPassType));
    }

    /**
     * 修改操作员密码
     * 
     * @param anMap
     * @return
     */
    protected boolean saveChangePassword(Long anOperId, String anPassword, CustPasswordType anPassType) {
        CustPassInfo passInfo = this.getOperaterPassByCustNo(anOperId, anPassType);

        HashPassword result = SystemAuthorizingRealm.encrypt(anPassword);
        if (passInfo == null) {
            passInfo = new CustPassInfo(anPassType, passValidLimit, anOperId, result.salt, result.password);
            this.insert(passInfo);
        }
        else {
            passInfo.modifyPassword(result.salt, result.password);
            this.updateByPrimaryKeySelective(passInfo);
        }

        return true;
    }

    public List<SimpleDataEntity> findPassAndSalt(Long anPassID, String[] anPassTypes) {
        List<SimpleDataEntity> result = new ArrayList(3);
        Map mapTerm = QueryTermBuilder.newInstance().put("custNo", anPassID).put("passType", anPassTypes).build();
        SimpleDataEntity sde;
        for (CustPassInfo passInfo : this.selectByProperty(mapTerm)) {
            sde = new SimpleDataEntity(passInfo.getPasswd(), passInfo.getPassSalt());
            sde.setThree(passInfo.getPassType());
            result.add(sde);
        }
        return result;
    }

    /**
     * 根据绑定微信账户，设置密码
     * 
     * @param anMap
     * @return
     */
    public boolean saveBindingTradePassword(CustPasswordType anLoginPassType, String anNewPasswd, String anOkPasswd, String anLoginPasswd){
        CustOperatorInfo user = UserUtils.getOperatorInfo();
        if (user == null){
            
            return false;
        }
        BTAssert.isTrue(Collections3.hasEmptyObject(anNewPasswd, anOkPasswd, anLoginPasswd) == false , "必须填写密码");
        
        CustPassInfo passInfo = this.getOperaterPassByCustNo(user.getId(), anLoginPassType);
        // 临时锁定
        if (passInfo.validLockType()) {
            throw new DisabledAccountException("操作员登录密码被锁定，请稍后再试");
        }
        
        // 判断旧密码
        String passwd = SystemAuthorizingRealm.findEncrypt(anLoginPasswd, passInfo.getPassSalt());
        if (passwd.equals(passInfo.getPasswd()) == false) {

            throw new DisabledAccountException("操作员登录密码不正确");
        }
        else if (anNewPasswd.equals(anOkPasswd) == false) {

            throw new DisabledAccountException("两次输入的密码不一致");
        }
        
        HashPassword result = SystemAuthorizingRealm.encrypt(anOkPasswd);        
        CustPassInfo newPassInfo = new CustPassInfo(anLoginPassType.exchangeTrade(), passValidLimit, user.getId(), result.salt, result.password);
        this.updateByPrimaryKeySelective(newPassInfo);

        return true;
    }
    
    public boolean savePassword(CustPasswordType anPassType, String anNewPasswd, String anOkPasswd, String anPasswd) {
        CustOperatorInfo user = UserUtils.getOperatorInfo();
        CustPassInfo passInfo = this.getOperaterPassByCustNo(user.getId(), anPassType);
        // 临时锁定
        if (passInfo.validLockType()) {
            throw new DisabledAccountException("操作员密码被锁定，请稍后再试");
        }
        // 判断旧密码
        String passwd = SystemAuthorizingRealm.findEncrypt(anPasswd, passInfo.getPassSalt());
        if (passwd.equals(passInfo.getPasswd()) == false) {

            throw new DisabledAccountException("操作员原密码不正确");
        }
        else if (anNewPasswd.equals(anOkPasswd) == false) {

            throw new DisabledAccountException("两次输入的密码不一致");
        }
        HashPassword result = SystemAuthorizingRealm.encrypt(anOkPasswd);
        CustPassInfo newPassInfo = new CustPassInfo(anPassType, passValidLimit, user.getId(), result.salt, result.password);
        this.updateByPrimaryKeySelective(newPassInfo);

        return true;
    }
    
}
