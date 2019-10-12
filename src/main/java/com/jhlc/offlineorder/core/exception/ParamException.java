package com.jhlc.offlineorder.core.exception;

import com.jhlc.offlineorder.domain.ResultCode;

/**
 * Created by staconfree on 2017/4/2.
 */
@SuppressWarnings("serial")
public class ParamException extends RuntimeException {

    private Integer errCode;

    public ParamException(String errMsg) {
        super(errMsg);
        this.errCode = ResultCode.SQLEXCEPTION.getCode();
    }


    public ParamException(Integer errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public ParamException(ResultCode error) {
        super(error.getMessage());
        this.errCode = error.getCode();
    }

}
