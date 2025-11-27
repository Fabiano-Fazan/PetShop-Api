package com.petshop.api.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(nullable = false, unique = true)
    private String cpf;


    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "street")),
        @AttributeOverride(name = "city", column = @Column(name = "city")),
        @AttributeOverride(name = "state", column = @Column(name = "state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "zip_code")),
        @AttributeOverride(name = "complement", column = @Column(name = "complement"))
    })

    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals;

    @OneToMany(mappedBy = "client")
    private List<Sale> sales;

}
