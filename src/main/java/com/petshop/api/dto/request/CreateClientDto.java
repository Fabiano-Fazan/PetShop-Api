package com.petshop.api.dto.request;



import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class CreateClientDto {

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(
            regexp = "^\\(?\\d{2}\\)?[\\s-]?9?\\d{4}-?\\d{4}$",
            message = "Phone number must be in the format (XX) XXXXX-XXXX or (XX) XXXX-XXXX"
    )
    private String phone;

    @CPF(message = "CPF ir invalid")
    @NotBlank(message = "CPF is required")
    private String cpf;


    @Valid
    @NotNull(message = "Address is required")
     private CreateAddressDto address;
}
