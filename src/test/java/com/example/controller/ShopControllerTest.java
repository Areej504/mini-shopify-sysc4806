package com.example.controller;

import com.example.cache.CartService;
import com.example.model.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopControllerTest {

    @InjectMocks
    private ShopController shopController;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartService cartService;

    @Mock
    private OrderInfoRepository orderInfoRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    private Shop testShop;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize testShop
        testShop = new Shop();
        testShop.setShopId(1L);
        testShop.setName("Test Shop");
        testShop.setDescription("This is a test shop");

        // Initialize testProduct
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setProductName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(BigDecimal.valueOf(100));
        testProduct.setInventory(10);
        testProduct.setShop(testShop);

        // Add product to shop
        testShop.addProduct(testProduct);
    }

    @Test
    void testOpenCustomerScreen() {
        List<Shop> shops = List.of(testShop);
        when(shopRepository.findAll()).thenReturn(shops);

        String result = shopController.openCustomerScreen(model);

        verify(model).addAttribute("shops", shops);
        assertEquals("searchShops", result);
    }

    @Test
    void testGetShopDetails() {
        when(shopRepository.findById(1L)).thenReturn(Optional.of(testShop));
        when(session.getAttribute("sessionId")).thenReturn("test-session-id");
        when(cartService.getCart("test-session-id", 1L)).thenReturn(Collections.emptyList());

        String result = shopController.getShopDetails(1L, session, model);

        verify(model).addAttribute("shopName", testShop.getName());
        verify(model).addAttribute("shopDescription", testShop.getDescription());
        verify(model).addAttribute("products", testShop.getProducts());
        verify(model).addAttribute("shopPromotion", testShop.getPromotion());
        verify(model).addAttribute("totalItemsInCart", 0);
        assertEquals("shopPage", result);
    }

    @Test
    void testAddToCart_ValidProduct() {
        Map<String, Object> requestBody = Map.of(
                "productId", 1L,
                "storeId", 1L,
                "inventory", 10
        );
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(session.getAttribute("sessionId")).thenReturn("test-session-id");
        when(cartService.addToCart("test-session-id", 1L, 1L, 1, 10)).thenReturn(true);
        when(cartService.getCart("test-session-id", 1L)).thenReturn(Collections.emptyList());

        ResponseEntity<Map<String, String>> response = shopController.addToCart(requestBody, session);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Product added to cart successfully!", response.getBody().get("message"));
    }

    @Test
    void testAddToCart_ProductNotFound() {
        Map<String, Object> requestBody = Map.of(
                "productId", 1L,
                "storeId", 1L,
                "inventory", 10
        );
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = shopController.addToCart(requestBody, session);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Product not found", response.getBody().get("message"));
    }

    @Test
    void testClearCart() {
        Map<String, Object> requestBody = Map.of("storeId", 1L);
        when(session.getAttribute("sessionId")).thenReturn("test-session-id");

        ResponseEntity<String> response = shopController.clearCart(requestBody, session);

        verify(cartService).clearCart("test-session-id", 1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cart cleared successfully", response.getBody());
    }
}
