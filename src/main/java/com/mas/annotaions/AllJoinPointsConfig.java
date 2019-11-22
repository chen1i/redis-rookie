package com.mas.annotaions;

import org.aspectj.lang.annotation.Pointcut;

public class AllJoinPointsConfig {
    @Pointcut("@annotation(com.mas.annotaions.TimeMeasured)")
    public void methodNeedTimeMeasure(){}
}
