package com.mas.impl;

import com.mas.ConcurrentRedis;

public class RedissonImpl implements ConcurrentRedis {
    @Override
    public long getIncrementalResult(int concurrentCount, int iteratingCount) {
        return 0;
    }
}
