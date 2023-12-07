package com.humber.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;  // Changed from int to Long for consistency with JPA standards
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name="price")
    private double price;
    @Column(name = "image_url")
    private String imageUrl; // Ensure your database has this field
}
