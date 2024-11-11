package com.example.controller;

import com.example.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


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

    // Tests for GET /merchantShop/{shopId}
    @Test
    public void testViewMerchantShop_WithValidShopId_ReturnsMerchantShopView() throws Exception {
        Shop shop = new Shop();
        shop.setShopId(1L);

        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shop));
        when(productRepository.findByShop(any(Shop.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/merchantShop/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("shop"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("merchantShop"));

        verify(shopRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).findByShop(any(Shop.class));
    }

//    @Test
//    public void testViewMerchantShop_InvalidShopId_ThrowsException() throws Exception {
//        when(shopRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/merchantShop/999"))
//                .andExpect(status().isNotFound());
//
//        verify(shopRepository, times(1)).findById(anyLong());
//        verify(productRepository, never()).findByShop(any(Shop.class));
//    }

    // Tests for POST /merchantShop/{shopId}
    @Test
    public void testAddProduct_WithValidShopId_RedirectsToMerchantShop() throws Exception {
        Shop shop = new Shop();
        shop.setShopId(1L);
        Product product = new Product();
        product.setProductName("Test Product");

        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shop));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/merchantShop/1")
                        .flashAttr("product", product))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/merchantShop/1"));

        verify(shopRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

//    @Test
//    public void testAddProduct_InvalidShopId_ThrowsException() throws Exception {
//        Product product = new Product();
//        product.setProductName("Test Product");
//
//        when(shopRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/merchantShop/999")
//                        .flashAttr("product", product))
//                .andExpect(status().isNotFound());
//
//        verify(shopRepository, times(1)).findById(anyLong());
//        verify(productRepository, never()).save(any(Product.class));
//    }

    // Tests for DELETE /merchantShop/{shopId}/{productId}
//    @Test
//    public void testRemoveProduct_WithValidProductId_RedirectsToMerchantShop() throws Exception {
//        when(productRepository.existsById(anyLong())).thenReturn(true);
//
//        mockMvc.perform(delete("/merchantShop/1/1"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/merchantShop/1"));
//
//
//        verify(productRepository, times(1)).existsById(anyLong());
//        verify(productRepository, times(1)).deleteById(anyLong());
//    }
//
//    @Test
//    public void testRemoveProduct_InvalidProductId_RedirectsToMerchantShop() throws Exception {
//        when(productRepository.existsById(anyLong())).thenReturn(false);
//
//        mockMvc.perform(delete("/merchantShop/1/999"))
//                .andExpect(status().is3xxRedirection());
//
//        verify(productRepository, times(1)).existsById(anyLong());
//        verify(productRepository, never()).deleteById(anyLong());
//    }
}
