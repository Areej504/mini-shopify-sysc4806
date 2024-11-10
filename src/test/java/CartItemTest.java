import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import com.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CartItemTest {

    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        cart = mock(Cart.class); // Mock Cart since we are focusing on CartItem behavior
        product = mock(Product.class); // Mock Product to simulate price

        // Ensure product always returns a valid price to avoid NullPointerException
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
    }

    @Test
    public void testConstructor_ValidValues() {
        // Arrange
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));

        // Act
        cartItem = new CartItem(cart, product, 5);

        // Assert
        assertEquals(cart, cartItem.getCart(), "Cart should be set correctly.");
        assertEquals(product, cartItem.getProduct(), "Product should be set correctly.");
        assertEquals(5, cartItem.getQuantity(), "Quantity should be set correctly.");
        assertEquals(BigDecimal.valueOf(50.0), cartItem.getTotalPrice(), "Total price should be calculated correctly.");
    }

    @Test
    public void testGetTotalPrice_PositiveQuantity() {
        // Arrange
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(20.0));
        cartItem = new CartItem(cart, product, 3);

        // Act
        BigDecimal totalPrice = cartItem.getTotalPrice();

        // Assert
        assertEquals(BigDecimal.valueOf(60.0), totalPrice, "Total price should be price * quantity.");
    }

    @Test
    public void testGetTotalPrice_ZeroQuantity() {
        // Arrange
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(20.0));
        cartItem = new CartItem(cart, product, 0);

        // Act
        BigDecimal totalPrice = cartItem.getTotalPrice();

        // Assert
        //assertEquals(BigDecimal.ZERO, totalPrice, "Total price should be zero when quantity is zero.");
        assertEquals(0, cartItem.getTotalPrice().compareTo(BigDecimal.ZERO), "Total price should be zero when quantity is zero.");
    }

    @Test
    public void testGetTotalPrice_NegativeQuantity() {
        // Arrange
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(20.0));
        cartItem = new CartItem(cart, product, -2);

        // Act
        BigDecimal totalPrice = cartItem.getTotalPrice();

        // Assert
        assertEquals(BigDecimal.valueOf(-40.0), totalPrice, "Total price should handle negative quantities correctly.");
    }

    @Test
    public void testSetQuantity_PositiveValue() {
        // Arrange
        cartItem = new CartItem(cart, product, 5);

        // Act
        cartItem.setQuantity(10);

        // Assert
        assertEquals(10, cartItem.getQuantity(), "Quantity should be updated correctly.");
    }

    @Test
    public void testSetQuantity_Zero() {
        // Arrange
        cartItem = new CartItem(cart, product, 5);

        // Act
        cartItem.setQuantity(0);

        // Assert
        assertEquals(0, cartItem.getQuantity(), "Quantity should be zero when set to zero.");
        //assertEquals(BigDecimal.ZERO, cartItem.getTotalPrice(), "Total price should be zero when quantity is zero.");
        assertEquals(0, cartItem.getTotalPrice().compareTo(BigDecimal.ZERO), "Total price should be zero when quantity is zero.");
    }

    @Test
    public void testSetQuantity_NegativeValue() {
        // Arrange
        cartItem = new CartItem(cart, product, 5);

        // Act
        cartItem.setQuantity(-3);

        // Assert
        assertEquals(-3, cartItem.getQuantity(), "Negative quantity should be handled.");
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(-3)), cartItem.getTotalPrice(), "Total price should handle negative quantity.");
    }

    @Test
    public void testSetProduct() {
        // Arrange
        Product newProduct = mock(Product.class);
        cartItem = new CartItem(cart, product, 1);

        // Act
        cartItem.setProduct(newProduct);

        // Assert
        assertEquals(newProduct, cartItem.getProduct(), "Product should be updated correctly.");
    }

    @Test
    public void testSetCart() {
        // Arrange
        Cart newCart = mock(Cart.class);
        cartItem = new CartItem(cart, product, 1);

        // Act
        cartItem.setCart(newCart);

        // Assert
        assertEquals(newCart, cartItem.getCart(), "Cart should be updated correctly.");
    }

    @Test
    public void testToString() {
        // Arrange
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(15.0));
        when(product.toString()).thenReturn("Product{name='Sample'}");
        cartItem = new CartItem(cart, product, 2);

        // Act
        String cartItemString = cartItem.toString();

        // Assert
        assertTrue(cartItemString.contains("CartItem"), "toString should include 'CartItem'.");
        assertTrue(cartItemString.contains("quantity=2"), "toString should include quantity.");
        assertTrue(cartItemString.contains("totalPrice=30.0"), "toString should include total price.");
    }
}

