package com.example.repository;

import com.example.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
}