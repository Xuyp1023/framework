package com.betterjr.common.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.betterjr.common.data.SaleSimpleRequest;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.utils.reflection.ReflectionUtils;

//全局的参数服务
public class ConfigService {
    protected static Logger logger = LoggerFactory.getLogger(ConfigService.class);
    private static ConfigService instance;

    private Map<String, ConfigServiceFace> configMap = new LinkedCaseInsensitiveMap();
    private Map<String, List> configValueMap = new LinkedCaseInsensitiveMap();
    private List<ConfigServiceFace> configServices = new ArrayList();

    public void initConfig() {
        instance = this;
        logger.info("this is initConfig");
        for (ConfigServiceFace configFace : configServices) {
            configValueMap.put(configFace.getMyServiceName(), configFace.findAll());
            configMap.put(configFace.getMyServiceName(), configFace);
        }

        // SaleFundInfoService.initFundInfo();
    }

    public static List findConfigList(String anKey) {

        return instance.configValueMap.get(anKey);
    }

    public List<ConfigServiceFace> getConfigServices() {

        return configServices;
    }

    public void setConfigServices(List<ConfigServiceFace> anConfigServices) {

        configServices = anConfigServices;
    }

    public static Object findConfigObj(Object anKey, SaleSimpleRequest anRequest) {

        ConfigServiceFace configFace = instance.configMap.get(anKey);
        if (configFace != null) {
            return configFace.findOne(anRequest);
        }

        return null;
    }

    public static Object findConfigValue(String anKey, SaleSimpleRequest anRequest) {
        SimpleDataEntity sde = new SimpleDataEntity(anKey);
        Object obj = findConfigObj(sde.getValue(), anRequest);

        return ReflectionUtils.invokeGetter(obj, sde.getName());
    }
}
