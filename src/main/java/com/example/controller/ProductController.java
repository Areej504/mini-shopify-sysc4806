package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("products", productRepository.findByShop(shop)); // Fetch all products
        return "merchantShop";
    }

    @PostMapping("/merchantShop/{shopId}")
    public String addProduct(@ModelAttribute Product product, @PathVariable Long shopId,Model model) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shop Id"));
        product.setShop(shop); //link the product to the shop
        // save Product
        productRepository.save(product);
        model.addAttribute("product", product);
        return "redirect:/merchantShop/" + shopId;
    }
    @DeleteMapping("/merchantShop/{shopId}/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long shopId, @PathVariable Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return ResponseEntity.ok().build(); // Return 200 OK status
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if product doesn't exist
        }
    }
}
