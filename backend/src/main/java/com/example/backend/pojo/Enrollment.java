package com.example.backend.pojo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User student;

    @ManyToOne
    private Course course;

    private boolean active = true;

    @Column(nullable = false)
    private LocalDate registrationDate= LocalDate.now();;
}