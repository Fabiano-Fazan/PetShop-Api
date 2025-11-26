package com.petshop.api.domain.Address;

import com.petshop.api.dto.request.UpdateClientDto;
import com.petshop.api.model.entities.Address;
import com.petshop.api.model.entities.Client;
import org.springframework.stereotype.Component;

@Component
public class AddressUpdater {

    public void updateAddress(UpdateClientDto updateClientDTO, Client client) {
        if (updateClientDTO.getAddress() != null) {
            if (client.getAddress() == null) {
                client.setAddress(new Address());
            }
        }
    }
}
