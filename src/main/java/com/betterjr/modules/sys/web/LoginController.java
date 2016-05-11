
package com.betterjr.modules.sys.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betterjr.common.config.Global;
import com.betterjr.common.exception.BytterTradeException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.security.shiro.session.SessionDAO;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.servlet.ValidateCodeServlet;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.CacheUtils;
import com.betterjr.common.utils.CookieUtils;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.common.web.BaseController;
import com.betterjr.common.web.Servlets;
import com.betterjr.modules.account.service.CustPassService;
import com.betterjr.modules.account.utils.UserUtils;
import com.betterjr.modules.sys.security.CaptchaFormAuthenticationFilter;
import com.betterjr.modules.sys.security.ShiroUser;
import com.google.common.collect.Maps;

/**
 * 登录Controller
 * 
 * @author zhoucy
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private CustPassService custPassService;

    /**
     * 登出
     */
    @RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
    public void loginout(HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println("logout=====");
        UserUtils.getSubject().logout();
        Map<String, Object> reslut = new HashMap<>();
        if (!"XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {// 不是ajax请求
            try {
                response.sendRedirect(request.getContextPath() + "/p/pages/login.html");
                return;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        reslut.put("code", 200);
        reslut.put("message", "退出成功");

        request.setAttribute("message", JsonMapper.toJsonString(reslut));
        response.setContentType("application/json");
        try {
            response.getWriter().write(JsonMapper.toJsonString(reslut));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePassword", method = { RequestMethod.POST })
    public @ResponseBody String updatePassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map anMap = Servlets.getParametersStartingWith(request, "");
        System.out.println("入参：" + anMap);
        try {
            Boolean workRequest = custPassService.updatePassword(anMap);
            // SaleWorkAccoRequest workRequest = saleTradeRequesetService.purchaseFund(anMap);
            System.out.println("修改密码成功返回：" + workRequest);

            UserUtils.getSubject().logout();
            return AjaxObject.newOk("修改密码成功", workRequest).toJson();

        }
        catch (BytterTradeException btEx) {
            return AjaxObject.newBusin(btEx.getMessage()).toJson();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return AjaxObject.newBusin(ex.getMessage()).toJson();
        }
    }

    /**
     * 管理登录
     */
    @RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        ShiroUser principal = UserUtils.getPrincipal();

        // // 默认页签模式
        // String tabmode = CookieUtils.getCookie(request, "tabmode");
        // if (tabmode == null){
        // CookieUtils.setCookie(response, "tabmode", "1");
        // }

        if (logger.isDebugEnabled()) {
            logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
            CookieUtils.setCookie(response, "LOGINED", "false");
        }

        // 如果已经登录，则跳转到管理首页
        if (principal != null && !principal.isMobileLogin()) {
            return "redirect:" + adminPath;
        }
        // String view;
        // view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
        // view = "classpath:";
        // view +=
        // "jar:file:/D:/GitHub/jeesite/src/main/webapp/WEB-INF/lib/jeesite.jar!";
        // view += "/"+getClass().getName().replaceAll("\\.",
        // "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
        // view += ".jsp";
        return "modules/sys/sysLogin";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
        ShiroUser principal = UserUtils.getPrincipal();

        // 如果已经登录，则跳转到管理首页
        if (principal != null) {
            return "redirect:" + adminPath;
        }

        String username = WebUtils.getCleanParam(request, CaptchaFormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        boolean rememberMe = WebUtils.isTrue(request, CaptchaFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
        boolean mobile = WebUtils.isTrue(request, CaptchaFormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
        String exception = (String) request.getAttribute(CaptchaFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        String message = (String) request.getAttribute(CaptchaFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);

        if (BetterStringUtils.isBlank(message) || BetterStringUtils.equals(message, "null")) {
            message = "用户或密码错误, 请重试.";
        }

        model.addAttribute(CaptchaFormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
        model.addAttribute(CaptchaFormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
        model.addAttribute(CaptchaFormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
        model.addAttribute(CaptchaFormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
        model.addAttribute(CaptchaFormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);

        if (logger.isDebugEnabled()) {
            logger.debug("login fail, active session size: {}, message: {}, exception: {}", sessionDAO.getActiveSessions(false).size(), message,
                    exception);
        }

        // 非授权异常，登录失败，验证码加1。
        if (!UnauthorizedException.class.getName().equals(exception)) {
            model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
        }

        // 验证失败清空验证码
        request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, SerialGenerator.uuid());

        // 如果是手机登录，则返回JSON字符串
        if (mobile) {
            return renderString(response, model);
        }

        return "modules/sys/sysLogin";
    }

    /**
     * 登录成功，进入管理首页
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "${adminPath}")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        ShiroUser principal = UserUtils.getPrincipal();

        // 登录成功后，验证码计算器清零
        isValidateCodeLogin(principal.getLoginName(), false, true);

        if (logger.isDebugEnabled()) {
            logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
        }

        // 如果已登录，再次访问主页，则退出原账号。
        if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))) {
            String logined = CookieUtils.getCookie(request, "LOGINED");
            if (BetterStringUtils.isBlank(logined) || "false".equals(logined)) {
                CookieUtils.setCookie(response, "LOGINED", "true");
            }
            else if (BetterStringUtils.equals(logined, "true")) {
                UserUtils.getSubject().logout();
                return "redirect:" + adminPath + "/login";
            }
        }

        // 如果是手机登录，则返回JSON字符串
        if (principal.isMobileLogin()) {
            if (request.getParameter("login") != null) {
                return renderString(response, principal);
            }
            if (request.getParameter("index") != null) {
                return "modules/sys/sysIndex";
            }
            return "redirect:" + adminPath + "/login";
        }

        // // 登录成功后，获取上次登录的当前站点ID
        // UserUtils.putCache("siteId",
        // StringUtils.toLong(CookieUtils.getCookie(request, "siteId")));

        // System.out.println("==========================a");
        // try {
        // byte[] bytes =
        // com.thinkgem.jeesite.common.utils.FileUtils.readFileToByteArray(
        // com.thinkgem.jeesite.common.utils.FileUtils.getFile("c:\\sxt.dmp"));
        // UserUtils.getSession().setAttribute("kkk", bytes);
        // UserUtils.getSession().setAttribute("kkk2", bytes);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        //// for (int i=0; i<1000000; i++){
        //// //UserUtils.getSession().setAttribute("a", "a");
        //// request.getSession().setAttribute("aaa", "aa");
        //// }
        // System.out.println("==========================b");
        return "modules/sys/sysIndex";
    }

    /**
     * 获取主题方案
     */
    @RequestMapping(value = "/theme/{theme}")
    public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response) {
        if (BetterStringUtils.isNotBlank(theme)) {
            CookieUtils.setCookie(response, "theme", theme);
        }
        else {
            theme = CookieUtils.getCookie(request, "theme");
        }
        return "redirect:" + request.getParameter("url");
    }

    /**
     * 是否是验证码登录
     * 
     * @param useruame
     *            用户名
     * @param isFail
     *            计数加1
     * @param clean
     *            计数清零
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean) {
        Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
        if (loginFailMap == null) {
            loginFailMap = Maps.newHashMap();
            CacheUtils.put("loginFailMap", loginFailMap);
        }
        Integer loginFailNum = loginFailMap.get(useruame);
        if (loginFailNum == null) {
            loginFailNum = 0;
        }
        if (isFail) {
            loginFailNum++;
            loginFailMap.put(useruame, loginFailNum);
        }
        if (clean) {
            loginFailMap.remove(useruame);
        }
        return loginFailNum >= 3;
    }
}
