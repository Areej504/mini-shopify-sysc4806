package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return "merchantView1";
    }

    @GetMapping("/create-shop")
    public String showCreateShopForm(Model model) {

        // Add the merchant and shop to the model
        model.addAttribute("shop", new Shop());
        model.addAttribute("categories", Category.values());  // Assuming you have categories to show

        return "createShop";
    }

    @PostMapping("/create-shop")
    public String createShop(@RequestParam String shopName, @ModelAttribute Shop shop, Model model) {
        // Add the shop name to the model for use in the view
        model.addAttribute("shopName", shopName);

        // Save the shop object
        shopRepository.save(shop);

        return "redirect:/manage-stores?created=true&shopName=" + shopName;
    }

    @GetMapping("/manage-stores")
    public String openManageStores(Model model){
        //TODO: Figure out a way to pass the merchantId to these pages so its not hard-coded
        Long merchantId = 1L;
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid merchant Id:" + merchantId));

        // Get the list of shops for the merchant
        List<Shop> shops = merchant.getShops();

        // Add the list of shops to the model
        model.addAttribute("shops", shops);

        return "manageStores";
    }




}
