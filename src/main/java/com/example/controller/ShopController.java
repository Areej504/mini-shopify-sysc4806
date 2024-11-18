package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@Controller
public class ShopController {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/shopPage/{shopId}")
    public String getShopDetails(@PathVariable Long shopId, Model model) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isPresent()) {
            Shop shopDetails = shop.get();
            model.addAttribute("shopName", shopDetails.getName());
            model.addAttribute("shopDescription", shopDetails.getDescription());
            model.addAttribute("products", shopDetails.getProducts());
        }

        // Retrieve the cart contents and calculate the number of items
        long totalItemsInCart = cartRepository.count(); //each cart entry represents one item
        model.addAttribute("totalItemsInCart", totalItemsInCart);

        return "shopPage"; // Render the shopPage template
    }

    @GetMapping("/cartView")
    public String openCartView(Model model) {
        Optional<Cart> cartOptional = cartRepository.findById(1L); // Replace 1L with user-specific logic

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            model.addAttribute("cartItems", cart.getCartItems());
            model.addAttribute("totalPrice", cart.getTotalPrice());
            model.addAttribute("totalItemsInCart", cart.getCartItems().size());
        } else {
            model.addAttribute("cartItems", new ArrayList<>());
            model.addAttribute("totalPrice", 0);
            model.addAttribute("totalItemsInCart", 0);
        }

        return "cartView";
    }

    @PostMapping("/addToCart")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addToCart(@RequestBody Map<String, Object> requestBody) {
        Long productId = ((Number) requestBody.get("productId")).longValue();
        int quantity = ((Number) requestBody.getOrDefault("quantity", 1)).intValue(); // Default quantity is 1

        // Find product by ID
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Product not found!");
            return ResponseEntity.status(404).body(errorResponse);
        }

        // Assuming a single cart for simplicity (use current user's cart in a real application)
        Cart cart = cartRepository.findById(1L).orElse(new Cart()); // Replace 1L with dynamic cart retrieval
        cart.addProduct(productOptional.get(), quantity);

        // Save the updated cart
        cartRepository.save(cart);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product added to cart successfully!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paymentView")
    public String openPaymentView(Model model){
        return "paymentView";
    }

}
