package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    private Payment payment;
    private Date paymentDate;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private OrderInfo orderInfo;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        paymentDate = new Date();
        amount = new BigDecimal("150.75");
        paymentMethod = PaymentMethod.CREDIT_CARD;
        orderInfo = new OrderInfo(); // Assume OrderInfo has a default constructor
        customer = new Customer();   // Assume Customer has a default constructor
        payment = new Payment(paymentDate, amount, paymentMethod, orderInfo, customer, "Sample payment details");
    }

    @Test
    public void testConstructor() {
        assertNotNull(payment);
        assertEquals(paymentDate, payment.getPaymentDate());
        assertEquals(amount, payment.getAmount());
        assertEquals(paymentMethod, payment.getPaymentMethod());
        assertEquals(orderInfo, payment.getOrder());
        assertEquals(customer, payment.getCustomer());
    }

    @Test
    public void testSettersAndGetters() {
        Date newDate = new Date();
        BigDecimal newAmount = new BigDecimal("200.00");
        PaymentMethod newMethod = PaymentMethod.PAYPAL;
        OrderInfo newOrderInfo = new OrderInfo();
        Customer newCustomer = new Customer();

        payment.setPaymentDate(newDate);
        payment.setAmount(newAmount);
        payment.setPaymentMethod(newMethod);
        payment.setOrder(newOrderInfo);
        payment.setCustomer(newCustomer);

        assertEquals(newDate, payment.getPaymentDate());
        assertEquals(newAmount, payment.getAmount());
        assertEquals(newMethod, payment.getPaymentMethod());
        assertEquals(newOrderInfo, payment.getOrder());
        assertEquals(newCustomer, payment.getCustomer());
    }

    @Test
    public void testPaymentMethodEnum() {
        payment.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY);
        assertEquals(PaymentMethod.CASH_ON_DELIVERY, payment.getPaymentMethod());

        payment.setPaymentMethod(PaymentMethod.BANK_TRANSFER);
        assertEquals(PaymentMethod.BANK_TRANSFER, payment.getPaymentMethod());
    }

    @Test
    public void testAmountField() {
        BigDecimal positiveAmount = new BigDecimal("50.00");
        BigDecimal zeroAmount = BigDecimal.ZERO;
        BigDecimal negativeAmount = new BigDecimal("-10.00");

        payment.setAmount(positiveAmount);
        assertEquals(positiveAmount, payment.getAmount());

        payment.setAmount(zeroAmount);
        assertEquals(zeroAmount, payment.getAmount());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            payment.setAmount(negativeAmount);
        });
        assertEquals("Amount cannot be negative", exception.getMessage());
    }

    @Test
    public void testNullValues() {
        Date paymentDate = new Date();
        BigDecimal amount = new BigDecimal("100.00");
        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
        OrderInfo orderInfo = new OrderInfo();  // Assuming this can be a valid object.
        Customer customer = new Customer();  // Assuming this can be a valid object.
        String paymentDetails = new String();

        // Test null paymentDate
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Payment(null, amount, paymentMethod, orderInfo, customer, paymentDetails);
        });
        assertEquals("Payment fields cannot be null", exception.getMessage());

        // Test null amount
        exception = assertThrows(NullPointerException.class, () -> {
            new Payment(paymentDate, null, paymentMethod, orderInfo, customer, paymentDetails);
        });
        assertEquals("Payment fields cannot be null", exception.getMessage());

        // Test null paymentMethod
        exception = assertThrows(NullPointerException.class, () -> {
            new Payment(paymentDate, amount, null, orderInfo, customer, paymentDetails);
        });
        assertEquals("Payment fields cannot be null", exception.getMessage());

        // Test null customer
        exception = assertThrows(NullPointerException.class, () -> {
            new Payment(paymentDate, amount, paymentMethod, orderInfo, null, paymentDetails);
        });
        assertEquals("Payment fields cannot be null", exception.getMessage());

        // Test null paymentDetails
        exception = assertThrows(NullPointerException.class, () -> {
            new Payment(paymentDate, amount, paymentMethod, orderInfo, customer, null);
        });
        assertEquals("Payment fields cannot be null", exception.getMessage());
    }

    @Test
    public void testAssociationWithOrderInfo() {
        assertEquals(orderInfo, payment.getOrder());

        OrderInfo newOrderInfo = new OrderInfo();
        payment.setOrder(newOrderInfo);
        assertEquals(newOrderInfo, payment.getOrder());
    }

    @Test
    public void testAssociationWithCustomer() {
        assertEquals(customer, payment.getCustomer());

        Customer newCustomer = new Customer();
        payment.setCustomer(newCustomer);
        assertEquals(newCustomer, payment.getCustomer());
    }

    @Test
    public void testToStringMethod() {
        String expectedString = "Payment{" +
                "paymentId=" + payment.getPaymentId() +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", paymentMethod=" + paymentMethod +
                ", order=" + orderInfo +
                ", customer=" + customer +
                '}';
        assertEquals(expectedString, payment.toString());
    }
}