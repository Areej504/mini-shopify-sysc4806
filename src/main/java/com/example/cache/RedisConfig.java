package com.example.cache;

import redis.clients.jedis.Jedis;

public class RedisConfig {
    private static Jedis jedis;

    static {
        jedis = new Jedis("localhost", 6379); // Replace with your Redis host and port
    }

    public static Jedis getRedisClient() {
        return jedis;
    }
}
