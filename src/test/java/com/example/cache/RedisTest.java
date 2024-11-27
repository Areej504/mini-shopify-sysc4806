//package com.example.cache;
//
//import org.junit.jupiter.api.*;
//import redis.clients.jedis.Jedis;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class RedisTest {
//
//    private Jedis jedis;
//
//    @BeforeAll
//    void setup() {
//        // Connect to Redis
//        jedis = new Jedis("localhost", 6379);
//        assertEquals("PONG", jedis.ping(), "Redis server is not responding to PING");
//    }
//
//    @Test
//    @DisplayName("Test Set and Get Key in Redis")
//    void testSetAndGetKey() {
//        // Set a key
//        jedis.set("testKey", "Redis is working!");
//        // Get the key
//        String value = jedis.get("testKey");
//
//        assertNotNull(value, "Value for testKey should not be null");
//        assertEquals("Redis is working!", value, "Value for testKey does not match expected value");
//    }
//
//    @Test
//    @DisplayName("Test Expiry of a Key in Redis")
//    void testKeyExpiry() throws InterruptedException {
//        // Set a key with a 2-second expiration
//        jedis.setex("expiryKey", 2, "This will expire in 2 seconds");
//
//        // Verify the key exists initially
//        String value = jedis.get("expiryKey");
//        assertNotNull(value, "expiryKey should exist immediately after being set");
//
//        // Wait for 3 seconds (key should expire)
//        Thread.sleep(3000);
//
//        // Verify the key no longer exists
//        value = jedis.get("expiryKey");
//        assertNull(value, "expiryKey should be null after expiration");
//    }
//
//    @Test
//    @DisplayName("Test Deletion of a Key in Redis")
//    void testDeleteKey() {
//        // Set a key
//        jedis.set("deleteKey", "This will be deleted");
//        // Verify the key exists
//        assertNotNull(jedis.get("deleteKey"), "deleteKey should exist initially");
//
//        // Delete the key
//        jedis.del("deleteKey");
//
//        // Verify the key no longer exists
//        assertNull(jedis.get("deleteKey"), "deleteKey should not exist after deletion");
//    }
//
//    @AfterAll
//    void tearDown() {
//        // Clean up all test keys
//        jedis.del("testKey", "expiryKey", "deleteKey");
//        jedis.close();
//    }
//}
