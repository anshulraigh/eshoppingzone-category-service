package com.eshoppingzone.categoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data  // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;
}
