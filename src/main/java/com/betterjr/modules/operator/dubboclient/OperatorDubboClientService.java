package com.betterjr.modules.operator.dubboclient;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.operator.dubbo.IOperatorService;

@Service
public class OperatorDubboClientService implements IOperatorService {

    @Reference(interfaceClass = IOperatorService.class)
    private IOperatorService operatorService;

    @Override
    public String webAddCustOperator(final Map<String, Object> anMap, final String anCustList) {
        return operatorService.webAddCustOperator(anMap, anCustList);
    }

    @Override
    public String webUpdateCustOperator(final Map<String, Object> anMap, final String anCustList) {
        return operatorService.webUpdateCustOperator(anMap, anCustList);
    }

    @Override
    public String webQueryCustOperator(final Map<String, String> anMap, final int anPageNum, final int anPageSize) {
        return operatorService.webQueryCustOperator(anMap, anPageNum, anPageSize);
    }

    @Override
    public String webFindSysMenuByMenuId(final Integer anMenuId) {
        return operatorService.webFindSysMenuByMenuId(anMenuId);
    }

    @Override
    public String webFindSysMenuByMenuRole(final String anRoleId) {
        return operatorService.webFindSysMenuByMenuRole(anRoleId);
    }

    @Override
    public String webFindAllSysMenu() {
        return operatorService.webFindAllSysMenu();
    }

    @Override
    public String webAddMenuRole(final String anRoleId, final String anRoleName, final String anMenuIdArr) {
        return operatorService.webAddMenuRole(anRoleId, anRoleName, anMenuIdArr);
    }

    @Override
    public String webFindOperatorById(final Long anOperatorId) {
        return operatorService.webFindOperatorById(anOperatorId);
    }

    @Override
    public String webFindCustOperator() {
        return operatorService.webFindCustOperator();
    }

    @Override
    public String webUpdatePassword(final String anNewPasswd, final String anOkPasswd, final String anPasswd) {
        return operatorService.webUpdatePassword(anNewPasswd, anOkPasswd, anPasswd);
    }

    @Override
    public String webChangeUserPassword(final Long anId, final String anPassword, final String anOkPasswd) {
        return operatorService.webChangeUserPassword(anId, anPassword, anOkPasswd);
    }

    @Override
    public CustOperatorInfo findCustClerkMan(final String anOperOrg, final String anClerkMan) {

        return operatorService.findCustClerkMan(anOperOrg, anClerkMan);
    }

    /***
     * 查询机构绑定的客户信息
     * 
     * @return
     */
    @Override
    public String webFindOperatorCustInfo() {
        return operatorService.webFindOperatorCustInfo();
    }

    /**
     * 根据入参查询对应操作机构操作员
     */
    @Override
    public String webFindCustOperatorByClerk(final String anClerk) {
        return operatorService.webFindCustOperatorByClerk(anClerk);
    }

    @Override
    public String webQueryCustOperatorByPage(final Map<String, String> anMap) {
        return operatorService.webQueryCustOperatorByPage(anMap);
    }

    @Override
    public String webUpdateOperatorWithImageFile(final Map<String, Object> anMap, final String anCustList, final String anFileList) {

        return operatorService.webUpdateOperatorWithImageFile(anMap, anCustList, anFileList);
    }
}
