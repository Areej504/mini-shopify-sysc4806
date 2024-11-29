package com.example.controller;

import com.example.model.*;
import com.example.cache.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.*;

@Controller
public class OrderController {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    private String getSessionId(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionId);
        }
        return sessionId;
    }

    @GetMapping("/checkout")
    public String openCheckoutView(@RequestParam Long storeId, Model model) {
        // Fetch store details
        Optional<Shop> shopOptional = shopRepository.findById(storeId);
        if (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();
            model.addAttribute("shop", shop); // Add store name to the model
        } else {
            model.addAttribute("shopName", "Unknown Store"); // Fallback if store not found
        }
        return "shopCheckout"; // View name
    }

    @GetMapping("/guestCheckout")
    public String guestCheckout(@RequestParam(required = false) Long storeId, HttpSession session, Model model) {
        // Fetch store name if storeId is provided
        System.out.println("Received storeId: " + storeId); // Debug log
        if (storeId != null) {
            shopRepository.findById(storeId).ifPresent(shop -> model.addAttribute("shopName", shop.getName()));
        } else {
            model.addAttribute("shopName", "Unknown Store");
        }

        // Get session-specific cart item count for the store
        String sessionId = getSessionId(session);
        List<Map<String, Object>> cartItems = cartService.getCart(sessionId, storeId);
        System.out.println("Store :" + storeId);
        System.out.println("Session: " + sessionId);
        System.out.println("ITEM: " + cartItems);

        Shop shop = shopRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + storeId));
        Cart cart = new Cart(shop);

        List<CartItem> cartItemList = new ArrayList<>();

        // Process cart items to calculate totals
        List<Map<String, Object>> processedCartItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (Map<String, Object> item : cartItems) {
            Long productId = (Long) item.get("productId"); // Retrieve the productId from the cart item

            if (productId == null) {
                System.out.println("Product ID is null for cart item: " + item);
                continue; // Skip this item if productId is null
            }

            // Fetch the Product object using productId
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + productId));

            int quantity = (int) item.get("quantity");

            // Calculate total for the item
            double price = product.getDiscountedPrice() != null
                    ? product.getDiscountedPrice().doubleValue()
                    : product.getPrice().doubleValue();
            double itemTotal = price * quantity;
//            processedCartItems.add(Map.of(
//                    "name", product.getProductName(),
//                    "quantity", quantity,
//                    "price", price,
//                    "total", itemTotal
//            ));
//            totalAmount += itemTotal;

            cartItemList.add(new CartItem(cart, product, quantity));

        }
        cart.setCartItems(cartItemList);
        // Add cart items and total amount to the model
        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", processedCartItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("promotion", PromotionType.BUY_ONE_GET_ONE);
//        // Add other dynamic fields
//        model.addAttribute("paymentMethods", List.of("Credit Card", "Debit Card", "PayPal"));
//
//        // Add any default values for payment fields, if needed
//        model.addAttribute("defaultPaymentMethod", "Credit Card");
//        model.addAttribute("totalAmount", 100.00); // Example value, fetch actual value dynamically

        // Add any other data needed for the payment page
        return "paymentView"; // Replace with your Thymeleaf payment page template name
    }



}
