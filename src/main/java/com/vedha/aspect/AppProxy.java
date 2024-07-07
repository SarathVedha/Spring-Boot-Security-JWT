package com.vedha.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AppProxy {

    @Around("execution(* com.vedha.service.*.*(..)) || execution(* com.vedha.controller.*.*(..)) " +
            "|| execution(* com.vedha.repository.*.*(..)) || execution(* com.vedha.security.*.*(..)) " +
            "|| execution(* com.vedha.exception.*.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("Method: {} called with arguments: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        log.info("Time taken by {} is {} s", joinPoint.getSignature().toShortString(), (endTime - startTime) / 1000);
        log.info("Method: {} returned: {}", joinPoint.getSignature().toShortString(), result);

        return result;
    }
}
