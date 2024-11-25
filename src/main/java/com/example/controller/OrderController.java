package com.example.controller;

import com.example.model.CartItemRepository;
import com.example.model.CartRepository;
import com.example.model.ProductRepository;
import com.example.model.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @Autowired
    ShopRepository shopRepository;

    @Autowired
    ProductRepository productRepository;



    @GetMapping("/paymentView")
    public String openPaymentView(Model model){
//        model.addAttribute("shopName)
        return "paymentView";
    }
}
