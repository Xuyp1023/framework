package com.betterjr.common.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.betterjr.common.config.Global;
import com.betterjr.common.selectkey.SelectKeyGenIDService;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.common.utils.PropertiesHolder;
import com.betterjr.common.utils.SpringContextHolder;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemInitListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext sc = event.getServletContext();
        String jsParamFileName = sc.getInitParameter("jsParamFileName");
        initDictAndConfigInfo();
        if (BetterStringUtils.isNotBlank(jsParamFileName)) {
            String workPath = sc.getRealPath(jsParamFileName);
            logger.info("workPath:" + workPath);
            System.out.println("workPath:" + workPath);

            DictUtils.createOutScript(workPath);
        }
        else {
            logger.warn("Please Set Real jsParamFileName value In Config Web.XML");
        }
    }

    public static void initDictAndConfigInfo() {
        Global gb = Global.getInstance();
        System.out.println(gb.getConfig("operator.loginTimeDiff"));
        PropertiesHolder dd = SpringContextHolder.getBean(PropertiesHolder.class);
        SelectKeyGenIDService baseFieldsService = SpringContextHolder.getBean(SelectKeyGenIDService.class);
        DictUtils.getDictList("test");
        SerialGenerator dxxd = SpringContextHolder.getBean(SerialGenerator.class);
        // dxxd.init();
        SerialGenerator dddd = SpringContextHolder.getBean(SerialGenerator.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
