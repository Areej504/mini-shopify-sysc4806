package com.example.controller;

import com.example.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ShopRepository shopRepository;

    @MockBean
    private OrderInfoRepository orderInfoRepository;

    // Tests for GET /merchantShop/{shopId}
    @Test
    public void testViewMerchantShop_WithValidShopId_ReturnsMerchantShopView() throws Exception {
        Shop shop = new Shop();
        shop.setShopId(1L);

        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shop));
        when(productRepository.findByShop(any(Shop.class))).thenReturn(Collections.emptyList());
        when(orderInfoRepository.findByShop(any(Shop.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/merchantShop/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shop"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("merchantShop"));

        verify(shopRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findByShop(any(Shop.class));
    }

    @Test
    public void testViewMerchantShop_InvalidShopId_ThrowsException() throws Exception {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/merchantShop/999"))
                .andExpect(status().isNotFound());

        verify(shopRepository, times(1)).findById(anyLong());
        verify(productRepository, never()).findByShop(any(Shop.class));
    }

    // Tests for POST /merchantShop/{shopId}
    @Test
    public void testAddProduct_WithValidShopId_RedirectsToMerchantShop() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "productImage",
                "test-image.jpg",
                "image/jpeg",
                "Sample Image Content".getBytes()
        );

        Shop shop = new Shop();
        shop.setShopId(1L);

        Product product = new Product();
        product.setProductName("Test Product");

        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shop));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(multipart("/merchantShop/{shopId}", 1L)
                        .file(mockFile)
                        .param("productName", "Test Product")
                        .param("price", "10.99")
                        .param("inventory", "100")
                        .param("description", "Test Description")
                        .param("category", Category.ELECTRONICS.name())
                        .param("promotionType", PromotionType.CLEARANCE.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/merchantShop/1"));

        verify(shopRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testAddProduct_InvalidShopId_ThrowsException() throws Exception {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.empty());

        MockMultipartFile mockFile = new MockMultipartFile(
                "productImage",
                "test-image.jpg",
                "image/jpeg",
                "Sample Image Content".getBytes()
        );

        mockMvc.perform(multipart("/merchantShop/{shopId}", 999L)
                        .file(mockFile)
                        .param("productName", "Test Product")
                        .param("price", "10.99")
                        .param("inventory", "100")
                        .param("description", "Test Description")
                        .param("category", Category.ELECTRONICS.name())
                        .param("promotionType", PromotionType.CLEARANCE.name()))
                .andExpect(status().isNotFound());

        verify(shopRepository, times(1)).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
    }

    // Tests for DELETE /merchantShop/{shopId}/{productId}
    @Test
    public void testRemoveProduct_WithValidProductId_ReturnsOk() throws Exception {
        when(productRepository.existsById(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/merchantShop/1/1"))
                .andExpect(status().isOk());

        verify(productRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testRemoveProduct_InvalidProductId_ReturnsNotFound() throws Exception {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/merchantShop/1/999"))
                .andExpect(status().isNotFound());

        verify(productRepository, times(1)).existsById(anyLong());
        verify(productRepository, never()).deleteById(anyLong());
    }

    // Tests for GET /merchantShop/{shopId}/product/{productId}
    @Test
    public void testGetProduct_WithValidShopIdAndProductId_ReturnsProduct() throws Exception {
        Shop shop = new Shop();
        shop.setShopId(1L);

        Product product = new Product();
        product.setProductId(1L);
        product.setShop(shop);

        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shop));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        mockMvc.perform(get("/merchantShop/{shopId}/product/{productId}", 1L, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1L));

        verify(shopRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetProduct_InvalidShopIdOrProductId_ThrowsException() throws Exception {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/merchantShop/{shopId}/product/{productId}", 999L, 999L))
                .andExpect(status().isNotFound());

        verify(shopRepository, times(1)).findById(anyLong());
        verify(productRepository, never()).findById(anyLong());
    }
}
