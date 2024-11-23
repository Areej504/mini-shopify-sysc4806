package com.example.controller;

import com.example.model.*;
import java.math.BigDecimal;

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
import java.util.Arrays;
import java.util.List;


@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private PromotionRepository promotionRepository;

    @GetMapping("/merchantShop/{shopId}")
    public String viewMerchantShop(@PathVariable Long shopId, Model model) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid shop Id"));

// Retrieve only specific promotion types
        List<PromotionType> relevantPromotions = Arrays.asList(
                PromotionType.DISCOUNT_10_PERCENT,
                PromotionType.DISCOUNT_20_PERCENT,
                PromotionType.DISCOUNT_5_DOLLARS,
                PromotionType.CLEARANCE,
                PromotionType.NONE
        );

        List<PromotionType> shopPromotions = Arrays.asList(
                PromotionType.FREE_SHIPPING,
                PromotionType.SEASONAL_HOLIDAY,
                PromotionType.BUY_ONE_GET_ONE,
                PromotionType.CLEARANCE,
                PromotionType.NONE
        );
        model.addAttribute("shop", shop); // Add shop details to the model
        model.addAttribute("product", new Product()); // Add Product model to Thymeleaf
        model.addAttribute("categories", shop.getCategories());
        model.addAttribute("promotions", relevantPromotions);
        model.addAttribute("shopPromotions", shopPromotions);
        model.addAttribute("products", productRepository.findByShop(shop)); // Fetch all products
        return "merchantShop";
    }

    @PostMapping("/merchantShop/{shopId}")
    public String addOrUpdateProduct(
            @ModelAttribute Product product,
            @RequestParam(value = "productId", required = false) Long productId,
            @PathVariable Long shopId,
            @RequestParam("productImage") MultipartFile file,
            Model model) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid shop Id"));
        product.setShop(shop);

        if (productId != null) {
            // Update existing product
            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid product Id"));

            existingProduct.setProductName(product.getProductName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setInventory(product.getInventory());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setPromotionType(product.getPromotionType());

            // Calculate and set the discounted price
            BigDecimal discountedPrice = existingProduct.calculateDiscountedPrice();
            System.out.println(discountedPrice);
            existingProduct.setDiscountedPrice(discountedPrice);

            // Handle the image update (if provided)
            if (!file.isEmpty()) {
                try {
                    String uploadDir = "src/main/resources/static/product-images/";
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    String fileName = file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);

                    if (!Files.exists(filePath)) {
                        try (InputStream inputStream = file.getInputStream()) {
                            Files.copy(inputStream, filePath);
                        }
                    }
                    existingProduct.setImageURL("/product-images/" + fileName);
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image upload failed");
                }
            }

            productRepository.save(existingProduct);
        } else {
            // Add new product
            BigDecimal discountedPrice = product.calculateDiscountedPrice();
            System.out.println(discountedPrice);
            product.setDiscountedPrice(discountedPrice);

            if (!file.isEmpty()) {
                try {
                    String uploadDir = "src/main/resources/static/product-images/";
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    String fileName = file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);

                    if (!Files.exists(filePath)) {
                        try (InputStream inputStream = file.getInputStream()) {
                            Files.copy(inputStream, filePath);
                        }
                    }

                    product.setImageURL("/product-images/" + fileName);
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image upload failed");
                }
            }
            productRepository.save(product);
        }

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

    @GetMapping("/merchantShop/{shopId}/product/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Long shopId, @PathVariable Long productId) {
        // Verify that the shop exists
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid shop Id"));

        // Verify that the product exists and belongs to the specified shop
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid product Id"));
        if (!product.getShop().getShopId().equals(shopId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product does not belong to this shop");
        }

        return ResponseEntity.ok(product); // Return the product details as JSON
    }

}