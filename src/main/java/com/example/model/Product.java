package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long productId;

    private String productName;
    private String description;
    private BigDecimal price;
    private BigDecimal discountedPrice = null;
    private Category category;
    private int inventory;
    private String imageURL;

    @Enumerated(EnumType.STRING)
    private PromotionType promotionType;

    @ManyToOne
    private OrderInfo orderInfo;

    @ManyToOne
    private Shop shop;

    // Constructors
    public Product() {}

    public Product(String productName, String description, BigDecimal price, int inventory, PromotionType promotionType) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.promotionType = promotionType;
        this.imageURL = getImageURL();
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getImageURL(){return imageURL;}

    public void setImageURL(String imageURL){this.imageURL = imageURL;}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
//        this.price = price;
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }
    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
//        this.inventory = inventory;
        if (inventory < 0) {
            throw new IllegalArgumentException("Inventory count cannot be negative");
        }
        this.inventory = inventory;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public OrderInfo getOrder(){return orderInfo;}

    public void setOrder(OrderInfo orderInfo){this.orderInfo = orderInfo;}

    public Shop getShop(){return shop;}

    public void setShop(Shop shop){this.shop=shop;}

    //Methods
    public BigDecimal calculateDiscountedPrice() {
        if (promotionType == null || promotionType == PromotionType.NONE) {
            discountedPrice = null;
            return null; // No discount
        }

        switch (promotionType) {
            case DISCOUNT_10_PERCENT:
                discountedPrice = price.multiply(BigDecimal.valueOf(0.90));
                return discountedPrice;
            case DISCOUNT_20_PERCENT:
                discountedPrice = price.multiply(BigDecimal.valueOf(0.80));
                return discountedPrice;
            case DISCOUNT_5_DOLLARS:
                discountedPrice = price.subtract(BigDecimal.valueOf(5)).max(BigDecimal.ZERO);
                return discountedPrice; // Ensures price doesn't go below zero
            case CLEARANCE:
                discountedPrice = price.multiply(BigDecimal.valueOf(0.50));
                return price.multiply(BigDecimal.valueOf(0.50)); // 50% off for clearance
            default:
                discountedPrice = null;
                return null;
        }
    }

    public void setInventoryCount(){};

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", inventory=" + inventory +
                ", promotionType=" + promotionType +
                '}';
    }
}
