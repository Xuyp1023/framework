package com.betterjr.modules.account.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.DisabledAccountException;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BTAssert;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.dao.CustPassInfoMapper;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustPassInfo;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm.HashPassword;

@Service
public class CustPassService extends BaseService<CustPassInfoMapper, CustPassInfo> {

    private final int passValidLimit = 1;

    public List<CustPassInfo> getCustPassByCustNo(final Long anPassID, final CustPasswordType anType) {

        return selectByProperty(QueryTermBuilder.newInstance().put("custNo", anPassID).put("passType", anType.getPassType()).build());
    }

    public CustPassInfo getOperaterPassByCustNo(final Long anPassID, final CustPasswordType anPassType) {

        return Collections3.getFirst(getCustPassByCustNo(anPassID, anPassType));
    }

    /**
     * 修改操作员密码
     *
     * @param anMap
     * @return
     */
    public boolean saveChangePassword(final Long anOperId, final String anPassword, final CustPasswordType anPassType) {
        CustPassInfo passInfo = this.getOperaterPassByCustNo(anOperId, anPassType);
        if(anPassword.length()<6 || anPassword.length()>16){
            throw new BytterTradeException("密码长度为6-16");
        }
        final HashPassword result = SystemAuthorizingRealm.encrypt(anPassword);
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

    public List<SimpleDataEntity> findPassAndSalt(final Long anPassID, final String[] anPassTypes) {
        final List<SimpleDataEntity> result = new ArrayList(3);
        final Map<String, Object> mapTerm = QueryTermBuilder.newInstance().put("custNo", anPassID).put("passType", anPassTypes).build();
        SimpleDataEntity sde;
        for (final CustPassInfo passInfo : this.selectByProperty(mapTerm)) {
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
    public boolean saveBindingTradePassword(final CustPasswordType anLoginPassType, final String anNewPasswd, final String anOkPasswd, final String anLoginPasswd){
        final CustOperatorInfo user = UserUtils.getOperatorInfo();
        if (user == null){

            return false;
        }
        BTAssert.isTrue(Collections3.hasEmptyObject(anNewPasswd, anOkPasswd, anLoginPasswd) == false , "必须填写密码");

        final CustPassInfo passInfo = this.getOperaterPassByCustNo(user.getId(), anLoginPassType);

        // 临时锁定
        if (passInfo.validLockType()) {
            throw new DisabledAccountException("操作员登录密码被锁定，请稍后再试");
        }

        // 判断旧密码
        final String passwd = SystemAuthorizingRealm.findEncrypt(anLoginPasswd, passInfo.getPassSalt());
        if (passwd.equals(passInfo.getPasswd()) == false) {

            throw new DisabledAccountException("操作员登录密码不正确");
        }
        else if (anNewPasswd.equals(anOkPasswd) == false) {

            throw new DisabledAccountException("两次输入的密码不一致");
        }

        final HashPassword result = SystemAuthorizingRealm.encrypt(anOkPasswd);
        final CustPasswordType passType = anLoginPassType.exchangeTrade();
        final CustPassInfo newPassInfo = new CustPassInfo(passType, passValidLimit, user.getId(), result.salt, result.password);

        final CustPassInfo tempPassInfo = findPassByIdAndType(user.getId(), passType);
        if (tempPassInfo == null) {
            this.insert(newPassInfo);
        } else {
            this.updateByPrimaryKeySelective(newPassInfo);
        }

        return true;
    }

    /**
     * @param anId
     * @param anPassType
     */
    private CustPassInfo findPassByIdAndType(final Long anId, final CustPasswordType anPassType) {
        final Map<String, Object> termMap = QueryTermBuilder.newInstance().put("custNo", anId).put("passType", anPassType).build();
        return Collections3.getFirst(this.selectByProperty(termMap));
    }

    public boolean savePassword(final CustPasswordType anPassType, final String anNewPasswd, final String anOkPasswd, final String anPasswd) {
        final CustOperatorInfo user = UserUtils.getOperatorInfo();
        final CustPassInfo passInfo = this.getOperaterPassByCustNo(user.getId(), anPassType);
        if(anNewPasswd.length()<6 || anNewPasswd.length()>16){
            throw new BytterTradeException("密码长度为6-16");
        }
        // 临时锁定
        if (passInfo.validLockType()) {
            throw new DisabledAccountException("操作员密码被锁定，请稍后再试");
        }
        // 判断旧密码
        final String passwd = SystemAuthorizingRealm.findEncrypt(anPasswd, passInfo.getPassSalt());
        if (passwd.equals(passInfo.getPasswd()) == false) {

            throw new DisabledAccountException("操作员原密码不正确");
        }
        else if (anNewPasswd.equals(anOkPasswd) == false) {

            throw new DisabledAccountException("两次输入的密码不一致");
        }
        final HashPassword result = SystemAuthorizingRealm.encrypt(anOkPasswd);
        final CustPassInfo newPassInfo = new CustPassInfo(anPassType, passValidLimit, user.getId(), result.salt, result.password);
        this.updateByPrimaryKeySelective(newPassInfo);

        return true;
    }
}
