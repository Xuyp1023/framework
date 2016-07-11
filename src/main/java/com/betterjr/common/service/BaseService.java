package com.betterjr.common.service;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.dao.SqlMapper;
import com.betterjr.common.exception.BytterClassNotFoundException;
import com.betterjr.common.exception.BytterValidException;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.mapper.common.Mapper;
import com.betterjr.mapper.entity.Example;
import com.betterjr.mapper.entity.Example.Criteria;
import com.betterjr.mapper.pagehelper.Page;
import com.betterjr.mapper.pagehelper.PageHelper;

@Service
public abstract class BaseService<D extends Mapper<T>, T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    protected D mapper;

    /**
     * 非常灵活的Mybatis SQL查询
     */
    @Autowired
    protected SqlMapper sqlMapper;

    /**
     * 
     * 根据对象查询单条记录
     * 
     * @param anRecord
     *            查询对象
     * @return 实体记录
     * @throws 异常情况
     * 
     */
    public T selectOne(T anRecord) {

        return (T) mapper.selectOne(anRecord);
    }

    /**
     * 
     * 根据实体中的属性值进行查询，查询条件使用等号
     * 
     * @param 查询条件
     * @return 查询结果
     * 
     */

    public List<T> select(T anRecord) {

        return mapper.select(anRecord);
    }

    /**
     * 查询所有记录
     * 
     * @return 查询结果
     * 
     */
    public List<T> selectAll() {

        return mapper.select(null);
    }

    /**
     * 根据实体中的属性查询总数，查询条件使用等号
     * 
     * @param 查询条件
     * @return 查询结果
     * 
     */
    public int selectCount(T anRecord) {

        return mapper.selectCount(anRecord);
    }

    /**
     * 根据实体主键查询实体，查询条件使用等号
     * 
     * @param 主键值
     * @return 查询结果
     * 
     */
    public T selectByPrimaryKey(Object anKey) {

        return (T) mapper.selectByPrimaryKey(anKey);
    }

    /**
     * 查询类的泛型类
     * 
     * @return 泛型类
     * @throws BytterClassNotFoundException
     * 
     */
    protected Class<T> findGenericClass() {
        try {
            // 通过反射取得Entity的Class.
            Object genericClz = getClass().getGenericSuperclass();
            if (genericClz instanceof ParameterizedType) {
                return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            }
        }
        catch (Exception e) {
            logger.error("error detail:", e);
        }
        throw new BytterClassNotFoundException("泛型定义没有找到");

    }

    /**
     * 根据实体的属性查询
     * 
     * @param 实体属性名称
     * @param1 实体属性值
     * @return 查询结果
     * 
     */
    public List<T> selectByProperty(String anProperty, Object anKey) {

        return selectByProperty(anProperty, anKey, null);
    }

    public List<T> selectByProperty(String anProperty, Object anKey, String anOrderBy) {
        if (StringUtils.isBlank(anProperty)) {
            logger.warn("selectByProperty 查询的属性或查询条件不存在");
            return Page.listToPage(new ArrayList());
        }

        Class workClass = findGenericClass();
        return selectByClassProperty(workClass, anProperty, anKey, anOrderBy);
    }

    /**
     * 根据实体的属性查询
     * 
     * @param 实体属性名称
     * @param1 实体属性值
     * @return 查询结果
     * 
     */
    public List<T> selectByListProperty(String anProperty, List anKeyList) {

        return selectByListProperty(anProperty, anKeyList, null);
    }

    public List<T> selectByListProperty(String anProperty, List anKeyList, String anOrderBy) {

        if (StringUtils.isBlank(anProperty)) {
            logger.warn("selectByProperty 查询的属性或查询条件不存在");
            return Page.listToPage(new ArrayList());
        }
        Class workClass = findGenericClass();

        return selectByClassProperty(workClass, anProperty, anKeyList, anOrderBy);
    }

    /**
     * 根据对象的ReferClass定义的属性，查询内容
     * 
     * @param 需要返回的对象
     *            ，必须Refer 一个定义的 Class
     * @param 属性
     * @param 条件
     * @return
     */
    public List selectByClassProperty(Class anClass, String anProperty, Object anKey) {

        return selectByClassProperty(anClass, anProperty, anKey, null);
    }

    public List selectByClassProperty(Class anClass, String anProperty, Object anKey, String anOrderBy) {
        Example ex = findExample(anClass, anProperty, anKey);
        if (ex == null) {
            return Page.listToPage(new ArrayList());
        }

        if (BetterStringUtils.isNotBlank(anOrderBy)) {
            ex.setOrderByClause(anOrderBy);
        }
        return mapper.selectByExample(ex);
    }

    private Criteria findCriteria(Criteria criteria, String propName, Object workValue) {
        if (workValue == null) {
            criteria.andIsNull(propName);

            return criteria;
        }

        if (workValue instanceof List) {
            List tmpL = (List) workValue;
            if (Collections3.isEmpty(tmpL)) {

                // 不处理
            }
            else if (tmpL.size() == 1) {
                criteria.andEqualTo(propName, tmpL.get(0));
            }
            else {
                criteria.andIn(propName, tmpL);
            }
        }
        else if (workValue.getClass().isArray()) {
            if (Array.getLength(workValue) == 0) {

                // 不处理
            }
            else if (Array.getLength(workValue) == 1) {
                criteria.andEqualTo(propName, Array.get(workValue, 0));
            }
            else {
                criteria.andIn(propName, Collections3.arrayToList(workValue));
            }
        }
        else {
            if (propName.startsWith("GTE")) {
                propName = propName.substring(3);
                criteria.andGreaterThanOrEqualTo(propName, workValue);
            }
            else if (propName.startsWith("LTE")) {
                propName = propName.substring(3);
                criteria.andLessThanOrEqualTo(propName, workValue);
            }
            else if (propName.startsWith("GT")) {
                propName = propName.substring(2);
                criteria.andGreaterThan(propName, workValue);
            }
            else if (propName.startsWith("LT")) {
                propName = propName.substring(2);
                criteria.andLessThan(propName, workValue);

            }
            else if (propName.startsWith("LIKE")) {
                propName = propName.substring(4);
                criteria.andLike(propName, workValue.toString());
            }
            else {
                criteria.andEqualTo(propName, workValue);
            }
        }
        return criteria;
    }

    public List selectByClassProperty(Class anClass, Map<String, Object> anPropValue) {

        return selectByClassProperty(anClass, anPropValue, null);
    }

    protected Example findExample(Map<String, Object> anPropValue) {

        return findExample(this.findGenericClass(), anPropValue);
    }

    protected Example findExample(Class anClass, Map<String, Object> anPropValue) {
        Example ex = new Example(anClass);

        Criteria criteria = ex.createCriteria();
        for (Map.Entry<String, Object> ent : anPropValue.entrySet()) {
            criteria = findCriteria(criteria, ent.getKey(), ent.getValue());
        }

        if (criteria.isValid()) {
            return ex;
        }
        else {
            return null;
            // throw new BytterValidException(40111, "this Criteria not find in Example, will operate all record");
        }

    }

    public List selectByClassProperty(Class anClass, Map<String, Object> anPropValue, String anOrderBy) {
        Example ex = findExample(anClass, anPropValue);
        if (ex == null) {
            return Page.listToPage(new ArrayList());
        }
        if (BetterStringUtils.isNotBlank(anOrderBy)) {
            ex.setOrderByClause(anOrderBy);
        }
        try {
            return mapper.selectByExample(ex);
        }
        finally {
            Example.clearRefferClass();
        }



    }

    /**
     * 根据实体的属性值映射关系查询实体
     * 
     * @param 属性和值的映射关系组
     * @return 查询结果
     * 
     */
    public List<T> selectByProperty(Map<String, Object> anPropValue) {

        return selectByProperty(anPropValue, null);
    }

    public List<T> selectByProperty(Map<String, Object> anPropValue, String anOrderBy) {
        Class workClass = findGenericClass();

        return selectByClassProperty(workClass, anPropValue, anOrderBy);
    }

    protected Example findExample(String anProperty, Object anKey) {

        return findExample(findGenericClass(), anProperty, anKey);
    }

    protected Example findExample(Class anClass, String anProperty, Object anKey) {
        Example ex = new Example(anClass);
        Criteria criteria = ex.createCriteria();
        criteria = findCriteria(criteria, anProperty, anKey);
        if (criteria.isValid()) {

            return ex;
        }
        else {
            return null;
            // throw new BytterValidException(40111, "this Criteria not find in Example, will operate all record");
        }
    }

    public int selectCountByProperty(String anProperty, Object anKey) {
        Example ex = findExample(anProperty, anKey);
        if (ex == null) {
            return 0;
        }

        return this.selectCountByExample(ex);
    }

    /**
     * 根据实体的属性值映射关系查询实体
     * 
     * @param 属性和值的映射关系组
     * @return 查询结果
     * 
     */
    public int selectCountByProperty(Map<String, Object> anPropValue) {
        Class workClass = findGenericClass();

        return selectCountByClassProperty(workClass, anPropValue);
    }

    public int selectCountByClassProperty(Class anClass, Map<String, Object> anPropValue) {
        Example ex = findExample(anClass, anPropValue);
        if (ex == null) {
            return 0;
        }

        return mapper.selectCountByExample(ex);
    }

    public BigDecimal selectSumByClassProperty(Class anClass, String anField, Object anValue, String anSumField) {
        Map<String, Object> map = new HashMap();
        map.put(anField, anValue);

        return selectSumByClassProperty(anClass, map, anSumField);
    }

    public BigDecimal selectSumByProperty(String anField, Object anValue, String anSumField) {
        Class workClass = findGenericClass();

        return selectSumByClassProperty(workClass, anField, anValue, anSumField);
    }

    public BigDecimal selectSumByProperty(Map<String, Object> anPropValue, String anSumField) {
        Class workClass = findGenericClass();

        return selectSumByClassProperty(workClass, anPropValue, anSumField);
    }

    public BigDecimal selectSumByClassProperty(Class anClass, Map<String, Object> anPropValue, String anSumField) {
        Example ex = findExample(anClass, anPropValue);
        if (ex == null) {
            return BigDecimal.ZERO;
        }
        ex.addSumField(anSumField);
        return mapper.selectSumByExample(ex);
    }

    /**
     * 创建数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * 
     * @param entity
     *            待创建数据对象
     */
    protected void preInsert(T anEntity) {

    }

    /**
     * 插入对象
     * 
     * @param 实体值
     * @return 插入操作记录数
     */
    public int insert(T anRecord) {
        preInsert(anRecord);
        return mapper.insert(anRecord);
    }

    /**
     * 插入对象，没有值的对象不会插入
     * 
     * @param 实体值
     * @return 插入操作记录数
     * 
     */
    public int insertSelective(T anRecord) {
        preInsert(anRecord);
        return mapper.insertSelective(anRecord);
    }

    /**
     * 更新数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * 
     * @param entity
     *            待更新数据对象
     */
    protected void preUpdate(T entity) {

    }

    /**
     * 根据主键更新实体全部字段，null值会被更新
     * 
     * @param 对象
     * @return 插入操作记录数
     * 
     */
    public int updateByPrimaryKey(T anRecord) {
        preUpdate(anRecord);

        return mapper.updateByPrimaryKey(anRecord);
    }

    /**
     * 根据主键更新属性不为null的值
     * 
     * @param 对象
     * @return 插入操作记录数
     */
    public int updateByPrimaryKeySelective(T anRecord) {
        preUpdate(anRecord);
        return mapper.updateByPrimaryKeySelective(anRecord);
    }

    /**
     * 
     * 根据实体属性作为条件进行删除，查询条件使用等号
     * 
     * @param 对象
     * 
     * @return 删除操作记录数
     * 
     */
    public int delete(T anRecord) {

        return mapper.delete(anRecord);
    }

    /**
     * 
     * 根据主键字段进行删除，方法参数必须包含完整的主键属性
     * 
     * @param 主键参数值
     * 
     * @return 删除操作记录数
     * 
     */
    public int deleteByPrimaryKey(Object anKey) {

        return mapper.deleteByPrimaryKey(anKey);
    }

    /**
     * 根据Example条件进行查询
     * 
     * @param Example条件
     * @return 对象列表
     * 
     */
    public List<T> selectByExample(Object anExample) {

        return mapper.selectByExample(anExample);
    }

    /**
     * 根据Example条件进行查询总数
     * 
     * @param Example条件
     * @return 对象记录数
     * 
     */
    public int selectCountByExample(Object anExample) {

        return mapper.selectCountByExample(anExample);
    }

    /**
     * 根据Example条件删除数据
     * 
     * @param Example条件
     * @return 删除对象记录数
     * 
     */
    public int deleteByExample(Example anExample) {

        return mapper.deleteByExample(anExample);
    }

    public int deleteByProperty(String anProper, Object anObj) {
        Map map = new HashMap();
        map.put(anProper, anObj);

        return deleteByExample(map);
    }

    public int deleteByExample(Map anProperties) {
        Example ex = findExample(anProperties);

        return mapper.deleteByExample(ex);
    }

    /**
     * 根据Example条件更新数据，null属性也会更新
     * 
     * @param Example条件
     * @return 更新记录数
     * 
     */
    public int updateByExample(T anRecord, Map anProperties) {
        Example ex = findExample(anProperties);
        if (ex == null) {
            return 0;
        }

        return mapper.updateByExample(anRecord, ex);
    }

    public int updateByExample(T anRecord, Example anExample) {

        return mapper.updateByExample(anRecord, anExample);
    }

    /**
     * 根据Example条件更新数据，不会更新null属性
     * 
     * @param Example条件
     * @return 更新记录数
     * 
     */
    public int updateByExampleSelective(T anRecord, Example anExample) {

        return mapper.updateByExampleSelective(anRecord, anExample);
    }

    public int updateByExampleSelective(T anRecord, String anKey, Object anValues) {
        Example ex = findExample(anKey, anValues);
        if (ex == null) {
            return 0;
        }

        return mapper.updateByExampleSelective(anRecord, ex);
    }

    public int updateByExampleSelective(T anRecord, Map anProperties) {
        Example ex = findExample(anProperties);
        if (ex == null) {
            return 0;
        }

        return mapper.updateByExampleSelective(anRecord, ex);
    }

    /**
     * 根据Example条件查询数据，定义区间段
     * 
     * @param1 Example条件
     * 
     * @param2 查询区间段
     * 
     * @return 查询记录列表
     * 
     */
    public List<T> selectByExampleAndRowBounds(Object anExample, RowBounds anRowBounds) {

        return mapper.selectByExampleAndRowBounds(anExample, anRowBounds);
    }

    /**
     * 
     * 根据实体条件查询数据，定义区间段
     * 
     * @param1 实体记录
     * 
     * @param2 查询区间段
     * @return 查询记录列表
     * 
     */
    public List<T> selectByRowBounds(T anRecord, RowBounds anRowBounds) {

        return mapper.selectByRowBounds(anRecord, anRowBounds);
    }

    /**
     * 根据Example条件分頁查询数据
     * 
     * @param Example条件
     * @param1 页号
     * @param2 每页记录数
     * 
     * @return 查询结果
     * 
     */
    public Page<T> selectByExampleAndPage(Example anExample, int anPageNum, int anPageSize, boolean anFirst) {
        PageHelper.startPage(anPageNum, anPageSize, anFirst);

        return (Page) mapper.selectByExample(anExample);
    }

    /**
     * 根据Example条件分頁查询数据
     * 
     * @param Example条件
     * 
     * @param1 页号
     * @param2 每页记录数
     * @return 查询结果
     * 
     */
    public Page<T> selectByPage(T anRecord, int anPageNum, int anPageSize, boolean anFirst) {
        PageHelper.startPage(anPageNum, anPageSize, anFirst);

        return (Page) mapper.select(anRecord);
    }

    /**
     * 根据Example条件分頁查询数据
     * 
     * @param Example条件
     * @param1 页号
     * @param2 每页记录数
     * @return 查询结果
     * 
     */
    public Page<T> selectPropertyByPage(String anProperty, Object anKey, int anPageNum, int anPageSize, boolean anFirst) {
        PageHelper.startPage(anPageNum, anPageSize, anFirst);

        return (Page) this.selectByProperty(anProperty, anKey);
    }

    /**
     * 根据Example条件分頁查询数据
     * 
     * @param Example条件
     * @param1 页号
     * @param2 每页记录数
     * @return 查询结果
     * 
     */
    public Page selectPropertyByPage(Class anClass, Map<String, Object> anMap, int anPageNum, int anPageSize, boolean anFirst) {
        PageHelper.startPage(anPageNum, anPageSize, anFirst);

        return (Page) this.selectByClassProperty(anClass, anMap);
    }

    /**
     * 根据Example条件分頁查询数据
     * 
     * @param Example条件
     * @param1 页号
     * @param2 每页记录数
     * @return 查询结果
     * 
     */
    public Page<T> selectPropertyByPage(Map<String, Object> anMap, int anPageNum, int anPageSize, boolean anFirst) {
        PageHelper.startPage(anPageNum, anPageSize, anFirst);

        return (Page) this.selectByProperty(anMap);
    }

    /**
     * 根据Example条件分頁查询数据
     * 
     * @param Example条件
     * @param1 页号
     * @param2 每页记录数
     * @return 查询结果
     * 
     */
    public Page<T> selectPropertyByPage(Map<String, Object> anMap, int anPageNum, int anPageSize, boolean anFirst, String anOrderBy) {
        PageHelper.startPage(anPageNum, anPageSize, anFirst);

        return (Page) this.selectByProperty(anMap, anOrderBy);
    }

}