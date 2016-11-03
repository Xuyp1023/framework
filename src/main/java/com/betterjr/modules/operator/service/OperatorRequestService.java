package com.betterjr.modules.operator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.dao.CustOperatorInfoMapper;
import com.betterjr.modules.account.data.CustOptData;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorInfoRequest;
import com.betterjr.modules.account.entity.CustOperatorRelation;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
import com.betterjr.modules.account.service.CustPassService;

/***
 * 操作员管理
 *
 * @author hubl
 *
 */
@Service
public class OperatorRequestService extends BaseService<CustOperatorInfoMapper, CustOperatorInfo> {

    private static String[] queryConds = new String[] { "name", "operCode", "phone", "status" };

    private final Logger logger = LoggerFactory.getLogger(OperatorRequestService.class);

    @Autowired
    private OperatorService custOptService;
    @Autowired
    private SysOperatorRoleRelationService operatorRoleRelationService;
    @Autowired
    private CustPassService custPassService;
    @Autowired
    private CustAndOperatorRelaService custAndOpService;

    /**
     * 新增操作员
     *
     * @param anMap
     * @return
     */
    public CustOptData saveCustOperator(final CustOperatorInfoRequest request,String anCustList) {
        final boolean optExists = this.custOptService.checkOperatorExists(request.getContIdentType(), request.getContIdentNo());
        if (optExists) {
            throw new BytterTradeException(40001, "抱歉，该证件号码已存在");
        }
        // 判断该操作员是否存在
        final boolean operCodeExists = this.custOptService.checkExistsByOperCodeAndOperOrg(request.getOperCode(), request.getOperOrg());
        if (operCodeExists) {
            throw new BytterTradeException(401, "抱歉，该操作员用户名存在【" + request.getOperCode() + "】");
        }
        if (BetterStringUtils.isBlank(request.getRuleList())) {
            logger.error("角色不能为空");
            throw new BytterTradeException(40001, "抱歉，角色不能为空");
        }
        if (BetterStringUtils.isBlank(anCustList)) {
            logger.error("机构信息不能为空");
            throw new BytterTradeException(40001, "抱歉，机构信息为空");
        }
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final String operOrg = custOperator.getOperOrg();
        request.setOperOrg(operOrg);
        request.setCustList(anCustList);
        final int res = custOptService.addCustOperator(request);
        if (res == 0) {
            throw new BytterTradeException(40001, "新增操作员失败");
        }
        final CustOptData workData = BeanMapper.map(request, CustOptData.class);
        logger.info("新增操作员对象：" + workData);
        return workData;
    }

    /**
     * 修改操作员
     *
     * @param anMap
     * @return
     */
    public CustOptData updateCustOperator(final CustOperatorInfoRequest request,String anCustList) {
        final CustOperatorInfo operator = BeanMapper.map(request, CustOperatorInfo.class);
        if (operator.getId() == null) {
            throw new BytterTradeException(40001, "抱歉，操作员编号不能为空");
        }
        if (BetterStringUtils.isBlank(anCustList)) {
            logger.error("机构信息不能为空");
            throw new BytterTradeException(40001, "抱歉，机构信息为空");
        }
        // 操作员角色信息绑定修改
        operatorRoleRelationService.saveSysOperatorRoleRelation(operator.getId(), operator.getRuleList());
        this.updateByPrimaryKeySelective(operator);
        // 操作员绑定机构信息，先清除之前的关系，再加入新的关系
        if(BetterStringUtils.isBlank(operator.getOperOrg())){
            CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();// 获取当前登录机构
            operator.setOperOrg(custOperator.getOperOrg());
        }
        Map<String, Object> operatorMp=new HashMap<String, Object>();
        operatorMp.put("operNo", operator.getId());
        operatorMp.put("operOrg", operator.getOperOrg());
        custAndOpService.deleteByExample(operatorMp);
        custAndOpService.addCustOperatorRelation(operator.getId(),operator.getOperOrg(),anCustList);
        final CustOptData workData = BeanMapper.map(operator, CustOptData.class);
        return workData;
    }

