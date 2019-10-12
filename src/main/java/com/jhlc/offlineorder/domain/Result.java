package com.jhlc.offlineorder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class Result<T> implements Serializable {
    /** 业务错误码 */
    private Integer code;
    /** 信息描述 */
    private String message;
    /** 返回参数 */
    private T data;

    private Result(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    /** 业务成功返回业务代码和描述信息 */
    public static Result<Void> success() {
        return new Result<Void>(ResultCode.SUCCESS, null);
    }

    /** 业务成功返回业务代码,描述和返回的参数 */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultCode.SUCCESS, data);
    }

    /** 业务成功返回业务代码,描述和返回的参数 */
    public static <T> Result<T> success(ResultCode resultCode, T data) {
        if (resultCode == null) {
            return success(data);
        }
        return new Result<T>(resultCode, data);
    }

    /** 业务异常返回业务代码和描述信息 */
    public static <T> Result<T> failure() {
        return new Result<T>(ResultCode.INTERNAL_SERVER_ERROR, null);
    }
    /** 业务异常返回业务代码和描述信息 */
    public static <T> Result<T> failure(int errCode,String msg) {
        return new Result<T>(errCode,msg, null);
    }

    /** 业务异常返回业务代码,描述和返回的参数 */
    public static <T> Result<T> failure(ResultCode resultCode) {
        return failure(resultCode, null);
    }

    /** 业务异常返回业务代码,描述和返回的参数 */
    public static <T> Result<T> failure(ResultCode resultCode, T data) {
        if (resultCode == null) {
            return new Result<T>(ResultCode.INTERNAL_SERVER_ERROR, null);
        }
        return new Result<T>(resultCode, data);
    }
}
