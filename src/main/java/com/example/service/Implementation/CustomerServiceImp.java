package com.example.service.Implementation;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import com.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Request to return a list of customers in the database.
     *
     * @return list of Customers.
     */
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Request to find a customer by the customer ID.
     *
     * @param customerId The customer ID.
     *
     * @return The customer whose ID matches the one provided.
     */
    @Override
    public Customer getCustomerById(Long customerId) {
        Optional<Customer> userOpt = customerRepository.findById(customerId);
        if(userOpt.isPresent())
            return userOpt.get();
        else
            throw new RuntimeException("Customer not found.");
    }

    /**
     * Request to find a customer by email.
     *
     * @param email The customer's email address.
     *
     * @return The customer whose email matches the one provided.
     */
    @Override
    public Customer findByEmail(String email) {
        Optional<Customer> userOpt = customerRepository.findByEmail(email);
        if(userOpt.isPresent())
            return userOpt.get();
        else
            throw new RuntimeException("Customer not found.");
    }

    /**
     * Service to save a new customer to the database.
     *
     * @param customer The customer to save to the database.
     */
    @Override
    public void saveCustomer(Customer customer) {
        Customer customerDetail = customerRepository.save(customer);
        System.out.println("Customer saved to db with customerId : " + customerDetail.getCustomerId());
    }

    /**
     * Service to update a customer in the database.
     *
     * @param customer The new data to enter into the customer's entry in the database.
     * @param customerId The ID of the customer in the database to update.
     */
    @Override
    public void updateCustomer(Customer customer, Long customerId) {
        Optional<Customer> customerDetailOpt = customerRepository.findById(customerId);
        if(customerDetailOpt.isPresent()){
            Customer customerDetail = customerDetailOpt.get();
            if(customer.getName() != null || customer.getName().isEmpty())
                customerDetail.setName(customer.getName());
            if(customer.getPassword() != null || customer.getPassword().isEmpty())
                customerDetail.setPassword(customer.getPassword());
            if(customer.getEmail() != null || customer.getEmail().isEmpty())
                customerDetail.setEmail(customer.getEmail());
            if(customer.getAddress() != null || customer.getAddress().isEmpty())
                customerDetail.setAddress(customer.getAddress());
            customerRepository.save(customerDetail);
        }else{
            throw new RuntimeException("Customer not found.");
        }
    }

    /**
     * Service to delete a customer from the database.
     *
     * @param customerId The ID of the customer to delete in the database.
     */
    @Override
    public void deleteCustomer(Long customerId) {
        Optional<Customer> userOpt = customerRepository.findById(customerId);
        if(userOpt.isPresent())
            customerRepository.deleteById(customerId);
        else
            throw new RuntimeException("Customer not found.");
    }
}
