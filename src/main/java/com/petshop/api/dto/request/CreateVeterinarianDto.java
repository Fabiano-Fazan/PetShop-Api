package com.petshop.api.dto.request;

import com.petshop.api.model.entities.VeterinarianCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVeterinarianDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "CRMv is required")
    private String crmv;

    @Pattern(
            regexp = "^\\(?\\d{2}\\)?[\\s-]?9?\\d{4}-?\\d{4}$",
            message = "Phone number must be in the format (XX) XXXXX-XXXX or (XX) XXXX-XXXX"
    )
    private String phone;

    @NotNull(message = "Veterinarian Category is required")
    private VeterinarianCategory category;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

}
