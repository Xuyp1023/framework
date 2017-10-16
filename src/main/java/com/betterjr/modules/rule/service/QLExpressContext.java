package com.betterjr.modules.rule.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.betterjr.common.config.ConfigService;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.exception.BytterFieldNotFoundException;
import com.betterjr.common.mapper.BeanMapperHelper;
import com.betterjr.common.service.SelectKeyGenService;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.account.data.SaleRequestFace;
import com.betterjr.modules.sys.service.SysConfigService;
import com.ql.util.express.IExpressContext;

/**
 * 表达式线下文信息，保存外部传输到表达式的内容。
 * 
 * @author zhoucy
 *
 */
public class QLExpressContext implements IExpressContext<String, Object> {

    private ApplicationContext context;
    private SaleRequestFace request = null;

    public SaleRequestFace getRequest() {
        return this.request;
    }

    // private static final String[] paramNames = new String[] { ParamNames.REQUEST, ParamNames.DATA };
    private Map map = new LinkedCaseInsensitiveMap();

    public QLExpressContext(ApplicationContext aContext) {
        this.context = aContext;
    }

    public QLExpressContext(Map<String, Object> aProperties, ApplicationContext aContext) {
        map.putAll(aProperties);
        this.context = aContext;
        Object obj;
        for (String tmpKey : aProperties.keySet()) {
            obj = aProperties.get(tmpKey);
            if (obj instanceof SaleRequestFace) {
                request = (SaleRequestFace) obj;
                break;
            }
        }
    }

    /**
     * 抽象方法：根据名称从属性列表中提取属性值
     */
    @Override
    public Object get(Object name) {
        Object result = null;
        result = map.get(name);
        if (result == null) {
            result = SysConfigService.getObject(name);
        }
        if (result == null) {
            if (request != null) {
                result = ConfigService.findConfigObj(name, request);
                if (result != null) {

                    map.put(name.toString(), result);
                }
            }
        }
        try {
            if (result == null && this.context != null && this.context.containsBean((String) name)) {
                // 如果在Spring容器中包含bean，则返回String的Bean
                result = this.context.getBean((String) name);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Object getBusinValue(String anName) {
        if (StringUtils.isBlank(anName)) {

            return null;
        }

        SimpleDataEntity sde = new SimpleDataEntity(anName);
        Object result = null;
        String name = sde.getValue();

        // 从系统参数中查找
        result = SysConfigService.getObject(name);
        if (result == null) {
            if (request != null) {
                // 从系统配置文件中查找
                result = ConfigService.findConfigObj(name, request);
                if (result != null) {

                    map.put(name.toString(), result);
                }
            }
        }
        try {
            if (result == null && this.context != null && this.context.containsBean(name)) {
                // 如果在Spring容器中包含bean，则返回String的Bean
                result = this.context.getBean(name);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 如果还是没有值，从数据字典或者SelectKey中查找。
        if ("Dict".equalsIgnoreCase(name)) {
            if (StringUtils.isNotBlank(sde.getThree())) {

                return DictUtils.getDictItem(sde.getThree(), sde.getValue());
            } else {

                return DictUtils.getDictList(sde.getName());
            }
        } else if ("selectKey".equalsIgnoreCase(name)) {
            if (StringUtils.isNotBlank(sde.getThree())) {

                return SelectKeyGenService.getValue(sde.getName(), sde.getThree());
            } else {

                return SelectKeyGenService.getValue(sde.getName(), sde.getName());
            }
        }

        // 找不到值，就返回他自己；适应于定义固定值的情况
        if (result == null) {

            return anName;
        }

        if (sde.onlyOne()) {

            return result;
        } else {
            if (result instanceof Map) {
                return ((Map) result).get(sde.getName());
            } else {
                if (StringUtils.isNotBlank(sde.getThree())) {
                    result = BeanMapperHelper.invokeGetterMethod(result, sde.getThree());
                }
                result = BeanMapperHelper.invokeGetterMethod(result, sde.getName());
            }

            return result;
        }
    }

    public Object getObjValue(String anName) {

        // 无需取值，直接返回
        if (StringUtils.isBlank(anName) || "Void".equalsIgnoreCase(anName)) {

            return null;
        }

        Object result = map.get(anName);
        if (result == null) {
            for (Object obj : map.values()) {
                if (obj == null) {
                    continue;
                }
                // 取对象属性信息
                if (obj instanceof BetterjrEntity) {
                    try {
                        result = BeanMapperHelper.invokeGetterMethod(obj, anName);

                        return result;
                    }
                    catch (BytterFieldNotFoundException ex) {

                        // 属性不在对象中，继续处理！
                    }
                } else if (obj instanceof Map) {
                    Map map = (Map) obj;
                    return map.get(anName);
                }
            }
        }

        return result;
    }

    public <T> T getBean(Class<T> requiredType) {

        return this.context.getBean(requiredType);
    }

    @Override
    public Object put(String name, Object object) {

        return map.put(name, object);
    }

}