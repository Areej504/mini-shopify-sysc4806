package com.example.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private CartService cartService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartService(redisTemplate);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testAddToCart_NewCartItem() {
        String sessionId = "session1";
        Long storeId = 1L;
        Long productId = 101L;
        int quantity = 2;
        int inventory = 5;

        when(valueOperations.get(anyString())).thenReturn(null);

        boolean result = cartService.addToCart(sessionId, storeId, productId, quantity, inventory);

        assertTrue(result);
        verify(valueOperations).set(eq("cart:session1:1"), any(), eq(Duration.ofSeconds(3600)));
    }

    @Test
    void testAddToCart_ExistingCartItem() {
        String sessionId = "session1";
        Long storeId = 1L;
        Long productId = 101L;
        int quantity = 2;
        int inventory = 5;

        Map<String, Object> existingItem = new HashMap<>();
        existingItem.put("productId", productId);
        existingItem.put("quantity", 1);

        List<Map<String, Object>> existingCart = new ArrayList<>();
        existingCart.add(existingItem);

        when(valueOperations.get(anyString())).thenReturn(existingCart);

        boolean result = cartService.addToCart(sessionId, storeId, productId, quantity, inventory);

        assertTrue(result);
        assertEquals(3, existingItem.get("quantity"));
        verify(valueOperations).set(eq("cart:session1:1"), eq(existingCart), eq(Duration.ofSeconds(3600)));
    }

    @Test
    void testAddToCart_ExceedsInventory() {
        String sessionId = "session1";
        Long storeId = 1L;
        Long productId = 101L;
        int quantity = 10;
        int inventory = 5;

        Map<String, Object> existingItem = new HashMap<>();
        existingItem.put("productId", productId);
        existingItem.put("quantity", 1);

        List<Map<String, Object>> existingCart = new ArrayList<>();
        existingCart.add(existingItem);

        when(valueOperations.get(anyString())).thenReturn(existingCart);

        boolean result = cartService.addToCart(sessionId, storeId, productId, quantity, inventory);

        assertFalse(result);
        verify(valueOperations, never()).set(anyString(), any(), any());
    }

    @Test
    void testRemoveFromCart_ItemExists() {
        String sessionId = "session1";
        Long storeId = 1L;
        Long cartItemId = 201L;

        Map<String, Object> existingItem = new HashMap<>();
        existingItem.put("cartItemId", cartItemId);

        List<Map<String, Object>> existingCart = new ArrayList<>();
        existingCart.add(existingItem);

        when(valueOperations.get(anyString())).thenReturn(existingCart);

        boolean result = cartService.removeFromCart(sessionId, storeId, cartItemId);

        assertTrue(result);
        assertTrue(existingCart.isEmpty());
        verify(valueOperations).set(eq("cart:session1:1"), eq(existingCart), eq(Duration.ofSeconds(3600)));
    }

    @Test
    void testRemoveFromCart_ItemDoesNotExist() {
        String sessionId = "session1";
        Long storeId = 1L;
        Long cartItemId = 201L;

        List<Map<String, Object>> existingCart = new ArrayList<>();

        when(valueOperations.get(anyString())).thenReturn(existingCart);

        boolean result = cartService.removeFromCart(sessionId, storeId, cartItemId);

        assertFalse(result);
        verify(valueOperations, never()).set(anyString(), any(), any());
    }

    @Test
    void testClearCart() {
        String sessionId = "session1";
        Long storeId = 1L;

        cartService.clearCart(sessionId, storeId);

        verify(redisTemplate).delete(eq("cart:session1:1"));
    }
}
