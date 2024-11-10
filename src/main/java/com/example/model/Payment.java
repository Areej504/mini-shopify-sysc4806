package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Payment {
    @Id
    @GeneratedValue
    private Long paymentId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne(mappedBy = "payment")
    private OrderInfo orderInfo;

    @ManyToOne
    private Customer customer;

    // Constructors
    public Payment() {}

    public Payment(Date paymentDate, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status, OrderInfo orderInfo, Customer customer, String paymentDetails) {
        if (paymentDate == null || amount == null || paymentMethod == null || status == null || customer == null || paymentDetails == null) {
            throw new NullPointerException("Payment fields cannot be null");
        }
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.orderInfo = orderInfo;
        this.customer = customer;
    }

    // Getters and Setters
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        if (paymentDate == null) {
            throw new NullPointerException("Payment Date cannot be null");
        }
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null) {
            throw new NullPointerException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new NullPointerException("Payment Method cannot be null");
        }
        this.paymentMethod = paymentMethod;

    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        if (status == null) {
            throw new NullPointerException("Payment Status cannot be null");
        }
        this.status = status;
    }

    public OrderInfo getOrder() {
        return orderInfo;
    }

    public void setOrder(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            throw new NullPointerException("Customer cannot be null");
        }
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", paymentMethod=" + paymentMethod +
                ", status=" + status +
                ", order=" + orderInfo +
                ", customer=" + customer +
                '}';
    }
}
