package com.mas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App
{
    public static void main( String[] args ) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        RedisRunner runner = context.getBean(RedisRunner.class);
//        runner.testWithPooledJedis1();
//        runner.testWithPooledJedis4();
        runner.testWithPooledJedis10();
        runner.testWithLettuce();
    }
}
