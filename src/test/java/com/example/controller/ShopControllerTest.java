package com.example.controller;

import com.example.cache.CartService;
import com.example.model.*;
import com.example.model.ProductRepository;
import com.example.model.ShopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShopController.class)
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopRepository shopRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CartService cartService;

    private Shop testShop;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Mock Shop
        testShop = new Shop();
        testShop.setShopId(1L);
        testShop.setName("Test Shop");
        testShop.setDescription("Test Description");
        testShop.setPromotion(PromotionType.NONE);
        testShop.setCategories(Set.of(Category.ELECTRONICS, Category.CLOTHING));

        // Mock Product
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setProductName("Test Product");
        testProduct.setInventory(10);
        testProduct.setShop(testShop);

        testShop.setProducts(List.of(testProduct));

        // Mock Repositories and Services
        Mockito.when(shopRepository.findById(1L)).thenReturn(Optional.of(testShop));
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        Mockito.when(cartService.getCart(anyString(), eq(1L)))
                .thenReturn(List.of(Map.of("productId", 1L, "quantity", 1, "cartItemId", 1L)));
    }

    @Test
    void testGetShopDetails() throws Exception {
        mockMvc.perform(get("/shopPage/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("shopPage"))
                .andExpect(model().attributeExists("shopName", "shopDescription", "products", "shopPromotion", "totalItemsInCart"))
                .andExpect(model().attribute("shopName", "Test Shop"))
                .andExpect(model().attribute("shopDescription", "Test Description"))
                .andExpect(model().attribute("shopPromotion", PromotionType.NONE));
    }

    @Test
    void testAddToCart() throws Exception {
        String requestBody = """
            {
                "productId": 1,
                "storeId": 1,
                "inventory": 10
            }
            """;

        Mockito.when(cartService.addToCart(anyString(), eq(1L), eq(1L), eq(1), eq(10))).thenReturn(true);

        mockMvc.perform(post("/addToCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product added to cart successfully!"))
                .andExpect(jsonPath("$.totalItemsInCart").value("1"));
    }

    @Test
    void testOpenCartView() throws Exception {
        mockMvc.perform(get("/cartView/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cartView"))
                .andExpect(model().attributeExists("cart", "storeId"));
    }

    @Test
    void testUpdateQuantity() throws Exception {
        String requestBody = """
            {
                "cartItemId": 1,
                "storeId": 1,
                "inventory": 10,
                "action": "increment"
            }
            """;

        Mockito.when(cartService.updateQuantity(anyString(), eq(1L), eq(1L), eq("increment"), eq(10)))
                .thenReturn(true);

        mockMvc.perform(post("/cart/updateQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Quantity updated successfully"));
    }

    @Test
    void testRemoveFromCart() throws Exception {
        String requestBody = """
            {
                "cartItemId": 1,
                "storeId": 1
            }
            """;

        Mockito.when(cartService.removeFromCart(anyString(), eq(1L), eq(1L))).thenReturn(true);

        mockMvc.perform(post("/cart/removeItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Item removed successfully"));
    }

    @Test
    void testClearCart() throws Exception {
        String requestBody = """
            {
                "storeId": 1
            }
            """;

        mockMvc.perform(post("/clearCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Cart cleared successfully"));
    }
}
