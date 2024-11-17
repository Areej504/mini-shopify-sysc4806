package com.example.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    private Customer customer;
    private OrderInfo mockOrder1;
    private OrderInfo mockOrder2;


    @BeforeEach
    public void setUp() {
        customer = new Customer("John Doe", "johndoe@example.com", "123 Street");
        mockOrder1 = mock(OrderInfo.class);
        mockOrder2 = mock(OrderInfo.class);
    }

    @Test
    public void testConstructor_ValidValues() {
        // Arrange & Act
        Customer customer = new Customer("Alice", "alice@example.com", "456 Avenue");

        // Assert
        assertEquals("Alice", customer.getName(), "Name should be set correctly.");
        assertEquals("alice@example.com", customer.getEmail(), "Email should be set correctly.");
        assertEquals("456 Avenue", customer.getAddress(), "Address should be set correctly.");
    }

    @Test
    public void testSettersAndGetters() {
        // Act
        customer.setName("Jane Doe");
        customer.setEmail("janedoe@example.com");
        customer.setAddress("789 Boulevard");

        // Assert
        assertEquals("Jane Doe", customer.getName(), "Name should be updated correctly.");
        assertEquals("janedoe@example.com", customer.getEmail(), "Email should be updated correctly.");
        assertEquals("789 Boulevard", customer.getAddress(), "Address should be updated correctly.");
    }

    @Test
    public void testGetCurrentOrders_NoOrders() {
        // Arrange
        customer.setOrders(Collections.emptyList());

        // Act
        int currentOrders = customer.getCurrentOrders();

        // Assert
        assertEquals(0, currentOrders, "Current orders should be 0 when there are no orders.");
    }

    @Test
    public void testGetCurrentOrders_WithProcessingOrders() {
        // Arrange
        when(mockOrder1.getStatus()).thenReturn(OrderStatus.PROCESSING);
        when(mockOrder2.getStatus()).thenReturn(OrderStatus.SHIPPED);
        customer.setOrders(Arrays.asList(mockOrder1, mockOrder2));

        // Act
        int currentOrders = customer.getCurrentOrders();

        // Assert
        assertEquals(2, currentOrders, "Current orders should count orders with PROCESSING and SHIPPED statuses.");
    }

    @Test
    public void testGetCurrentOrders_WithNonProcessingOrders() {
        // Arrange
        OrderInfo mockOrder3 = mock(OrderInfo.class);
        when(mockOrder1.getStatus()).thenReturn(OrderStatus.COMPLETED);
        when(mockOrder2.getStatus()).thenReturn(OrderStatus.CANCELLED);
        when(mockOrder3.getStatus()).thenReturn(OrderStatus.DELIVERED);
        customer.setOrders(Arrays.asList(mockOrder1, mockOrder2, mockOrder3));

        // Act
        int currentOrders = customer.getCurrentOrders();

        // Assert
        assertEquals(0, currentOrders, "Current orders should be 0 if no orders are in PROCESSING or SHIPPED status.");
    }

    @Test
    public void testGetCurrentOrders_NullOrdersList() {
        // Arrange
        customer.setOrders(null);

        // Act
        int currentOrders = customer.getCurrentOrders();

        // Assert
        assertEquals(0, currentOrders, "Current orders should be 0 when the orders list is null.");
    }

    @Test
    public void testSetOrders() {
        // Arrange
        List<OrderInfo> orders = Arrays.asList(mockOrder1, mockOrder2);

        // Act
        customer.setOrders(orders);

        // Assert
        assertEquals(orders, customer.getOrders(), "Orders list should be set correctly.");
    }

    @Test
    public void testToString() {
        // Act
        String customerString = customer.toString();

        // Assert
        assertTrue(customerString.contains("Customer{"), "toString should contain 'Customer{'.");
        assertTrue(customerString.contains("name='John Doe'"), "toString should contain customer name.");
        assertTrue(customerString.contains("email='johndoe@example.com'"), "toString should contain customer email.");
        assertTrue(customerString.contains("address='123 Street'"), "toString should contain customer address.");
    }

    // Placeholder tests for unimplemented methods
    @Test
    public void testPlaceOrder() {
        // Act & Assert
        assertDoesNotThrow(() -> customer.placeOrder(), "placeOrder should not throw an exception.");
    }

    @Test
    public void testSearchShops() {
        // Act & Assert
        assertDoesNotThrow(() -> customer.searchShops(), "searchShops should not throw an exception.");
    }

    @Test
    public void testMakePayment() {
        // Act & Assert
        assertDoesNotThrow(() -> customer.makePayment(), "makePayment should not throw an exception.");
    }

    @Test
    public void testAddToCart() {
        // Act & Assert
        assertDoesNotThrow(() -> customer.addToCart(), "addToCart should not throw an exception.");
    }

    @Test
    public void testRemoveFromCart() {
        // Act & Assert
        assertDoesNotThrow(() -> customer.removeFromCart(), "removeFromCart should not throw an exception.");
    }
}