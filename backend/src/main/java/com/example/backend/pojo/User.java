package com.example.backend.pojo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    private String role;

    private boolean verifiedEmail = false;

}