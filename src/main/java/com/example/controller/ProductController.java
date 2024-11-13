package com.example.controller;

import com.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;


@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShopRepository shopRepository;

    @GetMapping("/merchantShop/{shopId}")
    public String viewMerchantShop(@PathVariable Long shopId, Model model) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid shop Id"));
        model.addAttribute("shop", shop); // Add shop details to the model
        model.addAttribute("product", new Product()); // Add Product model to Thymeleaf
        model.addAttribute("categories", shop.getCategories());
        model.addAttribute("promotions", PromotionType.values());
        model.addAttribute("products", productRepository.findByShop(shop)); // Fetch all products
        return "merchantShop";
    }

    @PostMapping("/merchantShop/{shopId}")
    public String addProduct(@ModelAttribute Product product, @PathVariable Long shopId, @RequestParam("productImage") MultipartFile file, Model model) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid shop Id"));
        product.setShop(shop);

        // Save the uploaded image file using the original filename
        if (!file.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/product-images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);

                // Check if the file already exists
                if (!Files.exists(filePath)) {
                    // Use try-with-resources to ensure stream is closed after copying
                    try (InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, filePath);
                    }
                }

                product.setImageURL("/product-images/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image upload failed");
            }
        }
        productRepository.save(product);
        model.addAttribute("product", product);
        return "redirect:/merchantShop/" + shopId;
    }
    @DeleteMapping("/merchantShop/{shopId}/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long shopId, @PathVariable Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return ResponseEntity.ok().build(); // Return 200 OK status
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if product doesn't exist
        }
    }
}
