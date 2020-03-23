package com.zxk175.exception.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author zxk175
 * @since 2020-03-20 09:37
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Response<T> implements Serializable {

    @ApiModelProperty(required = true, value = "是否成功", example = "true")
    private Boolean success;

    @ApiModelProperty(required = true, value = "消息代码", example = "0")
    private Integer code;

    @ApiModelProperty(required = true, value = "消息提示", example = "请求成功")
    private String msg;

    @ApiModelProperty(required = true, value = "返回数据")
    private T data;


    public static <T> Response<T> success() {
        return setSuccess(CodeMsg.SUCCESS.code(), CodeMsg.SUCCESS.msg());
    }

    public static <T> Response<T> success(String msg) {
        return setSuccess(CodeMsg.SUCCESS.code(), msg);
    }

    public static <T> Response<T> success(T data) {
        Response<T> success = success();
        success.setData(data);

        return success;
    }

    public static <T> Response<T> success(CodeMsg codeMsg) {
        return success(codeMsg.code(), codeMsg.msg());
    }

    public static <T> Response<T> success(Integer code, String msg) {
        return setSuccess(code, msg);
    }

    public static <T> Response<T> success(CodeMsg codeMsg, T data) {
        Response<T> response = success(codeMsg);

        return response.setData(data);
    }

    public static <T> Response<T> fail() {
        return setFail(CodeMsg.FAIL.code(), CodeMsg.FAIL.msg());
    }

    public static <T> Response<T> fail(CodeMsg codeMsg) {
        return setFail(codeMsg.code(), codeMsg.msg());
    }

    public static <T> Response<T> fail(T data) {
        Response<T> response = fail();
        response.setData(data);

        return response;
    }

    public static <T> Response<T> fail(String msg) {
        return setFail(CodeMsg.FAIL.code(), msg);
    }

    public static <T> Response<T> fail(Integer code, String msg) {
        return setFail(code, msg);
    }

    public static <T> Response<T> fail(CodeMsg codeMsg, T data) {
        Response<T> response = setFail(codeMsg.code(), codeMsg.msg());
        return response.setData(data);
    }

    public static <T> Response<T> saveReturn(boolean flag) {
        return flag ? success(CodeMsg.DB_ADD_SUCCESS) : fail(CodeMsg.DB_ADD_ERROR);
    }

    public static <T> Response<T> saveReturn(boolean flag, T data) {
        return flag ? success(CodeMsg.DB_ADD_SUCCESS, data) : fail(CodeMsg.DB_ADD_ERROR);
    }

    public static <T> Response<T> removeReturn(boolean flag) {
        return flag ? success(CodeMsg.DB_DELETE_SUCCESS) : fail(CodeMsg.DB_DELETE_ERROR);
    }

    public static <T> Response<T> removeReturn(boolean flag, String errMsg) {
        return flag ? success(CodeMsg.DB_DELETE_SUCCESS) : fail(CodeMsg.FAIL.code(), errMsg);
    }

    public static <T> Response<T> modifyReturn(boolean flag) {
        return flag ? success(CodeMsg.DB_MODIFY_SUCCESS) : fail(CodeMsg.DB_MODIFY_ERROR);
    }

    public static <T> Response<T> modifyReturn(boolean flag, T data) {
        return flag ? success(CodeMsg.DB_MODIFY_SUCCESS, data) : fail(CodeMsg.DB_MODIFY_ERROR);
    }

    public static <T> Response<T> modifyReturn(boolean flag, String errMsg) {
        return flag ? success(CodeMsg.DB_MODIFY_SUCCESS) : fail(CodeMsg.FAIL.code(), errMsg);
    }

    public static <T> Response<T> diyReturn(boolean flag, String sucMsg, String errMsg) {
        return flag ? success(CodeMsg.SUCCESS.code(), sucMsg) : fail(CodeMsg.FAIL.code(), errMsg);
    }

    public static <T> Response<T> objectReturn(T data) {
        return ObjectUtil.isNull(data) ? Response.fail(CodeMsg.NO_RECORD_FOUND) : Response.success(data);
    }

    public static Response<Collection<?>> collReturn(Collection<?> data) {
        return CollUtil.isEmpty(data) ? Response.fail(CodeMsg.NO_RECORD_FOUND) : Response.success(data);
    }

    public static Response<Map<?, ?>> collReturn(Map<?, ?> data) {
        return CollUtil.isEmpty(data) ? Response.fail(CodeMsg.NO_RECORD_FOUND) : Response.success(data);
    }

    private static <T> Response<T> setSuccess(Integer code, String msg) {
        return setResult(code, msg, true);
    }

    private static <T> Response<T> setFail(Integer code, String msg) {
        return setResult(code, msg, false);
    }

    private static <T> Response<T> setResult(Integer code, String msg, boolean flag) {
        return new Response<T>()
                .setSuccess(flag)
                .setCode(code)
                .setMsg(msg);
    }

}
