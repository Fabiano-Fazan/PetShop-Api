package com.petshop.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMonetaryType {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
