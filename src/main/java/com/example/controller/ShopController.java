package com.example.controller;

import com.example.model.*;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Controller
public class ShopController {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
      private PromotionRepository promotionRepository;

    @GetMapping("/shopPage/{shopId}")
    public String getShopDetails(@PathVariable Long shopId, Model model) {
        // Retrieve the shop by ID or throw an exception if not found
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));

        // Determine the promotion details
        ShopPromotions promotion = shop.getShopPromotions();
        String selectedPromotion = promotion != null ? promotion.getPromotionType().name(): "No Active Promotion";
        LocalDate startDate = promotion != null ? promotion.getStartDate() : null;
        LocalDate endDate = promotion != null ? promotion.getEndDate() : null;

        // Add attributes to the model for the shop
        model.addAttribute("shop", shop);
        model.addAttribute("selectedPromotion", selectedPromotion);
        model.addAttribute("shopName", shop.getName());
        model.addAttribute("shopDescription", shop.getDescription());
        model.addAttribute("products", shop.getProducts());

        // Retrieve the total number of items in the cart
        long totalItemsInCart = cartRepository.count(); // Each cart entry represents one item
        model.addAttribute("totalItemsInCart", totalItemsInCart);

        return "shopPage"; // Render the shopPage template
    }

    @GetMapping("/cartView")
    public String openCartView(Model model) {
        // Retrieve necessary data for the cart view (e.g., cart items, total price)
        Iterable<Cart> cartItems = cartRepository.findAll();
        model.addAttribute("cartItems", cartItems);

        return "cartView";
    }

    @GetMapping("/paymentView")
    public String openPaymentView(Model model){
        return "paymentView";
    }


}
