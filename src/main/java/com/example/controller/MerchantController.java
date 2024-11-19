package com.example.controller;

import com.example.model.*;
import com.example.repository.MerchantRepository;
import com.example.repository.ShopRepository;
import com.example.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
public class MerchantController {
    @Autowired
    private MerchantRepository merchantRepository;
    private MerchantService merchantService;

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


}

//package com.example.controller;
//import com.example.model.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.util.UriUtils;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//public class MerchantController {
//
//    @Autowired
//    private MerchantRepository merchantRepository;
//
//    @Autowired
//    private ShopRepository shopRepository;
//
//    // Show the merchant creation form
//    @GetMapping("/create-merchant")
//    public String showCreateMerchantForm(Model model) {
//        model.addAttribute("merchant", new Merchant());
//        return "createMerchant";
//    }
//
//    // Create a new merchant
//    @PostMapping("/create-merchant")
//    public String createMerchant(@ModelAttribute Merchant merchant) {
//        try {
//            merchantRepository.save(merchant);
//            return "redirect:/merchant?merchantId=" + merchant.getMerchantId();
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
//        }
//    }
//
//    // Open the merchant's dashboard
//    @GetMapping("/merchant")
//    public String openMerchantHomeScreen(@RequestParam("merchantId") Long merchantId, Model model) {
//        try {
//            model.addAttribute("merchantId", merchantId);
//            return "merchantView1";
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
//        }
//    }
//
//    // Show the shop creation form for a given merchant
//    @GetMapping("/create-shop")
//    public String showCreateShopForm(@RequestParam("merchantId") Long merchantId, Model model) {
//        try {
//            Optional<Merchant> merchant = merchantRepository.findById(merchantId);
//            if (merchant.isEmpty()) {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Merchant Id");
//            }
//            model.addAttribute("shop", new Shop());
//            model.addAttribute("categories", List.of("Category1", "Category2"));
//            model.addAttribute("merchantId", merchantId);
//            return "createShop";
//        } catch (ResponseStatusException e) {
//            throw e;
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
//        }
//    }
//
//    // Create a new shop for a given merchant
//    @PostMapping("/create-shop")
//    public String createShop(@RequestParam("merchantId") Long merchantId, @ModelAttribute Shop shop) {
//        try {
//            Merchant merchant = merchantRepository.findById(merchantId)
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Merchant Id"));
//            shop.setMerchant(merchant);
//            shopRepository.save(shop);
//            String encodedShopName = UriUtils.encode(shop.getName(), StandardCharsets.UTF_8);
//            return "redirect:/manage-stores?merchantId=" + merchantId + "&created=true&shopName=" + encodedShopName;
//        } catch (ResponseStatusException e) {
//            throw e;
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
//        }
//    }
//
//    // Open the "Manage Stores" view for a merchant
//    @GetMapping("/manage-stores")
//    public String openManageStores(@RequestParam("merchantId") Long merchantId, Model model) {
//        try {
//            Merchant merchant = merchantRepository.findById(merchantId)
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Merchant Id"));
//            List<Shop> shops = shopRepository.findByMerchant(merchant);
//            model.addAttribute("merchant", merchant);
//            model.addAttribute("shops", shops);
//            return "manageStores";
//        } catch (ResponseStatusException e) {
//            throw e;
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
//        }
//    }
//}
//
////package com.example.controller;
////
////import com.example.model.*;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.http.HttpStatus;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.ModelAttribute;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RequestParam;
////import org.springframework.web.server.ResponseStatusException;
////
////import java.util.List;
////import java.util.Optional;
////
////@Controller
////public class MerchantController {
////
////    @Autowired
////    private MerchantRepository merchantRepository;
////
////    @Autowired
////    private ShopRepository shopRepository;
////
////    // Show the merchant creation form
////    @GetMapping("/create-merchant")
////    public String showCreateMerchantForm(Model model) {
////        model.addAttribute("merchant", new Merchant());
////        return "createMerchant";
////    }
////
////    // Create a new merchant
////    @PostMapping("/create-merchant")
////    public String createMerchant(@ModelAttribute Merchant merchant) {
////        try {
////            merchantRepository.save(merchant);
////            return "redirect:/merchant?merchantId=" + merchant.getMerchantId();
////        } catch (RuntimeException e) {
////            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
////        }
////    }
////
////    // Open the merchant's dashboard
////    @GetMapping("/merchant")
////    public String openMerchantHomeScreen(@RequestParam("merchantId") Long merchantId, Model model) {
////        try {
////            model.addAttribute("merchantId", merchantId);
////            return "merchantView1";
////        } catch (RuntimeException e) {
////            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
////        }
////    }
////
////    // Show the shop creation form for a given merchant
////    @GetMapping("/create-shop")
////    public String showCreateShopForm(@RequestParam("merchantId") Long merchantId, Model model) {
////        try {
////            Optional<Merchant> merchant = merchantRepository.findById(merchantId);
////            if (merchant.isEmpty()) {
////                throw new IllegalArgumentException("Invalid Merchant Id");
////            }
////            model.addAttribute("shop", new Shop());
////            model.addAttribute("categories", List.of("Category1", "Category2")); // Example categories
////            model.addAttribute("merchantId", merchantId);
////            return "createShop";
////        } catch (IllegalArgumentException e) {
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
////        } catch (RuntimeException e) {
////            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
////        }
////    }
////
////    // Create a new shop for a given merchant
////    @PostMapping("/create-shop")
////    public String createShop(@RequestParam("merchantId") Long merchantId, @ModelAttribute Shop shop) {
////        try {
////            Merchant merchant = merchantRepository.findById(merchantId)
////                    .orElseThrow(() -> new IllegalArgumentException("Invalid Merchant Id"));
////            shop.setMerchant(merchant);
////            shopRepository.save(shop);
////            return "redirect:/manage-stores?merchantId=" + merchantId + "&created=true&shopName=" + shop.getName();
////        } catch (IllegalArgumentException e) {
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
////        } catch (RuntimeException e) {
////            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
////        }
////    }
////
////    // Open the "Manage Stores" view for a merchant
////    @GetMapping("/manage-stores")
////    public String openManageStores(@RequestParam("merchantId") Long merchantId, Model model) {
////        try {
////            Merchant merchant = merchantRepository.findById(merchantId)
////                    .orElseThrow(() -> new IllegalArgumentException("Invalid Merchant Id"));
////            List<Shop> shops = shopRepository.findByMerchant(merchant);
////            model.addAttribute("merchant", merchant);
////            model.addAttribute("shops", shops);
////            return "manageStores";
////        } catch (IllegalArgumentException e) {
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
////        } catch (RuntimeException e) {
////            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
////        }
////    }
////}
