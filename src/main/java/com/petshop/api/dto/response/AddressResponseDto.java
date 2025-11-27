package com.petshop.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDto {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String complement;
}
