package com.example.controller;

import com.example.model.Customer;
import com.example.model.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;


    // Navigate to create Shopper page
    @GetMapping("/create-customer")
    public String showCreateCustomerForm(Model model) {
        model.addAttribute("Customer", new Customer()); // Empty shopper object for form binding
        return "createCustomer";
    }

    // Handle Shopper creation form submission
    @PostMapping("/create-customer")
    public String createShopper(@ModelAttribute Customer customer) {
        customerRepository.save(customer);
        return "redirect:/shopper?customerId=" + customer.getCustomerId(); // Redirect to shopper
    }

    // Mapping for the shopper button to open customerScreen.html
    @GetMapping("/shopper")
    public String openCustomerScreen() {
        return "customerScreen";
    }
}
