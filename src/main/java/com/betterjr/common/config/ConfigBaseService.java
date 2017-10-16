package com.betterjr.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.betterjr.common.data.DataTypeInfo;
import com.betterjr.common.exception.BytterClassNotFoundException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.DataTypeConvert;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.sys.entity.DictItemInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ConfigBaseService implements java.io.Serializable {

    private static final long serialVersionUID = -5384502048759860599L;

    protected Map<String, ConfigFace> configInfo = new LinkedCaseInsensitiveMap();
    protected Map<String, Object> configCache = new LinkedCaseInsensitiveMap();

    public ConfigBaseService(Map anConfigInfo) {
        this.addAll(anConfigInfo);

    }

    public ConfigBaseService(List anConfigList) {
        this.configInfo.clear();
        this.configCache.clear();
        ConfigFace face;
        for (Object obj : anConfigList) {
            if (obj instanceof ConfigFace) {
                face = (ConfigFace) obj;
                configInfo.put(face.getItemName(), face);
            } else {
                throw new BytterValidException(50001, "use ConfigService ConfigItem Must implements ConfigFace");
            }
        }
    }

    public ConfigBaseService() {

    }

    public void addAll(Map<String, Object> anValueMap) {
        this.configInfo.clear();
        this.configCache.clear();
        for (Map.Entry<String, Object> ent : anValueMap.entrySet()) {
            if (ent.getValue() instanceof ConfigFace) {
                configInfo.put(ent.getKey(), (ConfigFace) ent.getValue());
            } else {
                throw new BytterValidException(50001, "use ConfigService ConfigItem Must implements ConfigFace");
            }
        }
    }

    public ConfigFace getConfig(String anKey) {

        return this.configInfo.get(anKey);
    }

    public String objToJsonString(String anKey, Object anObj) {

        return objToJsonString(anKey, anObj, "");
    }

    public String objToJsonString(String anKey, Object anObj, String anDefault) {
        ConfigFace value = configInfo.get(anKey);
        if (value != null && anObj != null) {
            try {

                return JsonMapper.getInstance().writeValueAsString(anObj);
            }
            catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return anDefault;
    }

    // 根据定义的数据类型，获取对象
    public Object getObject(String anKey) {
        Object obj = this.configCache.get(anKey);
        if (obj != null) {

            return obj;
        }

        ConfigFace value = configInfo.get(anKey);
        if (value != null) {
            String tmpMode = value.getSplit();
            Class cc = DataTypeInfo.getClass(value.getDataType());
            if ("0".equals(tmpMode) || StringUtils.isBlank(tmpMode)) {
                obj = value.getItemValue();

                return DataTypeConvert.convert(obj, cc);
            } else if ("1".equals(tmpMode)) {
                obj = value.getListValue();
                List result = new ArrayList();
                for (String tmpStr : (List<String>) obj) {
                    result.add(DataTypeConvert.convert(tmpStr, cc));
                }
                this.configCache.put(anKey, result);
                return result;
            } else if ("2".equals(tmpMode)) {
                Map<String, Object> map = new HashMap();
                for (Map.Entry<String, String> ent : value.getMapValue().entrySet()) {
                    map.put(ent.getKey(), DataTypeConvert.convert(ent.getValue(), cc));
                }
                this.configCache.put(anKey, map);
                return map;
            } else if ("3".equals(tmpMode)) {
                try {
                    obj = JsonMapper.getInstance().fromJson(value.getItemValue(), Class.forName(value.getClassType()));
                    this.configCache.put(anKey, obj);
                    return obj;
                }
                catch (ClassNotFoundException e) {
                    throw new BytterClassNotFoundException(50003,
                            "ConfigBaseServer getObject Not Find Declare ClassType ", e);
                }
            } else {
                throw new BytterValidException(50002, "use ConfigService ConfigItem Split Type Not implements ");
            }
        }
        return null;
    }

    public <T> T getObject(String anKey, Class<T> anClass) {
        ConfigFace value = configInfo.get(anKey);
        if (value != null) {

            return JsonMapper.getInstance().fromJson(value.getItemValue(), anClass);
        } else {
            throw new BytterValidException(50002, "use ConfigService ConfigItem Must Define ");
        }
    }

    public int getInt(String key) {

        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        ConfigFace value = configInfo.get(key);
        if (value == null) {
            return defaultValue;
        }

        return Integer.parseInt(value.getItemValue());
    }

    public long getLong(String key) {

        return getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        ConfigFace value = configInfo.get(key);
        if (value == null) {
            return defaultValue;
        }

        return Long.parseLong(value.getItemValue());
    }

    public Double getDouble(String key) {

        return getDouble(key, 0);
    }

    public Double getDouble(String key, double defaultValue) {
        ConfigFace value = configInfo.get(key);
        if (value == null) {
            return defaultValue;
        }

        return Double.parseDouble(value.getItemValue());
    }

    public Boolean getBoolean(String key) {

        return getBoolean(key, false);
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        ConfigFace value = configInfo.get(key);
        if (value == null) {
            return defaultValue;
        }

        return Boolean.parseBoolean(value.getItemValue());
    }

    public String getString(String key) {

        return getString(key, "");
    }

    public java.util.Date getDate(String key) {

        return getDate(key, null);
    }

    public java.util.Date getDate(String key, java.util.Date defaultValue) {
        ConfigFace value = configInfo.get(key);
        if (value == null) {

            return defaultValue;
        }

        return BetterDateUtils.parseDate(value.getItemValue());
    }

    public String getString(String key, String defaultValue) {
        ConfigFace value = configInfo.get(key);
        if (value == null) {
            return defaultValue;
        }

        return value.getItemValue();
    }

    public List<String> getListValue(String anKey) {
        ConfigFace value = configInfo.get(anKey);
        if (value == null) {
            return new ArrayList<>();
        }

        return value.getListValue();
    }

    public List<DictItemInfo> getDictItem(String anKey) {
        ConfigFace value = configInfo.get(anKey);
        if (value == null) {
            return new ArrayList();
        } else {
            return DictUtils.getDictList(value.getDictItem());
        }
    }

    public Map<String, String> getMapValue(String anKey) {
        ConfigFace value = configInfo.get(anKey);
        if (value == null) {
            return new HashMap<>();
        }

        return value.getMapValue();
    }
}
