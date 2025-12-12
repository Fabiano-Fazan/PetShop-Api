package com.petshop.api.model.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Veterinarian {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String crmv;

    @Column(nullable = false)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private VeterinarianCategory category;

    @Column(nullable = false, unique = true)
    private String email;

}