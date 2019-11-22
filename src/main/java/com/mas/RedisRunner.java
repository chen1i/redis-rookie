package com.mas;

import com.mas.annotaions.TimeMeasured;
import com.mas.impl.PooledJedisImpl;
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

}
