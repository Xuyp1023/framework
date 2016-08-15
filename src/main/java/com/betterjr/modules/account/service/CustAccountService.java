package com.betterjr.modules.account.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.IdcardUtils;
import com.betterjr.common.utils.StaticThreadLocal;
import com.betterjr.common.web.Servlets;
import com.betterjr.modules.account.dao.CustInfoMapper;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustContactInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorRelation;
import com.betterjr.modules.account.entity.MechCustBaseInfo;
import com.betterjr.modules.account.entity.SaleAccoRequestInfo;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.modules.sys.entity.WorkUserInfo;
import com.betterjr.modules.sys.service.SysMenuRuleService;
import com.betterjr.modules.sys.service.SysMenuService;

@Service
public class CustAccountService extends BaseService<CustInfoMapper, CustInfo> {

    @Autowired
    private CustOperatorService custOperService;

    @Autowired
    private CustContactService contactService;

    @Autowired
    private MechCustBaseService mechCustService;

    @Autowired
    private CustAndOperatorRelaService custAndOpService;
 
    @Autowired
    private SysMenuService sysMenuService;
    
    @Autowired
    private SysMenuRuleService sysMenuRuleService;
    
    public SysMenuService getSysMenuService() {
        return this.sysMenuService;
    }

    public void setSysMenuService(SysMenuService anSysMenuService) {
        this.sysMenuService = anSysMenuService;
    }

    public SysMenuRuleService getSysMenuRuleService() {
        return this.sysMenuRuleService;
    }

    public void setSysMenuRuleService(SysMenuRuleService anSysMenuRuleService) {
        this.sysMenuRuleService = anSysMenuRuleService;
    }

    public CustOperatorService getCustOperService() {
        return custOperService;
    }

    public void setCustOperService(CustOperatorService custOperService) {
        this.custOperService = custOperService;
    }

    public CustContactService getContactService() {
        return contactService;
    }

    public void setContactService(CustContactService contactService) {
        this.contactService = contactService;
    }

    public MechCustBaseService getMechCustService() {
        return mechCustService;
    }

    public void setMechCustService(MechCustBaseService mechCustService) {
        this.mechCustService = mechCustService;
    }

    public CustAndOperatorRelaService getCustAndOpService() {
        return custAndOpService;
    }

    public void setCustAndOpService(CustAndOperatorRelaService custAndOpService) {
        this.custAndOpService = custAndOpService;
    }
 
    /**
     * 返回当前操作员能看到的客户信息，名称和客户号；用于账户类业务！
     * 
     * @return
     */
    public List<SimpleDataEntity> findCustInfo() {
        List<CustInfo> custList = UserUtils.findCustInfoList();
        if (Collections3.isEmpty(custList)) {
            //throw new SessionInvalidException(20005, "not find custinfo please relogin or add Org");
            logger.warn("not find CustNo List; please open account or relogin");
           return new ArrayList<SimpleDataEntity>();
        }
        List<SimpleDataEntity> dataList = new ArrayList();
        for (CustInfo custInfo : custList) {
            dataList.add(new SimpleDataEntity(custInfo.getCustName(), custInfo.getCustNo().toString()));
        }

        return dataList;
    }
    
    /**
     * 返回当前操作员能看到的客户信息，名称和客户号；用于账户类业务！且已正常开户的
     * 
     * @return
     */
    public List<SimpleDataEntity> findCustInfoIsOpen(){
        List<CustInfo> custList = UserUtils.findCustInfoList();
        if (Collections3.isEmpty(custList)) {
            logger.warn("not find CustNo List; please open account or relogin");
           return new ArrayList<SimpleDataEntity>();
        }
        List<SimpleDataEntity> dataList = new ArrayList();
        for (CustInfo custInfo : custList) {
            //List<String> agencyNoList = this.findOpenedAgencyNoList(custInfo.getCustNo());
            //if(agencyNoList.size()>0){
                dataList.add(new SimpleDataEntity(custInfo.getCustName(), custInfo.getCustNo().toString()));
            //}
        }
        return dataList;
    }

    /**
     * 
     * 
     * 检查客户是否已开户
     * 
     * @param 客户类型
     *            ，0：机构；1：个人
     * @param1 客户证件类型
     * @return 客户证件号码
     * @throws 异常情况
     */
    public boolean checkAccountExists(String anCustType, String anIdentType, String anIdentNo) {
        if (BetterStringUtils.isNotBlank(anIdentType) && BetterStringUtils.isNotBlank(anIdentType)) {
            Map<String, Object> map = new HashMap();
            map.put("custType", anCustType);
            map.put("identType", anIdentType);
            map.put("identNo", anIdentNo);
            List list = this.selectByProperty(map);
            return list.size() > 0;
        }
        
        return false;
    }

    public void initOperator(SaleAccoRequestInfo request, CustOperatorInfo operator) {
        operator.setId(SerialGenerator.getLongValue(SerialGenerator.OPERATOR_ID));
        operator.setName(request.getContName());
        operator.setIdentType(request.getContIdentType());
        operator.setIdentNo(request.getContIdentNo());
        operator.setMobileNo(request.getContMobileNo());
        operator.setPhone(request.getContPhone());
        operator.setIdentClass(request.getContIdentifyType());
        operator.setValidDate(request.getContCertValidDate());
        operator.setStatus("1");
        operator.setLastStatus("1");
        operator.setSex(IdcardUtils.getGenderByIdCard(operator.getIdentNo(), operator.getIdentType()));
        operator.setRegDate(BetterDateUtils.getNumDate());
        operator.setModiDate(BetterDateUtils.getNumDateTime());
        operator.setFaxNo(request.getContFax());
        operator.setAddress(request.getAddress());
        operator.setEmail(request.getContEmail());
        operator.setZipCode(request.getZipCode());
    }
    
