package com.example.service;

import com.example.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    /**
     * Request to return a list of customers in the database.
     *
     * @return list of Customers.
     */
    List<Customer> getAllCustomers();

    /**
     * Request to find a customer by the customer ID.
     *
     * @param customerId The customer ID.
     *
     * @return The customer whose ID matches the one provided.
     */
    Customer getCustomerById(Long customerId);

    /**
     * Request to find a customer by email.
     *
     * @param email The customer's email address.
     *
     * @return The customer whose email matches the one provided.
     */
    Customer findByEmail(String email);

    /**
     * Service to save a new customer to the database.
     *
     * @param customer The customer to save to the database.
     */
    void saveCustomer(Customer customer);

    /**
     * Service to update a customer in the database.
     *
     * @param customer The new data to enter into the customer's entry in the database.
     * @param customerId The ID of the customer in the database to update.
     */
    void updateCustomer(Customer customer, Long customerId);

    /**
     * Service to delete a customer from the database.
     *
     * @param customerId The ID of the customer to delete in the database.
     */
    void deleteCustomer(Long customerId);
}
