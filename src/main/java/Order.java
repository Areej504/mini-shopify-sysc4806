import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    private BigDecimal totalAmount;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL) // One-to-one relationship with Payment
    private Payment payment;  // Only one payment per order

    public Order() {}

    public Order(Date orderDate, BigDecimal totalAmount, Customer customer, List<Product> products, OrderStatus status) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.products = products;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> orderItems) {
        this.products = orderItems;
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

    //Methods
    public void calculateTotalPrice(){}

    public void updateStatus(){}

    public void setProductsOrdered(){}

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", customer=" + customer +
                ", products=" + products +
                ", status=" + status +
                ", payment=" + payment +
                '}';
    }

}
