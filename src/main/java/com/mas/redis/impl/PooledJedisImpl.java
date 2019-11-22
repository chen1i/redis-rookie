package com.mas.redis.impl;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

public class PooledJedisImpl extends AbstractConcurrentRedisImpl {
    private JedisPool jedisPool;

    public PooledJedisImpl(int threadCount, int redisDbIndex) {
        super(threadCount, redisDbIndex);
        this.jedisPool = configJedisPool();
    }

    private JedisPool configJedisPool() {
        GenericObjectPoolConfig jedisPoolConfig = new GenericObjectPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(20);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setMaxWaitMillis(10);

        return new JedisPool(jedisPoolConfig, "localhost", 6379);
    }

    private Jedis getTargetDb() {
        Jedis cli = jedisPool.getResource();
        cli.select(targetDbIndex);
        return cli;
    }

    @Override
    Collection<Callable<Long>> makeTasks(int concurrentCount, int iteratingCount) {
        Collection<Callable<Long>> tasks = new ArrayList<>(concurrentCount);
        for (int i = 0; i < concurrentCount; i++) {
            Jedis jedis = jedisPool.getResource();
            jedis.select(targetDbIndex);
            tasks.add(new PooledJedisImpl.Task(jedis, KEY_NAME, iteratingCount));
        }

        return tasks;
    }

    @Override
    long getResult() {
        return Long.parseLong(getTargetDb().get(KEY_NAME));
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
