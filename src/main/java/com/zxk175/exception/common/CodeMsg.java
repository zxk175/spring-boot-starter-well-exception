package com.zxk175.exception.common;

/**
 * @author zxk175
 * @since 2020-03-18 09:41
 */
public enum CodeMsg {

    // 通用错误码
    SUCCESS(0, "成功"),
    FAIL(999999, "失败"),

    // 500XX
    DB_ADD_SUCCESS(500105, "添加成功"),
    DB_ADD_ERROR(500106, "添加失败"),
    DB_DELETE_SUCCESS(500107, "删除成功"),
    DB_DELETE_ERROR(500108, "删除失败"),
    DB_MODIFY_SUCCESS(500109, "修改成功"),
    DB_MODIFY_ERROR(500110, "修改失败"),

    BIND_ERROR(1121, ""),

    NO_RECORD_FOUND(500301, "未查到记录"),
    ;

    private int code;
    private String msg;

    CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
