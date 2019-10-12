package com.jhlc.offlineorder.core.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jhlc.offlineorder.core.exception.CustomException;
import com.jhlc.offlineorder.domain.Result;
import com.jhlc.offlineorder.domain.ResultCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * @author guochunyuan
 * @create on  2018-12-20 14:43
 */
@Aspect
@Component//定义一个切面
public class LogRecordAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogRecordAspect.class);
    public final String string = "execution(*  *..*.*.controller..*.*(..))";
    private static final String UTF_8 = "utf-8";

    // 定义切点Pointcut
    @Pointcut(string)
    public void excudeService() {
    }

    //执行切点 之前
    @Before("excudeService()")
    public void exBefore(JoinPoint pjp) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
    }

    // 通知（环绕）
    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        long endTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Object[] args = pjp.getArgs();
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        String params = "";
        // result的值就是被拦截方法的返回值
        Object result = null;
        try {
            result = pjp.proceed();
            long startTime = (long) request.getAttribute("startTime");
            //获取请求参数集合并进行遍历拼接
            if (args.length > 0) {
                if ("POST".equals(method)) {
                    Object object = args[0];
                    params = JSON.toJSONString(object, SerializerFeature.WriteMapNullValue);
                } else if ("GET".equals(method)) {
                    params = queryString;
                }
                params = params != null ?URLDecoder.decode(params, UTF_8):"";
            }
            logger.info("requestMethod:{},url:{},params:{},responseBody:{},elapsed:{}ms.", method, uri, params,
                    JSON.toJSONString(result, SerializerFeature.WriteMapNullValue), (endTime - startTime));
        } catch (CustomException e) {
            result = e.getResult();
            e.printStackTrace();
        }catch (Exception e) {
            result = Result.failure(ResultCode.EXCEPTION);
            e.printStackTrace();
        }finally {
            logger.info("调用接口名称:{},调用方法名称：{},调用参数：{}",className,methodName,params);
            logger.info("返回结果:{}",JSON.toJSONString(result == null? Result.failure(ResultCode.EXCEPTION):result, SerializerFeature.WriteMapNullValue));
        }
        return result;
    }


    //    执行切点之后
    @After("excudeService()")
    public void exAfter(JoinPoint joinPoint) {
    }

}


