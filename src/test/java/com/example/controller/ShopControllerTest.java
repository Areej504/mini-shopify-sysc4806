package com.example.controller;

import com.example.model.*;
import com.example.repository.CartItemRepository;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.ShopRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShopController.class)
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShopRepository shopRepository;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CartItemRepository cartItemRepository;


    private Shop testShop;
    private Product testProduct;
    private Cart testCart;
    private CartItem testCartItem;

    @BeforeEach
    public void setup() {
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
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setName("Test Shop");
        shop.setDescription("A test shop description");

        when(shopRepository.findById(1L)).thenReturn(Optional.of(shop));
        when(cartRepository.count()).thenReturn(5L);

        mockMvc.perform(get("/shopPage/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("shopName", "Test Shop"))
                .andExpect(model().attribute("shopDescription", "A test shop description"))
                .andExpect(model().attribute("totalItemsInCart", 5L))
                .andExpect(view().name("shopPage"));

        verify(shopRepository, times(1)).findById(1L);
    }


    @Test
    public void testOpenCartView_WithItemsInCart_ReturnsCartView() throws Exception {
        // Setup product and cart items
        Product product = new Product();
        product.setProductName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.00));

        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        mockMvc.perform(get("/cartView"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cartItems"))
                .andExpect(model().attribute("totalPrice", cart.getTotalPrice()))
                .andExpect(model().attribute("totalItemsInCart", cart.getCartItems().size()))
                .andExpect(view().name("cartView"));

        verify(cartRepository, times(1)).findById(1L);
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
//    @Test
//    public void testOpenCartView_WithItemsInCart_ReturnsCartViewWithCartItems() throws Exception {
//        //testCart.getCartItems().add(new CartItem(testCart, testProduct, 2));
//        testCart.setCartItems(Collections.singletonList(testCartItem));
//        when(cartRepository.findAll()).thenReturn(Collections.singletonList(testCart));
//
//        mockMvc.perform(get("/cartView"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("cartItems", Collections.singletonList(testCart)))
//                .andExpect(view().name("cartView"));
//
//        //verify(cartRepository, times(1)).findAll();
//    }
//
//    // Scenario 4: Cart view with an empty cart
//    @Test
//    public void testOpenCartView_WithEmptyCart_ReturnsCartViewWithNoItems() throws Exception {
//        when(cartRepository.findAll()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/cartView"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("cartItems", Collections.emptyList()))
//                .andExpect(view().name("cartView"));
//
//        //verify(cartRepository, times(1)).findAll();
//    }

    // Scenario 5: Accessing the payment view
    @Test
    public void testOpenPaymentView_ReturnsPaymentView() throws Exception {
        mockMvc.perform(get("/paymentView"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentView"));
    }


    @Test
    public void testAddToCart_Success() throws Exception {
        // Mock product repository to return a product
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.00));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Mock cart repository to return a cart
        Cart cart = new Cart();
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        mockMvc.perform(post("/addToCart")
                        .contentType("application/json")
                        .content("{\"productId\": 1, \"quantity\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product added to cart successfully!"));

        verify(productRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }



}
