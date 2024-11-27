package com.example.controller;

import com.example.cache.CartService;
import com.example.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
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
            model.addAttribute("shopPromotion", shopDetails.getPromotion());
            model.addAttribute("NONE", PromotionType.NONE);
        }

        // Retrieve the cart contents and calculate the number of items
        long totalItemsInCart = cartRepository.count(); //each cart entry represents one item
        model.addAttribute("totalItemsInCart", totalItemsInCart);

        return "shopPage"; // Render the shopPage template
    }

//    @GetMapping("/cartView")
//    public String openCartView(Model model) {
//        Optional<Cart> cartOptional = cartRepository.findById(1L); // Replace 1L with user-specific logic
//
//        if (cartOptional.isPresent()) {
//            Cart cart = cartOptional.get();
//
//            PromotionType promotion = cart.getCartItems().get(0).getProduct().getShop().getPromotion();
//            model.addAttribute("cart", cart);
//            model.addAttribute("storePromotion", promotion);
//            model.addAttribute("BUY_ONE_GET_ONE", PromotionType.BUY_ONE_GET_ONE);
//        } else {
//            model.addAttribute("cart", new Cart()); // Empty cart object
//        }
//
//        return "cartView";
//    }
    private String getSessionId(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionId);
        }
        return sessionId;
    }

    @GetMapping("/cartView")
    public String openCartView(HttpSession session, Model model) {
        String sessionId = getSessionId(session); // Get or create a session ID
        CartService cartService = new CartService(); // Redis-based service

        // Retrieve the cart for the current session
        List<Map<String, Object>> cartItems = cartService.getCart(sessionId);

        Cart cart = new Cart(); // Initialize an empty cart
        List<PromotionType> promotions = new ArrayList<>(); // List to hold promotions

        if (!cartItems.isEmpty()) {
            // Map Redis cart items to CartItem objects
            cart.setCartItems(cartItems.stream().map(item -> {
                CartItem cartItem = new CartItem();

                Long productId;
                try {
                    Object productIdObject = item.get("productId");
                    if (productIdObject instanceof Double) {
                        // If it's a Double, convert to Long
                        productId = ((Double) productIdObject).longValue();
                    } else if (productIdObject instanceof Integer) {
                        // If it's an Integer, convert to Long
                        productId = ((Integer) productIdObject).longValue();
                    } else {
                        // Otherwise, parse as String
                        productId = Long.parseLong(productIdObject.toString());
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid productId format in cart: " + item.get("productId"), e);
                }

                int quantity;
                try {
                    Object quantityObject = item.get("quantity");
                    if (quantityObject instanceof Double) {
                        quantity = ((Double) quantityObject).intValue();
                    } else if (quantityObject instanceof Integer) {
                        quantity = (Integer) quantityObject;
                    } else {
                        quantity = Integer.parseInt(quantityObject.toString());
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid quantity format in cart: " + item.get("quantity"), e);
                }

                // Fetch the product from the database
                Optional<Product> productOptional = productRepository.findById(productId);
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    cartItem.setProduct(product);

                    // Add shop promotion to the list
                    if (product.getShop() != null && product.getShop().getPromotion() != null) {
                        promotions.add(product.getShop().getPromotion());
                    }
                }
                cartItem.setQuantity(quantity); // Set the quantity
                return cartItem;
            }).toList());
        }

        // Add attributes to the model
        model.addAttribute("storePromotions", promotions);
        model.addAttribute("BUY_ONE_GET_ONE", PromotionType.BUY_ONE_GET_ONE);
        model.addAttribute("cart", cart);
        return "cartView";
    }



//    @PostMapping("/addToCart")
//    @ResponseBody
//    public ResponseEntity<Map<String, String>> addToCart(@RequestBody Map<String, Object> requestBody) {
//        Long productId = ((Number) requestBody.get("productId")).longValue();
//        int quantity = ((Number) requestBody.getOrDefault("quantity", 1)).intValue(); // Default quantity is 1
//
//        // Find product by ID
//        Optional<Product> productOptional = productRepository.findById(productId);
//
//        if (productOptional.isEmpty()) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Product not found!");
//            return ResponseEntity.status(404).body(errorResponse);
//        }
//
//        Product product = productOptional.get();
//        // Check if the product is sold out
//        if (product.getInventory() <= 0) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Product sold out!");
//            return ResponseEntity.status(400).body(errorResponse);
//        }
//
//        // Assuming a single cart for simplicity (use current user's cart in a real application)
//        Cart cart = cartRepository.findById(1L).orElse(new Cart()); // Replace 1L with dynamic cart retrieval
//        cart.addProduct(productOptional.get(), quantity);
//
//        // Save the updated cart
//        cartRepository.save(cart);
//
//        // Calculate the total items in the cart
//        int totalItemsInCart = cart.getTotalItems();
//        System.out.println(totalItemsInCart);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "Product added to cart successfully!");
//        response.put("totalItemsInCart", String.valueOf(totalItemsInCart));
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/addToCart")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addToCart(@RequestBody Map<String, Object> requestBody, HttpSession session) {
        Long productId = ((Number) requestBody.get("productId")).longValue();

        int quantity;
        try {
            Object quantityObject = requestBody.getOrDefault("quantity", 1);
            if (quantityObject instanceof Double) {
                quantity = ((Double) quantityObject).intValue(); // Convert Double to Integer
            } else if (quantityObject instanceof Integer) {
                quantity = (Integer) quantityObject; // Use Integer directly
            } else {
                quantity = Integer.parseInt(quantityObject.toString()); // Fallback for strings
            }
        } catch (NumberFormatException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid quantity format!");
            return ResponseEntity.status(400).body(errorResponse);
        }

        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Product not found!");
            return ResponseEntity.status(404).body(errorResponse);
        }

        Product product = productOptional.get();
        if (product.getInventory() <= 0) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Product sold out!");
            return ResponseEntity.status(400).body(errorResponse);
        }

        String sessionId = getSessionId(session);
        CartService cartService = new CartService();
        cartService.addToCart(sessionId, productId, quantity);

        List<Map<String, Object>> cartItems = cartService.getCart(sessionId);
        int totalItemsInCart = cartItems.stream()
                .mapToInt(item -> {
                    Object quantityObject = item.get("quantity");
                    if (quantityObject instanceof Double) {
                        return ((Double) quantityObject).intValue(); // Convert Double to int
                    } else if (quantityObject instanceof Integer) {
                        return (Integer) quantityObject; // Use Integer directly
                    } else {
                        try {
                            return Integer.parseInt(quantityObject.toString()); // Fallback for unexpected cases
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("Invalid quantity format in cart: " + quantityObject, e);
                        }
                    }
                })
                .sum();

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

}
