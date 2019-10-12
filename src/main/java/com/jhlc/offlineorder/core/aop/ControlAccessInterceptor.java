package com.jhlc.offlineorder.core.aop;

import com.jhlc.offlineorder.conmmon.utils.MD5Util;
import com.jhlc.offlineorder.domain.Result;
import com.jhlc.offlineorder.domain.ResultCode;
import com.jhlc.offlineorder.core.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author 10096
 */
@Aspect
@Component
@Slf4j
public class ControlAccessInterceptor {

    /**
     * 过期时间
     */
    public static long exptime = 300;

    @Autowired
    RedisService redisService;

    /**
     * 控制该方法同时访问
     * @param proceedingJoinPoint
     * @return
     */
    @Around("@annotation(com.jhlc.offlineorder.core.annotation.ControlAccess)")
    public Result around(ProceedingJoinPoint proceedingJoinPoint) {
        String methodName = proceedingJoinPoint.getSignature().getName();
        //1.获取方法所有入参
        Object[] args = proceedingJoinPoint.getArgs();
        //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        //3.拼装redis入参
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] == null || args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse) {
                continue;
            }
            sb.append("\"").append(parameterNames[i].trim()).append("\"").append(":\"").append(args[i].toString().trim()).append("\",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("}");
        String redisKey = methodName + "_" + MD5Util.md5Hex(sb.toString());
        String redisValue = sb.toString();
        log.info("请求参数++++++" + redisKey + ":" + redisValue);

        Result result;
        try {
            if (redisService.setIfAbsent(redisKey,redisValue,exptime)) {
                result = (Result) proceedingJoinPoint.proceed();
                redisService.del(redisKey);
            } else {
                result = Result.success(ResultCode.MAS_REQUEST_CODE);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            result = Result.failure(ResultCode.EXCEPTION);
        }finally {
            redisService.del(redisKey);
        }
        return result;
    }
}
