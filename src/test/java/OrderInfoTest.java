import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderInfoTest {

    private OrderInfo order;
    private Customer mockCustomer;
    private Product mockProduct1;
    private Product mockProduct2;
    private Payment mockPayment;

    @BeforeEach
    public void setUp() {
        mockCustomer = mock(Customer.class);
        mockProduct1 = mock(Product.class);
        mockProduct2 = mock(Product.class);
        mockPayment = mock(Payment.class);

        order = new OrderInfo(new Date(), BigDecimal.ZERO, mockCustomer, Arrays.asList(mockProduct1, mockProduct2), OrderStatus.PROCESSING);
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
        assertNull(order.getProducts(), "Products list should be null by default.");
        assertNull(order.getStatus(), "Order status should be null by default.");
        assertNull(order.getPayment(), "Payment should be null by default.");
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Date orderDate = new Date();
        BigDecimal totalAmount = new BigDecimal("100.00");
        List<Product> products = Arrays.asList(mockProduct1, mockProduct2);
        Payment payment = mock(Payment.class);

        order.setOrderId(1L);
        order.setOrderDate(orderDate);
        order.setTotalAmount(totalAmount);
        order.setCustomer(mockCustomer);
        order.setProducts(products);
        order.setStatus(OrderStatus.SHIPPED);
        order.setPayment(payment);

        // Act & Assert
        assertEquals(1L, order.getOrderId(), "Order ID should be set correctly.");
        assertEquals(orderDate, order.getOrderDate(), "Order date should be set correctly.");
        assertEquals(totalAmount, order.getTotalAmount(), "Total amount should be set correctly.");
        assertEquals(mockCustomer, order.getCustomer(), "Customer should be set correctly.");
        assertEquals(products, order.getProducts(), "Products list should be set correctly.");
        assertEquals(OrderStatus.SHIPPED, order.getStatus(), "Order status should be set correctly.");
        assertEquals(payment, order.getPayment(), "Payment should be set correctly.");
    }

    @Test
    public void testGetProducts_WithNoProducts() {
        // Arrange
        order.setProducts(null);

        // Act
        List<Product> products = order.getProducts();

        // Assert
        assertNull(products, "Products list should be null when no products are added.");
    }

    @Test
    public void testGetProducts_WithProducts() {
        // Arrange
        List<Product> products = Arrays.asList(mockProduct1, mockProduct2);
        order.setProducts(products);

        // Act & Assert
        assertEquals(2, order.getProducts().size(), "Products list should contain two elements.");
    }

    @Test
    public void testCalculateTotalPrice() {
        // Arrange
        when(mockProduct1.getPrice()).thenReturn(new BigDecimal("50.00"));
        when(mockProduct2.getPrice()).thenReturn(new BigDecimal("25.00"));

        // Act & Assert
        assertDoesNotThrow(() -> {
            order.calculateTotalPrice(); // Placeholder for functionality
        }, "calculateTotalPrice method should not throw an exception.");
    }

    @Test
    public void testUpdateStatus() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            order.updateStatus(); // Placeholder for functionality
        }, "updateStatus method should not throw an exception.");
    }

    @Test
    public void testSetProductsOrdered() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            order.setProductsOrdered(); // Placeholder for functionality
        }, "setProductsOrdered method should not throw an exception.");
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