package com.petshop.api.service;


import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateClientDto;
import com.petshop.api.dto.request.UpdateClientDto;
import com.petshop.api.dto.response.ClientResponseDto;
import com.petshop.api.exception.CpfAlreadyExistsException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.mapper.AddressMapper;
import com.petshop.api.model.mapper.ClientMapper;
import com.petshop.api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final AddressMapper addressMapper;
    private final ValidatorEntities validatorEntities;


    public Page<ClientResponseDto> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(clientMapper::toResponseDto);
    }

    public ClientResponseDto getClientById(UUID id) {
        return clientRepository.findById(id)
                .map(clientMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }

    public Page<ClientResponseDto> getClientByNameContainingIgnoreCase(String name, Pageable pageable) {
        return clientRepository.findByNameContainingIgnoreCase(name,pageable)
                .map(clientMapper::toResponseDto);
    }

    @Transactional
    public ClientResponseDto createClient(CreateClientDto createClientDTO) {
        if (clientRepository.existsByCpf(createClientDTO.getCpf())){
            throw new CpfAlreadyExistsException("This CPF already exists");
        }
        Client client = clientMapper.toEntity(createClientDTO);
        return clientMapper.toResponseDto(clientRepository.save(client));
    }

    @Transactional
    public ClientResponseDto updateClient(UUID id, UpdateClientDto clientDTO) {
        Client client = validatorEntities.validateClient(id);
        clientMapper.updateClientFromDTO(clientDTO, client) ;
        addressMapper.updateAddressFromDTO(clientDTO.getAddress(), client.getAddress());
        return clientMapper.toResponseDto(clientRepository.save(client));
    }

    @Transactional
    public void deleteClient(UUID id) {
        if (!clientRepository.existsById(id)){
            throw new ResourceNotFoundException("Client not found with ID: " + id);
        }
        clientRepository.deleteById(id);
    }
}
