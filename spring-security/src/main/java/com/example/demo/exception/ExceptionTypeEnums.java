package com.example.demo.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zjh
 * @Description 接口返回码
 * @date 2020/12/07 14:10
 */
@Getter
public enum ExceptionTypeEnums implements ExceptionType {

    //公共错误码
    SERVER_INTERNAL_ERROR(-1, "服务器繁忙,请求处理失败", 500),
    SUCCESS(0, "请求处理成功", 200),
    FAIL(1, "请求处理失败", 200),

    /**
     * 请求方法相关错误
     */
    REQ_REJECT(1001, "请求被拒绝", 403),
    NOT_FOUND(1002, "404 没找到请求", 404),
    METHOD_NOT_SUPPORTED(1003, "不支持当前请求方法", 405),
    METHOD_NOT_ALLOWED(1004, "Method Not Allowed", 405),
    MEDIA_TYPE_NOT_SUPPORTED(1005, "不支持当前媒体类型", 415),

    /**
     * 请求参数校验错误
     */
    PARAM_TYPE_ERROR(1101, "请求参数类型错误", 400),
    PARAM_BIND_ERROR(1102, "请求参数绑定错误", 400),
    PARAM_VALID_ERROR(1103, "参数校验失败", 400),
    PARAM_MISS(1104, "缺少必要的请求参数", 400),
    MSG_NOT_READABLE(1105, "消息不能读取", 400),

    /**
     * token授权相关错误
     */
    UN_AUTHORIZED(1201, "请求未授权", 401),
    INVALID_TOKEN(1202, "无效token", 401),
    CLIENT_UN_AUTHORIZED(1203, "客户端请求未授权", 401),
    TOKEN_HAS_EXPIRED(1204, "token已失效", 401),
    JWT_DECODE_EXCEPTION(1205, "token解析异常，请检查token是否合法", 401),
    SIGNATURE_VERIFICATION_EXCEPTION(1206, "token令牌签名异常", 401),
    NO_TOKEN_EXCEPTION(1207, "token不能为空", 401),

    /**
     * 算法相关错误
     */
    ARITHMETIC_ERROR(1301, "算法错误", 500),

    /**
     * 数据库相关错误
     */
    DUPLICATE_PRIMARY_KEY(1401, "唯一键冲突", 500),

    /**
     * 业务异常
     */
    NO_EXIST_USER(1501, "该用户不存在", 200),
    PASSWORD_WRONG(1502, "密码错误", 200),
    OLD_PASSWORD_ERROR(1502, "旧密码输入错误", 200),
    NEW_PASSWORD_INVALID(32, "新密码不符合格式要求", 200),
    ;


    private Integer code;
    private String msg;
    private Integer httpCode;

    ExceptionTypeEnums(Integer code, String msg, Integer httpCode) {
        this.code = code;
        this.msg = msg;
        this.httpCode = httpCode;
    }

    public Integer code() {
        return code;
    }


    public Integer httpCode() {
        return httpCode;
    }

    public String msg() {
        return msg;
    }

    /**
     * @return ActivityTypeEnum
     * @Title getEnum
     * @Description 获取枚举
     */
    public static ExceptionTypeEnums getEnum(int code) {
        ExceptionTypeEnums resultEnum = null;
        ExceptionTypeEnums[] enumAry = ExceptionTypeEnums.values();
        for (ExceptionTypeEnums anEnumAry : enumAry) {
            if (anEnumAry.code() == code) {
                resultEnum = anEnumAry;
                break;
            }
        }
        if (resultEnum == null) {
            resultEnum = ExceptionTypeEnums.FAIL;
        }
        return resultEnum;
    }

    /**
     * @return Map<String, Map < String, Object>>
     * @Title toMap
     * @Description 转换为MAP
     */
    public static Map<String, Map<String, Object>> toMap() {
        ExceptionTypeEnums[] ary = ExceptionTypeEnums.values();
        Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
        for (ExceptionTypeEnums anAry : ary) {
            Map<String, Object> map = new HashMap<String, Object>();
            String key = String.valueOf(getEnum(anAry.code()));
            map.put("code", String.valueOf(anAry.code()));
            map.put("msg", anAry.msg());
            enumMap.put(key, map);
        }
        return enumMap;
    }

    /**
     * @return List
     * @Title toList
     * @Description 转换为列表
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List toList() {
        ExceptionTypeEnums[] ary = ExceptionTypeEnums.values();
        List list = new ArrayList();
        for (ExceptionTypeEnums anAry : ary) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", String.valueOf(anAry.code()));
            map.put("msg", anAry.msg());
            list.add(map);
        }
        return list;
    }

    /**
     * 获取httpcode
     *
     * @param code 错误码
     * @return
     * @author Gopoop
     * @date 2018年3月8日 上午11:01:59
     */
    public static int getHttpCode(int code) {
        ExceptionTypeEnums[] enumAry = ExceptionTypeEnums.values();
        for (ExceptionTypeEnums anEnumAry : enumAry) {
            if (anEnumAry.code() == code) {
                return anEnumAry.httpCode();
            }
        }
        return 200;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
