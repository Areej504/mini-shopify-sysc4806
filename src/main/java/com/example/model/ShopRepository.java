package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    List<Shop> findByName(String name);
    List<Shop> findByMerchant_MerchantId(Long merchantId);

    List<Shop> findByMerchant(Merchant merchant);
}



//public interface ShopRepository extends CrudRepository<Shop, Long> {
//    List<Shop> findByName(String name);
//    List<Shop> findByMerchant_MerchantId(Long merchantId);
//
//    List<Shop> findByMerchant(Merchant merchant);
//}