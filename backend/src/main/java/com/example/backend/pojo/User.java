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

    @Column(nullable = false)
    private String fullName;

    @Email
    @Column(unique = true, nullable = false)
    private String username;

    @Size(min = 6)
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Role is required")
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private boolean verifiedEmail = false;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Verify verify;
}