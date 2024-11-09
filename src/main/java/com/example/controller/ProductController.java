package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShopRepository shopRepository;

    @GetMapping("/merchantShop/{shopId}")
    public String viewMerchantShop(@PathVariable Long shopId, Model model) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shop Id"));

        model.addAttribute("shop", shop); // Add shop details to the model
        model.addAttribute("product", new Product()); // Add Product model to Thymeleaf
        model.addAttribute("products", productRepository.findAll()); // Fetch all products
        return "merchantShop";
    }

    @PostMapping("/merchantShop/{shopId}")
    public String addProduct(@ModelAttribute Product product, @PathVariable Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shop Id"));
        product.setShop(shop); //link the product to the shop
        // save Product
        productRepository.save(product);
        return "redirect:/merchantShop/" + shopId;
    }

}
