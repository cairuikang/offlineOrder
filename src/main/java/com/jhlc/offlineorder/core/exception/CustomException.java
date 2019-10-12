package com.jhlc.offlineorder.core.exception;

import com.jhlc.offlineorder.domain.Result;
import lombok.Getter;

/**
 * @author Joetao
 * Created at 2018/8/24.
 */
@Getter
public class CustomException extends RuntimeException{
    private Result result;

    public CustomException(Result result) {
        this.result = result;
    }
}
