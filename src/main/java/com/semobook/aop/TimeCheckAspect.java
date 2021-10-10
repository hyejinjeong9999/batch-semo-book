package com.semobook.aop;

import com.semobook.tools.TimeFormat;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@Aspect
@Slf4j
public class TimeCheckAspect {

    @Around("@annotation(TimeCheck)")
    public Object calculatePerformanceTime(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        try {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            String methodName = signature.getMethod().getName();
            long start = System.currentTimeMillis();
            log.info("method name is = {} ----------------------> start time = {}", methodName, TimeFormat.simpleDateFormat(start));
            result = proceedingJoinPoint.proceed();
            long end = System.currentTimeMillis();
            log.info("method name is = {} ----------------------> end time = {}", methodName, TimeFormat.simpleDateFormat(end));
            log.info("method name is = {} ----------------------> 수행 시간 {} ms", methodName, end - start);
        } catch (Throwable throwable) {
            log.info("Exception");
            throwable.printStackTrace();
        }
        return result;
    }
}
