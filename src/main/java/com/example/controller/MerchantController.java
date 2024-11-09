package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Controller
public class MerchantController {
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private ShopRepository shopRepository;

    //create Merchant page
    @GetMapping("/create-merchant")
    public String showCreateMerchantForm(Model model) {
        model.addAttribute("merchant", new Merchant()); // Empty merchant object for form binding
        return "createMerchant";
    }

    //Merchant creation form submission
    @PostMapping("/create-merchant")
    public String createMerchant(@ModelAttribute Merchant merchant, Model model) {
        merchantRepository.save(merchant);
        return "redirect:/merchant?merchantId=" + merchant.getMerchantId(); // Redirect to merchant dashboard
    }

    // Mapping for the merchant button to open merchantView1.html
    @RequestMapping("/merchant")
    public String openMerchantHomeScreen(@RequestParam Long merchantId, Model model) {
        model.addAttribute("merchantId", merchantId);
        return "merchantView1";
    }

    @GetMapping("/create-shop")
    public String showCreateShopForm(@RequestParam Long merchantId, Model model) {

        // Add the merchant and shop to the model
        model.addAttribute("shop", new Shop());
        model.addAttribute("categories", Category.values());  // Assuming you have categories to show
        model.addAttribute("merchantId", merchantId);
        
        return "createShop";
    }

    @PostMapping("/create-shop")
    public String createShop(@RequestParam Long merchantId, @ModelAttribute Shop shop, Model model) {
        // Add the shop name to the model for use in the view

        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() -> new IllegalArgumentException("Invalid Merchant Id"));
        shop.setMerchant(merchant);

        System.out.println(shop.getMerchant().getName());
        System.out.println(shop.getName());
        System.out.println(shop.getDescription());
        System.out.println(shop.getCategories());

        // Save the shop object
        shopRepository.save(shop);

        return "redirect:/manage-stores?merchantId=" + merchantId + "&created=true&shopName=" + URLEncoder.encode(shop.getName(), StandardCharsets.UTF_8);

    }

    @GetMapping("/manage-stores")
    public String openManageStores(@RequestParam Long merchantId, Model model){
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Merchant Id"));
        List<Shop> shops = shopRepository.findByMerchant(merchant);

        // Add attributes to the model for the view
        model.addAttribute("merchant", merchant);
        model.addAttribute("shops", shops);

        return "manageStores";
    }

    @GetMapping("/merchantShop/{shopId}")
    public String viewShop(@PathVariable Long shopId, Model model) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shop Id"));
        model.addAttribute("shop", shop); // Add shop details to the model
        return "merchantShop"; // Return the Thymeleaf template for the shop details
    }




}
