package com.betterjr.modules.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class LoginController {

    @RequestMapping(value = "login", method = {RequestMethod.GET,RequestMethod.POST})
    public String login(final String username, final String password) {
        System.out.println("controller " + username + ":" + password);
        return "/static/error.html";
    }

    @RequestMapping(value = "logout", method = {RequestMethod.GET,RequestMethod.POST})
    public String logout() {
        return "/static/error.html";
    }

}
