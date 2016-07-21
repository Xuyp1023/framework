package com.betterjr.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.betterjr.common.data.SimpleDataEntity;
import com.betterjr.common.service.SpringContextHolder;
import com.betterjr.common.utils.CacheUtils;
import com.betterjr.modules.sys.entity.DictInfo;
import com.betterjr.modules.sys.entity.DictItemInfo;
import com.betterjr.modules.sys.service.DictItemService;
import com.betterjr.modules.sys.service.DictService;

/**
 * 字典工具类
 * 
 * @author zhoucy
 * @version 2015-8-16
 */
public class DictUtils {

    private static DictService dictService = SpringContextHolder.getBean(DictService.class);
    private static DictItemService dictItemService = SpringContextHolder.getBean(DictItemService.class);
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
     * <br>
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
    
    public static String getDictCode(String anType, String anValue) {

        return getDictCode(anType, anValue, null);
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
     * <br>
     * @param1 多个标签的值，使用逗号分隔<br>
     * @param2 默认值<br>
     * @return 使用逗号分隔的标签值<br>
     * @throws 异常情况
     * <br>
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
     * <br>
     * @param1 标签<br>
     * @param1 默认值<br>
     * @return 字典值<br>
     * @throws 异常情况
     * <br>
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
    public static synchronized DictItemInfo getDictItem(String anType, String anValue) {
        if (StringUtils.isNotBlank(anType) && StringUtils.isNotBlank(anValue)) {
            for (DictItemInfo dictItem : getDictList(anType)) {
                if (dictItem.getItemValue().equalsIgnoreCase(anValue)) {

                    return dictItem;
                }
            }
        }

        return null;
    }

    public synchronized static String createOutScript() {
        StringBuilder sb = new StringBuilder(4096);
        sb.append("var BTDict = new BetterDictionary(); \r\n");
        sb.append("function BetterDictionary() { \r\n");
        String paramName;
        for (DictInfo dict : dictService.findOutPutScript()) {
            paramName = dict.getDictCode();
            sb.append("\t  //").append(dict.getDictName()).append(";\r\n");
            sb.append("\t  this.").append(paramName).append(" = new ListMap();\r\n");
            for (DictItemInfo itemInfo : dictItemService.findOutScriptByGroup(dict.getId())) {
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

    public synchronized static void createAreaOutScript(StringBuilder anSB) {
        anSB.append("\t this.Provinces = new ListMap('id', 'name', {id: '', name: '', citys: null});\r\n");
        // anSB.append("\t (function initProvinceCitys(){  \r\n");
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
     * <br>
     * @return 字典内容列表<br>
     * @throws 异常情况
     * <br>
     */
    public static synchronized List<DictItemInfo> getDictList(String anType){
        @SuppressWarnings("unchecked")
        Map<String, List<DictItemInfo>> dictMap = (Map<String, List<DictItemInfo>>) CacheUtils.get(CACHE_DICT_MAP);
        if (dictMap == null) {
            dictMap = Maps.newHashMap();
            for (DictInfo dict : dictService.selectAll()) {
                List<DictItemInfo> dictList = dictMap.get(dict.getDictCode());
                if (dictList == null) {
                    dictMap.put(dict.getDictCode(), dictItemService.findByGroup(dict.getId()));
                }
            }
            CacheUtils.put(CACHE_DICT_MAP, dictMap);
        }
        List<DictItemInfo> dictList = dictMap.get(anType);
        if (dictList == null) {
            dictList = Lists.newArrayList();
        }
        return dictList;
    }
    
    public static Map<String, String> getDictMap(String anType){
        Map<String, String> map = new HashMap();
        for(DictItemInfo tmpD : getDictList(anType)){
            map.put(tmpD.getItemValue(), tmpD.getItemName());
        }
        return map;
        
    }
}
