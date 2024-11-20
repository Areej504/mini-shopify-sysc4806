package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private CartItemRepository cartItemRepository;


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

        Product product = productOptional.get();
        // Check if the product is sold out
        if (product.getInventory() <= 0) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Product sold out!");
            return ResponseEntity.status(400).body(errorResponse);
        }

        // Assuming a single cart for simplicity (use current user's cart in a real application)
        Cart cart = cartRepository.findById(1L).orElse(new Cart()); // Replace 1L with dynamic cart retrieval
        cart.addProduct(productOptional.get(), quantity);

        // Save the updated cart
        cartRepository.save(cart);

        // Calculate the total items in the cart
        int totalItemsInCart = cart.getTotalItems();
        System.out.println(totalItemsInCart);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product added to cart successfully!");
        response.put("totalItemsInCart", String.valueOf(totalItemsInCart));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cart/count")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getCartItemCount() {
        // Assuming a single cart for simplicity (use the current user's cart in real applications)
        Cart cart = cartRepository.findById(1L).orElse(new Cart()); // Replace 1L with dynamic cart retrieval
        int totalItemsInCart = cart.getTotalItems();

        return ResponseEntity.ok(Map.of("totalItemsInCart", totalItemsInCart));
    }

    @PostMapping("/cart/updateQuantity")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestBody Map<String, Object> payload) {
        try {
            // Ensure cartItemId is a number
            Long cartItemId = Long.parseLong(payload.get("cartItemId").toString());
            String action = payload.get("action").toString();

            // Find CartItem
            Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
            if (optionalCartItem.isEmpty()) {
                return ResponseEntity.status(404).body("CartItem not found");
            }

            CartItem cartItem = optionalCartItem.get();

            // Check inventory limit
            Product product = cartItem.getProduct();
            if (product == null) {
                return ResponseEntity.status(400).body("Product associated with the CartItem is missing");
            }

            int currentQuantity = cartItem.getQuantity();
            int inventoryCount = product.getInventory();

            // Update quantity based on action
            if ("increase".equals(action)) {
                if (currentQuantity + 1 > inventoryCount) {
                    return ResponseEntity.status(400).body("Cannot exceed available inventory");
                }
                cartItem.setQuantity(currentQuantity + 1);
            } else if ("decrease".equals(action)) {
                if (currentQuantity > 1) {
                    cartItem.setQuantity(currentQuantity - 1);
                } else {
                    return ResponseEntity.status(400).body("Quantity cannot be less than 1");
                }
            } else {
                return ResponseEntity.status(400).body("Invalid action");
            }

            // Save updated cart item
            cartItemRepository.save(cartItem);
            return ResponseEntity.ok("Quantity updated successfully");

        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid cartItemId format");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while updating the quantity");
        }
    }

    @PostMapping("/cart/removeItem")
    @ResponseBody
    public ResponseEntity<String> removeFromCart(@RequestBody Map<String, Object> payload) {
        try {
            // Parse cartItemId
            Long cartItemId = Long.parseLong(payload.get("cartItemId").toString());

            // Find the CartItem
            Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
            if (optionalCartItem.isEmpty()) {
                return ResponseEntity.status(404).body("CartItem not found");
            }

            // Remove the CartItem
            cartItemRepository.delete(optionalCartItem.get());

            return ResponseEntity.ok("Item removed successfully");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body("Invalid cartItemId format");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while removing the item");
        }
    }



    @GetMapping("/paymentView")
    public String openPaymentView(Model model){
        return "paymentView";
    }

}
