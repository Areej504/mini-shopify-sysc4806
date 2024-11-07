import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

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

    //Methods
    public void addProduct(Product product, int quantity) {
        CartItem existingItem = findCartItem(product);
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
        return total;
    }

    private CartItem findCartItem(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().equals(product)) {
                return item;
            }
        }
        return null;
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
