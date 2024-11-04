package com.example.backend.security.controller;


import com.example.backend.pojo.User;
import com.example.backend.security.dto.UserLoginDTO;
import com.example.backend.security.dto.UserRegisterDTO;
import com.example.backend.security.jwt.JwtResponse;
import com.example.backend.security.jwt.JwtService;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.findByUsername(userLoginDTO.getUsername()).orElseThrow(() -> new Exception("User not found"));
            return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), currentUser.getFullName()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sai email hoặc mật khẩu!");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        try {
            if(userService.findByUsername(userRegisterDTO.getUsername()).isPresent()){
                throw new Exception("Đã tồn tại người dùng, vui lòng chọn tên đăng nhập khác");
            }

            String password = userRegisterDTO.getPassword();
            userService.save(userRegisterDTO);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRegisterDTO.getUsername(),password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateTokenLogin(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.findByUsername(userRegisterDTO.getUsername()).get();
            return ResponseEntity.ok("Create succesfully");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}

