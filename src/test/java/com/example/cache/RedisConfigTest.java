package com.example.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedisConnectionFactory() {
        assertThat(redisConnectionFactory).isNotNull();
    }

    @Test
    void testRedisTemplate() {
        assertThat(redisTemplate).isNotNull();
        assertThat(redisTemplate.getKeySerializer()).isInstanceOf(org.springframework.data.redis.serializer.StringRedisSerializer.class);
        assertThat(redisTemplate.getValueSerializer()).isInstanceOf(org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer.class);
    }
}
