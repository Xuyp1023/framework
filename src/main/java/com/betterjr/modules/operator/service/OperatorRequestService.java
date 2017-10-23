package com.betterjr.modules.operator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.data.CustPasswordType;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.QueryTermBuilder;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.dao.CustOperatorInfoMapper;
import com.betterjr.modules.account.data.CustOptData;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.entity.CustOperatorInfoRequest;
import com.betterjr.modules.account.service.CustAndOperatorRelaService;
import com.betterjr.modules.account.service.CustPassService;
import com.betterjr.modules.document.ICustFileService;
import com.betterjr.modules.role.entity.Role;
import com.betterjr.modules.role.service.RoleService;

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
    @Autowired
    private RoleService roleService;
    @Autowired
    private ICustFileService fileItemService;

    /**
     * 新增操作员
     *
     * @param anMap
     * @return
     */
    public CustOptData addCustOperator(final CustOperatorInfoRequest request, final String anCustList,
            final String anFileList) {
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final String operOrg = custOperator.getOperOrg();
        checkParam(request, anCustList, operOrg); // 参数检查
        request.setOperOrg(operOrg);
        request.setCustList(anCustList);

        if (StringUtils.isNotBlank(anFileList) && "1".equals(request.getClerkMan())) {
            request.setBatchNo(fileItemService.updateCustFileItemInfo(anFileList, request.getBatchNo()));
        }
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
    public CustOptData saveCustOperator(final CustOperatorInfoRequest request, final String anCustList) {

        return saveCustOperator(request, anCustList, null);
    }

    public CustOptData saveCustOperator(final CustOperatorInfoRequest request, final String anCustList,
            final String anFileList) {
        final CustOperatorInfo operator = BeanMapper.map(request, CustOperatorInfo.class);
        if (operator.getId() == null) {
            throw new BytterTradeException(40001, "抱歉，操作员编号不能为空");
        }
        checkSaveParam(operator);
        operator.setIdentNo(operator.getContIdentNo());
        operator.setIdentType(operator.getContIdentType());

        // 操作员角色信息绑定修改
        operatorRoleRelationService.saveSysOperatorRoleRelation(operator.getId(), operator.getRuleList());

        if (StringUtils.isNotBlank(anFileList) && "1".equals(operator.getClerkMan())) {
            operator.setBatchNo(fileItemService.updateCustFileItemInfo(anFileList, operator.getBatchNo()));
        }
        this.updateByPrimaryKeySelective(operator);

        // 操作员绑定机构信息，先清除之前的关系，再加入新的关系
        if (StringUtils.isBlank(operator.getOperOrg())) {
            final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();// 获取当前登录机构
            operator.setOperOrg(custOperator.getOperOrg());
        }
        if (StringUtils.isNotBlank(anCustList)) {
            custAndOpService.addCustOperatorRelation(operator.getId(), operator.getOperOrg(), anCustList);
        }
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
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put(tmpKey, tmpValue);
            }
        }
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final String operOrg = custOperator.getOperOrg();
        map.put("operOrg", operOrg);
        final Page page = this.selectPropertyByPage(CustOperatorInfo.class, map, pageNum, pageSize,
                "1".equals(anParam.get("flag")));
        final List list = page.getResult();
        final List result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            final CustOperatorInfo custOperatorInfo = (CustOperatorInfo) list.get(i);
            final String ruleList = operatorRoleRelationService.findSysRoleByOperatorId(custOperatorInfo.getId());
            if (StringUtils.isNotBlank(ruleList)) {
                custOperatorInfo.setRuleList(ruleList);
            }
            // 获取绑定的客户关系
            final String custList = custAndOpService.findCustOperator(custOperatorInfo.getId(), operOrg);
            if (StringUtils.isNotBlank(custList)) {
                custOperatorInfo.setCustList(custList);
            }
            custOperatorInfo.setContIdentType(custOperatorInfo.getIdentType());
            custOperatorInfo.setContIdentNo(custOperatorInfo.getIdentNo());
            result.add(custOperatorInfo);
        }
        return Page.listToPage(result, pageNum, pageSize, page.getPages(), page.getStartRow(), page.getTotal());
    }

    /***
     * 查询复核审批员信息
     * 
     * @param anMap
     * @return
     */
    public Page<CustOptData> queryCustOperatorByPage(final Map<String, String> anMap) {
        final int pageNum = anMap.get("pageNum") == null || "".equals(anMap.get("pageNum")) ? 1 : Integer.valueOf(anMap
                .get("pageNum"));
        final int pageSize = anMap.get("pageSize") == null || "".equals(anMap.get("pageSize")) ? 20 : Integer
                .valueOf(anMap.get("pageSize"));
        return this.findRequest(anMap, pageNum, pageSize);
    }

    private Page<CustOptData> findRequest(final Map<String, String> anParam, final int pageNum, final int pageSize) {
        final Map<String, Object> map = new HashMap<String, Object>();
        String tmpValue;
        for (final String tmpKey : queryConds) {
            tmpValue = anParam.get(tmpKey);
            if (StringUtils.isNotBlank(tmpValue)) {
                map.put(tmpKey, tmpValue);
            }
        }
        if (StringUtils.isNotBlank(anParam.get("ruleList"))) {
            map.remove("ruleList");
        }
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final String operOrg = custOperator.getOperOrg();
        map.put("operOrg", operOrg);
        final Page page = this.selectPropertyByPage(CustOperatorInfo.class, map, pageNum, pageSize, false);
        final List list = page.getResult();
        final List result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            final String ruleList = anParam.get("ruleList");
            final CustOperatorInfo tmpOperatorInfo = (CustOperatorInfo) list.get(i);
            // 根据操作员编号查询对应的角色信息
            final List<Role> roleList = operatorRoleRelationService.findRoleByOperId(tmpOperatorInfo.getId());
            for (final Role role : roleList) {
                if (StringUtils.isNotBlank(role.getRoleType()) && StringUtils.isNotBlank(ruleList)) {
                    if (role.getRoleType().contains(ruleList)) {
                        result.add(list.get(i));
                    }
                } else {
                    result.add(list.get(i));
                }
            }
        }
        return Page.listToPage(result);
    }

    /***
     * 查询操作员信息
     *
     * @param operatorId
     * @return
     */
    public CustOperatorInfo findOperatorById(final Long operatorId) {
        final CustOperatorInfo custOperatorInfo = this.selectByPrimaryKey(operatorId);
        final String roleList = operatorRoleRelationService.findSysRoleByOperatorId(custOperatorInfo.getId());
        if (StringUtils.isNotBlank(roleList)) {
            custOperatorInfo.setRuleList(roleList);
        }
        final String roleName = roleService.findRoleName(roleList);
        custOperatorInfo.setRoleName(roleName);
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
            if (StringUtils.isNotBlank(ruleList)) {
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
    public boolean savePasword(final String anNewPasswd, final String anOkPasswd, final String anPasswd) {
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
            if (StringUtils.isNotBlank(anPassword) && anPassword.equals(anOkPasswd)) {

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
        if (StringUtils.isNotBlank(anOperOrg) && user.getOperOrg().equals(anOperOrg)) {

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
        final boolean operCodeExists = this.custOptService.checkExistsByOperCodeAndOperOrg(request.getOperCode(),
                request.getOperOrg());
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
     * 
     * @param anOperOrg
     * @return
     */
    public CustOperatorInfo findCustClerkMan(final String anOperOrg, final String anClerkMan) {
        final Map termMap = QueryTermBuilder.newInstance().put("operOrg", anOperOrg).put("clerkMan", anClerkMan)
                .build();
        final List<CustOperatorInfo> tmpList = this.selectByProperty(termMap);

        return Collections3.getFirst(tmpList);
    }

    /***
     * 检查参数约束
     * 
     * @param request
     * @param anCustList
     */
    public void checkParam(final CustOperatorInfoRequest request, final String anCustList, final String anOperOrg) {
        final boolean optExists = this.custOptService.checkOperatorExists(request.getContIdentType(),
                request.getContIdentNo());
        if (optExists) {
            throw new BytterTradeException(40001, "抱歉，该证件号码已存在");
        }
        // 判断该操作员是否存在
        final boolean operCodeExists = this.custOptService.checkExistsByOperCodeAndOperOrg(request.getOperCode(),
                anOperOrg);
        if (operCodeExists) {
            throw new BytterTradeException(401, "抱歉，该操作员用户名存在【" + request.getOperCode() + "】");
        }
        // 判断邮箱是否存在
        Map<String, Object> anMap = new HashMap<String, Object>();
        anMap.put("email", request.getEmail());
        final boolean emailExists = this.custOptService.checkExistsByMap(anMap);
        if (emailExists) {
            throw new BytterTradeException(402, "抱歉，该操作员邮箱已存在");
        }
        // 判断手机号码是否存在
        anMap = new HashMap<String, Object>();
        anMap.put("mobileNo", request.getMobileNo());
        final boolean mobileExists = this.custOptService.checkExistsByMap(anMap);
        if (mobileExists) {
            throw new BytterTradeException(403, "抱歉，该操作员手机号已存在");
        }

        if (StringUtils.isBlank(request.getRuleList())) {
            logger.error("角色不能为空");
            throw new BytterTradeException(40001, "抱歉，角色不能为空");
        }
        final CustOperatorInfo custOperator = findCustClerkMan(anOperOrg, "1");
        if (custOperator != null && StringUtils.equalsIgnoreCase(request.getClerkMan(), "1")) {
            throw new BytterTradeException(40001, "对外经办人已存在，如需设置当前操作员为对外经办人，请先解除已有的对外经办人-" + custOperator.getName());
        }

        // if (BetterStringUtils.isBlank(anCustList)) {
        // logger.error("机构信息不能为空");
        // throw new BytterTradeException(40001, "抱歉，机构信息为空");
        // }
    }

    public void checkSaveParam(final CustOperatorInfo request) {
        final CustOperatorInfo operator = this.selectByPrimaryKey(request.getId());
        if (!StringUtils.equalsIgnoreCase(request.getContIdentNo(), operator.getContIdentNo())) {
            final boolean optExists = this.custOptService.checkOperatorExists(request.getContIdentType(),
                    request.getContIdentNo());
            if (optExists) {
                throw new BytterTradeException(40001, "抱歉，该证件号码已存在");
            }
        }
        if (!StringUtils.equalsIgnoreCase(request.getOperCode(), operator.getOperCode())) {
            final boolean operCodeExists = this.custOptService.checkExistsByOperCodeAndOperOrg(request.getOperCode(),
                    operator.getOperOrg());
            if (operCodeExists) {
                throw new BytterTradeException(401, "抱歉，该操作员用户名存在【" + request.getOperCode() + "】");
            }
        }

        Map<String, Object> anMap = new HashMap<String, Object>();
        if (!StringUtils.equalsIgnoreCase(request.getEmail(), operator.getEmail())) {
            anMap.put("email", request.getEmail());
            final boolean emailExists = this.custOptService.checkExistsByMap(anMap);
            if (emailExists) {
                throw new BytterTradeException(402, "抱歉，该操作员邮箱已存在");
            }
        }

        if (!StringUtils.equalsIgnoreCase(request.getMobileNo(), operator.getMobileNo())) {
            anMap = new HashMap<String, Object>();
            anMap.put("mobileNo", request.getMobileNo());
            final boolean mobileExists = this.custOptService.checkExistsByMap(anMap);
            if (mobileExists) {
                throw new BytterTradeException(403, "抱歉，该操作员手机号已存在");
            }
        }
        if (StringUtils.equalsIgnoreCase(request.getClerkMan(), "1")) {
            final CustOperatorInfo custOperator = findCustClerkMan(operator.getOperOrg(), "1");
            if (custOperator != null) {
                if (!StringUtils.equalsIgnoreCase(operator.getId().toString(), custOperator.getId().toString())) {
                    throw new BytterTradeException(40001, "对外经办人已存在，如需设置当前操作员为对外经办人，请先解除已有的对外经办人-"
                            + custOperator.getName());
                }
            }
        }

    }

    public List<CustOptData> findCustOperatorByClerk(final String anClerk) {
        final List result = new ArrayList<>();
        final Map<String, Object> map = new HashMap<String, Object>();
        final CustOperatorInfo custOperator = (CustOperatorInfo) UserUtils.getPrincipal().getUser();
        final String operOrg = custOperator.getOperOrg();
        map.put("operOrg", operOrg);
        map.put("clerkMan", anClerk);
        final List<CustOperatorInfo> list = this.selectByProperty(map);
        for (int i = 0; i < list.size(); i++) {
            final CustOperatorInfo custOperatorInfo = list.get(i);
            final String ruleList = operatorRoleRelationService.findSysRoleByOperatorId(custOperatorInfo.getId());
            if (StringUtils.isNotBlank(ruleList)) {
                custOperatorInfo.setRuleList(ruleList);
            }
            result.add(custOperatorInfo);
        }
        return result;
    }

}