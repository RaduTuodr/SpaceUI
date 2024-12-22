package com.example.spaceui.model;

import jakarta.persistence.*;

@Entity
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
