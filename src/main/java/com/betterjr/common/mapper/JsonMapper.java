package com.betterjr.common.mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterjr.common.exception.BettjerNestedException;
import com.betterjr.common.exception.BytterValidException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper. 封装不同的输出风格, 使用不同的builder函数创建实例.
 * 
 * @author zhoucy
 */
public class JsonMapper extends ObjectMapper {

    private static final long serialVersionUID = 2572525755468717616L;

    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
    public static String WORK_DATANODE = "data#@$%";

    private static JsonMapper mapper;
    private static JsonMapper nonDefaultMapper = new JsonMapper(Include.NON_DEFAULT);

    public JsonMapper() {
        this(Include.NON_EMPTY);
    }

    private static List prepareList(List dataList) {
        if (dataList.size() == 1 && dataList.get(0) instanceof List) {
            dataList = (List) dataList.get(0);
            return prepareList(dataList);
        }

        return dataList;
    }

    public static Map<String, Object> prepareResult(Object anObj) {
        List dataList = null;
        List result = new LinkedList<>();
        if (anObj instanceof List) {
            dataList = prepareList((List) anObj);
            Map<String, Object> map = new HashMap();
            map.put(WORK_DATANODE, dataList);
            return map;
        } else if (anObj instanceof Map) {
            result.add(anObj);
            return (Map) anObj;
        } else {
            Map<String, Object> map = new HashMap();
            map.put(WORK_DATANODE, anObj);
            return map;
        }
    }

    public static List prepareResult123(Object anObj) {
        List dataList = null;
        List result = new LinkedList<>();
        if (anObj instanceof List) {
            dataList = prepareList((List) anObj);
            return dataList;
        } else if (anObj instanceof Map) {
            result.add(anObj);
            return result;
        } else {
            dataList = new LinkedList<>();
        }
        int listSize;
        Map subMap;
        for (Object obj : dataList) {
            if (obj instanceof List) {
                List tmpList = (List) obj;
                listSize = tmpList.size();

                if (listSize == 1) {
                    Object subObj = tmpList.get(0);
                    if (subObj instanceof List) {
                        tmpList = (List) subObj;
                        listSize = tmpList.size();
                        if (listSize == 1) {
                            result.add(tmpList.get(0));
                        } else if (listSize > 1) {
                            result.add(tmpList);
                        }
                    } else if (subObj instanceof Map) {
                        subMap = (Map) subObj;
                        if (subMap.size() > 0) {
                            result.add(subMap);
                        }
                    }
                } else if (listSize > 1) {
                    result.add(tmpList);
                }
            } else if (obj instanceof Map) {
                subMap = (Map) obj;
                if (subMap.size() > 0) {
                    result.add(subMap);
                }
            } else {
                result.add(obj);
            }
        }
        return result;
    }

    public static Map<String, Object> parserJson(String anText) {
        Object obj = nonDefaultMapper.fromJson(anText, Object.class);

        return prepareResult(obj);
    }

