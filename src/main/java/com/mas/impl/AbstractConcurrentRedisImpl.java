package com.mas.impl;

import com.mas.ConcurrentRedis;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractConcurrentRedisImpl implements ConcurrentRedis {
    static final String KEY_NAME = "test:redis:uniq_id";
    protected int targetDbIndex;

    private ExecutorService es;

    AbstractConcurrentRedisImpl(int threadCount, int targetDbIndex) {
        this.es = Executors.newFixedThreadPool(threadCount);
        this.targetDbIndex = targetDbIndex;
    }

    @Override
    public long getIncrementalResult(int concurrentCount, int iteratingCount) {
        try {
            es.invokeAll(makeTasks(concurrentCount, iteratingCount));
            es.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return getResult();
    }

    abstract Collection<Callable<Long>> makeTasks(int concurrentCount, int iteratingCount);

    abstract long getResult();

    static abstract class AbstractTask implements Callable<Long> {
        String key;
        int steps;

        AbstractTask(String key, int steps) {
            this.key = key;
            this.steps = steps;
        }

        abstract public Long call();
    }

}
