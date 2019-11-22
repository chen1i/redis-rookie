package com.mas.redis.impl;

import com.mas.redis.ConcurrentRedis;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * THIS Implementation is BROKEN because non-pool Jedis instance is not thread-safe.
 */
@Deprecated
public class JedisImpl implements ConcurrentRedis {
    private ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private Jedis jedis = new Jedis("localhost", 6379);
    
    private static final String KEY_NAME = "test:redis:uniq_id";

    @Override
    public long getIncrementalResult(int concurrentCount, final int iteratingCount){
        jedis.connect();
        jedis.select(2);
        jedis.setnx(KEY_NAME, "0");

        Collection<Callable<Long>> tasks = makeTasks(concurrentCount, iteratingCount);

        try {
            es.invokeAll(tasks);
            return Long.parseLong(jedis.get(KEY_NAME));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Collection<Callable<Long>> makeTasks(int concurrentCount, int iteratingCount) {
        Collection<Callable<Long>> tasks = new ArrayList<>(concurrentCount);
        for (int i = 0; i <concurrentCount; i++) {
            tasks.add(new Task(jedis, KEY_NAME, iteratingCount));
        }

        return tasks;
    }

    private static class Task implements Callable<Long> {
        private Jedis jedis;
        private String key;
        private int steps;

        Task(Jedis jedis, String key, int steps) {
            this.jedis = jedis;
            this.key = key;
            this.steps = steps;
        }

        @Override
        public Long call(){
            for (int i = 0; i < this.steps; i++) {
                jedis.incr(this.key);
            }
            return Long.parseLong(jedis.get(key));
        }
    }
}
