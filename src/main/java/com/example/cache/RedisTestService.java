//package com.example.cache;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RedisTestService {
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    public void testConnection() {
//        redisTemplate.opsForValue().set("testKey", "Hello from Local App!");
//        String value = redisTemplate.opsForValue().get("testKey");
//        System.out.println("Value retrieved from Redis: " + value);
//    }
//}
