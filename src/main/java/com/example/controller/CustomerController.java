package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShopRepository shopRepository;


    // Navigate to create Shopper page
    @GetMapping("/create-customer")
    public String showCreateCustomerForm(Model model) {
        model.addAttribute("Customer", new Customer()); // Empty shopper object for form binding
        return "createCustomer";
    }

    // Handle Shopper creation form submission
    @PostMapping("/create-customer")
    public String createShopper(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {

        if (customerRepository.findByEmail(email).isPresent()) {
            model.addAttribute("errorMessage", "Email already exists");
            return "createCustomer"; // show sign-up page with the error
        }

        Customer newCustomer = new Customer();
        newCustomer.setEmail(email);
        newCustomer.setPassword(password); //TODO: Hash the password with passwordEncoder.
        customerRepository.save(newCustomer);

        return "redirect:/customer-login";
    }

    // Navigate to customer login page
    @GetMapping("/customer-login")
    public String showCustomerLoginForm(Model model) {
        return "customerLogin";
    }

    @PostMapping("/customer-login")
    public ResponseEntity<String> loginCustomer(@RequestParam("email") String email, @RequestParam("password") String password) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            Customer foundCustomer = customer.get();
            //TODO: Confirm the password hash with passwordEncoder
            if (password.equals(foundCustomer.getPassword())) {
                // Return merchant ID on successful login
                return ResponseEntity.ok(foundCustomer.getCustomerId().toString());
            } else {
                // password doesn't match
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } else {
            // invalid email
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
    }

    // Mapping for the shopper button to open searchShops.html
    @GetMapping("/shopper")
    public String openCustomerScreen(Model model) {
        List<Shop> shops = (List<Shop>) shopRepository.findAll(); // Fetch all shops from the database
        model.addAttribute("shops", shops); // Add the shops to the model
        return "searchShops"; // Return the Thymeleaf template for the customer screen;
    }
}