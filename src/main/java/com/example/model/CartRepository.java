package com.example.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
//    Optional<Cart> findByStoreAndCustomer(Shop store, Customer customer);
}