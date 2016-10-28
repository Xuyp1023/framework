package com.betterjr.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.mapper.JsonMapper;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.CacheUtils;
import com.betterjr.modules.sys.entity.DictInfo;
import com.betterjr.modules.sys.entity.DictItemInfo;
import com.betterjr.modules.sys.service.DictService;

/**
 * 字典工具类
 * 
 * @author zhoucy
 * @version 2015-8-16
 */
public class DictUtils {

    private static DictService dictService = SpringContextHolder.getBean(DictService.class);
    public static final String CACHE_DICT_MAP = "dictMap";

    /**
     * 
     * 根据字段值获取字典的标签<br>
     * 
     * @param anType
     *            字典名称<br>
     * @param1 字典值<br>
     * @param1 如果没有找到使用的默认值<br>
     * @return 查找到的标签<br>
     * @throws 异常情况
     *             <br>
     */
    public static String getDictLabel(String anType, String anValue) {

        return getDictLabel(anType, anValue, anValue);
    }

    public static String getDictLabel(String anType, String anValue, String anDefaultLabel) {
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anValue)) {
            for (DictItemInfo dictItem : getDictList(anType)) {
                if (dictItem.getItemValue().equalsIgnoreCase(anValue)) {
                    return dictItem.getItemName();
                }
            }
        }
        return anDefaultLabel;
    }

    public static String getDictLabelByCode(String anType, String anValue) {
        String itemCode;
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anValue)) {
            for (DictItemInfo dictItem : getDictList(anType)) {
                itemCode = dictItem.getItemCode();
                if (BetterStringUtils.isNotBlank(itemCode) && itemCode.equalsIgnoreCase(anValue)) {

                    return dictItem.getItemName();
                }
            }
        }
        return anValue;
    }

    public static String getDictValueByCode(String anType, String anValue) {
        String itemCode;
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anValue)) {
            for (DictItemInfo dictItem : getDictList(anType)) {
                itemCode = dictItem.getItemCode();
                if (BetterStringUtils.isNotBlank(itemCode) && itemCode.equalsIgnoreCase(anValue)) {

                    return dictItem.getItemValue();
                }
            }
        }
        return anValue;
    }

    public static String getDictCode(String anType, String anValue) {

        return getDictCode(anType, anValue, null);
    }

    /**
     * 将对象以json格式保存在数据字典中
     * 
     * @param anType
     *            数据类型，之前需要再t_cfg_dict中定义
     * @param anKey
     *            数据的key值
     * @param anDesc
     *            数据描述
     * @param anObj
     *            保存的对象
     */
    public static void saveObject(String anType, String anKey, String anDesc, Object anObj) {
        if (anObj == null) {
            return;
        }
        String tmpValue;
        DictItemInfo dictItem=null;
        try {
            tmpValue = JsonMapper.getInstance().writeValueAsString(anObj);
            dictItem = getDictItem(anType, anKey);
            if (dictItem == null) {
                dictItem = dictService.saveDictItem(anType, anKey, tmpValue);
            }
            else {
                dictItem.setItemCode(tmpValue);
                dictService.saveDictItem(dictItem);
            }

        }
        catch (JsonProcessingException e) {
        }
        finally{
            //清除缓存
            CacheUtils.remove(CACHE_DICT_MAP,anType);
        }

    }

    public static void addDictItem(String anType, DictItemInfo anItem) {
        List<DictItemInfo> data = getDictList(anType);
        if (Collections3.isEmpty(data)) {
            data = new ArrayList<DictItemInfo>();
            Map<String, List<DictItemInfo>> dictMap = (Map<String, List<DictItemInfo>>) CacheUtils.get(CACHE_DICT_MAP);
            dictMap.put(anType, data);
        }
        data.add(anItem);
    }

    public static void saveObject(String anType, String anKey, Object anObj) {

        saveObject(anType, anKey, null, anObj);
    }

    /**
     * 
     * @param anType
     *            数据类型，之前需要再t_cfg_dict中定义
     * @param anKey
     *            数据的key值
     * @param anClass
     *            需要转换的对象
     * @return
     */
    public static <T> T loadObject(String anType, String anKey, Class<T> anClass) {
        String tmpCode = getDictCode(anType, anKey);
        if (BetterStringUtils.isBlank(tmpCode)) {

            return null;
        }

        return JsonMapper.getInstance().fromJson(tmpCode, anClass);
    }

    public static String getDictCode(String anType, String anValue, String anDefaultLabel) {
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anValue)) {
            for (DictItemInfo dictItem : getDictList(anType)) {
                if (dictItem.getItemValue().equalsIgnoreCase(anValue)) {
                    return dictItem.getItemCode();
                }
            }
        }
        return anDefaultLabel;
    }

    // 判断数据是否存在于给定的字典中，用于校验入参
    public static boolean isDictValue(String anType, String anValue) {
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anValue)) {
            for (DictItemInfo dictItem : getDictList(anType)) {
                if (dictItem.getItemValue().equalsIgnoreCase(anValue)) {

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 
     * 查找多个标签<br>
     * 
     * @param 字典名称
     *            <br>
     * @param1 多个标签的值，使用逗号分隔<br>
     * @param2 默认值<br>
     * @return 使用逗号分隔的标签值<br>
     * @throws 异常情况
     *             <br>
     */
    public static String getDictLabels(String anType, String anValues) {
        return StringUtils.join(getDictListLabels(anType, anValues), ",");
    }

    public static List<String> getDictListLabels(String anType, String anValues) {
        List<String> valueList = Lists.newArrayList();
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anValues)) {
            for (String anValue : StringUtils.split(anValues, ",")) {
                valueList.add(getDictLabel(anType, anValue));
            }
            return valueList;
        }

        return valueList;
    }

    /**
     * 
     * 根据标签获取字典值<br>
     * 
     * @param 字典名称
     *            <br>
     * @param1 标签<br>
     * @param1 默认值<br>
     * @return 字典值<br>
     * @throws 异常情况
     *             <br>
     */
    public static String getDictValue(String anType, String anLabel, String defaultValue) {
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anLabel)) {
            for (DictItemInfo dictItem : getDictList(anType)) {
                if (dictItem.getItemName().equalsIgnoreCase(anLabel)) {
                    return dictItem.getItemValue();
                }
            }
        }
        return defaultValue;
    }

    /**
     * 获取字典条目信息
     * 
     * @param 类型
     * @param 字典条目的值
     * @return
     */
    public static DictItemInfo getDictItem(String anType, String anValue) {
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anValue)) {
            for (DictItemInfo dictItem : getDictList(anType)) {
                if (dictItem.getItemValue().equalsIgnoreCase(anValue)) {

                    return dictItem;
                }
            }
        }

        return null;
    }

    public static String createOutScript() {
        StringBuilder sb = new StringBuilder(4096);
        sb.append("var BTDict = new BetterDictionary(); \r\n");
        sb.append("function BetterDictionary() { \r\n");
        String paramName;
        for (DictInfo dict : dictService.findOutPutScript()) {
            paramName = dict.getDictCode();
            sb.append("\t  //").append(dict.getDictName()).append(";\r\n");
            sb.append("\t  this.").append(paramName).append(" = new ListMap();\r\n");
            for (DictItemInfo itemInfo : dictService.findOutScriptByGroup(dict.getId())) {
                sb.append("\t  this.").append(paramName).append(".set('");
                sb.append(itemInfo.getItemValue()).append("','").append(itemInfo.getItemName()).append("');\r\n");
            }
            sb.append("\r\n");
        }
        sb.append("\r\n");
        createAreaOutScript(sb);

        sb.append("}\r\n");
        return sb.toString();
        // System.out.println(sb.toString());
    }

    public static void createAreaOutScript(StringBuilder anSB) {
        anSB.append("\t this.Provinces = new ListMap('id', 'name', {id: '', name: '', citys: null});\r\n");
        // anSB.append("\t (function initProvinceCitys(){ \r\n");
        anSB.append("\t   var citys;\r\n");

        for (SimpleDataEntity sde : AreaUtils.findProvList()) {
            anSB.append("\t   citys = new ListMap('id', 'name');\r\n");
            anSB.append("\t   this.Provinces.setItem({id: '").append(sde.getValue()).append("', name: '");
            anSB.append(sde.getName()).append("', citys: citys});\r\n");
            for (SimpleDataEntity subSde : AreaUtils.findAreaList(sde.getValue())) {
                anSB.append("\t   citys.set('").append(subSde.getValue()).append("', '").append(subSde.getName()).append("');\r\n");
            }
            anSB.append("\r\n");
        }
        // anSB.append("\t })();");
        anSB.append("\r\n");
    }

    /**
     * 
     * 获取字典信息<br>
     * 
     * @param 字典名称
     *            <br>
     * @return 字典内容列表<br>
     * @throws 异常情况
     *             <br>
     */
    public static List<DictItemInfo> getDictList(String anType) {
        @SuppressWarnings("unchecked")
        List<DictItemInfo> dictList = (List<DictItemInfo>) CacheUtils.get(CACHE_DICT_MAP,anType);
        if (dictList == null) {
           DictInfo dict = dictService.findByCode(anType);

           dictList=dictService.findByGroup(dict.getId());
           CacheUtils.put(CACHE_DICT_MAP, dict.getDictCode(), dictList);
        }
        if (dictList == null) {
            dictList = Lists.newArrayList();
        }
        return dictList;
    }


    public static Map<String, String> getDictMap(String anType) {
        Map<String, String> map = new HashMap();
        for (DictItemInfo tmpD : getDictList(anType)) {
            map.put(tmpD.getItemValue(), tmpD.getItemName());
        }
        return map;

    }

    /**
     * 查询核心企业信息，主要查询核心企业
     * @return
     */
    public static List<Long> findCoreCustNoList() {
        List<Long> result = new ArrayList();
        for (DictItemInfo item : DictUtils.getDictList("FactorCoreCustInfo")) {
            try {
                result.add(Long.parseLong(item.getItemCode()));
            }
            catch (Exception ex) {
            }
        }
        return result;
    }
    }
