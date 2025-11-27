package com.petshop.api.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClientDto {

    @Size(min = 1, message = "Name cannot be empty")
    private String name;

    @Pattern(
            regexp = "^\\(?\\d{2}\\)?[\\s-]?9?\\d{4}-?\\d{4}$",
            message = "Phone number must be in the format (XX) XXXXX-XXXX or (XX) XXXX-XXXX"
    )
    private String phone;

    @Valid
    private UpdateAddressDto address;

}
