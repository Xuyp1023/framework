package com.betterjr.modules.sys.service;

import com.betterjr.common.config.ConfigBaseService;
import com.betterjr.common.config.ConfigFace;
import com.betterjr.common.service.BaseService;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.modules.sys.dao.SysConfigInfoMapper;
import com.betterjr.modules.sys.entity.SysConfigInfo;

import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class SysConfigService extends BaseService<SysConfigInfoMapper, SysConfigInfo> {

    private static ConfigBaseService configService = new ConfigBaseService(new ArrayList());

    @PostConstruct
    public void initConfigValue() {
        List<SysConfigInfo> configList = this.selectByProperty("status", "1");
        configService = new ConfigBaseService(configList);
    }

    public static String objToJsonString(String anKey, Object anObj) {

        return configService.objToJsonString(anKey, anObj);
    }

    public static Object getObject(Object anKey) {
        if (anKey instanceof String) {
            return configService.getObject((String) anKey);
        }
        else {
            return null;
        }
    }

    public static List<String> getList(String anKey) {
        
        return configService.getListValue(anKey);
    }

    public static String getString(Object anKey) {
        if (anKey instanceof String) {
            return configService.getString((String) anKey);
        }
        else {
            return null;
        }
    }
    
    public static void saveParamValue(String anParamName, String anParamValue) {
        SysConfigService sysConfigService = SpringContextHolder.getBean(SysConfigService.class);
        sysConfigService.updateParamValue(anParamName, anParamValue);
    }
    
    public void updateParamValue(String anParamName, String anParamValue) {
        ConfigFace face = configService.getConfig(anParamName);
        if (face != null && face instanceof SysConfigInfo) {
            SysConfigInfo configInfo = (SysConfigInfo) face;
            configInfo.setItemValue(anParamValue);
            this.updateByPrimaryKeySelective(configInfo);
        }
    }
}
