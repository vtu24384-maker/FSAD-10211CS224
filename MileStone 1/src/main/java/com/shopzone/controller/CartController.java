package com.shopzone.controller;

import com.shopzone.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.shopzone.model.Cart;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private String getSessionId(HttpSession session) {
        String sid = (String) session.getAttribute("cartSessionId");
        if (sid == null) {
            sid = java.util.UUID.randomUUID().toString().replace("-", "");
            session.setAttribute("cartSessionId", sid);
        }
        return sid;
    }

    @GetMapping("/cart")
    public String cartPage(Model model, HttpSession session) {
        String sid = getSessionId(session);
        List<Cart> cartItems = cartService.getCart(sid);
        double total = cartItems.stream()
            .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
            .sum();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", total);
        model.addAttribute("userName", session.getAttribute("userName"));
        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam(name = "productId") Integer productId, HttpSession session) {
        cartService.addToCart(getSessionId(session), productId);
        return "redirect:/";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam(name = "productId") Integer productId,
                             @RequestParam(name = "quantity") int quantity,
                             HttpSession session) {
        if (quantity < 1) {
            cartService.removeItem(getSessionId(session), productId);
        } else {
            cartService.updateQuantity(getSessionId(session), productId, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam(name = "productId") Integer productId, HttpSession session) {
        cartService.removeItem(getSessionId(session), productId);
        return "redirect:/cart";
    }
}
