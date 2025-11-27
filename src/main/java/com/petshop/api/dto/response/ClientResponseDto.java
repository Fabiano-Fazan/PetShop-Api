package com.petshop.api.dto.response;


import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponseDto {

    private UUID id;
    private String name;
    private String phone;
    private String cpf;
    private AddressResponseDto address;
    private List<AnimalResponseDto> animals;
}
