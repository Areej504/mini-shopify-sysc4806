package com.example.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

public class ShopPromotionDates {
    private PromotionType promotionType; // Use enum here
    private LocalDate startDate;
    private LocalDate endDate;

    // Getters and setters
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

//@Service
//public class PromotionService {
//    @Autowired
//    private PromotionRepository promotionRepository;
//
//    public void setStorePromotion(PromotionType promotionType, LocalDate startDate, LocalDate endDate) {
//        ShopPromotions promotion = new ShopPromotions();
//        promotion.setPromotionType(promotionType);
//        promotion.setStartDate(startDate);
//        promotion.setEndDate(endDate);
//
//        // Save the promotion to the database
//        promotionRepository.save(promotion);
//    }

