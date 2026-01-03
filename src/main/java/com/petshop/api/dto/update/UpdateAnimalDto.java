package com.petshop.api.dto.update;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateAnimalDto {
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
}
