package com.example.controller;
import com.example.model.*;
import com.example.model.PromotionType;
import com.example.model.ShopPromotions;
import com.example.model.promoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.time.LocalDate;

public class promoController {

    @Autowired
    private promoService promotionService;

    @PostMapping("/setPromotion/{shopId}")
    public String setShopPromotion(
            @PathVariable Long shopId,
            @RequestParam("promotion") PromotionType promotionType) {

        // Create a new ShopPromotions object
        ShopPromotions promotion = new ShopPromotions();
        promotion.setPromotionType(promotionType);
        promotion.setStartDate(LocalDate.now());
        promotion.setEndDate(LocalDate.now().plusMonths(1));

        // Save to in-memory storage
        promotionService.savePromotion(shopId, promotion);

        return "redirect:/merchantShop/" + shopId;
    }

    @GetMapping("/shopPage/{shopId}")
    public String getShopDetails(@PathVariable Long shopId, Model model) {
        ShopPromotions promotion = promotionService.getPromotion(shopId);

        // Add promotion details to the model
        if (promotion != null) {
            model.addAttribute("selectedPromotion", promotion.getPromotionType().name());
            model.addAttribute("promotionStartDate", promotion.getStartDate());
            model.addAttribute("promotionEndDate", promotion.getEndDate());
        } else {
            model.addAttribute("selectedPromotion", "No Active Promotion");
        }

        return "shopPage"; // Render the shop page
    }

}
