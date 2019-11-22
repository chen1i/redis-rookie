package com.mas.impl;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

public class RedissonImpl extends AbstractConcurrentRedisImpl {
    private RedissonClient redisClient;

    public RedissonImpl(int threadCount, int targetDbIndex) {
        super(threadCount, targetDbIndex);
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setDatabase(targetDbIndex);
        redisClient = Redisson.create(config);
    }

    @Override
    long getResult() {
        long v = redisClient.getAtomicLong(KEY_NAME).get();
        redisClient.shutdown();
        return v;
    }

    @Override
    Collection<Callable<Long>> makeTasks(int concurrentCount, int iteratingCount) {
        Collection<Callable<Long>> tasks = new ArrayList<>(concurrentCount);
        for (int i = 0; i < concurrentCount; i++) {
            tasks.add(new MyTask(redisClient, KEY_NAME, iteratingCount));
        }
        return tasks;
    }

    private static class MyTask extends AbstractTask {
        private RedissonClient redissonClient;

        MyTask(RedissonClient client, String key, int steps) {
            super(key, steps);
            this.redissonClient = client;
        }

        @Override
        public Long call() {
            for (int i = 0; i < steps; i++) {
                redissonClient.getAtomicLong(key).incrementAndGet();
            }

            return redissonClient.getAtomicLong(key).get();
        }
    }
}
