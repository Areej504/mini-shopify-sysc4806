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

    @OneToOne(mappedBy = "order")
    private OrderInfo orderInfo;

    @ManyToOne
    private Customer customer;

    // Constructors
    public Payment() {}

    public Payment(Date paymentDate, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status, OrderInfo orderInfo, Customer customer, String paymentDetails) {
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
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
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
