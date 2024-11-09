package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class CartItem {
    @Id
    @GeneratedValue
    private Long cartItemId;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;

    private int quantity;

    // Constructor
    public CartItem() {}

    public CartItem(Cart cart, Product product, int quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Calculate the total price for this CartItem (quantity * price)
    public BigDecimal getTotalPrice() {
        //return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        return (product != null && product.getPrice() != null)
                ? product.getPrice().multiply(BigDecimal.valueOf(quantity))
                : BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", product=" + product +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }
}
