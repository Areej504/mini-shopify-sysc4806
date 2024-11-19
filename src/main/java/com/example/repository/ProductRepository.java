package com.example.repository;

import com.example.model.Category;
import com.example.model.Product;
import com.example.model.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByCategory(Category category);
    List<Product> findByShop(Shop shop);
}