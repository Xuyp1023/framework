package org.apache.ibatis.executor.resultset;

import java.util.Map;

public interface UnmappedEntity {
    public Map<String, Object> getMap();

    public Object removeUnmappedValues(String anPropName);

    public void addUnmappedColumn(String anPropName, Object anValue);
}
