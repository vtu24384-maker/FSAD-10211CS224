package com.shopzone.service;

import com.shopzone.model.*;
import com.shopzone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @Transactional
    public Order placeOrder(User user, String sessionId, String paymentMethod) {
        List<Cart> cartItems = cartRepository.findBySessionId(sessionId);
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty.");

        double total = cartItems.stream()
            .mapToDouble(c -> c.getProduct().getPrice() * c.getQuantity())
            .sum();

        Order order = new Order();
        order.setUser(user);
        order.setSessionId(sessionId);
        order.setTotal(total);
        order.setPaymentMethod(paymentMethod);

        List<OrderItem> items = cartItems.stream().map(c -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(c.getProduct());
            item.setName(c.getProduct().getName());
            item.setPrice(c.getProduct().getPrice());
            item.setQuantity(c.getQuantity());
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        Order saved = orderRepository.save(order);
        cartService.clearCart(sessionId);
        return saved;
    }

    public Order getOrder(Integer orderId, Integer userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new RuntimeException("Order not found."));
    }
}
