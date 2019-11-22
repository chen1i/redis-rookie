package com.mas.aop;

import org.aspectj.lang.annotation.Pointcut;

public class AllJoinPointsConfig {
    @Pointcut("@annotation(com.mas.annotation.TimeMeasured)")
    public void methodNeedTimeMeasure(){}
}
