package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ShopTest {

    private Shop shop;
    private String name;
    private String description;
    private Set<Category> categories;
    private Merchant merchant;
    private List<Product> products;
    private PromotionType promotion;

    @BeforeEach
    public void setUp() {
        name = "Tech Store";
        description = "A store selling the latest tech gadgets.";
        categories = new HashSet<>(Set.of(Category.ELECTRONICS, Category.ACCESSORIES));
        merchant = new Merchant(); // Assuming a Merchant constructor exists
        merchant.setName("John's Tech");
        shop = new Shop(name, description, categories, merchant);
        promotion = PromotionType.DISCOUNT_10_PERCENT;
    }

    @Test
    public void testConstructor() {
        assertNotNull(shop);
        assertEquals(name, shop.getName());
        assertEquals(description, shop.getDescription());
        assertEquals(categories, shop.getCategories());
        assertEquals(merchant, shop.getMerchant());
    }

    @Test
    public void testSettersAndGetters() {
        String newName = "Updated Tech Store";
        String newDescription = "Updated Description";
        PromotionType newPromotion = PromotionType.BUY_ONE_GET_ONE;
        Set<Category> newCategories = new HashSet<>(Set.of(Category.HOME, Category.BEAUTY));
        Merchant newMerchant = new Merchant();
        newMerchant.setName("Sarah's Shop");

        shop.setName(newName);
        shop.setDescription(newDescription);
        shop.setPromotion(newPromotion);
        shop.setCategories(newCategories);
        shop.setMerchant(newMerchant);

        assertEquals(newName, shop.getName());
        assertEquals(newDescription, shop.getDescription());
        assertEquals(newPromotion, shop.getPromotion());
        assertEquals(newCategories, shop.getCategories());
        assertEquals(newMerchant, shop.getMerchant());
    }

    @Test
    public void testPromotionType() {
        shop.setPromotion(PromotionType.CLEARANCE);
        assertEquals(PromotionType.CLEARANCE, shop.getPromotion());

        shop.setPromotion(PromotionType.BUY_ONE_GET_ONE);
        assertEquals(PromotionType.BUY_ONE_GET_ONE, shop.getPromotion());
    }

    @Test
    public void testCategories() {
        assertEquals(2, shop.getCategories().size());
        assertTrue(shop.getCategories().contains(Category.ELECTRONICS));
        assertTrue(shop.getCategories().contains(Category.ACCESSORIES));

        shop.setCategories(new HashSet<>(Set.of(Category.FOOD, Category.SPORTS)));
        assertTrue(shop.getCategories().contains(Category.FOOD));
        assertTrue(shop.getCategories().contains(Category.SPORTS));
    }

    @Test
    public void testAddAndRemoveProduct() {
        Product product1 = new Product("Laptop", "High-end laptop", new BigDecimal("999.99"), 10, PromotionType.DISCOUNT_10_PERCENT);
        Product product2 = new Product("Headphones", "Noise-canceling headphones", new BigDecimal("199.99"), 50, PromotionType.CLEARANCE);

        // Add products using addProduct method
        shop.addProduct(product1);
        shop.addProduct(product2);

        assertEquals(2, shop.getProducts().size());
        assertTrue(shop.getProducts().contains(product1));
        assertTrue(shop.getProducts().contains(product2));
        assertEquals(shop, product1.getShop());
        assertEquals(shop, product2.getShop());

        // Remove product using removeProduct method
        shop.removeProduct(product1);

        assertEquals(1, shop.getProducts().size());
        assertFalse(shop.getProducts().contains(product1));
        assertNull(product1.getShop());  // Verify product1 no longer references the shop

    }

    @Test
    public void testEmptyCategories() {
        shop.setCategories(new HashSet<>());
        assertTrue(shop.getCategories().isEmpty());
    }

    @Test
    public void testNullValues() {
        assertThrows(NullPointerException.class, () -> shop.setName(null));
        assertThrows(NullPointerException.class, () -> shop.setDescription(null));
    }

    @Test
    public void testAssociationWithMerchant() {
        assertEquals(merchant, shop.getMerchant());

        Merchant newMerchant = new Merchant();
        newMerchant.setName("Jane's Boutique");
        shop.setMerchant(newMerchant);
        assertEquals(newMerchant, shop.getMerchant());
    }

    @Test
    public void testToStringMethod() {
//        String expectedString = "Shop{" +
//                "shopId=null, name='Tech Store', description='A store selling the latest tech gadgets.', " +
//                "promotion=" + promotion + ", categories=" + categories +
//                ", merchant=" + merchant.getName() + ", numberOfProducts=0}";
//        assertEquals(expectedString, shop.toString());
        String shopString = shop.toString();
        assertTrue(shopString.contains("Tech Store"));
        assertTrue(shopString.contains("A store selling the latest tech gadgets."));
        assertTrue(shopString.contains("categories=" + categories));
        assertTrue(shopString.contains("merchant=" + merchant.getName()));
    }
}