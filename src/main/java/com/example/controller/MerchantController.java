package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
//@RequestMapping("/merchantShop")
public class MerchantController {
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private PromotionRepository promotionRepository;

    //create Merchant page
    @GetMapping("/create-merchant")
    public String showCreateMerchantForm(Model model) {
        model.addAttribute("merchant", new Merchant()); // Empty merchant object for form binding
        return "createMerchant";
    }

    //Merchant creation form submission
    @PostMapping("/create-merchant")
    public String createMerchant(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        if (merchantRepository.findByEmail(email).isPresent()) {
            model.addAttribute("errorMessage", "Email already exists");
            return "createMerchant"; // show sign-up page with the error
        }

        Merchant newMerchant = new Merchant();
        newMerchant.setEmail(email);
        newMerchant.setPassword(password); //TODO: Hash the password with passwordEncoder.
        merchantRepository.save(newMerchant);

        return "redirect:/merchant-login";
    }

    // Navigate to merchant login page
    @GetMapping("/merchant-login")
    public String showMerchantLoginForm(Model model) {
        return "merchantLogin";
    }

    @PostMapping("/merchant-login")
    public ResponseEntity<String> loginMerchant(@RequestParam("email") String email, @RequestParam("password") String password) {
        Optional<Merchant> merchant = merchantRepository.findByEmail(email);

        if (merchant.isPresent()) {
            Merchant foundMerchant = merchant.get();
            //TODO: Confirm the password hash with passwordEncoder
            if (password.equals(foundMerchant.getPassword())) {
                // Return merchant ID on successful login
                return ResponseEntity.ok(foundMerchant.getMerchantId().toString());
            } else {
                // password doesn't match
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }
        } else {
            // invalid email
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }
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
        model.addAttribute("categories", Category.values());
        model.addAttribute("merchantId", merchantId);

        model.addAttribute("promotionTypes", PromotionType.values());
        return "createShop";
    }

    @PostMapping("/create-shop")
    public String createShop(@RequestParam Long merchantId, @ModelAttribute Shop shop, Model model) {
        // Add the shop name to the model for use in the view

//        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow(() -> new IllegalArgumentException("Invalid Merchant Id"));
//        shop.setMerchant(merchant);
        Optional<Merchant> merchantOpt = merchantRepository.findById(merchantId);
        if (merchantOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid Merchant ID");
            return "errorPage"; // Redirect to a user-friendly error page
        }

        Merchant merchant = merchantOpt.get();
        shop.setMerchant(merchant);

        System.out.println(shop.getMerchant().getName());
        System.out.println(shop.getName());
        System.out.println(shop.getDescription());
        System.out.println(shop.getCategories());

        // Save the shop object
        shopRepository.save(shop);

        return "redirect:/manage-stores?merchantId=" + merchantId + "&created=true&shopName=" +
                URLEncoder.encode(shop.getName(), StandardCharsets.UTF_8);

    }

    @GetMapping("/manage-stores")
    public String openManageStores(@RequestParam Long merchantId, Model model) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Merchant Id"));
        List<Shop> shops = shopRepository.findByMerchant(merchant);

        // Add attributes to the model for the view
        model.addAttribute("merchant", merchant);
        model.addAttribute("shops", shops);

        return "manageStores";
    }


    @PostMapping("/setPromotion/{shopId}")
    public String setShopPromotion(@PathVariable Long shopId, @RequestParam("promotion") PromotionType promotionType) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));

        // Update the shop's promotion
        shop.setPromotion(promotionType);
        shopRepository.save(shop);

        return "redirect:/merchantShop/" + shopId;
    }

    @PostMapping("/setPromotion/{shopId}/Form")
    public String setPromotionDates(
            @PathVariable Long shopId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("promotion") PromotionType promotionType) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found"));

        // Ensure the ShopPromotions object is created or updated
        //ShopPromotions promotion = Optional.ofNullable(shop.getShopPromotions()).orElse(new ShopPromotions());

        ShopPromotions promotion = shop.getShopPromotions();
        if (promotion == null) {
            promotion = new ShopPromotions();
            promotion.setShop(shop);
        }

        promotion.setPromotionType(promotionType);
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        //promotion.setShop(shop);

        // Save the promotion and associate it with the shop
        shop.setShopPromotions(promotion);
        shopRepository.save(shop); // Cascade ensures promotion is saved

        System.out.println("Promotion Saved:");
        System.out.println("Type: " + promotionType);
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);

        return "redirect:/merchantShop/" + shopId;
    }
}