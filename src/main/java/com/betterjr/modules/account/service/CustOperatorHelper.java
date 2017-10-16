package com.betterjr.modules.account.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorInfoRequest;
import com.betterjr.modules.rule.entity.RuleBusiness;
import com.betterjr.modules.rule.entity.WorkRuleValidator;
import com.betterjr.modules.rule.service.BusinRuleService;

@Service
public class CustOperatorHelper {

    @Autowired
    private BusinRuleService businSerivce;

    public void buildRequest(CustOperatorInfoRequest request, Map<String, String> anMap, String anBusinName) {
        RuleBusiness busin = businSerivce.findRuleByBusinName(anBusinName);

        if (busin != null) {
            String tmpValue;
            String fieldName;

            Map<String, String> map = new HashMap();
            for (WorkRuleValidator validator : busin.getValidators()) {
                fieldName = validator.getFieldName();
                tmpValue = anMap.get(fieldName);
                if (tmpValue != null) {
                    map.put(fieldName, tmpValue);
                    // map.put(fieldName, new SimpleDataEntity(validator.getWorkPattern(), tmpValue));
                }
            }
            map.put("name", anMap.get("name"));
            BeanMapper.copy(anMap, request);
            CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
            String operOrg = custOperator.getOperOrg();
            request.setAddress(anMap.get("address"));
            // long contactorSerial = Long.valueOf(anMap.get("contactorSerial"));
            // request.setContactorSerial(contactorSerial);
            request.setEmail(anMap.get("email"));
            request.setFaxNo(anMap.get("faxNo"));
            request.setIdentClass("3");
            request.setMobileNo(anMap.get("mobileNo"));
            request.setOperCode(anMap.get("operCode"));
            request.setOperOrg(operOrg);
            request.setPhone(anMap.get("phone"));
            request.setRevokeBusin(anMap.get("revokeBusin"));
            request.setRuleList(anMap.get("ruleList"));
            request.setSex(anMap.get("sex"));
            request.setTransBusin(anMap.get("transBusin"));
            request.setContCertValidDate(anMap.get("contCertValidDate"));
        }

    }

}
