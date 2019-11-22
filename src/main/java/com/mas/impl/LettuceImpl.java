package com.mas.impl;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

public class LettuceImpl extends AbstractConcurrentRedisImpl {
    private final RedisClient redisClient;

    public LettuceImpl(int threadCount, int targetDbIndex) {
        super(threadCount, targetDbIndex);
        this.redisClient = RedisClient.create("redis://localhost/" + targetDbIndex);

    }

    @Override
    Collection<Callable<Long>> makeTasks(int concurrentCount, int iteratingCount) {
        Collection<Callable<Long>> tasks = new ArrayList<>(concurrentCount);
        for (int i = 0; i < concurrentCount; i++) {
            tasks.add(new MyTask(redisClient, KEY_NAME, iteratingCount));
        }

        return tasks;
    }

    @Override
    long getResult() {
        return Long.parseLong(getRedisInstance().get(KEY_NAME));
    }

    private RedisCommands<String, String> getRedisInstance() {
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        return connection.sync();
    }

    static class MyTask extends AbstractTask {
        StatefulRedisConnection<String, String> connection;

        MyTask(RedisClient redisClient, String key, int steps) {
            super(key, steps);
            connection = redisClient.connect();
        }

        @Override
        public Long call() {
//            System.err.println(Thread.currentThread().getName() + " ready to run");
//            System.err.println(Thread.currentThread().getName() + " start value is " + getCachedValue());

            for (int i = 0; i < steps; i++) {
                getRedisInstance().incr(key);
            }

//            System.err.println(Thread.currentThread().getName() + "   end value is " + getCachedValue());

            return getCachedValue();
        }

        private Long getCachedValue() {
            return Long.valueOf(getRedisInstance().get(key));
        }

        private RedisCommands<String, String> getRedisInstance() {
            return connection.sync();
        }
    }
}
