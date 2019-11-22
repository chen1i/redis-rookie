package com.mas;

import com.mas.annotaions.TimeMeasured;
import com.mas.impl.LettuceImpl;
import com.mas.impl.PooledJedisImpl;
import com.mas.impl.RedissonImpl;
import org.springframework.stereotype.Service;

@Service
public class RedisRunner {

    @TimeMeasured
    public void testWithPooledJedis10() {
        ConcurrentRedis test1 = new PooledJedisImpl(10, 2);
        long incrementalResult = test1.getIncrementalResult(10, 10000);

        System.out.println("value of last check: " + incrementalResult);
    }

    @TimeMeasured
    public void testWithPooledJedis4() {
        ConcurrentRedis test1 = new PooledJedisImpl(4, 2);
        long incrementalResult = test1.getIncrementalResult(4, 10000);

        System.out.println("value of last check: " + incrementalResult);
    }

    @TimeMeasured
    public void testWithPooledJedis1() {
        ConcurrentRedis test1 = new PooledJedisImpl(1, 2);
        long incrementalResult = test1.getIncrementalResult(1, 10000);

        System.out.println("value of last check: " + incrementalResult);
    }

    @TimeMeasured
    void testWithLettuce() {
        ConcurrentRedis subject = new LettuceImpl(10, 2);
        long result = subject.getIncrementalResult(10, 10000);
        System.out.println("value of last check " + result);
    }

    @TimeMeasured
    void testWithRedisson() {
        ConcurrentRedis subject = new RedissonImpl(10, 2);
        long result = subject.getIncrementalResult(10, 10000);
        System.out.println("value of last check " + result);
    }
}
