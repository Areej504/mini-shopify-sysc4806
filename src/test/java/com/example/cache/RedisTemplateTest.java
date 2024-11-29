//package com.example.cache;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class RedisTemplateTest {
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    private ValueOperations<String, String> valueOps;
//
//    @BeforeAll
//    void setup() {
//        valueOps = redisTemplate.opsForValue();
//    }
//
//    @Test
//    @DisplayName("Test Set and Get Key in Redis")
//    void testSetAndGetKey() {
//        // Set a key
//        valueOps.set("testKey", "Redis is working!");
//        // Get the key
//        String value = valueOps.get("testKey");
//
//        assertNotNull(value, "Value for testKey should not be null");
//        assertEquals("Redis is working!", value, "Value for testKey does not match expected value");
//    }
//    @Test
//    @DisplayName("Test Expiry of a Key in Redis")
//    void testKeyExpiry() throws InterruptedException {
//        // Set a key with a 2-second expiration
//        valueOps.set("expiryKey", "This will expire in 2 seconds");
//        redisTemplate.expire("expiryKey", java.time.Duration.ofSeconds(2));
//
//        // Verify the key exists initially
//        String value = valueOps.get("expiryKey");
//        assertNotNull(value, "expiryKey should exist immediately after being set");
//
//        // Wait for 3 seconds (key should expire)
//        Thread.sleep(3000);
//
//        // Verify the key no longer exists
//        value = valueOps.get("expiryKey");
//        assertNull(value, "expiryKey should be null after expiration");
//    }
//
//    @Test
//    @DisplayName("Test Deletion of a Key in Redis")
//    void testDeleteKey() {
//        // Set a key
//        valueOps.set("deleteKey", "This will be deleted");
//        // Verify the key exists
//        assertNotNull(valueOps.get("deleteKey"), "deleteKey should exist initially");
//
//        // Delete the key
//        redisTemplate.delete("deleteKey");
//
//        // Verify the key no longer exists
//        assertNull(valueOps.get("deleteKey"), "deleteKey should not exist after deletion");
//    }
//
//    @AfterAll
//    void tearDown() {
//        // Clean up all test keys
//        redisTemplate.delete("testKey");
//        redisTemplate.delete("expiryKey");
//        redisTemplate.delete("deleteKey");
//    }
//}
