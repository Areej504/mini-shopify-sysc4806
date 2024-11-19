package com.example.repository;

import com.example.model.Merchant;
import com.example.model.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Long> {
    List<Shop> findByName(String name);
    List<Shop> findByMerchant_MerchantId(Long merchantId);

    List<Shop> findByMerchant(Merchant merchant);
}