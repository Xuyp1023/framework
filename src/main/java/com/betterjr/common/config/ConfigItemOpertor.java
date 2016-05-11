package com.betterjr.common.config;

import java.util.List;
import java.util.Map;

public interface ConfigItemOpertor {
    String getItemValue();

    String getSplit();

    List<String> getListValue();

    Map<String, String> getMapValue();

 }
