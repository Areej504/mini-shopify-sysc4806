package com.example.cache;

import com.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int expiryDurationInSeconds = 3600;

    @Autowired
    public CartService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String generateCartKey(String sessionId, Long storeId) {
        return "cart:" + sessionId + ":" + storeId; // Composite key
    }

    public Boolean addToCart(String sessionId, Long storeId, Long productId, int quantity, int inventory) {
        String key = generateCartKey(sessionId, storeId);
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) redisTemplate.opsForValue().get(key);
        System.out.println("Session aC: " + sessionId);

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        boolean itemExists = false;
        for (Map<String, Object> item : cartItems) {
            System.out.println("Product ID: " + item.get("productId"));
            System.out.println("Quantity: " + item.get("quantity"));
            System.out.println("Inventory: " + inventory);

            if (item.get("productId").equals(productId)) {
                if (((int) item.get("quantity") + quantity) <= inventory) {
                    item.put("quantity", (int) item.get("quantity") + quantity);
                    itemExists = true;
                    break;
                }
                else {
                    return false;
                }
            }
        }
        Random r = new Random();

        if (!itemExists) {
            Map<String, Object> newItem = new HashMap<>();
            newItem.put("productId", productId);
            newItem.put("quantity", quantity);
            newItem.put("cartItemId",r.nextLong(100));
            cartItems.add(newItem);
        }

        redisTemplate.opsForValue().set(key, cartItems, Duration.ofSeconds(expiryDurationInSeconds)); // 1-hour TTL
        return true;
    }

    public boolean updateQuantity(String sessionId, Long storeId, Long cartItemId, String action, int inventory) {
        // Retrieve cart items for the given sessionId and storeId
        List<Map<String, Object>> cartItems = getCart(sessionId, storeId);
        System.out.println("Session uQ: " + sessionId);
        System.out.println("Cart Items Retrieved: " + cartItems);

        for (Map<String, Object> item : cartItems) {
            System.out.println("Item stored: " + item.get("cartItemId"));
            System.out.println("Item com: " + cartItemId);
            if (item.get("cartItemId").toString().equals(cartItemId.toString())) {
                int currentQuantity = Integer.parseInt(item.get("quantity").toString());
                System.out.println(currentQuantity);
                System.out.println(item.get("quantity"));

                if ("increase".equals(action) && inventory >= currentQuantity+1) {
                    item.put("quantity", currentQuantity + 1);
                } else if ("decrease".equals(action) && currentQuantity > 1) {
                    item.put("quantity", currentQuantity - 1);
                } else {
                    return false;
                }

                String key = generateCartKey(sessionId, storeId);
                redisTemplate.opsForValue().set(key, cartItems, Duration.ofSeconds(expiryDurationInSeconds));
                System.out.println("Updated cart saved to Redis: " + cartItems);
                return true;
            }
        }
        return false;
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

    public boolean removeFromCart(String sessionId, Long storeId, Long cartItemId) {
        String key = generateCartKey(sessionId, storeId);
        List<Map<String, Object>> cartItems = (List<Map<String, Object>>) redisTemplate.opsForValue().get(key);

        if (cartItems != null) {
            // Remove item with matching cartItemId
            boolean removed = cartItems.removeIf(item -> item.get("cartItemId").toString().equals(cartItemId.toString()));
            if (removed) {
                redisTemplate.opsForValue().set(key, cartItems, Duration.ofSeconds(expiryDurationInSeconds)); // Update expiration
                System.out.println("Item removed. Updated cart: " + cartItems);
                return true;
            }
        }
        System.out.println("Item not found in cart.");
        return false; // Item not found
    }


    public void clearCart(String sessionId, Long storeId) {
        String key = generateCartKey(sessionId, storeId);
        redisTemplate.delete(key);
    }
}
