package org.apache.ibatis.executor.resultset;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;

public class ProcessUnmapped implements Serializable {
    private static final long serialVersionUID = 1980944539226072107L;
    private static Map<String, ResultMapping> columnMap = null;
    private static final Log logger = LogFactory.getLog(ProcessUnmapped.class);

    // private static Map<String, ResultMapping> propertyMap = new
    // HashMap<String, ResultMapping>();

    public static boolean process(List<String> anColumnList, ResultSet anRecd, MetaObject metaObject, String columnPrefix) throws SQLException {
        Object obj = metaObject.getOriginalObject();
        if (metaObject.getOriginalObject() != null && obj instanceof UnmappedEntity) {
            UnmappedEntity entity = (UnmappedEntity) obj;
            for (String workColumn : anColumnList) {
                ResultMapping propertyMapping = columnMap.get(workColumn);
                if (propertyMapping == null) {
                    logger.warn("this column not config from project :".concat(workColumn));
                    continue;
                }
                else {
                    final TypeHandler<?> typeHandler = propertyMapping.getTypeHandler();
                    final String column = prependPrefix(propertyMapping.getColumn(), columnPrefix);
                    Object value = typeHandler.getResult(anRecd, column);
                    entity.addUnmappedColumn(propertyMapping.getProperty(), value);
                }
            }
        }
        return true;
    }

    private static String prependPrefix(String columnName, String prefix) {
        if (columnName == null || columnName.length() == 0 || prefix == null || prefix.length() == 0) {
            return columnName;
        }
        return prefix + columnName;
    }

    private static boolean compareResultMapping(ResultMapping one, ResultMapping other) {
        if ((one == null) && (other == null)) {
            return true;
        }
        else if (((one != null) && (other == null)) || ((one == null) && (other != null))) {
            return false;
        }
        return one.getColumn().equals(other.getColumn()) && one.getJavaType().equals(other.getJavaType())
                && one.getProperty().equals(other.getProperty());
    }

    public static void createMetaData(SqlSessionFactory anSessionFactory) {
        if (columnMap == null) {
            logger.warn("begin to init metaData");
            Map<String, ResultMapping> userMap = new HashMap();
            columnMap = new HashMap<String, ResultMapping>();
            for (Object tmpObj : anSessionFactory.getConfiguration().getResultMaps()) {
                if (tmpObj instanceof ResultMap) {
                    ResultMap map = (ResultMap) tmpObj;
                    List<ResultMapping> mapping = map.getResultMappings();
                    for (ResultMapping mp : mapping) {
                        if (mp.getColumn() != null) {
                        //    System.out.println(mp.getColumn());
                            ResultMapping tmpMP = userMap.get(mp.getColumn().trim());
                            if ((tmpMP != null) && (compareResultMapping(mp, tmpMP) == false)) {
                                // logger.warn(map.getId() + "-->" + mp.getColumn() + "-->" + mp.getProperty() + "-->" + mp.getJavaType());
                                 //logger.warn(map.getId() + "-->" + tmpMP.getColumn() + "-->" + tmpMP.getProperty() + "-->" + tmpMP.getJavaType());
                            }
                            else {
                                userMap.put(mp.getColumn().toUpperCase(Locale.ENGLISH), mp);
                            }
                        }
                    }
                }
            }
            synchronized (columnMap) {
                columnMap = userMap;
            }
            logger.warn("end to init metaData");
        }
    }
}
