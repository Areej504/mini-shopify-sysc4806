package com.example.controller;

import com.example.model.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MerchantController {
    // Mapping for the shopper button to open customerShop.html
    @GetMapping("/merchant")
    public String openMerchantHomeScreen() {
        return "merchantView1";
    }

    @GetMapping("/create-shop")
    public String openCreateShop(Model model) {
        // Add Category enum values to the model
        model.addAttribute("categories", Category.values());
        return "createShop";
    }

    @PostMapping("/create-shop")
    public String createShop(@RequestParam("shopName") String shopName, Model model) {
        // Add the shop name as a parameter to be accessed in the view
        model.addAttribute("shopName", shopName);
        return "redirect:/manage-stores?created=true&shopName=" + shopName;
    }

    @GetMapping("/manage-stores")
    public String openManageStores(){
        return "manageStores";
    }


}
