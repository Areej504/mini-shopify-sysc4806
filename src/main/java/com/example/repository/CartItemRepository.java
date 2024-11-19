package com.example.repository;

import com.example.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    // Find all CartItems by Cart ID
    List<CartItem> findByCart_CartId(Long cartId);

    // Optional: Additional query methods if needed
}
