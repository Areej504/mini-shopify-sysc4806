package com.example.controller;

import com.example.cache.CartService;
import com.example.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ShopController {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService; // Injected CartService

    private String getSessionId(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionId);
        }
        return sessionId;
    }

    @GetMapping("/shopPage/{shopId}")
    public String getShopDetails(@PathVariable Long shopId, HttpSession session, Model model) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isPresent()) {
            Shop shopDetails = shop.get();
            model.addAttribute("shopName", shopDetails.getName());
            model.addAttribute("shopDescription", shopDetails.getDescription());
            model.addAttribute("products", shopDetails.getProducts());
            model.addAttribute("shopPromotion", shopDetails.getPromotion());
            model.addAttribute("storeId", shopId); // Pass storeId to the view
        }

        // Get session-specific cart item count for the store
        String sessionId = getSessionId(session);
        List<Map<String, Object>> cartItems = cartService.getCart(sessionId, shopId);

        int totalItemsInCart = cartItems != null
                ? cartItems.stream().mapToInt(item -> Integer.parseInt(item.get("quantity").toString())).sum()
                : 0;

        model.addAttribute("totalItemsInCart", totalItemsInCart);

        return "shopPage";
    }

    @GetMapping("/cartView/{storeId}")
    public String openCartView(@PathVariable Long storeId, HttpSession session, Model model) {
        String sessionId = getSessionId(session);

        // Retrieve the cart for the given sessionId and storeId
        System.out.println("Session CV: " + sessionId);
        List<Map<String, Object>> cartItems = cartService.getCart(sessionId, storeId);
        System.out.println("cart Items: " + cartItems);

        Cart cart = new Cart();
        List<CartItem> cartItemList = new ArrayList<>();

        // If the cart is not empty, populate it with products
        if (cartItems != null && !cartItems.isEmpty()) {
            for (Map<String, Object> item : cartItems) {
                try {
                    Long productId = Long.valueOf(item.get("productId").toString());
                    int quantity = Integer.parseInt(item.get("quantity").toString());
                    Long cartItemId = Long.valueOf(item.get("cartItemId").toString());

                    Optional<Product> productOptional = productRepository.findById(productId);
                    if (productOptional.isPresent()) {
                        Product product = productOptional.get();
                        cartItemList.add(new CartItem(cart, product, quantity, cartItemId));
                    }
                    System.out.println("Cart Item ID: " + cartItemId);
                } catch (Exception e) {
                    // Skip malformed items
                    continue;
                }
            }
        }
        System.out.println("Cart items 2: " + cartItemList);
        cart.setCartItems(cartItemList);

        // Add cart and storeId to the model for rendering
        model.addAttribute("cart", cart);
        model.addAttribute("storeId", storeId);

        return "cartView";
    }


    @PostMapping("/addToCart")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addToCart(@RequestBody Map<String, Object> requestBody, HttpSession session) {
        // Validate and extract productId
        Long productId = null;
        Long storeId = null;

        try {
            productId = Long.parseLong(requestBody.get("productId").toString());
            storeId = Long.parseLong(requestBody.get("storeId").toString());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", "Invalid or missing Product ID or Store ID"));
        }

        // Validate product existence
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Product not found"));
        }

        // Validate inventory
        Product product = productOptional.get();
        if (product.getInventory() <= 0) {
            return ResponseEntity.status(400).body(Map.of("message", "Product is out of stock"));
        }

        // Add product to cart
        String sessionId = getSessionId(session);
        cartService.addToCart(sessionId, storeId, productId, 1);

        // Get updated cart item count
        List<Map<String, Object>> cartItems = cartService.getCart(sessionId, storeId);
        System.out.println("Items: " + cartItems);
        int totalItemsInCart = cartItems.stream()
                .mapToInt(item -> Integer.parseInt(item.get("quantity").toString()))
                .sum();

        return ResponseEntity.ok(Map.of("message", "Product added to cart successfully!", "totalItemsInCart", String.valueOf(totalItemsInCart)));
    }

    @PostMapping("/cart/updateQuantity")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(
            @RequestBody Map<String, Object> requestBody, HttpSession session) {
        System.out.println("Received payload: " + requestBody);
        Long cartItemId = ((Number) requestBody.get("cartItemId")).longValue();
        Long storeId = ((Number) requestBody.get("storeId")).longValue();
        int inventory = ((Number) requestBody.get("inventory")).intValue();
        String action = requestBody.get("action").toString();


        System.out.println("cartItemId: " + cartItemId);
        System.out.println("storeId: " + storeId);
        System.out.println("action: " + action);
        System.out.println("inventory: "+ inventory);

        String sessionId = getSessionId(session);

        boolean success = cartService.updateQuantity(sessionId, storeId, cartItemId, action, inventory);

        if (success) {
            return ResponseEntity.ok("Quantity updated successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to update quantity");
        }
    }

    @PostMapping("/removeFromCart")
    @ResponseBody
    public ResponseEntity<String> removeFromCart(@RequestBody Map<String, Object> requestBody, HttpSession session) {
        Long productId = ((Number) requestBody.get("productId")).longValue();
        Long storeId = ((Number) requestBody.get("storeId")).longValue(); // Extract storeId

        String sessionId = getSessionId(session);
        cartService.removeFromCart(sessionId, storeId, productId);

        return ResponseEntity.ok("Item removed successfully");
    }

    @PostMapping("/clearCart")
    @ResponseBody
    public ResponseEntity<String> clearCart(@RequestBody Map<String, Object> requestBody, HttpSession session) {
        Long storeId = ((Number) requestBody.get("storeId")).longValue(); // Extract storeId
        String sessionId = getSessionId(session);
        cartService.clearCart(sessionId, storeId);

        return ResponseEntity.ok("Cart cleared successfully");
    }
}
