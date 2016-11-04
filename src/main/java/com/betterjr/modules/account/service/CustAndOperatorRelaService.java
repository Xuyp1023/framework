package com.betterjr.modules.account.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.account.dao.CustOperatorRelationMapper;
import com.betterjr.modules.account.entity.CustOperatorRelation;

@Service
public class CustAndOperatorRelaService extends BaseService<CustOperatorRelationMapper, CustOperatorRelation> {
    
    /**
     * 
     * 检查操作员和客户关系
     * @param 操作员编号
     * @param1 客户号
     * @return 如果存在操作关系，则返回True,否则返回False
     * @throws 异常情况
     */
    public boolean checkOperatorAndCustNo(Long anOperNo, Long anCustNo){
        if (anOperNo != null && anCustNo != null){
            Map<String, Object> map = new HashMap();
            map.put("operNo", anOperNo);
            map.put("custNo", anCustNo);
            List list = this.selectByProperty(map);
            
            return list.size() > 0;
        }
        
        return false; 
    }
    
    public List<Long> findCustNoList(String anOperOrg){
        
        
        return this.findCustNoList(null,anOperOrg);
    }
    
    public List<Long> findCustNoList(Long anOperNo,String anOperOrg){
        Map<String, Object> map = new HashMap();
        map.put("operNo", anOperNo);
        map.put("operOrg", anOperOrg);
        map.put("status", "1");
        List<Long> custNoList =  new ArrayList<>();
        for(CustOperatorRelation custOR : this.selectByProperty(map)){
           custNoList.add(custOR.getCustNo()); 
        }
        return custNoList;
    }
    
    public List<Long> findOperNoList(Long anCustNo){
        Map<String, Object> map = new HashMap();
        map.put("custNo", anCustNo);
        List<Long> custNoList =  new ArrayList<Long>();
        for(CustOperatorRelation custOR : this.selectByProperty(map)){
           custNoList.add(custOR.getOperNo()); 
        }
        return custNoList;
    }
    
    public String findOperOrgByCustNo(Long anCustNo){
       List<CustOperatorRelation> tmpList = this.selectByProperty("custNo", anCustNo);
       if (Collections3.isEmpty(tmpList)){
           return "";
       }
       else{
           return Collections3.getFirst(tmpList).getOperOrg();
       }
    }
    
    /****
     * 查询客户操作员绑定关系信息
     * @param anOperId
     * @param anOperOrg
     * @return
     */
    public String findCustOperator(Long anOperId,String anOperOrg){
        StringBuffer sb=new StringBuffer();
        Map<String, Object> map = new HashMap();
        map.put("operOrg", anOperOrg);
        map.put("operNo", anOperId);
        map.put("status", "1");
        int i=0;
        for(CustOperatorRelation custOR : this.selectByProperty(map)){
            if(i==0){
                sb.append(custOR.getCustNo());
            }else{
                sb.append(","+custOR.getCustNo());
            }
           i++;
        }
        return sb.toString();
    }
    
    /***
     * 添加绑定客户操作员
     * @param anCustList
     */
    public void addCustOperatorRelation(Long anOperId,String anOperOrg,String anCustList){
        Map<String, Object> operatorMp=new HashMap<String, Object>();
        operatorMp.put("operNo",anOperId);
        operatorMp.put("operOrg", anOperOrg);
        this.deleteByExample(operatorMp);
        
        String[] custArr=anCustList.split(",");
        for(int i=0;i<custArr.length;i++){
            String custStr=custArr[i];
            // 增加操作员和客户之间关系
            this.insert(new CustOperatorRelation(anOperId, Long.parseLong(custStr),anOperOrg));
        }
    }
}
