package com.betterjr.modules.account.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.modules.account.dao.CustPassInfoMapper;
import com.betterjr.modules.account.data.CustPassRequest;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustPassInfo;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.sys.security.SystemAuthorizingRealm;

@Service
public class CustPassService extends BaseService<CustPassInfoMapper, CustPassInfo> {

    @Autowired
    CustPassInfoMapper passInfoMapper;

    public List<CustPassInfo> getCustPassByCustNo(Long anPassID, String anType) {
        Map<String, Object> map = new HashMap();
        map.put("custNo", anPassID);
        if (StringUtils.isNotBlank(anType)) {
            map.put("passType", anType);
        }
        List<CustPassInfo> passList = selectByProperty(map);
        return passList;
    }

    public CustPassInfo getOperaterPassByCustNo(Long anPassID) {
        List<CustPassInfo> passList = getCustPassByCustNo(anPassID, "6");
        if (passList != null && passList.size() == 1) {
            return passList.get(0);
        }
        else {
            return new CustPassInfo();
        }
    }

    /**
     * 修改密码
     * 
     * @param anMap
     * @return
     */
    public boolean updatePassword(Map anMap) {
        CustPassRequest custPassRequest = new CustPassRequest();
        BeanMapper.copy(anMap, custPassRequest);
        String newPasswd = custPassRequest.getNewPasswd();
        String okPasswd = custPassRequest.getOkPasswd();
        CustOperatorInfo user = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        CustPassInfo passInfo = this.getOperaterPassByCustNo(user.getId());
        // 临时锁定
        if (passInfo.validLockType()) {
            throw new DisabledAccountException("操作员密码被锁定，请稍后再试");
        }
        // 判断旧密码
        String passwd = SystemAuthorizingRealm.findEncrypt(custPassRequest.getPasswd(), passInfo.getPassSalt());
        if (!passwd.equals(passInfo.getPasswd())) {
            throw new DisabledAccountException("操作员原密码不正确");
        }
        else if (!newPasswd.equals(okPasswd)) {
            throw new DisabledAccountException("两次输入的密码不一致");
        }
        CustPassInfo newPassInfo = new CustPassInfo(user, okPasswd);
        this.updateByPrimaryKeySelective(newPassInfo);
        return true;
    }

}
