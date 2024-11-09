package com.example.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderInfoRepository extends CrudRepository<OrderInfo, Long> {
    List<OrderInfo> findByStatus(OrderStatus status);
}
