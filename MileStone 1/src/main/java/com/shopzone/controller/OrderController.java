package com.shopzone.controller;

import com.shopzone.model.Cart;
import com.shopzone.model.Order;
import com.shopzone.model.User;
import com.shopzone.repository.UserRepository;
import com.shopzone.service.CartService;
import com.shopzone.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserRepository userRepository;

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login?redirect=/checkout";

        String sid = (String) session.getAttribute("cartSessionId");
        List<Cart> cartItems = sid != null ? cartService.getCart(sid) : Collections.emptyList();
        if (cartItems.isEmpty()) return "redirect:/cart";

        double total = cartItems.stream()
            .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
            .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", total);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "checkout";
    }

    @PostMapping("/order/place")
    public String placeOrder(@RequestParam(name = "paymentMethod") String paymentMethod,
                             HttpSession session,
                             RedirectAttributes ra) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login?redirect=/checkout";

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "redirect:/login";

        String sid = (String) session.getAttribute("cartSessionId");
        try {
            Order order = orderService.placeOrder(user, sid, paymentMethod);
            return "redirect:/receipt/" + order.getId();
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/receipt/{orderId}")
    public String receiptPage(@PathVariable(name = "orderId") Integer orderId,
                              HttpSession session,
                              Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Order order = orderService.getOrder(orderId, userId);
        String formattedId = String.format("%06d", order.getId());
        String formattedDate = order.getCreatedAt()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));

        model.addAttribute("order", order);
        model.addAttribute("orderId", formattedId);
        model.addAttribute("orderDate", formattedDate);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "receipt";
    }
}