    /***
     * 操作员分页查询
     *
     * @param anParam
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<CustOptData> queryCustOperator(final Map<String, String> anParam, final int pageNum, final int pageSize) {
        final Map<String, Object> map = new HashMap<String, Object>();
        String tmpValue;
        for (final String tmpKey : queryConds) {
            tmpValue = anParam.get(tmpKey);
            if (BetterStringUtils.isNotBlank(tmpValue)) {
                map.put(tmpKey, tmpValue);
            }
        }
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final String operOrg = custOperator.getOperOrg();
        map.put("operOrg", operOrg);
        final Page page = this.selectPropertyByPage(CustOperatorInfo.class, map, pageNum, pageSize, "1".equals(anParam.get("flag")));
        final List list = page.getResult();
        final List result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            final CustOperatorInfo custOperatorInfo = (CustOperatorInfo) list.get(i);
            final String ruleList = operatorRoleRelationService.findSysRoleByOperatorId(custOperatorInfo.getId());
            if (BetterStringUtils.isNotBlank(ruleList)) {
                custOperatorInfo.setRuleList(ruleList);
            }
            // 获取绑定的客户关系
            final String custList=custAndOpService.findCustOperator(custOperatorInfo.getId(), operOrg);
            if (BetterStringUtils.isNotBlank(custList)) {
                custOperatorInfo.setCustList(custList);
            }
            result.add(custOperatorInfo);
        }
        return Page.listToPage(result, pageNum, pageSize,page.getPages(),page.getStartRow(),page.getTotal());
    }

    /***
     * 查询操作员信息
     *
     * @param operatorId
     * @return
     */
    public CustOperatorInfo findOperatorById(final Long operatorId) {
        final CustOperatorInfo custOperatorInfo = this.selectByPrimaryKey(operatorId);
        final String ruleList = operatorRoleRelationService.findSysRoleByOperatorId(custOperatorInfo.getId());
        if (BetterStringUtils.isNotBlank(ruleList)) {
            custOperatorInfo.setRuleList(ruleList);
        }
        return custOperatorInfo;
    }

    /***
     * 查找当前登录机构操作员
     *
     * @return
     */
    public List<CustOptData> findCustOperator() {
        final List result = new ArrayList<>();
        final Map<String, Object> map = new HashMap<String, Object>();
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final String operOrg = custOperator.getOperOrg();
        map.put("operOrg", operOrg);
        final List<CustOperatorInfo> list = this.selectByProperty(map);
        for (int i = 0; i < list.size(); i++) {
            final CustOperatorInfo custOperatorInfo = list.get(i);
            final String ruleList = operatorRoleRelationService.findSysRoleByOperatorId(custOperatorInfo.getId());
            if (BetterStringUtils.isNotBlank(ruleList)) {
                custOperatorInfo.setRuleList(ruleList);
            }
            result.add(custOperatorInfo);
        }
        return result;
    }

    /***
     * 修改密码
     *
     * @param anNewPasswd
     * @param anOkPasswd
     * @param anPasswd
     * @return
     */
    public boolean updatePasword(final String anNewPasswd, final String anOkPasswd, final String anPasswd) {
        try {
            return custPassService.savePassword(CustPasswordType.ORG, anNewPasswd, anOkPasswd, anPasswd);
        }
        catch (final Exception e) {
            throw new BytterTradeException(e.getMessage());
        }
    }

    /***
     * 密码重置
     *
     * @param anOperId
     * @param anPassword
     * @param anOkPasswd
     * @return
     */
    public boolean saveChangePassword(final Long anOperId, final String anPassword, final String anOkPasswd) {
        if (checkOperator(anOperId, null)) {
            if (BetterStringUtils.isNotBlank(anPassword) && anPassword.equals(anOkPasswd)) {

                return this.custPassService.saveChangePassword(anOperId, anPassword, CustPasswordType.ORG);
            }
        }
        return false;
    }

    /**
     * 检查管理员是否有权限修改操作员信息，只有同机构的管理员才能修改自己的操作员信息
     *
     * @param anOperId
     *            操作员ID号
     * @param anOperOrg
     *            操作机构
     * @return
     */
    protected boolean checkOperator(final Long anOperId, final String anOperOrg) {
        final CustOperatorInfo user = UserUtils.getOperatorInfo();
        if (BetterStringUtils.isNotBlank(anOperOrg) && user.getOperOrg().equals(anOperOrg)) {

            return true;
        }
        final CustOperatorInfo tmpUser = this.selectByPrimaryKey(anOperId);
        if (tmpUser != null) {

            return tmpUser.getOperOrg().equals(user.getOperOrg());
        }

        return false;
    }

    /**
     * 新增系统默认操作员
     *
     * @param anMap
     * @return
     */
    public boolean addDefaultOperator(final String anOperOrg, final String anName, final String anPassword) {
        boolean bool = false;
        final CustOperatorInfoRequest request = new CustOperatorInfoRequest();
        request.initAdminOperator(anOperOrg, anName, anPassword);
        // 判断该操作员是否存在
        final boolean operCodeExists = this.custOptService.checkExistsByOperCodeAndOperOrg(request.getOperCode(), request.getOperOrg());
        if (operCodeExists) {
            throw new BytterTradeException(401, "该机构已经添加系统操作员");
        }
        final int res = custOptService.addSysCustOperator(request);
        if (res > 0) {
            bool = true;
        }
        return bool;
    }
    
    /**
     * 根据客户信息，查找机构默认经办人信息
     * @param anOperOrg
     * @return
     */
    public CustOperatorInfo findCustClerkMan(String anOperOrg){
        Map termMap = QueryTermBuilder.newInstance().put("operOrg", anOperOrg).put("clerkMan", "2").build();
        List<CustOperatorInfo> tmpList = this.selectByProperty(termMap);
        
        return Collections3.getFirst(tmpList);
    }
}