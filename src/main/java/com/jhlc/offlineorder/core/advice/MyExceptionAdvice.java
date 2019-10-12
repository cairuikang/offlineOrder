package com.jhlc.offlineorder.core.advice;

import com.jhlc.offlineorder.core.exception.CustomException;
import com.jhlc.offlineorder.domain.Result;
import com.jhlc.offlineorder.domain.ResultCode;
import com.jhlc.offlineorder.core.exception.SqlException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 因为日志打印，设置成环绕切面，将异常进行了trycache，所以导致通知失效
 * @author 10096
 */
@ControllerAdvice
@Log4j2
public class MyExceptionAdvice {


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result defaultException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return Result.failure(ResultCode.EXCEPTION,e.getMessage());

    }

    @ExceptionHandler(value = CustomException.class)
    @ResponseBody
    public Result CustomException(HttpServletRequest request, CustomException e) {
        e.printStackTrace();
        return e.getResult();

    }

}
