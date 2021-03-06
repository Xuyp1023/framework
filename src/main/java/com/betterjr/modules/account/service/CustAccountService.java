package com.betterjr.modules.account.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.data.WorkUserInfo;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.IdcardUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.dao.CustInfoMapper;
import com.betterjr.modules.account.data.CustContextInfo;
import com.betterjr.modules.account.entity.CustContactInfo;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorRelation;
import com.betterjr.modules.account.entity.MechCustBaseInfo;
import com.betterjr.modules.account.entity.SaleAccoRequestInfo;
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

    public void setSysMenuService(final SysMenuService anSysMenuService) {
        this.sysMenuService = anSysMenuService;
    }

    public SysMenuRuleService getSysMenuRuleService() {
        return this.sysMenuRuleService;
    }

    public void setSysMenuRuleService(final SysMenuRuleService anSysMenuRuleService) {
        this.sysMenuRuleService = anSysMenuRuleService;
    }

    public CustOperatorService getCustOperService() {
        return custOperService;
    }

    public void setCustOperService(final CustOperatorService custOperService) {
        this.custOperService = custOperService;
    }

    public CustContactService getContactService() {
        return contactService;
    }

    public void setContactService(final CustContactService contactService) {
        this.contactService = contactService;
    }

    public MechCustBaseService getMechCustService() {
        return mechCustService;
    }

    public void setMechCustService(final MechCustBaseService mechCustService) {
        this.mechCustService = mechCustService;
    }

    public CustAndOperatorRelaService getCustAndOpService() {
        return custAndOpService;
    }

    public void setCustAndOpService(final CustAndOperatorRelaService custAndOpService) {
        this.custAndOpService = custAndOpService;
    }

    /**
     * 返回当前操作员能看到的客户信息，名称和客户号；用于账户类业务！
     *
     * @return
     */
    public List<SimpleDataEntity> findCustInfo() {
        final List<CustInfo> custList = UserUtils.findCustInfoList();
        if (Collections3.isEmpty(custList)) {
            // throw new SessionInvalidException(20005, "not find custinfo please relogin or add Org");
            logger.warn("not find CustNo List; please open account or relogin");
            return new ArrayList<SimpleDataEntity>();
        }
        final List<SimpleDataEntity> dataList = new ArrayList();
        for (final CustInfo custInfo : custList) {
            dataList.add(new SimpleDataEntity(custInfo.getCustName(), custInfo.getCustNo().toString()));
        }

        return dataList;
    }

    /**
     * 查询所有可用客户
     * @return
     */
    public List<CustInfo> queryValidCustInfo() {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("identValid", "1");
        conditionMap.put("businStatus", "0");
        return this.selectByProperty(conditionMap);
    }

    /**
     * 根据条件查询
     * @return
     */
    public List<CustInfo> findValidCustInfo(final Map<String, Object> anMap) {
        anMap.put("identValid", "1");
        anMap.put("businStatus", "0");
        return this.selectByProperty(anMap);
    }

    /**
     * 查询所有可用客户 分页
     * @param anPageSize
     * @param anPageNum
     * @param anFlag
     * @return
     */
    public Page<CustInfo> queryValidCustInfo(final Map<String, Object> anParam, final int anFlag, final int anPageNum,
            final int anPageSize) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("identValid", "1");
        conditionMap.put("businStatus", "0");
        if (anParam != null) {
            final String custName = (String) anParam.get("LIKEcustName");
            if (StringUtils.isBlank(custName)) {
                anParam.remove("LIKEcustName");
            } else {
                conditionMap.put("LIKEcustName", "%" + custName + "%");
            }
        }
        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * 查询未审核及无效客户 分页
     * @return
     */
    public Page<CustInfo> queryInvalidCustInfo(final Map<String, Object> anParam, final int anFlag, final int anPageNum,
            final int anPageSize) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("identValid", "0");
        conditionMap.put("businStatus", new String[] { "0", "1", "9" });
        if (anParam != null) {
            final String custName = (String) anParam.get("LIKEcustName");
            if (StringUtils.isBlank(custName)) {
                anParam.remove("LIKEcustName");
            } else {
                conditionMap.put("LIKEcustName", "%" + custName + "%");
            }
        }
        return this.selectPropertyByPage(conditionMap, anPageNum, anPageSize, anFlag == 1);
    }

    /**
     * 查询所有可用客户
     * @return
     */
    public List<CustInfo> queryCustInfoByOperOrgSet(final Set<String> operOrgSet) {
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("identValid", "1");
        conditionMap.put("businStatus", "0");
        conditionMap.put("operOrg", operOrgSet.toArray(new String[operOrgSet.size()]));
        return this.selectByProperty(conditionMap);
    }

    /**
     * 返回当前操作员能看到的客户信息，名称和客户号；用于账户类业务！且已正常开户的
     *
     * @return
     */
    public List<SimpleDataEntity> findCustInfoIsOpen() {
        final List<CustInfo> custList = UserUtils.findCustInfoList();
        if (Collections3.isEmpty(custList)) {
            logger.warn("not find CustNo List; please open account or relogin");
            return new ArrayList<SimpleDataEntity>();
        }
        final List<SimpleDataEntity> dataList = new ArrayList();
        for (final CustInfo custInfo : custList) {
            // List<String> agencyNoList = this.findOpenedAgencyNoList(custInfo.getCustNo());
            // if(agencyNoList.size()>0){
            dataList.add(new SimpleDataEntity(custInfo.getCustName(), custInfo.getCustNo().toString()));
            // }
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
    public boolean checkAccountExists(final String anCustType, final String anIdentType, final String anIdentNo) {
        if (StringUtils.isNotBlank(anIdentType) && StringUtils.isNotBlank(anIdentType)) {
            final Map<String, Object> map = new HashMap();
            map.put("custType", anCustType);
            map.put("identType", anIdentType);
            map.put("identNo", anIdentNo);
            final List list = this.selectByProperty(map);
            return list.size() > 0;
        }

        return false;
    }

    public void initOperator(final SaleAccoRequestInfo request, final CustOperatorInfo operator) {
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

    public CustOperatorInfo insertCustOperator(final SaleAccoRequestInfo request) {
        final CustOperatorInfo operator = new CustOperatorInfo();
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

    public CustInfo initCustInfo(final SaleAccoRequestInfo request) {
        final CustInfo custInfo = new CustInfo(request);

        return custInfo;
    }

    public void openOrgAccount(final SaleAccoRequestInfo request) {
        final CustContextInfo custContext = UserUtils.getOperatorContextInfo();
        final CustOperatorInfo operator = new CustOperatorInfo(custContext.getOperatorInfo());
        final CustInfo custInfo = initCustInfo(request);

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
        custAndOpService
                .insert(new CustOperatorRelation(operator.getId(), custInfo.getCustNo(), operator.getOperOrg()));

        // 将新用户加入到上下文中
        custContext.addNewCustInfo(custInfo);
        // custContext.addNewTradeAccount(tradeAccount);
        custContext.setOperatorInfo(operator);

    }

    /**
     * 客户登录成功后，注册相关客户和交易账户信息
     * @param contextInfo 操作员上下文信息，如果没有填null
     * @param anOperator 操作员信息
     * @return
     */
    public CustContextInfo loginOperate(CustContextInfo contextInfo, final CustOperatorInfo anOperator) {
        if (contextInfo == null) {
            // String token = Servlets.getSession().getId();
            final String token = UserUtils.getSessionId();
            contextInfo = new CustContextInfo(token, null, null);
            CustContextInfo.putCustContextInfo(contextInfo);
            contextInfo.setOperatorInfo(anOperator);
        }

        final List<CustInfo> custList = findCustInfoByOperator(anOperator.getId(), anOperator.getOperOrg());
        contextInfo.login(custList);

        // 增加交易账户信息
        // contextInfo.addTradeAccount(tradeAccountService.findTradeAccountByCustInfo(custList));
        // todo;登录信息和状态暂时不处理
        return contextInfo;
    }

    /**
     * 客户登录成功后，注册相关客户和交易账户信息
     * @param contextInfo 操作员上下文信息，如果没有填null
     * @param anOperator 操作员信息
     * @return
     */
    public CustContextInfo loginOperateByToken(final String token, final CustOperatorInfo anOperator) {
        // String token = Servlets.getSession().getId();
        final CustContextInfo contextInfo = new CustContextInfo(token, null, null);
        CustContextInfo.putCustContextInfo(contextInfo);
        contextInfo.setOperatorInfo(anOperator);

        final List<CustInfo> custList = findCustInfoByOperator(anOperator.getId(), anOperator.getOperOrg());
        contextInfo.login(custList);
        return contextInfo;
    }

    public List<CustInfo> findCustInfoByOperator(final Long anOperNo, final String anOperOrg) {
        final List<Long> custList = custAndOpService.findCustNoList(anOperNo, anOperOrg);
        return this.selectByProperty("custNo", custList);
    }

    /**
     * 根据当前用户类型获取对应角色的菜单列表
     * @param anOperOrg
     * @return
     */
    public List findSysMenuByOperator(final String anOperOrg) {
        final WorkUserInfo userInfo = UserUtils.getUser();
        List<String> menuIds = null;
        if (UserUtils.isBytterUser()) {
            menuIds = sysMenuRuleService.findAllByRuleList("BYTTER_USER");
        } else {
            menuIds = sysMenuRuleService.findAllByRuleList(userInfo.getRuleList());
        }
        return sysMenuService.findMenuList(menuIds);
    }

    /**
     * 根据客户号，查询客户名字了如果没有查到，直接返回入参客户编号
     * @param anCustNo
     * @return
     */
    public String queryCustName(final Long anCustNo) {
        if (anCustNo == null) {
            return "";
        }
        final CustInfo custInfo = this.selectByPrimaryKey(anCustNo);
        if (custInfo == null) {
            return anCustNo.toString();
        }
        return custInfo.getCustName();
    }

    /**
     * 根据企业名称查询企业信息
     * @param anCustName
     * @return
     */
    public CustInfo queryCustByCustName(final String anCustName) {

        CustInfo custInfo = new CustInfo();
        custInfo.setCustName(anCustName);
        custInfo.setBusinStatus("0");
        CustInfo info = this.selectOne(custInfo);
        if (info != null && info.getCustNo() != null) {

            return info;
        } else {
            return null;
        }

    }

    /**
     * 根据客户编号,查询客户
     * @param anCustNo
     * @return
     */
    public CustInfo findCustInfo(final Long anCustNo) {
        final CustInfo custInfo = this.selectByPrimaryKey(anCustNo);
        return custInfo;
    }

    /**
     * 查询当前机构下有效的用户
     * @return
     */
    public List<SimpleDataEntity> findCustOperator() {
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("identValid", "1");
        conditionMap.put("businStatus", "0");
        conditionMap.put("operOrg", custOperator.getOperOrg());
        final List<SimpleDataEntity> dataList = new ArrayList();
        for (final CustInfo custInfo : this.selectByProperty(conditionMap)) {
            dataList.add(new SimpleDataEntity(custInfo.getCustName(), custInfo.getCustNo().toString()));
        }
        return dataList;
    }

}
