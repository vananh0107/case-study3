package com.example.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String redirectUrl;

        if (exception.getMessage() != null && exception.getMessage().contains("User account is not active")) {
            redirectUrl = "/non-active";
        } else {
            redirectUrl = "/login?error=true";
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}