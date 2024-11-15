package com.example.controller;

import com.example.model.Customer;
import com.example.model.CustomerRepository;
import com.example.model.Shop;
import com.example.model.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    public String createShopper(@ModelAttribute Customer customer) {
        customerRepository.save(customer);
        return "redirect:/shopper?customerId=" + customer.getCustomerId(); // Redirect to shopper
    }

    // Navigate to customer login page
    @GetMapping("/customer-login")
    public String showCustomerLoginForm(Model model) {
        return "customerLogin";
    }

    @PostMapping("/customer-login")
    public String loginCustomer(@RequestParam("email") String email, Model model) {
        // Find the customer by email
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer email not found"));

        return "redirect:/shopper?customerId=" + customer.getCustomerId(); //redirect to shopper
    }

    // Mapping for the shopper button to open searchShops.html
    @GetMapping("/shopper")
    public String openCustomerScreen(Model model) {
        List<Shop> shops = (List<Shop>) shopRepository.findAll(); // Fetch all shops from the database
        model.addAttribute("shops", shops); // Add the shops to the model
        return "searchShops"; // Return the Thymeleaf template for the customer screen;
    }
}

//package com.example.controller;
//
//import com.example.model.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//
//@Controller
//public class CustomerController {
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private ShopRepository shopRepository;
//
//
//    // Navigate to create Shopper page
//    @GetMapping("/create-customer")
//    public String showCreateCustomerForm(Model model) {
//        model.addAttribute("Customer", new Customer()); // Empty shopper object for form binding
//        return "createCustomer";
//    }
//
//    // Handle Shopper creation form submission
//    @PostMapping("/create-customer")
//    public String createShopper(@ModelAttribute Customer customer) {
//        try {
//            customerRepository.save(customer);
//            return "redirect:/shopper?customerId=" + customer.getCustomerId(); // Redirect to shopper
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
//        }
//    }
//
//    // Mapping for the shopper button to open searchShops.html
//    @GetMapping("/shopper")
//    public String openCustomerScreen(Model model) {
//        try{
//            List<Shop> shops = (List<Shop>) shopRepository.findAll(); // Fetch all shops from the database
//            model.addAttribute("shops", shops); // Add the shops to the model
//            return "customerScreen"; // Return the Thymeleaf template for the customer screen;
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
//        }
//    }
//}
