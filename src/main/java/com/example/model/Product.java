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
    private Category category;
    private int inventory;

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
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

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
        this.price = price;
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
