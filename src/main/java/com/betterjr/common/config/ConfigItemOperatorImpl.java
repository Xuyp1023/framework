package com.betterjr.common.config;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.betterjr.common.utils.XmlUtils;

public abstract class ConfigItemOperatorImpl implements ConfigItemOpertor {

    @Override
    public List<String> getListValue() {
        List<String> list;

        if ("1".equals(this.getSplit())) {
            list = XmlUtils.split(this.getItemValue());
        } else {
            list = new LinkedList<String>();
        }
        return list;
    }

    @Override
    public Map<String, String> getMapValue() {
        Map<String, String> map = new LinkedHashMap<>();
        if ("2".equals(this.getSplit())) {
            StringTokenizer tokenizer = new StringTokenizer(this.getItemValue(), ";");
            while (tokenizer.hasMoreTokens()) {
                String[] subArr = tokenizer.nextToken().split(":");
                if (subArr.length == 2) {
                    map.put(subArr[0], subArr[1]);
                }
            }

        }
        return map;
    }
}
