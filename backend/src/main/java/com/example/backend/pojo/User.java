package com.example.backend.pojo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

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

    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;
    private boolean verifiedEmail = false;
}