package com.shopzone.service;

import com.shopzone.model.Cart;
import com.shopzone.model.Product;
import com.shopzone.repository.CartRepository;
import com.shopzone.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public List<Cart> getCart(String sessionId) {
        return cartRepository.findBySessionId(sessionId);
    }

    public void addToCart(String sessionId, Integer productId) {
        Optional<Cart> existing = cartRepository.findBySessionIdAndProduct_Id(sessionId, productId);
        if (existing.isPresent()) {
            Cart cart = existing.get();
            cart.setQuantity(cart.getQuantity() + 1);
            cartRepository.save(cart);
        } else {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
            Cart cart = new Cart();
            cart.setSessionId(sessionId);
            cart.setProduct(product);
            cart.setQuantity(1);
            cartRepository.save(cart);
        }
    }

    public void updateQuantity(String sessionId, Integer productId, int quantity) {
        cartRepository.findBySessionIdAndProduct_Id(sessionId, productId).ifPresent(cart -> {
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        });
    }

    public void removeItem(String sessionId, Integer productId) {
        cartRepository.findBySessionIdAndProduct_Id(sessionId, productId)
            .ifPresent(cartRepository::delete);
    }

    @Transactional
    public void clearCart(String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
    }
}
