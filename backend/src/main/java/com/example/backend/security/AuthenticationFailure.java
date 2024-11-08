package com.example.backend.security;

import com.example.backend.pojo.User;
import com.example.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String redirectUrl;
        HttpSession session = request.getSession();

        if (exception.getMessage() != null && exception.getMessage().contains("Account is not verify")) {
            String username = request.getParameter("username");
            Optional<User> user = userService.findByUsername(username);
            if (user != null && !user.get().isVerifiedEmail()) {
                session.setAttribute("user", user);
            }
            redirectUrl = "/verify";
        } else {
            redirectUrl = "/login?error=true";
        }
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}