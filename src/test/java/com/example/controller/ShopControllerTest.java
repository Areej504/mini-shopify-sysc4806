package com.example.controller;

import com.example.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

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

    }

     //Scenario 3: Accessing the cart view with no items
//    @Test
//    public void testOpenCartView_WithItemsInCart_ReturnsCartView() throws Exception {
//        // Setup product and cart items
//        Product product = new Product();
//        product.setProductName("Sample Product");
//        product.setPrice(BigDecimal.valueOf(10.00));
//
//        CartItem cartItem = new CartItem();
//        cartItem.setCartItemId(1L);
//        cartItem.setProduct(product);
//        cartItem.setQuantity(2);
//
//        Cart cart = new Cart();
//
//        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
//
//        mockMvc.perform(get("/cartView"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("cartItems"))
//                .andExpect(model().attribute("totalPrice", cart.getTotalPrice()))
//                .andExpect(model().attribute("totalItemsInCart", cart.getCartItems().size()))
//                .andExpect(view().name("cartView"));
//
//        verify(cartRepository, times(1)).findById(1L);
//        verify(shopRepository, times(1)).findById(1L);
//    }


    @Test
    public void testOpenCartView_WithItemsInCart_ReturnsCartView() throws Exception {
        // Setup product, shop, promotion, and cart items
        Shop shop = new Shop();
        shop.setPromotion(PromotionType.BUY_ONE_GET_ONE);

        Product product = new Product();
        product.setProductName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.00));
        product.setShop(shop); // Associate the product with a shop

        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem); // Add the CartItem to a list

        Cart cart = new Cart();
        cart.setCartItems(cartItems); // Use setCartItems to populate the cart

        // Mock the cart repository
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        // Perform the mock request
        mockMvc.perform(get("/cartView"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart")) // Check that the cart attribute exists
                .andExpect(model().attributeExists("storePromotion")) // Check for the store promotion attribute
                .andExpect(model().attribute("storePromotion", PromotionType.BUY_ONE_GET_ONE)) // Verify the promotion type
                .andExpect(model().attribute("BUY_ONE_GET_ONE", PromotionType.BUY_ONE_GET_ONE)) // Verify promotion constant
                .andExpect(view().name("cartView")); // Verify the view name

        // Verify repository interactions
        verify(cartRepository, times(1)).findById(1L);
    }


    @Test
    public void testAddToCart_Success() throws Exception {
        // Mock product repository to return a product
        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.00));
        product.setInventory(10);
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




//    @Test
//    public void testAddToCart_Success() throws Exception {
//        // Mock product repository to return a product
//        Product product = new Product();
//        product.setProductId(1L);
//        product.setProductName("Sample Product");
//        product.setPrice(BigDecimal.valueOf(10.00));
//        product.setInventory(10); // Ensure inventory is sufficient for the test
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        // Mock cart repository to return a cart
//        Cart cart = new Cart();
//        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
//        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
//
//        // Perform the mock request
//        mockMvc.perform(post("/addToCart")
//                        .contentType("application/json")
//                        .content("{\"productId\": 1, \"quantity\": 1}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Product added to cart successfully!"))
//                .andExpect(jsonPath("$.totalItemsInCart").exists()); // Check for total items in cart field
//
//        // Verify repository interactions
//        verify(productRepository, times(1)).findById(1L);
//        verify(cartRepository, times(1)).save(any(Cart.class));
//    }


    @Test
    void testRemoveFromCart_Success() throws Exception {
        Long cartItemId = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(cartItemId);
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        mockMvc.perform(post("/cart/removeItem")
                        .contentType("application/json")
                        .content("{\"cartItemId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item removed successfully"));

        verify(cartItemRepository, times(1)).findById(cartItemId);
        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void testUpdateQuantity_ExceedsInventory() throws Exception {
        Product product = new Product();
        product.setProductName("Sample Product");
        product.setPrice(BigDecimal.valueOf(10.00));
        product.setInventory(1); //set the inventory to 1

        Long cartItemId = 1L;
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCartItemId(cartItemId);
        cartItem.setQuantity(2); //set the quantity to exceed the product inventory

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));

        mockMvc.perform(post("/cart/updateQuantity")
                        .contentType("application/json")
                        .content("{\"cartItemId\": 1, \"action\": \"increase\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Cannot exceed available inventory"));

        verify(cartItemRepository, times(1)).findById(cartItemId);
    }

}
