package com.example.controller;

import com.example.model.*;
import com.example.cache.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class OrderController {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private CartRepository cartRepository;

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

            cartItemList.add(new CartItem(cart, product, quantity));

        }
        cart.setCartItems(cartItemList);
        // Add cart items and total amount to the model
        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", processedCartItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("promotion", PromotionType.BUY_ONE_GET_ONE);
        model.addAttribute("shipping", new Shipping());

        return "paymentView"; // Replace with your Thymeleaf payment page template name
    }

    //added by warda
    @GetMapping("/merchantShop/{shopId}/orders")
    public ResponseEntity<List<OrderInfo>> getOrdersByShop(@PathVariable Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Shop not found with ID: " + shopId));
        List<OrderInfo> orders = orderInfoRepository.findByShop(shop);
        System.out.println("Orders: " + orders);
        orders.sort(Comparator.comparing(order ->
                order.getStatus() == OrderStatus.REFUNDED || order.getStatus() == OrderStatus.CANCELED));
        return ResponseEntity.ok(orders);
    }


    @PostMapping("/processPayment")
    public String processPayment(HttpSession session, Long storeId, @ModelAttribute Shipping shipping, Model model) {
        // Get session-specific cart item count for the store
        String sessionId = getSessionId(session);
        List<Map<String, Object>> cartItems = cartService.getCart(sessionId, storeId);

        // Step 3: Create the OrderInfo object
        OrderInfo order = new OrderInfo();
        order.setOrderDate(new Date()); // Set current date as the order date

        shippingRepository.save(shipping);
        order.setShipping(shipping);

        System.out.println("Print reached here!");

        // Step 5: Associate cart items with the order
        Shop shop = shopRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + storeId));
        Cart cart = new Cart(shop);

        List<CartItem> cartItemList = new ArrayList<>();

        for (Map<String, Object> item : cartItems) {
            System.out.println("Point 2!");
            Long productId = (Long) item.get("productId");

            if (productId == null) {
                System.out.println("Product ID is null for cart item: " + item);
                continue;
            }

            // Fetch the Product object using productId
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + productId));

            int quantity = (int) item.get("quantity");

            cartItemList.add(new CartItem(cart, product, quantity));
            System.out.println(cartItemList);
        }
        cart.setCartItems(cartItemList);

        // Save the Cart before associating it with OrderInfo
        cart = cartRepository.save(cart);

        order.setCart(cart);
        order.setStatus(OrderStatus.PROCESSING);
        order.setShop(shop);
        order.setTotalAmount(cart.getTotalPrice());

        // Step 6: Save the order
        order.setStatus(OrderStatus.PROCESSING);
        orderInfoRepository.save(order);
        System.out.println("Current order: " + order);
        System.out.println("Saved Orders: " + orderInfoRepository.findByShop(shop));
        cartService.clearCart(sessionId, storeId);

        model.addAttribute("order", order);
        return "orderConfirmation"; // Redirect to an order confirmation view
    }
}
