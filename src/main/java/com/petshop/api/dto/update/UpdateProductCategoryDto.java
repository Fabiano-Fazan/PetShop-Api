package com.petshop.api.dto.update;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductCategoryDto {

    @Size(min =3, max = 50, message = "The name must be between 3 and 50 characters")
    private String name;

    private String description;

}
