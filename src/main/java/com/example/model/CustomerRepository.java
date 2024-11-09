package com.example.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    @Override
    Optional<Customer> findById(Long aLong);

    Optional<Customer> findByEmail(String email);
}