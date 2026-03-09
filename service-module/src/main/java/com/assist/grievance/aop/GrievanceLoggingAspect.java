package com.assist.grievance.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class GrievanceLoggingAspect {

    // Pointcut: only target methods in GrievanceServiceImpl class
    @Pointcut("execution(* com.assist.grievance.service.Impl.GrievanceServiceImpl.receivedGrievance(..))")
    public void grievanceServiceMethods() {
    }

    // Method entry
    @Before("grievanceServiceMethods()")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info(
                "AOP-START | Class: {} | Method: {} | Args: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs())
        );
    }

    // Method exit + execution time
    @Around("grievanceServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = pjp.proceed(); // actual method call

        long timeTaken = System.currentTimeMillis() - startTime;

        log.info(
                "AOP-END | Method: {} | Time Taken: {} ms",
                pjp.getSignature().toShortString(),
                timeTaken
        );

        return result;
    }

    // Exception logging
    @AfterThrowing(
            pointcut = "grievanceServiceMethods()",
            throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Exception ex) {

        log.error(
                "AOP-EXCEPTION | Method: {} | Message: {}",
                joinPoint.getSignature().toShortString(),
                ex.getMessage(),
                ex
        );
    }
}
