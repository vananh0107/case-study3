package com.example.backend.controller;

import com.example.backend.dto.UserLoginDTO;
import com.example.backend.dto.UserRegisterDTO;
import com.example.backend.pojo.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRegisterDTO userDTO, BindingResult result, Model model, HttpSession session) {
        if (userService.usernameExists(userDTO.getUsername())) {
            result.rejectValue("username", "error.user", "Tên đăng nhập đã tồn tại");
        }
        if (result.hasErrors()) {
            return "register";
        }

        model.addAttribute("message", "Registration successful! Please check your email for a verification code.");
        userService.save(userDTO);
        session.setAttribute("userEmail", userDTO.getUsername());
        return "redirect:/verify";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserLoginDTO());
        return "login";
    }

}
