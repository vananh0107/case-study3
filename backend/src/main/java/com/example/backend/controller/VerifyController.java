package com.example.backend.controller;
import com.example.backend.pojo.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showVerifyPage() {
        return "verify";
    }

    @PostMapping
    public String verifyCode(@RequestParam("code") String code,  Model model,HttpSession session) {
        String userEmail = (String) session.getAttribute("username");
        Optional<User> user = userService.findByUsername(userEmail);
        boolean isVerified = userService.verifyCode(user.get(), code);
        if (isVerified) {
            session.removeAttribute("username");
            session.removeAttribute("user");
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Mã bạn vừa điền không đúng");
            return "verify";
        }
    }
    @PostMapping("/resend")
    public String resendVerification(HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        Optional<User> user = userService.findByUsername(username);
        if (user != null && !user.get().isVerifiedEmail()) {
            userService.generateVerificationCode(user.get());
            redirectAttributes.addFlashAttribute("message", "Mã xác minh mới đã được chuyển đến mail của bạn.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể gửi mã xác nhận đến mail của bạn.");
        }
        return "redirect:/verify";
    }

}
