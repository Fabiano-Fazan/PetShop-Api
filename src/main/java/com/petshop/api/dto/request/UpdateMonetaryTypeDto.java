package com.petshop.api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMonetaryTypeDto {

    @Size(min =3, max = 50, message = "The name must be between 3 and 50 characters")
    private String name;

    private String description;
}
