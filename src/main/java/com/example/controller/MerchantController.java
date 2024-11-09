package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MerchantController {
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private ShopRepository shopRepository;

    // Mapping for the merchant button to open merchantView1.html
    @GetMapping("/merchant")
    public String openMerchantHomeScreen() {
        //TODO: Add a login screen so this isn't hard-coded!
        Merchant merchant = new Merchant();
        merchant.setName("Rama");
        merchant.setEmail("rama@shopify.com");
        merchantRepository.save(merchant);
        return "merchantView1";
    }

    @GetMapping("/create-shop")
    public String showCreateShopForm(Model model) {
        // Add Category enum values to the model
        model.addAttribute("categories", Category.values());
        model.addAttribute("shop", new Shop());
        return "createShop";
    }

    @PostMapping("/create-shop")
    public String createShop(@RequestParam String shopName, @ModelAttribute Shop shop, Model model) {
        // Add the shop name to the model for use in the view
        model.addAttribute("shopName", shopName);
        // Save the shop object
        shopRepository.save(shop);

        // Redirect to "manage-stores" with query parameters indicating success
        return "redirect:/manage-stores?created=true&shopName=" + shopName;
    }

    @GetMapping("/manage-stores")
    public String openManageStores(){
        return "manageStores";
    }


}
