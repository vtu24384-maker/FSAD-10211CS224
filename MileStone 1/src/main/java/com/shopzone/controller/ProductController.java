package com.shopzone.controller;

import com.shopzone.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("userName", session.getAttribute("userName"));
        return "index";
    }
}
