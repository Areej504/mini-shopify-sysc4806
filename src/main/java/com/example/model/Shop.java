package com.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Shop {
    @Id
    @GeneratedValue
    private Long shopId;
    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private PromotionType promotion;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    private Set<Category> categories;

    @ManyToOne
    @JoinColumn(name = "merchant_id")  // Specifies the foreign key column
    private Merchant merchant;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public Shop() {}

    public Shop(String name, String description, Set<Category> categories, Merchant merchant) {
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.merchant = merchant;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new NullPointerException("Description cannot be null");
        }
        this.description = description;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public PromotionType getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionType promotion) {
        this.promotion = promotion;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(){
    }

    public void removeProduct(){
    }

    public void addCategory(){}

    public void removeCategory(){}

    public void setPromotion(){}

    @Override
    public String toString() {
        return "Shop{" +
                "shopId=" + shopId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", promotion=" + promotion +
                ", categories=" + categories +
                ", merchant=" + merchant.getName() +
                ", numberOfProducts=" + (products != null ? products.size() : 0) +
                '}';
    }

}
