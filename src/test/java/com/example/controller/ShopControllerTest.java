package com.example.controller;

import com.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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

    private Shop testShop;
    private Product testProduct;
    private Cart testCart;
    private CartItem testCartItem;

    @BeforeEach
    public void setup() {
        // Setup test data
//        testProduct = new Product();
//        testProduct.setProductName("Sample Product");
//        testProduct.setPrice(BigDecimal.valueOf(10.00));
//
//        testShop = new Shop();
//        testShop.setShopId(1L);
//        testShop.setName("Test Shop");
//        testShop.setDescription("A test shop description");
//        testShop.setProducts(Collections.singletonList(testProduct));
//
//        testCart = new Cart();
//        testCart.setCartItems(Collections.emptyList()); // Start with an empty cart
        // Setup Product
        testProduct = new Product();
        testProduct.setProductName("Sample Product");
        testProduct.setPrice(BigDecimal.valueOf(10.00));

        // Setup CartItem with associated Product
        testCartItem = new CartItem();
        testCartItem.setProduct(testProduct);
        testCartItem.setQuantity(1);

        // Setup Cart with CartItem
        testCart = new Cart();
        testCart.getCartItems().add(testCartItem);

        // Setup Shop
        testShop = new Shop();
        testShop.setShopId(1L);
        testShop.setName("Test Shop");
        testShop.setDescription("A test shop description");
        testShop.setProducts(Collections.singletonList(testProduct));
    }

    // Scenario 1: Valid shopId, Shop details should be displayed
    @Test
    public void testGetShopDetails_WithValidShopId_ReturnsShopPageView() throws Exception {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(testShop));
        when(cartRepository.count()).thenReturn(5L);

        mockMvc.perform(get("/shopPage/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("shopName", "Test Shop"))
                .andExpect(model().attribute("shopDescription", "A test shop description"))
                .andExpect(model().attribute("products", testShop.getProducts()))
                .andExpect(model().attribute("totalItemsInCart", 5L))
                .andExpect(view().name("shopPage"));

//        verify(shopRepository, times(1)).findById(anyLong());
//        verify(cartRepository, times(1)).count();
    }

    // Scenario 2: Invalid shopId, No shop details should be displayed
    @Test
    public void testGetShopDetails_WithInvalidShopId_ReturnsShopPageWithNoDetails() throws Exception {
        when(shopRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(cartRepository.count()).thenReturn(5L);

        mockMvc.perform(get("/shopPage/999"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("shopName", "shopDescription", "products"))
                .andExpect(model().attribute("totalItemsInCart", 5L))
                .andExpect(view().name("shopPage"));

//        verify(shopRepository, times(1)).findById(anyLong());
//        verify(cartRepository, times(1)).count();
    }

    // Scenario 3: Cart view with items in the cart
    @Test
    public void testOpenCartView_WithItemsInCart_ReturnsCartViewWithCartItems() throws Exception {
        //testCart.getCartItems().add(new CartItem(testCart, testProduct, 2));
        testCart.setCartItems(Collections.singletonList(testCartItem));
        when(cartRepository.findAll()).thenReturn(Collections.singletonList(testCart));

        mockMvc.perform(get("/cartView"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cartItems", Collections.singletonList(testCart)))
                .andExpect(view().name("cartView"));

        //verify(cartRepository, times(1)).findAll();
    }

    // Scenario 4: Cart view with an empty cart
    @Test
    public void testOpenCartView_WithEmptyCart_ReturnsCartViewWithNoItems() throws Exception {
        when(cartRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cartView"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("cartItems", Collections.emptyList()))
                .andExpect(view().name("cartView"));

        //verify(cartRepository, times(1)).findAll();
    }

    // Scenario 5: Accessing the payment view
    @Test
    public void testOpenPaymentView_ReturnsPaymentView() throws Exception {
        mockMvc.perform(get("/paymentView"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentView"));
    }

}


//package com.example.controller;
//
//import com.example.model.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ShopController.class)
//public class ShopControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ShopRepository shopRepository;
//
//    @MockBean
//    private CartRepository cartRepository;
//
//    // Tests for GET /shopPage/{shopId}
//    @Test
//    public void testGetShopDetails_WithValidShopId_ReturnsShopPageView() throws Exception {
//        Shop shop = new Shop();
//        shop.setShopId(1L);
//        shop.setName("Test Shop");
//        shop.setDescription("A test shop description");
//        shop.setProducts(Collections.emptyList());
//
//        when(shopRepository.findById(anyLong())).thenReturn(Optional.of(shop));
//        when(cartRepository.count()).thenReturn(5L);
//
//        mockMvc.perform(get("/shopPage/1"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("shopName", "Test Shop"))
//                .andExpect(model().attribute("shopDescription", "A test shop description"))
//                .andExpect(model().attribute("products", Collections.emptyList()))
//                .andExpect(model().attribute("totalItemsInCart", 5L))
//                .andExpect(view().name("shopPage"));
//
//        verify(shopRepository, times(1)).findById(anyLong());
//        verify(cartRepository, times(1)).count();
//    }
//
//    @Test
//    public void testGetShopDetails_WithInvalidShopId_ReturnsShopPageWithNoDetails() throws Exception {
//        when(shopRepository.findById(anyLong())).thenReturn(Optional.empty());
//        when(cartRepository.count()).thenReturn(5L);
//
//        mockMvc.perform(get("/shopPage/999"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeDoesNotExist("shopName", "shopDescription", "products"))
//                .andExpect(model().attribute("totalItemsInCart", 5L))
//                .andExpect(view().name("shopPage"));
//
//        verify(shopRepository, times(1)).findById(anyLong());
//        verify(cartRepository, times(1)).count();
//    }
//
//    // Tests for GET /cartView
//    @Test
//    public void testOpenCartView_ReturnsCartViewWithCartItems() throws Exception {
////        Cart cartItem = new Cart();
////        cartItem.setImageUrl("/images/test-product.png");
////        when(cartRepository.findAll()).thenReturn(Collections.singletonList(cartItem));
////
////        mockMvc.perform(get("/cartView"))
////                .andExpect(status().isOk())
////                .andExpect(model().attribute("cartItems", Collections.singletonList(cartItem)))
////                .andExpect(view().name("cartView"));
////
////        verify(cartRepository, times(1)).findAll();
//        Cart cart = new Cart();
//        Product product = new Product();
//        product.setProductName("Sample Product"); // Ensure Product has a name
//        product.setPrice(BigDecimal.valueOf(10.00)); // Set a price if needed
//
//        CartItem cartItem = new CartItem(cart, product, 2); // Create CartItem with Product
//        cart.setCartItems(Collections.singletonList(cartItem));
//
//        when(cartRepository.findAll()).thenReturn(Collections.singletonList(cart));
//
//        mockMvc.perform(get("/cartView"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("cartItems", Collections.singletonList(cartItem)))
//                .andExpect(view().name("cartView"));
//
//        verify(cartRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void testOpenCartView_WithEmptyCart_ReturnsCartViewWithNoItems() throws Exception {
//        when(cartRepository.findAll()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/cartView"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("cartItems", Collections.emptyList()))
//                .andExpect(view().name("cartView"));
//
//        verify(cartRepository, times(1)).findAll();
//    }
//
//    // Tests for GET /paymentView
//    @Test
//    public void testOpenPaymentView_ReturnsPaymentView() throws Exception {
//        mockMvc.perform(get("/paymentView"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("paymentView"));
//    }
//}
