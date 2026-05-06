package com.shopzone.repository;

import com.shopzone.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findBySessionId(String sessionId);
    Optional<Cart> findBySessionIdAndProduct_Id(String sessionId, Integer productId);
    void deleteBySessionId(String sessionId);
}
