package com.mas;

import com.mas.impl.JedisImpl;
import com.mas.impl.PooledJedisImpl;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ConcurrentRedis test1 = new PooledJedisImpl();
        long incrementalResult = test1.getIncrementalResult(10, 1000000);

        System.out.println("value of last check: " + incrementalResult);
    }
}
