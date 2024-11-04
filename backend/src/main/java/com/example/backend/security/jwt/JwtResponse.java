package com.example.backend.security.jwt;

import lombok.Data;

@Data
public class JwtResponse {
    private Integer id;
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private String name;


    public JwtResponse(String accessToken, Integer id, String username, String name) {
        this.accessToken = accessToken;
        this.username = username;
        this.name = name;
        this.id = id;
    }
}
