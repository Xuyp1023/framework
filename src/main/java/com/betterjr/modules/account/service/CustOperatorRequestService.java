package com.betterjr.modules.account.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.dao.CustOperatorInfoMapper;
import com.betterjr.modules.account.data.CustOptData;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorInfoRequest;
import com.betterjr.common.utils.UserUtils;
 

@Service
public class CustOperatorRequestService extends BaseService<CustOperatorInfoMapper, CustOperatorInfo> {
    private static String[] queryConds = new String[] { "name", "operCode", "phone", "status" };
    @Autowired
    private CustOperatorHelper requestHelper;
    @Autowired
    private CustOperatorService custOptService;

    /**
     * 新增操作员
     * 
     * @param anMap
     * @return
     */
    public CustOptData saveCustOperator(Map anMap) {
        CustOptData workData = null;
        CustOperatorInfoRequest request = new CustOperatorInfoRequest();
        requestHelper.buildRequest(request, anMap, "addCustOperator");
        boolean optExists = this.custOptService.checkOperatorExists(request.getContIdentType(), request.getContIdentNo());
        if (optExists) {
            throw new BytterTradeException(40001, "抱歉，该证件号码已存在");
        }
        // 判断该操作员是否存在
        boolean operCodeExists = this.custOptService.checkExistsByOperCodeAndOperOrg(request.getOperCode(), request.getOperOrg());
        if (operCodeExists) {
            throw new BytterTradeException(401, "抱歉，该操作员用户名存在【" + request.getOperCode() + "】");
        }
        if (BetterStringUtils.isBlank(request.getRuleList())) {
            logger.error("角色不能为空");
            throw new BytterTradeException(40001, "抱歉，角色不能为空");
        }
         long res = custOptService.addCustOperator(request);
        System.out.println("res:" + res);
 
        workData = BeanMapper.map(request, CustOptData.class);
        return workData;
    }

    /**
     * 修改操作员
     * 
     * @param anMap
     * @return
     */
    public CustOptData updateCustOperator(Map anMap) {
        CustOptData workData = null;
        CustOperatorInfo anRecord = new CustOperatorInfo();
        if (anMap.get("id") == null || "".equalsIgnoreCase("id")) {
            throw new BytterTradeException(40001, "抱歉，操作员编号不能为空");
        }
        BeanMapper.copy(anMap, anRecord);
        if (BetterStringUtils.isBlank(anRecord.getRuleList())) {
            logger.error("角色不能为空");
            throw new BytterTradeException(40001, "抱歉，角色不能为空");
        }
        this.updateByPrimaryKeySelective(anRecord);
        workData = BeanMapper.map(anMap, CustOptData.class);
        return workData;
    }

    public Page<CustOptData> queryCustOperatorByPage(Map<String, String> anMap) {
        int pageNum = anMap.get("pageNum") == null || "".equals(anMap.get("pageNum")) ? 1 : Integer.valueOf(anMap.get("pageNum"));
        int pageSize = anMap.get("pageSize") == null || "".equals(anMap.get("pageSize")) ? 20 : Integer.valueOf(anMap.get("pageSize"));
        return this.findRequest(anMap, pageNum, pageSize);
    }

    @SuppressWarnings("unchecked")
    private Page<CustOptData> findRequest(Map<String, String> anParam, int pageNum, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        String tmpValue;
        for (String tmpKey : queryConds) {
            tmpValue = anParam.get(tmpKey);
            if (BetterStringUtils.isNotBlank(tmpValue)) {
                map.put(tmpKey, tmpValue);
            }
        }
        if (BetterStringUtils.isNotBlank(anParam.get("ruleList"))) {
            map.remove("ruleList");
        }
        CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        String operOrg = custOperator.getOperOrg();
        map.put("operOrg", operOrg);
        Page page = this.selectPropertyByPage(CustOperatorInfo.class, map, pageNum, pageSize, false);
        List list = page.getResult();
        List result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String ruleList = anParam.get("ruleList");
            CustOperatorInfo CustOperatorInfo = (CustOperatorInfo) list.get(i);
            if (BetterStringUtils.isNotBlank(ruleList)) {
                if (CustOperatorInfo.getRuleList().contains(ruleList)) {
                    result.add(list.get(i));
                }
            }
            else {
                result.add(list.get(i));
            }
        }
        return Page.listToPage(result);
    }

}
