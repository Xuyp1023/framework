/*
 *    Copyright 2009-2013 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.executor.keygen;

import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;

import com.betterjr.common.service.SelectKeyGenService;

/**
 * @author Clinton Begin
 * @author Jeff Butler
 */
public class SelectKeyGenerator implements KeyGenerator {

    public static final String SELECT_KEY_SUFFIX = "!selectKey";
    private boolean executeBefore;
    private MappedStatement keyStatement;

    public SelectKeyGenerator(MappedStatement keyStatement, boolean executeBefore) {
        this.executeBefore = executeBefore;
        this.keyStatement = keyStatement;
    }

    @Override
    public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        if (executeBefore) {
            processGeneratedKeys(executor, ms, parameter);
        }
    }

    @Override
    public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
        if (!executeBefore) {
            processGeneratedKeys(executor, ms, parameter);
        }
    }

    /**
     * 
     * <p>
     * 使用MyBatis
     * 的insert，update的特性；自动注入一些系统环境变量和序列的值；例如：seqID，操作时间、操作日期、操作员ID、操作员名称、客户端IP、
     * 会话ID等信息
     * </p>
     * <p>
     * 自定义的selectKey使用set 字段=变量的方式，字段为JAVA实体对象的属性名称，变量为将要赋值的系统取值，#ID表示自动增加的SEQ
     * </p>
     * <p>
     * #date表示字符型的当前日期(yyyyMMdd)，#time表示字符型的当前时间(hhmmss)，
     * #clientIP表示客户端的IP，#sessionID表示当前会话的ID
     * </p>
     * <p>
     * #userID表示当前客户的ID号，#userName表示当前客户名称，#operID表示当前操作员ID,#operName表示当前操作员名字
     * </p>
     * <p>
     * 如果定义自动增加字段，需要维护数据表：T_CFG_SNOGENERAL 中的相关信息， 例如：set id = #id, age= #id
     * </p>
     * <p>
     * 
     * @param anSQL
     *            在映射文件中定义的SQL
     *            </p>
     *            <p>
     * @param1 anMetaParam 操作对象的辅助对象
     *         </p>
     *         <p>
     * @param2 anObj 需要在数据库中保存的对象
     *         </p>
     *         <p>
     * @return true表示已经通过自定义的操作处理，false让MyBatis自己定义的来处理
     *         </p>
     */
    private boolean checkKeyGenerator(String anSQL, MetaObject anMetaParam, Configuration configuration, Object anObj) {
        if (StringUtils.isNotBlank(anSQL)) {
            anSQL = StringUtils.deleteWhitespace(anSQL);

            if (anSQL.toLowerCase().startsWith("set")) {
                String arrStr[] = anSQL.substring(3).split(",");
                int pos = 0;
                String modeName = keyStatement.getParameterMap().getType().getSimpleName();
                Map<String, Object> fieldMap = new HashMap();
                for (String tmpStr : arrStr) {
                    pos = tmpStr.indexOf("=");
                    String workField = tmpStr.substring(0, pos);
                    String workMode = tmpStr.substring(pos + 2);
                    // System.out.println(workField +" = " +workMode);
                    Object obj = SelectKeyGenService.getValue(workMode, modeName.concat(".").concat(workField));
                    Class cc = anMetaParam.getSetterType(workField);
                    // System.out.println(cc +", " + obj.getClass());
                    if (Number.class.isAssignableFrom(cc)) {
                        if (obj instanceof String) {
                            obj = Integer.parseInt((String) obj);
                        }
                        if ((obj instanceof Long) && Integer.class.isAssignableFrom(cc)) {
                            obj = new Integer(((Long) obj).intValue());
                        }
                    } else if (String.class.isAssignableFrom(cc)) {
                        if (obj instanceof Number) {
                            obj = obj.toString();
                        }
                    }

                    fieldMap.put(workField, obj);
                }
                String keyProperties[] = fieldMap.keySet().toArray(new String[fieldMap.size()]);
                MetaObject metaResult = configuration.newMetaObject(fieldMap);
                handleMultipleProperties(keyProperties, anMetaParam, metaResult);
                return true;
            }
        }
        return false;
    }

    private void processGeneratedKeys(Executor executor, MappedStatement ms, Object parameter) {
        try {
            if (parameter != null && keyStatement != null && keyStatement.getKeyProperties() != null) {
                String[] keyProperties = keyStatement.getKeyProperties();
                final Configuration configuration = ms.getConfiguration();
                final MetaObject metaParam = configuration.newMetaObject(parameter);
                if (keyProperties != null) {
                    if (checkKeyGenerator(this.keyStatement.getBoundSql(parameter).getSql(), metaParam, configuration,
                            parameter)) {

                        return;
                    }

                    // Do not close keyExecutor.
                    // The transaction will be closed by parent executor.
                    Executor keyExecutor = configuration.newExecutor(executor.getTransaction(), ExecutorType.SIMPLE);
                    List<Object> values = keyExecutor.query(keyStatement, parameter, RowBounds.DEFAULT,
                            Executor.NO_RESULT_HANDLER);
                    if (values.size() == 0) {
                        throw new ExecutorException("SelectKey returned no data.");
                    } else if (values.size() > 1) {
                        throw new ExecutorException("SelectKey returned more than one value.");
                    } else {
                        MetaObject metaResult = configuration.newMetaObject(values.get(0));
                        if (keyProperties.length == 1) {
                            if (metaResult.hasGetter(keyProperties[0])) {
                                setValue(metaParam, keyProperties[0], metaResult.getValue(keyProperties[0]));
                            } else {
                                // no getter for the property - maybe just a
                                // single value object
                                // so try that
                                setValue(metaParam, keyProperties[0], values.get(0));
                            }
                        } else {
                            handleMultipleProperties(keyProperties, metaParam, metaResult);
                        }
                    }
                }
            }
        }
        catch (ExecutorException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ExecutorException("Error selecting key or setting result to parameter object. Cause: " + e, e);
        }
    }

    private void handleMultipleProperties(String[] keyProperties, MetaObject metaParam, MetaObject metaResult) {
        String[] keyColumns = keyStatement.getKeyColumns();

        if (keyColumns == null || keyColumns.length == 0) {
            // no key columns specified, just use the property names
            for (int i = 0; i < keyProperties.length; i++) {
                setValue(metaParam, keyProperties[i], metaResult.getValue(keyProperties[i]));
            }
        } else {
            if (keyColumns.length != keyProperties.length) {
                throw new ExecutorException(
                        "If SelectKey has key columns, the number must match the number of key properties.");
            }
            for (int i = 0; i < keyProperties.length; i++) {
                setValue(metaParam, keyProperties[i], metaResult.getValue(keyColumns[i]));
            }
        }
    }

    private void setValue(MetaObject metaParam, String property, Object value) {
        if (metaParam.hasSetter(property)) {
            metaParam.setValue(property, value);
        } else {
            throw new ExecutorException("No setter found for the keyProperty '" + property + "' in "
                    + metaParam.getOriginalObject().getClass().getName() + ".");
        }
    }
}
