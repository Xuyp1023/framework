package com.betterjr.modules.sys.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.betterjr.common.annotation.MetaData;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.UserUtils;
import com.betterjr.common.web.AjaxObject;

public class UserSecurityInterceptor implements HandlerInterceptor {
    private List<String> excludedUrls = new ArrayList<>();
    private String pcFailUrl ="/scf/app/etc/assignCredit.html";
    private String mobileFailUrl = "/scf/app/etc/buyerConfirm.html";
    private String forbidMessage = "你暂时无权访问该功能，请致电前海拜特互联网金融服务公司";

    private static final Logger logger = LoggerFactory.getLogger(UserSecurityInterceptor.class);

    public String getPcFailUrl() {
        return this.pcFailUrl;
    }

    public void setPcFailUrl(String anPcFailUrl) {
        this.pcFailUrl = anPcFailUrl;
    }

    public String getMobileFailUrl() {
        return this.mobileFailUrl;
    }

    public void setMobileFailUrl(String anMobileFailUrl) {
        this.mobileFailUrl = anMobileFailUrl;
    }

    public String getForbidMessage() {
        return this.forbidMessage;
    }

    public void setForbidMessage(String anForbidMessage) {
        this.forbidMessage = anForbidMessage;
    }

    @Override
    public void afterCompletion(HttpServletRequest anArg0, HttpServletResponse anArg1, Object anArg2, Exception anArg3) throws Exception {

    }

    public List<String> getExcludedUrls() {
        return excludedUrls;
    }

    public void setExcludedUrls(List<String> excludedUrls) {
        this.excludedUrls = excludedUrls;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse anArg1, Object anArg2, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        request.removeAttribute("startTime");
        long endTime = System.currentTimeMillis();
        logger.info("handlingTime :" +(  endTime - startTime));
    }

    /**
     * 检查是否是例外的URI地址
     * @param requestUri 申请地址
     * @return
     */
    private boolean checkExcludeUrl(String requestUri){
        logger.info("requestUri = "+requestUri);
        for (String url : excludedUrls) {
            if (requestUri.endsWith(url)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object anHandle) throws Exception {
        boolean bbq = true;
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        if ((anHandle != null) && (anHandle instanceof HandlerMethod)) {
            if (checkExcludeUrl(request.getRequestURI())){
                
                return true;
            }
            
            //logger.info(anHandle.getClass() + " preHandle anArg2:" + anHandle);
            HandlerMethod hd = (HandlerMethod) anHandle;
          //  logger.info("preHandle find DeclaringClass :" + hd.getMethod().getDeclaringClass());
            String[] resultMsg = new String[1];
            bbq = UserUtils.checkAccess(hd.getMethodAnnotation(MetaData.class), request.getParameter("password"), resultMsg);
            if (bbq == false) {
                String errMsg = resultMsg[0];
                if (BetterStringUtils.isBlank(errMsg)){
                    errMsg = forbidMessage;
                }
                if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))){
                    response.setContentType("application/json");
                    response.getWriter().write(AjaxObject.newForbidden(errMsg).toJson());
                }
                else {
                    response.sendRedirect(UserUtils.isMobileLogin() ? this.mobileFailUrl : this.pcFailUrl);
                }
            }
        }
        return bbq;
    }
}
