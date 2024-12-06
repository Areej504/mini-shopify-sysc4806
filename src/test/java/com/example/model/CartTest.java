package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CartTest {

    private Cart cart;
    private Product product1;
    private Product product2;
    private Shop shop;

    @BeforeEach
    void setUp() {
        shop = new Shop();
        shop.setShopId(1L);
        shop.setName("Test Shop");
        shop.setPromotion(null); // Default no promotion

        cart = new Cart(shop);

        product1 = new Product();
        product1.setProductId(1L);
        product1.setProductName("Product 1");
        product1.setPrice(BigDecimal.valueOf(100.00));
        product1.setDiscountedPrice(BigDecimal.valueOf(80.00));
        product1.setInventory(10);
        product1.setShop(shop);

        product2 = new Product();
        product2.setProductId(2L);
        product2.setProductName("Product 2");
        product2.setPrice(BigDecimal.valueOf(200.00));
        product2.setInventory(5);
        product2.setShop(shop);
    }

    @Test
    void testAddProduct() {
        cart.addProduct(product1, 2);
        assertThat(cart.getCartItems()).hasSize(1);
        assertThat(cart.getCartItems().get(0).getProduct()).isEqualTo(product1);
        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    void testRemoveProduct() {
        cart.addProduct(product1, 1); // Add product with quantity 1
        cart.removeProduct(product1); // Remove product completely
        assertThat(cart.getCartItems()).isEmpty(); // Expect the cart to be empty
    }

    @Test
    void testGetTotalPrice() {
        cart.addProduct(product1, 2); // 2 * 80.00 = 160.00
        cart.addProduct(product2, 1); // 1 * 200.00 = 200.00
        BigDecimal expectedTotal = BigDecimal.valueOf(360.00).setScale(2);

        assertThat(cart.getTotalPrice()).isEqualTo(expectedTotal);
    }

//    @Test
//    void testGetFinalPrice() {
//        cart.addProduct(product1, 2); // 2 * 80.00 = 160.00
//        cart.addProduct(product2, 1); // 1 * 200.00 = 200.00
//        cart.setTotalPrice(cart.getTotalPrice()); // Set the total price
//        cart.setStoreDiscount(BigDecimal.valueOf(20.00)); // Apply a 20.00 discount
//        cart.setShippingCost(7.00); // Set flat shipping cost
//
//        // Expected Final Price: (Total Price - Discount) + GST + Shipping
//        BigDecimal expectedFinalPrice = BigDecimal.valueOf(360.00)
//                .subtract(BigDecimal.valueOf(20.00)) // Subtract discount
//                .add(BigDecimal.valueOf((360.00 - 20.00) * 0.13).setScale(2, RoundingMode.HALF_UP)) // GST
//                .add(BigDecimal.valueOf(7.00)) // Shipping
//                .setScale(2, RoundingMode.HALF_UP);
//
//        assertThat(cart.getFinalPrice()).isEqualTo(expectedFinalPrice);
//    }

    @Test
    void testGetStoreDiscountWithoutPromotion() {
        cart.addProduct(product1, 2); // No promotion
        assertThat(cart.getStoreDiscount()).isEqualTo(BigDecimal.ZERO.setScale(2));
    }

    @Test
    void testGetStoreDiscountWithBogoPromotion() {
        shop.setPromotion(PromotionType.BUY_ONE_GET_ONE); // Apply BOGO promotion
        cart.addProduct(product1, 2); // 2 items, 1 free
        BigDecimal expectedDiscount = product1.getDiscountedPrice(); // 80.00 discount
        assertThat(cart.getStoreDiscount()).isEqualTo(expectedDiscount.setScale(2));
    }

    @Test
    void testGetShippingCost() {
        cart.addProduct(product1, 1);
        assertThat(cart.getShippingCost()).isEqualTo(7.00);

        shop.setPromotion(PromotionType.FREE_SHIPPING); // Apply free shipping
        assertThat(cart.getShippingCost()).isEqualTo(0.00);
    }

    @Test
    void testEmptyCart() {
        assertThat(cart.getCartItems()).isEmpty();
        assertThat(cart.getTotalPrice()).isEqualTo(BigDecimal.ZERO.setScale(2));
        assertThat(cart.getStoreDiscount()).isEqualTo(BigDecimal.ZERO.setScale(2));
    }

    @Test
    void testGetTotalItems() {
        cart.addProduct(product1, 2);
        cart.addProduct(product2, 1);
        assertThat(cart.getTotalItems()).isEqualTo(3);
    }
}
