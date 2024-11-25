package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Entity
public class ShopPromotions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    //    @OneToOne(mappedBy = "shopPromotions")
//    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Enumerated(EnumType.STRING) // Store as string in the database
    private PromotionType promotionType;

    private LocalDate startDate;
    private LocalDate endDate;

    // Static method to return the list of promotion types
    public static List<PromotionType> getAvailablePromotions() {
        return Arrays.asList(
                PromotionType.FREE_SHIPPING,
                PromotionType.SEASONAL_HOLIDAY,
                PromotionType.BUY_ONE_GET_ONE,
                PromotionType.CLEARANCE,
                PromotionType.NONE
        );
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(PromotionType promotionType) {
        this.promotionType = promotionType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
