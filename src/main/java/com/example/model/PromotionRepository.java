package com.example.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends CrudRepository<ShopPromotions, Long> {
    Object save(ShopPromotions promotion);
}

