package com.example.controller;

import com.example.cache.CartService;
import com.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopRepository shopRepository;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderInfoRepository orderInfoRepository;

    @MockBean
    private ShippingRepository shippingRepository;

    @MockBean
    private CartRepository cartRepository;

    private Shop testShop;
    private Product testProduct;
    private CartItem testCartItem;
    private Shipping testShipping;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        // Set up test shop
        testShop = new Shop();
        testShop.setShopId(1L);
        testShop.setName("Test Shop");

        // Set up test product
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setProductName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100.00));
        testProduct.setDiscountedPrice(BigDecimal.valueOf(90.00));
        testProduct.setInventory(10);
        testProduct.setShop(testShop);

        // Set up test cart item
        testCartItem = new CartItem();
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(2);

        // Set up test shipping
        testShipping = new Shipping();
        testShipping.setId(1L);
        testShipping.setShippingAddress("123 Test Street");

        // Set up test cart
        testCart = new Cart(testShop);
        testCart.setCartItems(List.of(testCartItem));
    }

    @Test
    void testGuestCheckout() throws Exception {
        // Mock dependencies
        Mockito.when(shopRepository.findById(1L)).thenReturn(Optional.of(testShop));
        Mockito.when(cartService.getCart(anyString(), eq(1L))).thenReturn(List.of(
                Map.of("productId", 1L, "quantity", 2)
        ));
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/guestCheckout").param("storeId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentView"))
                .andExpect(model().attributeExists("cart", "cartItems", "totalAmount", "promotion", "shipping"));
    }

    @Test
    void testProcessPayment() throws Exception {
        // Mock dependencies
        Mockito.when(shopRepository.findById(1L)).thenReturn(Optional.of(testShop));
        Mockito.when(cartService.getCart(anyString(), eq(1L))).thenReturn(List.of(
                Map.of("productId", 1L, "quantity", 2)
        ));
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        Mockito.when(shippingRepository.save(any(Shipping.class))).thenReturn(testShipping);
        Mockito.when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
        Mockito.when(orderInfoRepository.save(any(OrderInfo.class))).thenReturn(new OrderInfo());

        mockMvc.perform(post("/processPayment")
                        .param("storeId", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("shippingAddress", "123 Test Street"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderConfirmation"))
                .andExpect(model().attributeExists("order"));
    }
}
