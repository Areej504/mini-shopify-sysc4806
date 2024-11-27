package com.example.cache;

import com.example.cache.RedisConfig;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import java.util.*;

public class CartService {
    private final Jedis jedis = RedisConfig.getRedisClient();
    private final Gson gson = new Gson();

    // Add item to cart
    public void addToCart(String sessionId, Long productId, int quantity) {
        String key = "cart:" + sessionId;
        String cartJson = jedis.get(key);

        List<Map<String, Object>> cartItems;
        if (cartJson != null) {
            cartItems = gson.fromJson(cartJson, List.class);
        } else {
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

        jedis.set(key, gson.toJson(cartItems));
        jedis.expire(key, 60); // Set cart expiration to 1 hour
    }

    // Retrieve cart
    public List<Map<String, Object>> getCart(String sessionId) {
        String key = "cart:" + sessionId;
        String cartJson = jedis.get(key);

        if (cartJson != null) {
            return gson.fromJson(cartJson, List.class);
        } else {
            return new ArrayList<>();
        }
    }

    // Remove item from cart
    public void removeFromCart(String sessionId, Long productId) {
        String key = "cart:" + sessionId;
        String cartJson = jedis.get(key);

        if (cartJson != null) {
            List<Map<String, Object>> cartItems = gson.fromJson(cartJson, List.class);
            cartItems.removeIf(item -> item.get("productId").equals(productId));
            jedis.set(key, gson.toJson(cartItems));
        }
    }

    // Clear cart
    public void clearCart(String sessionId) {
        String key = "cart:" + sessionId;
        jedis.del(key);
    }
}
