package com.shopzone.controller;

import com.shopzone.model.User;
import com.shopzone.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "redirect", required = false) String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @PostMapping("/auth/login")
    public String doLogin(@RequestParam(name = "email") String email,
                          @RequestParam(name = "password") String password,
                          @RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect,
                          HttpSession session,
                          RedirectAttributes ra) {
        return authService.login(email, password).map(user -> {
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            return "redirect:" + redirect;
        }).orElseGet(() -> {
            ra.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        });
    }

    @PostMapping("/auth/register")
    public String doRegister(@RequestParam(name = "name") String name,
                             @RequestParam(name = "email") String email,
                             @RequestParam(name = "password") String password,
                             @RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect,
                             HttpSession session,
                             RedirectAttributes ra) {
        try {
            User user = authService.register(name, email, password);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            return "redirect:" + redirect;
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
