package com.mas;

public interface ConcurrentRedis {
    long getIncrementalResult(int concurrentCount, int iteratingCount);
}
