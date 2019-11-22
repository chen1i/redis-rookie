package com.mas.impl;

import com.mas.ConcurrentRedis;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractConcurrentRedisImpl implements ConcurrentRedis {
    static final String KEY_NAME = "test:redis:uniq_id";
    private ExecutorService es;
    private int targetDbIndex;

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
//        @Override
//        public Long call() {
////            System.err.println(Thread.currentThread().getName() + " ready to run");
////            System.err.println(Thread.currentThread().getName() + " start value is " + jedis.get(key));
//            for (int i = 0; i < this.steps; i++) {
//                redisConnection.incr(this.key);
//            }
////            System.err.println(Thread.currentThread().getName() + " end value is " + jedis.get(key));
//            return Long.parseLong(jedis.get(key));
//        }
    }

}
