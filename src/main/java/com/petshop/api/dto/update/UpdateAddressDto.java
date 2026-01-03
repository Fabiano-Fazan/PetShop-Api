package com.petshop.api.dto.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressDto {

    @Size(min = 1, message = "Street cannot be empty")
    private String street;

    @Size(min = 1, message = "City cannot be empty")
    private String city;

    @Size(min = 2, max = 2, message = "State must be 2 characters")
    private String state;

    @Pattern(regexp = "^[0-9]{5}-?[0-9]{3}$",
            message = "ZIP code must be in the format XXXXX-XXX or XXXXXXXX")
    private String zipCode;

    private String complement;

}

