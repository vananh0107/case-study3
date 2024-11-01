package com.example.backend.pojo;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "entrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User student;

    @ManyToOne
    private Course course;

    private boolean active = true;

}