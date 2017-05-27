package com.betterjr.modules.operator.dubboclient;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.operator.dubbo.IOperatorService;

@Service
public class OperatorDubboClientService implements IOperatorService {

    @Reference(interfaceClass=IOperatorService.class)
    private IOperatorService operatorService;
    
    @Override
    public String webAddCustOperator(Map<String, Object> anMap,String anCustList) {
        return operatorService.webAddCustOperator(anMap,anCustList);
    }

    @Override
    public String webUpdateCustOperator(Map<String, Object> anMap,String anCustList) {
        return operatorService.webUpdateCustOperator(anMap,anCustList);
    }

    @Override
    public String webQueryCustOperator(Map<String, String> anMap, int anPageNum, int anPageSize) {
        return operatorService.webQueryCustOperator(anMap,anPageNum,anPageSize);
    }

    @Override
    public String webFindSysMenuByMenuId(Integer anMenuId) {
        return operatorService.webFindSysMenuByMenuId(anMenuId);
    }

    @Override
    public String webFindSysMenuByMenuRole(String anRoleId) {
        return operatorService.webFindSysMenuByMenuRole(anRoleId);
    }

    @Override
    public String webFindAllSysMenu() {
        return operatorService.webFindAllSysMenu();
    }

    @Override
    public String webAddMenuRole(String anRoleId, String anRoleName, String anMenuIdArr) {
        return operatorService.webAddMenuRole(anRoleId,anRoleName,anMenuIdArr);
    }

    @Override
    public String webFindOperatorById(Long anOperatorId) {
        return operatorService.webFindOperatorById(anOperatorId);
    }

    @Override
    public String webFindCustOperator() {
        return operatorService.webFindCustOperator();
    }

    @Override
    public String webUpdatePassword(String anNewPasswd, String anOkPasswd, String anPasswd) {
        return operatorService.webUpdatePassword(anNewPasswd,anOkPasswd,anPasswd);
    }

    @Override
    public String webChangeUserPassword(Long anId, String anPassword, String anOkPasswd) {
        return  operatorService.webChangeUserPassword(anId,anPassword,anOkPasswd);
    }
    
    @Override
    public CustOperatorInfo findCustClerkMan(String anOperOrg,String anClerkMan){

        return operatorService.findCustClerkMan(anOperOrg,anClerkMan);
    }
    
    /***
     * 查询机构绑定的客户信息
     * @return
     */
    public String webFindOperatorCustInfo(){
        return operatorService.webFindOperatorCustInfo();
    }

    /**
     * 根据入参查询对应操作机构操作员
     */
    public String webFindCustOperatorByClerk(String anClerk) {
        return operatorService.webFindCustOperatorByClerk(anClerk);
    }
    public String webQueryCustOperatorByPage(Map<String, String> anMap){
        return operatorService.webQueryCustOperatorByPage(anMap);
    }
}
