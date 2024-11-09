package com.example.controller;

import com.example.model.Customer;
import com.example.model.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    // Mapping for the shopper button to open customerShop.html
    @GetMapping("/shopper")
    public String openCustomerScreen() {
        //TODO: Add a login screen so this isn't hard-coded!
        Customer customer = new Customer();
        customer.setName("Areej");
        customer.setEmail("areej@shopify.com");
        customerRepository.save(customer);
        return "customerScreen"; // This points to customerScreen.html in the templates folder
    }
}
