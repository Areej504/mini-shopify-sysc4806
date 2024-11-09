public enum OrderStatus {
    PROCESSING("Order is being prepared"),
    SHIPPED("Order has been shipped"),
    DELIVERED("Order delivered to customer"),
    CANCELED("Order was canceled"),
    RETURN_REQUESTED("Return has been requested by customer"),
    RETURNED("Order was returned by customer"),
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
