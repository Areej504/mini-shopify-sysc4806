package com.example.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RedisLocalTestRunner implements CommandLineRunner {
    @Autowired
    private RedisTestService redisTestService;

    @Override
    public void run(String... args) throws Exception {
        redisTestService.testConnection();
    }
}
