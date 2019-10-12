package com.jhlc.offlineorder.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@Getter
public enum ResultCode {

    SUCCESS(HttpStatus.OK, 200, "OK"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "Bad Request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "Internal Server Error"),

    FAIL(100, "请求失败"),
    TIMEOUT(101, "请求超时"),
    UNLOGIN(201, "未登录"),
    ORDER_MISS_CODE(201,"没有该订单"),
    USER_MISS_CODE(211,"没有该用户"),

    EXCEPTION(300, "系统异常"),
    MAS_REQUEST_CODE(302,"该请求正在处理，请勿重复操作！"),


    SQL_ERROR(400, "sql异常"),
    SQLEXCEPTION(500, "入参有误"),
    UNAUTHORIZED(401, "认证失败"),
    LOGIN_ERROR(401, "登陆失败，用户名或密码无效"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "请求的资源不存在"),
    OPERATE_ERROR(405, "操作失败，请求操作的资源不存在"),
    TIME_OUT(408, "请求超时"),

    SERVER_ERROR(500, "服务器内部错误"),
;


    /** 返回的HTTP状态码,  符合http请求 */
    private HttpStatus httpStatus;
    /** 业务异常码 */
    private Integer code;
    /** 业务异常信息描述 */
    private String message;

    ResultCode(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
    ResultCode(Integer code, String message) {
        this.httpStatus = HttpStatus.OK;
        this.code = code;
        this.message = message;
    }
}
