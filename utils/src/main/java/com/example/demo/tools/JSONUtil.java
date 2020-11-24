package com.example.demo.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zjh
 * @Description json工具类
 * @date 2020/09/09 13:27
 */
@Slf4j
public class JSONUtil {

    /**定义jackson对象*/
    private static ObjectMapper mapper;

    static {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
    }

    /**
     * 将对象转换成json字符串。
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("JSONUtils_objectToJson解析出错： {}", e);
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     * @param jsonStr json数据
     * @param beanType 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonStr, Class<T> beanType) {
        try {
            return mapper.readValue(jsonStr, beanType);
        } catch (IOException e) {
            log.error("JSONUtils_jsonToPojo解析出错： {}", e);
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     * @param jsonStr
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> beanType) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
            return mapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            log.error("JSONUtils_jsonToList解析出错： {}", e);
        }
        return null;
    }

    /**
     * 将json数据转换成map
     * @param jsonStr
     * @return
     */
    public static <T> Map<String, Object> jsonToMap(String jsonStr) {
        try {
            return mapper.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            log.error("JSONUtils_jsonToMap解析出错： {}", e);
        }
        return null;
    }

    /**
     * 将json数据转换成节点树
     * @param jsonStr
     * @return
     */
    public static JsonNode readTree(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (IOException e) {
            log.error("JSONUtils_readTree解析出错： {}", e);
        }
        return null;
    }

    /**
     * 将json数据获取字段值
     * @param jsonStr  json数据
     * @param fieldName json数据中的字段名称
     * @return
     */
    public static JsonNode jsonToTree(String jsonStr, String fieldName) {
        try {
            JsonNode jsonNode = mapper.readTree(jsonStr);
            if (null == fieldName) {
                return jsonNode;
            }
            return jsonNode.get(fieldName);
        } catch (IOException e) {
            log.error("JSONUtils_jsonToTree解析出错： {}", e);
        }
        return null;
    }
}
