package com.example.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CartService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String generateCartKey(String sessionId, Long storeId) {
        return "cart:" + sessionId + ":" + storeId; // Composite key
    }

    public void addToCart(String sessionId, Long storeId, Long productId, int quantity) {
        String key = generateCartKey(sessionId, storeId);
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) redisTemplate.opsForValue().get(key);

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        boolean itemExists = false;
        for (Map<String, Object> item : cartItems) {
            if (item.get("productId").equals(productId)) {
                item.put("quantity", (int) item.get("quantity") + quantity);
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            Map<String, Object> newItem = new HashMap<>();
            newItem.put("productId", productId);
            newItem.put("quantity", quantity);
            cartItems.add(newItem);
        }

        redisTemplate.opsForValue().set(key, cartItems, Duration.ofHours(1)); // 1-hour TTL
    }

    public List<Map<String, Object>> getCart(String sessionId, Long storeId) {
        String key = generateCartKey(sessionId, storeId);
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) redisTemplate.opsForValue().get(key);

        // Initialize cart if null
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        return cartItems;
    }


    public void removeFromCart(String sessionId, Long storeId, Long productId) {
        String key = generateCartKey(sessionId, storeId);
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) redisTemplate.opsForValue().get(key);

        if (cartItems != null) {
            cartItems.removeIf(item -> item.get("productId").equals(productId));
            redisTemplate.opsForValue().set(key, cartItems, Duration.ofHours(1)); // Update expiration
        }
    }

    public void clearCart(String sessionId, Long storeId) {
        String key = generateCartKey(sessionId, storeId);
        redisTemplate.delete(key);
    }
}