    public JsonMapper(Include include) {
        // 设置输出时包含属性的风格
        if (include != null) {
            this.setSerializationInclusion(include);
        }
        // 允许单引号、允许不带引号的字段名称
        this.enableSimple();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                jgen.writeString("");
            }
        });
        // 进行HTML解码。
        this.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonProcessingException {
                jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
            }
        }));

        this.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        // 设置时区
        this.setTimeZone(TimeZone.getDefault());// getTimeZone("GMT+8:00")
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
     */
    public static JsonMapper getInstance() {
        if (mapper == null) {
            mapper = new JsonMapper().enableSimple();
        }
        return mapper;
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper, 最节约的存储方式，建议在内部接口中使用。
     */
    public static JsonMapper nonDefaultMapper() {
        if (mapper == null) {
            mapper = new JsonMapper(Include.NON_DEFAULT);
        }
        return mapper;
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object) {
        try {
            return this.writeValueAsString(object);
        }
        catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     * 
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     * 
     * @see #fromJson(String, JavaType)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, clazz);
        }
        catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     * 
     * @see #createCollectionType(Class, Class...)
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) this.readValue(jsonString, javaType);
        }
        catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 创建输出全部属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNormalMapper() {
        return new JsonMapper(Include.ALWAYS);
    }

    /**
     * 创建只输出非空属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNonNullMapper() {
        return new JsonMapper(Include.NON_NULL);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNonDefaultMapper() {
        return new JsonMapper(Include.NON_DEFAULT);
    }

    /**
     * 创建只输出初始值被改变的属性到Json字符串的Mapper.
     */
    public static JsonMapper buildOutDefaultMapper() {
        JsonMapper jm = new JsonMapper(Include.NON_DEFAULT);
        jm.setSerializationInclusion(Include.ALWAYS);
        jm.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        return jm;
    }

    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
     */
    public static JsonMapper buildNonEmptyMapper() {
        return new JsonMapper(Include.NON_EMPTY);
    }

    @SuppressWarnings("unchecked")
    public <T> T fromJson(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
        return (T) this.fromJson(jsonString, constructParametricType(parametrized, parameterClasses));
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> fromJsonToList(String jsonString, Class<T> classMeta) {
        return (List<T>) this.fromJson(jsonString, constructParametricType(List.class, classMeta));
    }

    /**
     * 構造泛型的Type如List<MyBean>, 则调用constructParametricType(ArrayList.class,MyBean.class) Map<String,MyBean>则调用(HashMap.class,String.class,
     * MyBean.class)
     */
    public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return mapper.getTypeFactory().constructParametrizedType(parametrized, parametrized, parameterClasses);
    }

    /**
     * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     */
    @SuppressWarnings("unchecked")
    public <T> T update(String jsonString, T object) {
        try {
            return (T) this.readerForUpdating(object).readValue(jsonString);
        }
        catch (JsonProcessingException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * 輸出JSONP格式數據.
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum, 為False時時使用Enum的name()函數來讀寫Enum, 默認為False. 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    public JsonMapper enableEnumUseToString() {
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return this;
    }

    /**
     * 支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。 默认会先查找jaxb的annotation，如果找不到再找jackson的。
     */
    public JsonMapper enableJaxbAnnotation() {
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        this.registerModule(module);
        return this;
    }

    /**
     * 允许单引号 允许不带引号的字段名称
     */
    public JsonMapper enableSimple() {
        this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return this;
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return this;
    }

    /**
     * 对象转换为JSON字符串
     * 
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        return JsonMapper.getInstance().toJson(object);
    }

    /**
     * JSON字符串转换为对象
     * 
     * @param jsonString
     * @param clazz
     * @return
     */
    public static Object fromJsonString(String jsonString, Class<?> clazz) {
        return JsonMapper.getInstance().fromJson(jsonString, clazz);
    }

    /**
     * 输出全部属性
     * 
     * @param object
     * @return
     */
    public static String toNormalJson(Object object) {
        return new JsonMapper(Include.ALWAYS).toJson(object);
    }

    /**
     * 输出非空属性
     * 
     * @param object
     * @return
     */
    public static String toNonNullJson(Object object) {
        return new JsonMapper(Include.NON_NULL).toJson(object);
    }

    /**
     * 输出初始值被改变部分的属性
     * 
     * @param object
     * @return
     */
    public static String toNonDefaultJson(Object object) {
        return new JsonMapper(Include.NON_DEFAULT).toJson(object);
    }

    /**
     * 输出非Null且非Empty(如List.isEmpty)的属性
     * 
     * @param object
     * @return
     */
    public static String toNonEmptyJson(Object object) {
        return new JsonMapper(Include.NON_EMPTY).toJson(object);
    }

    public void setDateFormat(String dateFormat) {
        mapper.setDateFormat(new SimpleDateFormat(dateFormat));
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum, 為False時時使用Enum的name()函數來讀寫Enum, 默認為False. 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    public void setEnumUseToString(boolean value) {
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, value);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, value);
    }

    public JsonNode parseNode(String json) {
        try {
            return mapper.readValue(json, JsonNode.class);
        }
        catch (IOException e) {
            throw BettjerNestedException.wrap(e);
        }
    }

    /**
     * 将对象转换为集合，按照给定的对象
     * 
     * @param src
     * @param collectionClass
     * @param valueType
     * @return
     * @throws Exception
     */
    public static List jacksonToCollection(String anSrc, Class<?> anValueType) {

        return jacksonToCollection(anSrc, LinkedList.class, new Class[] { anValueType });
    }

    public static <T> T jacksonToCollection(String src, Class<?> collectionClass, Class<?>... valueType) {
        ObjectMapper jacksonMapper = getInstance();
        JavaType javaType = jacksonMapper.getTypeFactory().constructParametrizedType(collectionClass, collectionClass,
                valueType);

        try {
            return (T) jacksonMapper.readValue(src, javaType);
        }
        catch (IOException e) {
            throw new BytterValidException(71234, "解析Json出现异常", e);
        }

    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        List<Map<String, Object>> list = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", 1);
        map.put("pId", -1);
        map.put("name", "根节点");
        list.add(map);
        map = Maps.newHashMap();
        map.put("id", 2);
        map.put("pId", 1);
        map.put("name", "你好");
        map.put("open", true);
        list.add(map);
        String json = JsonMapper.getInstance().toJson(list);
        System.out.println(json);
    }

}
