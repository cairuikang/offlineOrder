package com.jhlc.offlineorder.core.exception;

import com.jhlc.offlineorder.domain.ResultCode;
import lombok.Data;

/**
 * Created by staconfree on 2017/4/2.
 */
@Data
public class SqlException extends RuntimeException {
    private Integer errCode;

    public SqlException(String errMsg) {
        super(errMsg);
        this.errCode = ResultCode.SQL_ERROR.getCode();
    }


    public SqlException(Integer errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public SqlException(ResultCode resultEnum) {
        super(resultEnum.getMessage());
        this.errCode = resultEnum.getCode();
    }
}
