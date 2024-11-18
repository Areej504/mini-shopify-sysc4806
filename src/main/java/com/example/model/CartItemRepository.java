package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Find all CartItems by Cart ID
    List<CartItem> findByCart_CartId(Long cartId);

    // Optional: Additional query methods if needed
}
