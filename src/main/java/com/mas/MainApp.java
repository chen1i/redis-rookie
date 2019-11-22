package com.mas;

import com.mas.service.RedisRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApp
{
    public static void main( String[] args ) {
        ConfigurableApplicationContext context = SpringApplication.run(MainApp.class, args);

        RedisRunner runner = context.getBean(RedisRunner.class);
//        runner.testWithPooledJedis1();
//        runner.testWithPooledJedis4();
        runner.testWithPooledJedis10();
        runner.testWithLettuce();
        runner.testWithRedisson();
    }
}
