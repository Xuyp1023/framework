package org.apache.ibatis.builder.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.ResultType;

import com.betterjr.common.entity.BetterjrEntity;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class AnnonHelper {

    /**
     * 获取全部的Field
     *
     * @param entityClass
     * @param fieldList
     * @return
     */
    private static List<Field> getAllField(Class<?> entityClass, List<Field> fieldList) {
        if (fieldList == null) {
            fieldList = new LinkedList<Field>();
        }
        if (entityClass.equals(Object.class)) {
            return fieldList;
        }
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            // 排除静态字段，解决bug#2
            if (!Modifier.isStatic(field.getModifiers())) {
                fieldList.add(field);
            }
        }
        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class) && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass) && !Collection.class.isAssignableFrom(superClass)))) {
            return getAllField(entityClass.getSuperclass(), fieldList);
        }
        return fieldList;
    }

    private static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return (Class) type;
        }
    }

    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
            return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }

    public static String[] parseSQL(Method anMethod) {
        org.apache.ibatis.annotations.Select ssct = anMethod.getAnnotation(org.apache.ibatis.annotations.Select.class);
        // System.out.println("this is parseSQL ");
        if (ssct == null) {
            return null;
        }

        // 先取定义的返回结果类来映射
        Class myClass = null;
        ResultType wc = anMethod.getAnnotation(ResultType.class);
        if (wc != null) {
            myClass = wc.value();
        }

        // 如果没有取到返回结果,则从方法的返回值的泛型中取类来影射
        if (myClass == null) {
            myClass = getClass(anMethod.getGenericReturnType(), 0);
            // 返回结果定义的是一个Map，直接返回
            if (Map.class.isAssignableFrom(myClass)) {
                return null;
            }

            // 如果没有取到返回结果,则从对象的泛型中取类来影射
            if (BetterjrEntity.class.isAssignableFrom(myClass) == false) {

                for (Type tt : anMethod.getDeclaringClass().getGenericInterfaces()) {
                    myClass = getClass(tt, 0);
                    if (BetterjrEntity.class.isAssignableFrom(myClass)) {
                        break;
                    }
                }
            }
        }
        if (Map.class.isAssignableFrom(myClass) || myClass == Map.class) {

            return null;
        }

        if (BetterjrEntity.class.isAssignableFrom(myClass)) {
            // Map<String, String> fieldMap = new HashMap();
            String[] arrStr = ssct.value();
            String sql = "";
            for (String tmpStr : arrStr) {
                sql = sql + tmpStr;
            }
            sql = sqlParse(myClass, sql);
            if (sql != null) {
                return new String[] { sql };
            }
        }

        return null;
    }

    public static List<SelectItem> findSelectItem(List<SelectItem> anList, Map<String, String> anFieldMap,
            Table anTable) {
        for (Map.Entry<String, String> field : anFieldMap.entrySet()) {
            net.sf.jsqlparser.schema.Column cc = new net.sf.jsqlparser.schema.Column(anTable, field.getKey());
            SelectExpressionItem it = new SelectExpressionItem(cc);
            it.setAlias(new Alias(field.getValue(), true));
            anList.add(it);
        }

        return anList;
    }

    public static String sqlParse(Class myClass, String sql) {
        Map<String, String> fieldMap = new HashMap();
        int workPos = sql.toLowerCase().indexOf(" from ");
        try {
            Statement stmt = CCJSqlParserUtil.parse(sql.substring(0, workPos));
            Select sss = (Select) stmt;
            PlainSelect select = (PlainSelect) sss.getSelectBody();
            List<Field> fieldList = getAllField(myClass, null);
            for (Field field : fieldList) {
                if (field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    fieldMap.put(column.name().toLowerCase(), field.getName());
                    // System.out.println(column.name().toLowerCase() + " =
                    // " + field.getName());
                }
            }

            String colName = null;
            String aliasName = null;
            List<SelectItem> itemList = new ArrayList();
            for (SelectItem item : select.getSelectItems()) {
                if (item instanceof AllColumns) {

                    itemList = findSelectItem(itemList, fieldMap, null);
                } else if (item instanceof AllTableColumns) {
                    AllTableColumns at = (AllTableColumns) item;
                    itemList = findSelectItem(itemList, fieldMap, at.getTable());
                } else {
                    SelectExpressionItem it = (SelectExpressionItem) item;
                    if (it.getAlias() == null) {
                        if (it.getExpression() instanceof net.sf.jsqlparser.schema.Column) {
                            net.sf.jsqlparser.schema.Column cc = (net.sf.jsqlparser.schema.Column) it.getExpression();
                            colName = cc.getColumnName();
                        }
                    } else {
                        colName = it.getAlias().getName();
                    }
                    aliasName = fieldMap.get(colName.toLowerCase());
                    if (aliasName == null) {
                        aliasName = colName;
                    }
                    it.setAlias(new Alias(aliasName, true));
                    itemList.add(it);
                }
            }
            select.setSelectItems(itemList);
            // System.out.println("XXXXXXZZZ :" + select.toString() + sql.substring(workPos));
            return select.toString() + sql.substring(workPos);
        }
        catch (JSQLParserException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String parseNode(String anData, Class anClass) {
        if (anClass != null && StringUtils.isNoneBlank(anData)) {
            if (Map.class.isAssignableFrom(anClass) || anClass == Map.class) {
                return anData;
            }

            String tmpData = sqlParse(anClass, anData);
            if (tmpData != null) {
                anData = tmpData;
            }
        }
        return anData;
    }
}
