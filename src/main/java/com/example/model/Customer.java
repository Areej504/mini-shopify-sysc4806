package com.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long customerId;

    private String name;
    private String email;
    private String password;
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<OrderInfo> orderInfos;

    @OneToMany(mappedBy = "customer")
    private List<Cart> carts = new ArrayList<>();

    // Constructors
    public Customer() {}

    public Customer(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cart> getCarts() {
        return carts;
    }
    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }
    public void setOrderInfos(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderInfo> getOrders() {
        return orderInfos;
    }

    public void setOrders(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }

    public int getCurrentOrders() {
        return orderInfos != null
                ? (int) orderInfos.stream()
                .filter(orderInfo -> orderInfo.getStatus() == OrderStatus.PROCESSING || orderInfo.getStatus() == OrderStatus.SHIPPED)
                .count()
                : 0;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
