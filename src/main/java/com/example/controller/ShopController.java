package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Controller
public class ShopController {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    CartRepository cartRepository;

    @GetMapping("/shopPage/{shopId}")
    public String getShopDetails(@PathVariable Long shopId, Model model) {
        Optional<Shop> shop = shopRepository.findById(shopId);
        if (shop.isPresent()) {
            Shop shopDetails = shop.get();
            model.addAttribute("shopName", shopDetails.getName());
            model.addAttribute("shopDescription", shopDetails.getDescription());
            model.addAttribute("products", shopDetails.getProducts());
        }

        // Retrieve the cart contents and calculate the number of items
        long totalItemsInCart = cartRepository.count(); //each cart entry represents one item
        model.addAttribute("totalItemsInCart", totalItemsInCart);

        return "shopPage"; // Render the shopPage template
    }

    @GetMapping("/cartView")
    public String openCartView(Model model) {
        // Retrieve necessary data for the cart view (e.g., cart items, total price)
        Iterable<Cart> cartItems = cartRepository.findAll();
        model.addAttribute("cartItems", cartItems);

        return "cartView";
    }

    @GetMapping("/paymentView")
    public String openPaymentView(Model model){
        return "paymentView";
    }

}

//package com.example.controller;
//
//import com.example.model.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import java.util.List;
//import java.util.Optional;
//
//
//@Controller
//public class ShopController {
//
//    @Autowired
//    ShopRepository shopRepository;
//
//    @Autowired
//    CartRepository cartRepository;
//
//    @GetMapping("/shopPage/{shopId}")
//    public String getShopDetails(@PathVariable Long shopId, Model model) {
//        Optional<Shop> shop = shopRepository.findById(shopId);
//        if (shop.isPresent()) {
//            Shop shopDetails = shop.get();
//            model.addAttribute("shopName", shopDetails.getName());
//            model.addAttribute("shopDescription", shopDetails.getDescription());
//            model.addAttribute("products", shopDetails.getProducts());
//        }
//
//        // Retrieve the cart contents and calculate the number of items
//        long totalItemsInCart = cartRepository.count(); //each cart entry represents one item
//        model.addAttribute("totalItemsInCart", totalItemsInCart);
//
//        return "shopPage"; // Render the shopPage template
//    }
//
//    @GetMapping("/cartView")
//    public String openCartView(Model model) {
//        // Retrieve necessary data for the cart view (e.g., cart items, total price)
//        Iterable<Cart> cartItems = cartRepository.findAll();
//        model.addAttribute("cartItems", cartItems);
//
//        return "cartView";
//    }
//
//    @GetMapping("/paymentView")
//    public String openPaymentView(Model model){
//        return "paymentView";
//    }
//
//}
