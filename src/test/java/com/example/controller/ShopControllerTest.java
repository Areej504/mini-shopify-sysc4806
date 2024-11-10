package com.example.controller;

import com.example.model.Cart;
import com.example.model.CartRepository;
import com.example.model.Shop;
import com.example.model.ShopRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShopController.class)
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopRepository shopRepository;

    @MockBean
    private CartRepository cartRepository;

    // Tests for GET /shopPage/{shopId}
    @Test
    public void testGetShopDetails_WithValidShopId_ReturnsShopPageView() throws Exception {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setName("Test Shop");
        shop.setDescription("A test shop description");
        shop.setProducts(Collections.emptyList());

        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shop));
        when(cartRepository.count()).thenReturn(5L);

        mockMvc.perform(get("/shopPage/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("shopName", "Test Shop"))
                .andExpect(model().attribute("shopDescription", "A test shop description"))
                .andExpect(model().attribute("products", Collections.emptyList()))
                .andExpect(model().attribute("totalItemsInCart", 5L))
                .andExpect(view().name("shopPage"));

        verify(shopRepository, times(1)).findById(anyLong());
        verify(cartRepository, times(1)).count();
    }

    @Test
    public void testGetShopDetails_WithInvalidShopId_ReturnsShopPageWithNoDetails() throws Exception {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(cartRepository.count()).thenReturn(5L);

        mockMvc.perform(get("/shopPage/999"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("shopName", "shopDescription", "products"))
                .andExpect(model().attribute("totalItemsInCart", 5L))
                .andExpect(view().name("shopPage"));

        verify(shopRepository, times(1)).findById(anyLong());
        verify(cartRepository, times(1)).count();
    }

    // Tests for GET /cartView
    @Test
    public void testOpenCartView_ReturnsCartViewWithCartItems() throws Exception {
        Cart cartItem = new Cart();
        when(cartRepository.findAll()).thenReturn(Collections.singletonList(cartItem));

        mockMvc.perform(get("/cartView"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cartItems", Collections.singletonList(cartItem)))
                .andExpect(view().name("cartView"));

        verify(cartRepository, times(1)).findAll();
    }

    @Test
    public void testOpenCartView_WithEmptyCart_ReturnsCartViewWithNoItems() throws Exception {
        when(cartRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cartView"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cartItems", Collections.emptyList()))
                .andExpect(view().name("cartView"));

        verify(cartRepository, times(1)).findAll();
    }

    // Tests for GET /paymentView
    @Test
    public void testOpenPaymentView_ReturnsPaymentView() throws Exception {
        mockMvc.perform(get("/paymentView"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentView"));
    }
}
