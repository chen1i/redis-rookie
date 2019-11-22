package com.mas.redis;

public interface ConcurrentRedis {
    long getIncrementalResult(int concurrentCount, int iteratingCount);
}