    public CustOperatorInfo insertCustOperator(SaleAccoRequestInfo request){
        CustOperatorInfo operator = new CustOperatorInfo();
        operator.setId(SerialGenerator.getLongValue(SerialGenerator.OPERATOR_ID));
        operator.setName(request.getContName());
        operator.setIdentType(request.getContIdentType());
        operator.setIdentNo(request.getContIdentNo());
        operator.setMobileNo(request.getContMobileNo());
        operator.setPhone(request.getContPhone());
        operator.setIdentClass(request.getContIdentifyType());
        operator.setValidDate(request.getContCertValidDate());
        operator.setStatus("1");
        operator.setLastStatus("1");
        operator.setSex(IdcardUtils.getGenderByIdCard(request.getContIdentNo(), request.getContIdentType()));
        operator.setRegDate(BetterDateUtils.getNumDate());
        operator.setModiDate(BetterDateUtils.getNumDateTime());
        operator.setFaxNo(request.getContFax());
        operator.setAddress(request.getAddress());
        operator.setEmail(request.getContEmail());
        operator.setZipCode(request.getZipCode());
        this.custOperService.insert(operator);
        return operator;
    }

    public CustInfo initCustInfo(SaleAccoRequestInfo request) {
        CustInfo custInfo = new CustInfo(request);

        return custInfo;
    }

    public void openOrgAccount(SaleAccoRequestInfo request) {
        CustContextInfo custContext = UserUtils.getOperatorContextInfo();
        CustOperatorInfo operator = new CustOperatorInfo(custContext.getOperatorInfo());
        CustInfo custInfo = initCustInfo(request);

        this.insert(custInfo);
        // 新操作员，需要增加
        if (operator.getId() == null) {
            initOperator(request, operator);
            this.custOperService.insert(operator);
        }
        request.setContactor(operator.getId());

        // 增加联系方式信息
        contactService.insert(new CustContactInfo(request));

        // 增加企业基本信息
        mechCustService.insert(new MechCustBaseInfo(request));

        // 增加操作员和客户之间关系
        custAndOpService.insert(new CustOperatorRelation(operator.getId(), custInfo.getCustNo(), operator.getOperOrg()));

         // 将新用户加入到上下文中
        custContext.addNewCustInfo(custInfo);
        //custContext.addNewTradeAccount(tradeAccount);
        custContext.setOperatorInfo(operator);

    }
      
     
    /**
     * 客户登录成功后，注册相关客户和交易账户信息
     * @param contextInfo 操作员上下文信息，如果没有填null
     * @param anOperator 操作员信息
     * @return
     */
    public CustContextInfo loginOperate(CustContextInfo contextInfo, CustOperatorInfo anOperator) {
        if (contextInfo == null) {
//            String token = Servlets.getSession().getId();
        	String token=UserUtils.getSessionId();
            contextInfo = new CustContextInfo(token, null, null);
            CustContextInfo.putCustContextInfo(contextInfo);
            contextInfo.setOperatorInfo(anOperator);
        }

        List<CustInfo> custList = findCustInfoByOperator(anOperator.getId(), anOperator.getOperOrg());
        contextInfo.login(custList);

        // 增加交易账户信息
//        contextInfo.addTradeAccount(tradeAccountService.findTradeAccountByCustInfo(custList));
        // todo;登录信息和状态暂时不处理
        return contextInfo;
    }

    public List<CustInfo> findCustInfoByOperator(Long anOperNo, String anOperOrg) {
        List<Long> custList = custAndOpService.findCustNoList(anOperOrg);
        return this.selectByProperty("custNo", custList);
    }
    
    /**
     * 根据当前用户类型获取对应角色的菜单列表
     * @param anOperOrg
     * @return
     */
    public List findSysMenuByOperator(String anOperOrg){
        WorkUserInfo userInfo = UserUtils.getUser();
        List<String> menuIds  = null;
        if(UserUtils.isBytterUser()){
            menuIds = sysMenuRuleService.findAllByRuleList("BYTTER_USER");
        }else{
            menuIds = sysMenuRuleService.findAllByRuleList(userInfo.getRuleList());
        }
        return sysMenuService.findMenuList(menuIds);
    }
    
    /**
     * 根据客户号，查询客户名字了如果没有查到，直接返回入参客户编号 
     * @param anCustNo
     * @return
     */
    public String queryCustName(Long anCustNo){
        if (anCustNo == null){
            return "";
        }        
       CustInfo custInfo = this.selectByPrimaryKey(anCustNo);
       if (custInfo == null){
           return anCustNo.toString();
       }
       return custInfo.getCustName();
    }

    /**
     * 根据客户编号,查询客户
     * @param anCustNo
     * @return
     */
    public CustInfo findCustInfo(Long anCustNo) {
        CustInfo custInfo = this.selectByPrimaryKey(anCustNo);
        return custInfo;
    }
     
}
