package com.example.controller;
import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
public class MerchantController {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ShopRepository shopRepository;

    // Show the merchant creation form
    @GetMapping("/create-merchant")
    public String showCreateMerchantForm(Model model) {
        model.addAttribute("merchant", new Merchant());
        return "createMerchant";
    }

    // Create a new merchant
    @PostMapping("/create-merchant")
    public String createMerchant(@ModelAttribute Merchant merchant) {
        try {
            merchantRepository.save(merchant);
            return "redirect:/merchant?merchantId=" + merchant.getMerchantId();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
        }
    }

    // Open the merchant's dashboard
    @GetMapping("/merchant")
    public String openMerchantHomeScreen(@RequestParam("merchantId") Long merchantId, Model model) {
        try {
            model.addAttribute("merchantId", merchantId);
            return "merchantView1";
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
        }
    }

    // Show the shop creation form for a given merchant
    @GetMapping("/create-shop")
    public String showCreateShopForm(@RequestParam("merchantId") Long merchantId, Model model) {
        try {
            Optional<Merchant> merchant = merchantRepository.findById(merchantId);
            if (merchant.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Merchant Id");
            }
            model.addAttribute("shop", new Shop());
            model.addAttribute("categories", List.of("Category1", "Category2"));
            model.addAttribute("merchantId", merchantId);
            return "createShop";
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
        }
    }

    // Create a new shop for a given merchant
    @PostMapping("/create-shop")
    public String createShop(@RequestParam("merchantId") Long merchantId, @ModelAttribute Shop shop) {
        try {
            Merchant merchant = merchantRepository.findById(merchantId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Merchant Id"));
            shop.setMerchant(merchant);
            shopRepository.save(shop);
            String encodedShopName = UriUtils.encode(shop.getName(), StandardCharsets.UTF_8);
            return "redirect:/manage-stores?merchantId=" + merchantId + "&created=true&shopName=" + encodedShopName;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
        }
    }

    // Open the "Manage Stores" view for a merchant
    @GetMapping("/manage-stores")
    public String openManageStores(@RequestParam("merchantId") Long merchantId, Model model) {
        try {
            Merchant merchant = merchantRepository.findById(merchantId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid Merchant Id"));
            List<Shop> shops = shopRepository.findByMerchant(merchant);
            model.addAttribute("merchant", merchant);
            model.addAttribute("shops", shops);
            return "manageStores";
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
        }
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
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.server.ResponseStatusException;
//
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
//                throw new IllegalArgumentException("Invalid Merchant Id");
//            }
//            model.addAttribute("shop", new Shop());
//            model.addAttribute("categories", List.of("Category1", "Category2")); // Example categories
//            model.addAttribute("merchantId", merchantId);
//            return "createShop";
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
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
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid Merchant Id"));
//            shop.setMerchant(merchant);
//            shopRepository.save(shop);
//            return "redirect:/manage-stores?merchantId=" + merchantId + "&created=true&shopName=" + shop.getName();
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
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
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid Merchant Id"));
//            List<Shop> shops = shopRepository.findByMerchant(merchant);
//            model.addAttribute("merchant", merchant);
//            model.addAttribute("shops", shops);
//            return "manageStores";
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database Error", e);
//        }
//    }
//}
