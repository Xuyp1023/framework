package com.betterjr.modules.operator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.dao.CustOperatorInfoMapper;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorInfoRequest;
import com.betterjr.modules.account.entity.CustPassInfo;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
import com.betterjr.modules.account.service.CustOperatorHelper;
import com.betterjr.modules.account.service.CustPassService;

@Service
public class OperatorService extends BaseService<CustOperatorInfoMapper, CustOperatorInfo> {

    @Autowired
    private CustOperatorHelper requestHelper;
    @Autowired
    private CustPassService custPassService;
    @Autowired
    private SysOperatorRoleRelationService operatorRoleRelationService;
    @Autowired
    private CustAndOperatorRelaService custAndOpService;

    public boolean isFirstOperator(final String anOperOrg) {

        final int kk = this.selectCountByProperty("operOrg", anOperOrg);
        return kk == 0;
    }

    public boolean isManager(final Long anOperID) {
        final CustOperatorInfo custOper = this.selectByPrimaryKey(anOperID);
        if (custOper != null && custOper.getDefOper() != null) {
            return custOper.getDefOper();
        } else {
            return false;
        }
    }

    public static Object workForCustNo(final String anValue) {
        if (StringUtils.isNotBlank(anValue)) {
            return new Long(anValue);
        } else {
            return findCustNoList();
        }
    }

    public static List findCustNoList() {
        final CustContextInfo contextInfo = UserUtils.getOperatorContextInfo();
        List custList;
        if (contextInfo == null) {
            custList = new ArrayList(1);
        } else {
            custList = contextInfo.findCustList();
        }
        if (custList.size() == 0) {
            custList.add(-1L);
        }
        return custList;
    }

    public CustOperatorInfo findCustOperatorByIndentInfo(final String anIndentType, final String anUserIdentNo) {
        final Map<String, Object> map = new HashMap();
        map.put("identNo", anUserIdentNo);
        map.put("identType", anIndentType);
        map.put("status", "1");
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
        // 操作员角色信息绑定修改
        operatorRoleRelationService.saveSysOperatorRoleRelation(operator.getId(), operator.getRuleList());
        this.custPassService.insert(custPassInfo);
        if (StringUtils.isNotBlank(request.getCustList())) {
            custAndOpService.addCustOperatorRelation(operator.getId(), operator.getOperOrg(), request.getCustList());// 添加客户绑定操作员关系
        }
        operator.setRuleList("");
        return this.insert(operator);
    }

    public int addSysCustOperator(final CustOperatorInfoRequest request) {
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

    public boolean checkExistsByMap(final Map<String, Object> anMap) {
        final List list = this.selectByProperty(anMap);
        return list.size() > 0;
    }

    public CustOperatorInfo queryCustOperatorInfo(final Long operId) {
        return this.selectByPrimaryKey(operId);
    }
}
