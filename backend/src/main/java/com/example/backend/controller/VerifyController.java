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
        String userEmail = (String) session.getAttribute("userEmail");
        Optional<User> user = userService.findByUsername(userEmail);
        boolean isVerified = userService.verifyCode(user.get(), code);
        if (isVerified) {
            session.removeAttribute("userEmail");
            session.removeAttribute("user");
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Invalid or expired code");
            return "verify";
        }
    }
    @PostMapping("/resend")
    public String resendVerification(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        log.info("resendVerification");
        log.info(user.toString());
        log.info(user.isVerifiedEmail()?"Verified":"Not Verified");
        if (user != null && !user.isVerifiedEmail()) {
            userService.generateVerificationCode(user);
            redirectAttributes.addFlashAttribute("message", "A new verification code has been sent to your email.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Unable to resend the verification code.");
        }

        return "redirect:/verify";
    }

}
