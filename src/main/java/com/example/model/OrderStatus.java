package com.example.model;

public enum OrderStatus {
    PROCESSING("processed"),
    SHIPPED("shipped"),
    DELIVERED("delivered to customer"),
    CANCELED("canceled"),
    RETURN_REQUESTED("Return has been requested by customer"),
    RETURNED("returned by customer"),
    REFUNDED("Customer has been refunded"),
    COMPLETED("Customer completed order"),
    CANCELLED("Customer cancelled order"),;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
