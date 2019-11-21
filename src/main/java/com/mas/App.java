package com.mas;

import com.mas.impl.PooledJedisImpl;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws IOException, RunnerException {
        org.openjdk.jmh.Main.main(args);
//        new App().testWithPooledJedis1();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations=1)
    @Measurement(iterations=3, time = 1)
    @Fork(value = 1)
    public void testWithPooledJedis10() {
        ConcurrentRedis test1 = new PooledJedisImpl();
        long incrementalResult = test1.getIncrementalResult(10, 10000);

        System.out.println("value of last check: " + incrementalResult);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations=1)
    @Measurement(iterations=3, time = 1)
    @Fork(value = 1)
    public void testWithPooledJedis4() {
        ConcurrentRedis test1 = new PooledJedisImpl();
        long incrementalResult = test1.getIncrementalResult(4, 10000);

        System.out.println("value of last check: " + incrementalResult);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Warmup(iterations=0)
    @Measurement(iterations = 1, time = 1)
    @Fork(value = 1)
    public void testWithPooledJedis1() {
        ConcurrentRedis test1 = new PooledJedisImpl();
        long incrementalResult = test1.getIncrementalResult(1, 10000);

        System.out.println("value of last check: " + incrementalResult);
    }
}
