package com.example.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderInfoTest {

    private OrderInfo order;
    private Customer mockCustomer;
    private Cart mockCart;
    private CartItem mockCartItem1;
    private CartItem mockCartItem2;
    private Product mockProduct1;
    private Product mockProduct2;

    @BeforeEach
    public void setUp() {
        mockCustomer = mock(Customer.class);
        mockCart = mock(Cart.class);
        mockCartItem1 = mock(CartItem.class);
        mockCartItem2 = mock(CartItem.class);
        mockProduct1 = mock(Product.class);
        mockProduct2 = mock(Product.class);

        when(mockCartItem1.getProduct()).thenReturn(mockProduct1);
        when(mockCartItem1.getQuantity()).thenReturn(2);
        when(mockCartItem2.getProduct()).thenReturn(mockProduct2);
        when(mockCartItem2.getQuantity()).thenReturn(1);

        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(mockCartItem1);
        cartItems.add(mockCartItem2);

        when(mockCart.getCartItems()).thenReturn(cartItems);

//        Shop testShop = new Shop();
//        testShop.setShopId(1L);
        order = new OrderInfo(new Date(), BigDecimal.ZERO, mockCustomer, mockCart, OrderStatus.PROCESSING);
    }

    @Test
    public void testConstructor_DefaultValues() {
        // Arrange & Act
        OrderInfo order = new OrderInfo();

        // Assert
        assertNull(order.getOrderId(), "Order ID should be null by default.");
        assertNull(order.getOrderDate(), "Order date should be null by default.");
        assertNull(order.getTotalAmount(), "Total amount should be null by default.");
        assertNull(order.getCustomer(), "Customer should be null by default.");
        assertNull(order.getCart(), "Cart should be null by default.");
        assertNull(order.getStatus(), "Order status should be null by default.");
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Date orderDate = new Date();
        BigDecimal totalAmount = new BigDecimal("100.00");
        Payment payment = mock(Payment.class);

        order.setOrderId(1L);
        order.setOrderDate(orderDate);
        order.setTotalAmount(totalAmount);
        order.setCustomer(mockCustomer);
        order.setCart(mockCart);
        order.setStatus(OrderStatus.SHIPPED);

        // Act & Assert
        assertEquals(1L, order.getOrderId(), "Order ID should be set correctly.");
        assertEquals(orderDate, order.getOrderDate(), "Order date should be set correctly.");
        assertEquals(totalAmount, order.getTotalAmount(), "Total amount should be set correctly.");
        assertEquals(mockCustomer, order.getCustomer(), "Customer should be set correctly.");
        assertEquals(mockCart, order.getCart(), "Cart should be set correctly.");
        assertEquals(OrderStatus.SHIPPED, order.getStatus(), "Order status should be set correctly.");
    }

    @Test
    public void testGetProducts_WithNoProducts() {
        // Arrange
        when(mockCart.getCartItems()).thenReturn(new ArrayList<>());

        // Act
        List<CartItem> items = order.getCart().getCartItems();

        // Assert
        assertTrue(items.isEmpty(), "Cart should be empty when no items are added.");
    }

    @Test
    public void testGetProducts_WithProducts() {
        // Act
        List<CartItem> items = order.getCart().getCartItems();

        // Assert
        assertEquals(2, items.size(), "Cart should contain two items.");
        assertEquals(mockProduct1, items.get(0).getProduct(), "First product should match.");
        assertEquals(2, items.get(0).getQuantity(), "First product quantity should match.");
        assertEquals(mockProduct2, items.get(1).getProduct(), "Second product should match.");
        assertEquals(1, items.get(1).getQuantity(), "Second product quantity should match.");
    }

    @Test
    public void testCalculateTotalPrice() {
        // Arrange
        when(mockProduct1.getPrice()).thenReturn(new BigDecimal("50.00"));
        when(mockProduct2.getPrice()).thenReturn(new BigDecimal("25.00"));

        // Act
        BigDecimal totalPrice = mockCart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Assert
        assertEquals(new BigDecimal("125.00"), totalPrice, "Total price should match the sum of item prices.");
    }

    @Test
    public void testUpdateStatus() {
        // Act
        order.setStatus(OrderStatus.SHIPPED);

        // Assert
        assertEquals(OrderStatus.SHIPPED, order.getStatus(), "Order status should be updated to SHIPPED.");
    }

    @Test
    public void testCartItemsQuantity() {
        // Act
        List<CartItem> items = order.getCart().getCartItems();

        // Assert
        assertEquals(2, items.get(0).getQuantity(), "First item's quantity should be correct.");
        assertEquals(1, items.get(1).getQuantity(), "Second item's quantity should be correct.");
    }

    @Test
    public void testToString() {
        // Arrange
        order.setOrderDate(new Date());
        order.setTotalAmount(new BigDecimal("150.00"));
        order.setStatus(OrderStatus.PROCESSING);

        // Act
        String orderString = order.toString();

        // Assert
        assertTrue(orderString.contains("Order{"), "toString should contain 'Order{'.");
        assertTrue(orderString.contains("totalAmount=150.00"), "toString should contain total amount.");
        assertTrue(orderString.contains("status=PROCESSING"), "toString should contain order status.");
    }
}