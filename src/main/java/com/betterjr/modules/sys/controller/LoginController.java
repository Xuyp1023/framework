package com.betterjr.modules.sys.controller;

import static com.betterjr.common.web.ControllerExceptionHandler.exec;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betterjr.common.utils.Encodes;
import com.betterjr.common.web.AjaxObject;
import com.betterjr.common.web.Servlets;
import com.betterjr.modules.cert.dubboclient.CustCertDubboClientService;
import com.betterjr.modules.cert.entity.CustCertInfo;

@Controller
@RequestMapping(value = "/")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Inject
    private CustCertDubboClientService certDubboClientService;

    @RequestMapping(value = "roleList", method = {RequestMethod.POST})
    public @ResponseBody String roleList(final HttpServletRequest request) {
        try {
            final X509Certificate cert = Servlets.findCertificate(request);
            if (cert == null) {
                return AjaxObject.newError("请使用证书登陆！").toJson();
            }

            if (cert != null) {

                final CustCertInfo certInfo= certDubboClientService.checkValidityWithBase64(Encodes.encodeBase64(cert.getEncoded()));
                if (certInfo.validCertInfo()) {
                    return exec(()->certDubboClientService.queryCustCertRoleList(certInfo.getSerialNo()), "", logger);
                }
            }
        }
        catch (ServletException | IOException | CertificateEncodingException e) {
            // TODO e.printStackTrace();
            logger.error("获取机构角色列表错误！", e);
        }

        return AjaxObject.newError("获取机构角色列表错误！").toJson();
    }

    @RequestMapping(value = "login", method = {RequestMethod.GET,RequestMethod.POST})
    public String login(final String username, final String password) {
        // TODO System.out.println("controller " + username + ":" + password);
        return "/static/main.html";
    }

    @RequestMapping(value = "tokenLogin", method = {RequestMethod.GET,RequestMethod.POST})
    public String tokenLogin(final String username, final String password) {
        // TODO System.out.println("controller " + username + ":" + password);
        return "/static/main.html";
    }

    @RequestMapping(value = "logout", method = {RequestMethod.GET,RequestMethod.POST})
    public String logout() {
        return "/static/error.html";
    }

}
