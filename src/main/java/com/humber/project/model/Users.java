package com.humber.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Users {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;
    private String password;
    private String role;
}

