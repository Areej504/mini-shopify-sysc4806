package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Long cartId;

    @ManyToOne
    private Shop shop;

    @ManyToOne
    private Customer customer;  // Link the cart to the customer

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private BigDecimal totalPrice; // This is the subtotal of the products
    private final double gst = 0.13; // GST amount
    private BigDecimal storeDiscount; // Store promotion discount percentage
    private BigDecimal finalPrice; // Final price after discount and GST
    private double shippingCost; // Shipping cost

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;
    // Constructor
    public Cart() {}

    public Cart(Customer customer) {
        this.customer = customer;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getShippingCost(){
        if (cartItems.isEmpty()){
            return 0;
        }
        PromotionType promotion = cartItems.get(0).getProduct().getShop().getPromotion();
        shippingCost = 7;
        if (promotion == null){
            return shippingCost;
        }
        if (promotion == PromotionType.FREE_SHIPPING){
            shippingCost = 0;
        }
        return shippingCost;
    }

    public BigDecimal getStoreDiscount() {
        BigDecimal discount = BigDecimal.ZERO;
        // Ensure cart is not empty
        if (cartItems.isEmpty()) {
            System.out.println("st 1");
            return discount;
        }

        // Get the promotion type of the shop
        PromotionType promotion = cartItems.get(0).getProduct().getShop().getPromotion();

        if (promotion == null) {
            System.out.println("st 2");
            return discount;
        }

        if (promotion == PromotionType.BUY_ONE_GET_ONE) {
            System.out.println("st 3");
            List<BigDecimal> prices = new ArrayList<>();

            // Collect all prices, considering the quantity and discount logic
            for (CartItem item : cartItems) {
                BigDecimal itemPrice = item.getProduct().getPrice();
                BigDecimal discountedPrice = item.getProduct().getDiscountedPrice();
                BigDecimal priceToAdd = (discountedPrice != null) ? discountedPrice : itemPrice;

                for (int i = 0; i < item.getQuantity(); i++) {  // Add price for each unit of the item
                    prices.add(priceToAdd);  // Add either discounted price or regular price
                }
            }
            System.out.println(prices);

            // Sort prices in descending order
            prices.sort(Comparator.naturalOrder());
            System.out.println(prices);

            // Apply BOGO discount logic
            for (int i = 0; i < prices.size()/2; i++) {
                System.out.println(prices.get(i));
                discount = discount.add(prices.get(i));
            }
        }

        return discount;
    }


    public void setStoreDiscount(BigDecimal storeDiscount) {
        this.storeDiscount = storeDiscount;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public void setShippingCost(double shippingCost){
        this.shippingCost = shippingCost;
    }

    public BigDecimal getGst(){
        return totalPrice.subtract(getStoreDiscount()).multiply(BigDecimal.valueOf(gst));
    }

    //Methods
    public void addProduct(Product product, int quantity) {
        CartItem existingItem = findCartItem(product);
        if (quantity <= 0) {
            return; // Ignore non-positive quantities
        }
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(this, product, quantity);
            cartItems.add(newItem);
        }

    }

    public void removeProduct(Product product) {
        CartItem existingItem = findCartItem(product);
        if (existingItem != null) {
            if (existingItem.getQuantity() > 1) {
                existingItem.setQuantity(existingItem.getQuantity() - 1); // Decrease quantity by 1
            } else {
                cartItems.remove(existingItem); // Remove CartItem if quantity is 1
            }
        } else {
            System.out.println("Product not found in the cart.");
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.getTotalPrice());
        }
        totalPrice = total;
        return total;
    }

    public BigDecimal getFinalPrice() {
        BigDecimal subtotal = getTotalPrice();
        BigDecimal gstAmount = getGst();
        BigDecimal discountAmount =  getStoreDiscount();
        double shipping = getShippingCost();

        System.out.println(subtotal);
        System.out.println(gstAmount);
        System.out.println(discountAmount);
        System.out.println(shipping);

        // Final price calculation: Subtotal + GST - Discount + Shipping
        BigDecimal finalAmount = subtotal.add(gstAmount).subtract(discountAmount).add(BigDecimal.valueOf(shipping));
        finalPrice = finalAmount;
        return finalAmount;
    }

    private CartItem findCartItem(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().equals(product)) {
                return item;
            }
        }
        return null;
    }

    public int getTotalItems(){
        int items = 0;
        for (CartItem item : cartItems){
            items += item.getQuantity();
        }
        return items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", customer=" + customer +
                ", totalPrice=" + getTotalPrice() +
                ", cartItems=" + cartItems +
                '}';
    }
}
