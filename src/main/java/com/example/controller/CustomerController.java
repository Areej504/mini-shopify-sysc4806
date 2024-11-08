package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    // Mapping for the shopper button to open customerShop.html
    @GetMapping("/shopper")
    public String openCustomerShop() {
        return "customerScreen"; // This points to customerShop.html in the templates folder
    }
}
