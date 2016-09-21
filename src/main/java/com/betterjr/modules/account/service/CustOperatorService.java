package com.betterjr.modules.account.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.dao.CustOperatorInfoMapper;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorInfoRequest;
import com.betterjr.modules.account.entity.CustPassInfo;

@Service
public class CustOperatorService extends BaseService<CustOperatorInfoMapper, CustOperatorInfo> {

    // @Autowired
    // private CustOperatorInfoMapper operatorMapper;
    @Autowired
    private CustOperatorHelper requestHelper;
    @Autowired
    private CustPassService custPassService;
    @Autowired
    private CustAndOperatorRelaService operatorRelaService;

    public CustOperatorService(){
    }

    public boolean isFirstOperator(final String anOperOrg) {

        final int kk = this.selectCountByProperty("operOrg", anOperOrg);
        return kk == 0;
    }

    public boolean isManager(final Long anOperID) {
        final CustOperatorInfo custOper = this.selectByPrimaryKey(anOperID);
        if (custOper != null && custOper.getDefOper() != null) {
            return custOper.getDefOper();
        }
        else {
            return false;
        }
    }

    public static Object workForCustNo(final String anValue) {
        if (StringUtils.isNotBlank(anValue)) {
            return new Long(anValue);
        }
        else {
            return findCustNoList();
        }
    }

    public static List findCustNoList() {
        final CustContextInfo contextInfo = UserUtils.getOperatorContextInfo();
        List custList;
        if (contextInfo == null) {
            custList = new ArrayList(1);
        }
        else {
            custList = contextInfo.findCustList();
        }
        if (custList.size() == 0){
            custList.add(-1L);
        }
        return custList;
    }

    public CustOperatorInfo findCustOperatorByIndentInfo(final String anIndentType, final String anUserIdentNo) {
        final Map<String, Object> map = new HashMap();
        map.put("identNo", anUserIdentNo);
        map.put("identType", anIndentType);
        map.put("status", "1");
        // return operatorMapper.findCustOperatorByIndentInfo(anIndentType, anUserIdentNo);
        final List<CustOperatorInfo> list = this.selectByProperty(map);
        if (list.size() > 0) {

            return list.get(0);
        }

        return null;
    }

    public CustOperatorInfo findCustOperatorByOperCode(final String anOperOrg, final String anOperCode) {
        final Map<String, Object> map = new HashMap();
        map.put("operOrg", anOperOrg);
        map.put("operCode", anOperCode);
        // return operatorMapper.findCustOperatorByOperCode(anOperOrg, anOperCode);
        final List<CustOperatorInfo> list = this.selectByProperty(map);
        if (list.size() > 0) {

            return list.get(0);
        }
        return null;
    }

    public int addCustOperator(final CustOperatorInfoRequest request) {
        final CustOperatorInfo operator = new CustOperatorInfo(request);
        final CustPassInfo custPassInfo = new CustPassInfo(operator, request.getPassword());

        this.custPassService.insert(custPassInfo);
        return this.insert(operator);
    }

    public void insertCustPass(final CustPassInfo custPssInfo) {
        this.custPassService.insert(custPssInfo);
    }

    public boolean checkOperatorExists(final String identType, final String identNo) {
        if (StringUtils.isNotBlank(identType) && StringUtils.isNotBlank(identType)) {
            final Map<String, Object> map = new HashMap();
            map.put("identType", identType);
            map.put("identNo", identNo);
            final List list = this.selectByProperty(map);

            return list.size() > 0;
        }

        return false;
    }

    public boolean checkExistsByOperCodeAndOperOrg(final String operCode, final String operOrg) {
        if (StringUtils.isNotBlank(operCode) && StringUtils.isNotBlank(operOrg)) {
            final Map<String, Object> map = new HashMap();
            map.put("operCode", operCode);
            map.put("operOrg", operOrg);
            final List list = this.selectByProperty(map);

            return list.size() > 0;
        }

        return false;
    }

    public CustOperatorInfo findCustOperatorInfo(final Long anOperId){
        return this.selectByPrimaryKey(anOperId);
    }

    public List<CustOperatorInfo> queryOperatorInfoByCustNo(final Long anCustNo) {
        final List<Long> operators = operatorRelaService.findOperNoList(anCustNo);

        return this.selectByProperty("id", operators);
    }

    public boolean saveBindingTradePassword(final CustPasswordType anLoginPassType, final String anNewPasswd, final String anOkPasswd, final String anLoginPasswd){

        return custPassService.saveBindingTradePassword(anLoginPassType, anNewPasswd, anOkPasswd, anLoginPasswd);
    }

    public boolean saveModifyTradePassword(final String anNewPassword, final String anOkPassword, final String anOldPassword){
        return custPassService.savePassword(CustPasswordType.ORG_TRADE, anNewPassword, anOkPassword, anOldPassword);
    }
}
