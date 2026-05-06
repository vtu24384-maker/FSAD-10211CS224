package com.shopzone.repository;

import com.shopzone.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByIdAndUserId(Integer id, Integer userId);
}
