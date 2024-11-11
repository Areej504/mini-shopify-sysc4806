import com.example.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    private Product product;
    private String productName;
    private String description;
    private BigDecimal price;
    private int inventory;
    private PromotionType promotionType;
    private OrderInfo orderInfo;
    private Shop shop;

    @BeforeEach
    public void setUp() {
        productName = "Test Product";
        description = "Test Description";
        price = new BigDecimal("49.99");
        inventory = 100;
        promotionType = PromotionType.DISCOUNT_10_PERCENT;
        Category category = Category.ELECTRONICS;
        orderInfo = new OrderInfo();  // Assuming OrderInfo has a default constructor
        shop = new Shop();            // Assuming Shop has a default constructor
        product = new Product(productName, description, price, inventory, promotionType);
    }

    @Test
    public void testConstructor() {
        assertNotNull(product);
        assertEquals(productName, product.getProductName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(inventory, product.getInventory());
        assertEquals(promotionType, product.getPromotionType());
    }

    @Test
    public void testSettersAndGetters() {
        String newName = "New Product Name";
        String newDescription = "New Product Description";
        BigDecimal newPrice = new BigDecimal("99.99");
        int newInventory = 50;
        PromotionType newPromotionType = PromotionType.BUY_ONE_GET_ONE;
        Category newCategory = Category.BOOKS;
        OrderInfo newOrderInfo = new OrderInfo();
        Shop newShop = new Shop();

        product.setProductName(newName);
        product.setDescription(newDescription);
        product.setPrice(newPrice);
        product.setInventory(newInventory);
        product.setPromotionType(newPromotionType);
        product.setCategory(newCategory);
        product.setOrder(newOrderInfo);
        product.setShop(newShop);

        assertEquals(newName, product.getProductName());
        assertEquals(newDescription, product.getDescription());
        assertEquals(newPrice, product.getPrice());
        assertEquals(newInventory, product.getInventory());
        assertEquals(newPromotionType, product.getPromotionType());
        assertEquals(newCategory, product.getCategory());
        assertEquals(newOrderInfo, product.getOrder());
        assertEquals(newShop, product.getShop());
    }

    @Test
    public void testPriceValidation() {
        BigDecimal validPrice = new BigDecimal("10.00");
        BigDecimal zeroPrice = BigDecimal.ZERO;
        BigDecimal negativePrice = new BigDecimal("-10.00");

        product.setPrice(validPrice);
        assertEquals(validPrice, product.getPrice());

        product.setPrice(zeroPrice);
        assertEquals(zeroPrice, product.getPrice());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setPrice(negativePrice);
        });
        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    public void testInventoryValidation() {
        product.setInventory(10);
        assertEquals(10, product.getInventory());

        product.setInventory(0);
        assertEquals(0, product.getInventory());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            product.setInventory(-5);
        });
        assertEquals("Inventory count cannot be negative", exception.getMessage());
    }

    @Test
    public void testPromotionTypeEnum() {
        product.setPromotionType(PromotionType.CLEARANCE);
        assertEquals(PromotionType.CLEARANCE, product.getPromotionType());

        product.setPromotionType(PromotionType.FREE_SHIPPING);
        assertEquals(PromotionType.FREE_SHIPPING, product.getPromotionType());
    }

    @Test
    public void testCategoryEnum() {
        product.setCategory(Category.TOYS);
        assertEquals(Category.TOYS, product.getCategory());

        product.setCategory(Category.FOOD);
        assertEquals(Category.FOOD, product.getCategory());
    }

    @Test
    public void testAssociationWithOrderInfo() {
        assertNull(product.getOrder());

        product.setOrder(orderInfo);
        assertEquals(orderInfo, product.getOrder());

        OrderInfo newOrderInfo = new OrderInfo();
        product.setOrder(newOrderInfo);
        assertEquals(newOrderInfo, product.getOrder());
    }

    @Test
    public void testAssociationWithShop() {
        assertNull(product.getShop());

        product.setShop(shop);
        assertEquals(shop, product.getShop());

        Shop newShop = new Shop();
        product.setShop(newShop);
        assertEquals(newShop, product.getShop());
    }

    @Test
    public void testToStringMethod() {
        String expectedString = "Product{" +
                "productId=" + product.getProductId() +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", inventory=" + inventory +
                ", promotionType=" + promotionType +
                '}';
        assertEquals(expectedString, product.toString());
    }
}