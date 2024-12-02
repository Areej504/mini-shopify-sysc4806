package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class OrderInfo {
    @Id
    @GeneratedValue
    private Long orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    private BigDecimal totalAmount;

    @ManyToOne
    private Customer customer;

    @OneToOne
    private Cart cart;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne // One-to-one relationship with Payment
    private Payment payment;  // Only one payment per order

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    @OneToOne
    private Shipping shipping;

    public OrderInfo() {}

    public OrderInfo(Date orderDate, BigDecimal totalAmount, Customer customer, Cart cart, OrderStatus status) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.cart = cart;
        this.status = status;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setStatus(OrderStatus status){
        this.status = status;
    }

    public OrderStatus getStatus(){
        return status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setCart(Cart cart){
        this.cart = cart;
    }
    public Cart getCart(){
        return cart;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", customer=" + customer +
                ", cart=" + cart +
                ", status=" + status +
                ", payment=" + payment +
                '}';
    }

}
