package com.example.repository;

import com.example.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Override
    Optional<Customer> findById(Long id);

    Optional<Customer> findByEmail(String email);
}