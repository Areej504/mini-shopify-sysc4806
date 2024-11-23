//package com.example.model;
//import java.util.HashMap;
//import java.util.Map;
//
//public class promoService {
//    public class PromotionService {
//        private final Map<Long, ShopPromotions> promotionsStorage = new HashMap<>();
//
//        // Save or update promotion
//        public void savePromotion(Long shopId, ShopPromotions promotion) {
//            promotionsStorage.put(shopId, promotion);
//        }
//
//        // Retrieve promotion by shopId
//        public ShopPromotions getPromotion(Long shopId) {
//            return promotionsStorage.get(shopId);
//        }
//
//        // Remove promotion by shopId (optional)
//        public void deletePromotion(Long shopId) {
//            promotionsStorage.remove(shopId);
//        }
//    }
//}
