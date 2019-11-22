package com.mas.annotaions;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MeasurementAspect {
    @Around("com.mas.annotaions.AllJoinPointsConfig.methodNeedTimeMeasure()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.err.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
        return proceed;    }
}
