import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import com.example.model.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartTest {

    private Cart cart;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = mock(Customer.class);
        cart = new Cart(customer);
    }

    @Test
    public void testAddProduct_NewProduct() {
        // Arrange
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));

        // Act
        cart.addProduct(product, 1);

        // Assert
        assertEquals(1, cart.getCartItems().size());
        assertEquals(1, cart.getCartItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(10.0), cart.getTotalPrice());
    }

    @Test
    public void testAddProduct_ExistingProduct() {
        // Arrange
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
        cart.addProduct(product, 1);  // Add initial product

        // Act
        cart.addProduct(product, 2);  // Add more quantity of the same product

        // Assert
        assertEquals(1, cart.getCartItems().size());
        assertEquals(3, cart.getCartItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(30.0), cart.getTotalPrice());
    }

    @Test
    public void testRemoveProduct_DecreaseQuantity() {
        // Arrange
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
        cart.addProduct(product, 2);  // Add product with quantity of 2

        // Act
        cart.removeProduct(product);  // Should decrease quantity by 1

        // Assert
        assertEquals(1, cart.getCartItems().size());
        assertEquals(1, cart.getCartItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(10.0), cart.getTotalPrice());
    }

    @Test
    public void testRemoveProduct_RemoveItemWhenQuantityIsOne() {
        // Arrange
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
        cart.addProduct(product, 1);  // Add product with quantity of 1

        // Act
        cart.removeProduct(product);  // Should remove product from cart

        // Assert
        assertEquals(0, cart.getCartItems().size());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }

    @Test
    public void testGetTotalPrice() {
        // Arrange
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        when(product1.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
        when(product2.getPrice()).thenReturn(BigDecimal.valueOf(15.0));

        cart.addProduct(product1, 1);
        cart.addProduct(product2, 2);  // Two quantities of the second product

        // Act
        BigDecimal totalPrice = cart.getTotalPrice();

        // Assert
        assertEquals(BigDecimal.valueOf(40.0), totalPrice);
    }

    @Test
    public void testToString() {
        // Arrange
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));
        cart.addProduct(product, 1);

        // Act
        String cartString = cart.toString();

        // Assert
        assertTrue(cartString.contains("Cart"));
        assertTrue(cartString.contains("totalPrice=10.0"));
    }

    @Test
    public void testFindCartItem_ProductExists() {
        // Arrange
        Product product = mock(Product.class);
        cart.addProduct(product, 1);

        // Act
        CartItem foundItem = cart.getCartItems().stream().filter(item -> item.getProduct().equals(product)).findFirst().orElse(null);

        // Assert
        assertNotNull(foundItem);
    }

    @Test
    public void testFindCartItem_ProductDoesNotExist() {
        // Arrange
        Product product = mock(Product.class);

        // Act
        CartItem foundItem = cart.getCartItems().stream().filter(item -> item.getProduct().equals(product)).findFirst().orElse(null);

        // Assert
        assertNull(foundItem);
    }
    @Test
    public void testAddProduct_WithZeroQuantity() {
        Product product = mock(Product.class);
        cart.addProduct(product, 0);

        assertEquals(0, cart.getCartItems().size(), "Zero quantity should not add product.");
    }

    @Test
    public void testAddProduct_WithNegativeQuantity() {
        Product product = mock(Product.class);
        cart.addProduct(product, -1);

        assertEquals(0, cart.getCartItems().size(), "Negative quantity should not add product.");
    }

    @Test
    public void testRemoveProduct_NotInCart() {
        Product product = mock(Product.class);
        cart.removeProduct(product);

        assertEquals(0, cart.getCartItems().size(), "Removing non-existent product should not change cart.");
    }

    @Test
    public void testAddProduct_WithLargeQuantity() {
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(10.0));

        cart.addProduct(product, Integer.MAX_VALUE);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(Integer.MAX_VALUE, cart.getCartItems().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(10.0).multiply(BigDecimal.valueOf(Integer.MAX_VALUE)), cart.getTotalPrice());
    }

    @Test
    public void testGetTotalPrice_EmptyCart() {
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice(), "Empty cart should have zero total price.");
    }

    @Test
    public void testAddProduct_WithNegativePrice() {
        Product product = mock(Product.class);
        when(product.getPrice()).thenReturn(BigDecimal.valueOf(-10.0));

        cart.addProduct(product, 1);

        assertEquals(1, cart.getCartItems().size(), "Negative price product should be added.");
        assertEquals(BigDecimal.valueOf(-10.0), cart.getTotalPrice(), "Total price should reflect negative price.");
    }
}