//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.math.BigDecimal;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class CartItemTest {
//
//    private Cart cart;
//    private Product product;
//    private CartItem cartItem;
//
//    @BeforeEach
//    public void setUp() {
//        cart = mock(Cart.class); // Mock Cart since we are focusing on CartItem behavior
//        product = mock(Product.class); // Mock Product to simulate price
//    }
//
//    @Test
//    public void testConstructor_ValidValues() {
//        // Arrange
//        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
//
//        // Act
//        cartItem = new CartItem(cart, product, 5);
//
//        // Assert
//        assertEquals(cart, cartItem.getCart(), "Cart should be set correctly.");
//        assertEquals(product, cartItem.getProduct(), "Product should be set correctly.");
//        assertEquals(5, cartItem.getQuantity(), "Quantity should be set correctly.");
//        assertEquals(BigDecimal.valueOf(50.0), cartItem.getTotalPrice(), "Total price should be calculated correctly.");
//    }
//
//    @Test
//    public void testGetTotalPrice_PositiveQuantity() {
//        // Arrange
//        when(product.getPrice()).thenReturn(BigDecimal.valueOf(20.0));
//        cartItem = new CartItem(cart, product, 3);
//
//        // Act
//        BigDecimal totalPrice = cartItem.getTotalPrice();
//
//        // Assert
//        assertEquals(BigDecimal.valueOf(60.0), totalPrice, "Total price should be price * quantity.");
//    }
//
//    @Test
//    public void testGetTotalPrice_ZeroQuantity() {
//        // Arrange
//        when(product.getPrice()).thenReturn(BigDecimal.valueOf(20.0));
//        cartItem = new CartItem(cart, product, 0);
//
//        // Act
//        BigDecimal totalPrice = cartItem.getTotalPrice();
//
//        // Assert
//        assertEquals(BigDecimal.ZERO, totalPrice, "Total price should be zero when quantity is zero.");
//    }
//
//    @Test
//    public void testGetTotalPrice_NegativeQuantity() {
//        // Arrange
//        when(product.getPrice()).thenReturn(BigDecimal.valueOf(20.0));
//        cartItem = new CartItem(cart, product, -2);
//
//        // Act
//        BigDecimal totalPrice = cartItem.getTotalPrice();
//
//        // Assert
//        assertEquals(BigDecimal.valueOf(-40.0), totalPrice, "Total price should handle negative quantities correctly.");
//    }
//
//    @Test
//    public void testSetQuantity_PositiveValue() {
//        // Arrange
//        cartItem = new CartItem(cart, product, 5);
//
//        // Act
//        cartItem.setQuantity(10);
//
//        // Assert
//        assertEquals(10, cartItem.getQuantity(), "Quantity should be updated correctly.");
//    }
//
//    @Test
//    public void testSetQuantity_Zero() {
//        // Arrange
//        cartItem = new CartItem(cart, product, 5);
//
//        // Act
//        cartItem.setQuantity(0);
//
//        // Assert
//        assertEquals(0, cartItem.getQuantity(), "Quantity should be zero when set to zero.");
//        assertEquals(BigDecimal.ZERO, cartItem.getTotalPrice(), "Total price should be zero when quantity is zero.");
//    }
//
//    @Test
//    public void testSetQuantity_NegativeValue() {
//        // Arrange
//        cartItem = new CartItem(cart, product, 5);
//
//        // Act
//        cartItem.setQuantity(-3);
//
//        // Assert
//        assertEquals(-3, cartItem.getQuantity(), "Negative quantity should be handled.");
//        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(-3)), cartItem.getTotalPrice(), "Total price should handle negative quantity.");
//    }
//
//    @Test
//    public void testSetProduct() {
//        // Arrange
//        Product newProduct = mock(Product.class);
//        cartItem = new CartItem(cart, product, 1);
//
//        // Act
//        cartItem.setProduct(newProduct);
//
//        // Assert
//        assertEquals(newProduct, cartItem.getProduct(), "Product should be updated correctly.");
//    }
//
//    @Test
//    public void testSetCart() {
//        // Arrange
//        Cart newCart = mock(Cart.class);
//        cartItem = new CartItem(cart, product, 1);
//
//        // Act
//        cartItem.setCart(newCart);
//
//        // Assert
//        assertEquals(newCart, cartItem.getCart(), "Cart should be updated correctly.");
//    }
//
//    @Test
//    public void testToString() {
//        // Arrange
//        when(product.getPrice()).thenReturn(BigDecimal.valueOf(15.0));
//        when(product.toString()).thenReturn("Product{name='Sample'}");
//        cartItem = new CartItem(cart, product, 2);
//
//        // Act
//        String cartItemString = cartItem.toString();
//
//        // Assert
//        assertTrue(cartItemString.contains("CartItem"), "toString should include 'CartItem'.");
//        assertTrue(cartItemString.contains("quantity=2"), "toString should include quantity.");
//        assertTrue(cartItemString.contains("totalPrice=30.0"), "toString should include total price.");
//    }
//}