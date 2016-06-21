package com.betterjr.modules.account.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.common.web.Servlets;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.modules.account.data.CustOptData;
import com.betterjr.modules.account.service.CustOperatorRequestService;
 
/**
 * 机构操作员管理
 * 
 * @author xieyj
 *
 */
@Controller
@RequestMapping("/custOperator")
@RequiresAuthentication()
public class CustOperatorController {
    private static final Logger logger = LoggerFactory.getLogger(CustOperatorController.class);

    @Autowired
    private CustOperatorRequestService custOperatorReqService;

    /**
     * 新增操作员
     * 
     * @param anMap
     * @return
     */
    @RequestMapping(value = "/saveCustOperator", method = RequestMethod.POST)
    @RequiresRoles("OPERATOR_ADMIN")
    public @ResponseBody String saveCustOperator(HttpServletRequest request) {
        Map anMap = Servlets.getParametersStartingWith(request, "");
        System.out.println("入参：" + anMap);
        try {
            CustOptData workRequest = custOperatorReqService.saveCustOperator(anMap);
            // SaleWorkAccoRequest workRequest = saleTradeRequesetService.purchaseFund(anMap);
            System.out.println("新增操作员返回：" + workRequest);
            if (workRequest == null) {
                return AjaxObject.newBusin("新增操作员失败，请检查").toJson();
            }
            else {
                return AjaxObject.newOk("新增操作员成功", workRequest).toJson();
            }
        }
        catch (BytterTradeException btEx) {
            return AjaxObject.newBusin(btEx.getMessage()).toJson();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return AjaxObject.newBusin("新增操作员失败，请检查").toJson();
        }
    }

    /**
     * 编辑操作员
     * 
     * @param anMap
     * @return
     */
    @RequestMapping(value = "/editCustOperator", method = RequestMethod.POST)
    @RequiresRoles("OPERATOR_ADMIN")
    public @ResponseBody String editCustOperator(HttpServletRequest request) {
        Map anMap = Servlets.getParametersStartingWith(request, "");
        System.out.println("入参：" + anMap);
        try {
            CustOptData workRequest = custOperatorReqService.updateCustOperator(anMap);
            // SaleWorkAccoRequest workRequest = saleTradeRequesetService.purchaseFund(anMap);
            System.out.println("修改操作员返回：" + workRequest);
            if (workRequest == null) {
                return AjaxObject.newBusin("修改操作员失败，请检查").toJson();
            }
            else {
                return AjaxObject.newOk("修改操作员成功", workRequest).toJson();
            }
        }
        catch (BytterTradeException btEx) {
            return AjaxObject.newBusin(btEx.getMessage()).toJson();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return AjaxObject.newBusin("修改操作员失败，请检查").toJson();
        }
    }

    /**
     * 查询操作员
     * 
     * @param anMap
     * @return
     */
    @RequestMapping(value = "/queryCustOperatorByPage", method = RequestMethod.POST)
    @RequiresRoles("OPERATOR_ADMIN")
    public @ResponseBody String queryCustOperatorByPage(HttpServletRequest request) {
        Map anMap = Servlets.getParametersStartingWith(request, "");
        logger.info("入参:" + anMap);
        Object obj = SecurityUtils.getSubject().getPrincipal();
        logger.info("ent test:" + obj.getClass());
        logger.info("ent test OPERATOR_USER:" + SecurityUtils.getSubject().hasRole("OPERATOR_USER"));
        logger.info("ent test OPERATOR_ADMIN:" + SecurityUtils.getSubject().hasRole("OPERATOR_ADMIN"));

        try {
            Page<CustOptData> workRequest = custOperatorReqService.queryCustOperatorByPage(anMap);
            logger.info("查询操作员信息返回：" + workRequest);
            if (workRequest == null) {
                return AjaxObject.newError("查询操作员信息失败，请检查").toJson();
            }
            else {
                return AjaxObject.newOkWithPage("查询操作员信息成功", workRequest).toJson();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return AjaxObject.newError("查询操作员信息失败，请检查").toJson();
        }
    }
}
