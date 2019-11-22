package com.mas.impl;

import com.mas.ConcurrentRedis;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PooledJedisImpl implements ConcurrentRedis {
    private static final String KEY_NAME = "test:redis:uniq_id";
    private ExecutorService es;
    private JedisPool jedisPool;
    private int targetDbIndex;

    public PooledJedisImpl(int threadCount, int redisDbIndex) {
        this.es = Executors.newFixedThreadPool(threadCount);
        this.jedisPool = configJedisPool();
        this.targetDbIndex = redisDbIndex;
    }

    private JedisPool configJedisPool() {
        GenericObjectPoolConfig jedisPoolConfig = new GenericObjectPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(20);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setMaxWaitMillis(10);

        return new JedisPool(jedisPoolConfig, "localhost", 6379);
    }


    @Override
    public long getIncrementalResult(int concurrentCount, final int iteratingCount) {
        Collection<Callable<Long>> tasks = makeTasks(concurrentCount, iteratingCount);

        getTargetDb().setnx(KEY_NAME, "0");

        try {
            es.invokeAll(tasks);
            es.shutdown();
            return Long.parseLong(getTargetDb().get(KEY_NAME));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            jedisPool.destroy();
        }

        return 0;
    }

    private Jedis getTargetDb() {
        Jedis cli = jedisPool.getResource();
        cli.select(targetDbIndex);
        return cli;
    }

    private Collection<Callable<Long>> makeTasks(int concurrentCount, int iteratingCount) {
        Collection<Callable<Long>> tasks = new ArrayList<>(concurrentCount);
        for (int i = 0; i < concurrentCount; i++) {
            Jedis jedis = jedisPool.getResource();
            jedis.select(targetDbIndex);
            tasks.add(new PooledJedisImpl.Task(jedis, KEY_NAME, iteratingCount));
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
        public Long call() {
//            System.err.println(Thread.currentThread().getName() + " ready to run");
//            System.err.println(Thread.currentThread().getName() + " start value is " + jedis.get(key));
            for (int i = 0; i < this.steps; i++) {
                jedis.incr(this.key);
            }
//            System.err.println(Thread.currentThread().getName() + " end value is " + jedis.get(key));
            return Long.parseLong(jedis.get(key));
        }
    }
}
