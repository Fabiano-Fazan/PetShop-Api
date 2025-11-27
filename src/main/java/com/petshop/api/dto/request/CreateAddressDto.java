package com.petshop.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAddressDto {
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "ZipCode is required")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "ZipCode must follow the format XXXXX-XXX")
    private String zipCode;

    private String complement;
}

