package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateClientDto;
import com.petshop.api.dto.update.UpdateClientDto;
import com.petshop.api.dto.response.ClientResponseDto;
import com.petshop.api.dto.update.UpdateAddressDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.CpfAlreadyExistsException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Address;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.mapper.AddressMapper;
import com.petshop.api.model.mapper.ClientMapper;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.MedicalAppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MedicalAppointmentRepository medicalAppointmentRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private ValidatorEntities validatorEntities;


    @Test
    @DisplayName("Should return a page of clients")
    void getAllClients_ShouldReturnPageOfClients() {

        Pageable pageable = PageRequest.of(0, 10);
        Client client = new Client();
        ClientResponseDto responseDto = new ClientResponseDto();
        Page<Client> clientPage = new PageImpl<>(List.of(client));

        when(clientRepository.findAll(pageable)).thenReturn(clientPage);
        when(clientMapper.toResponseDto(client)).thenReturn(responseDto);

        Page<ClientResponseDto> result = clientService.getAllClients(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(clientRepository).findAll(pageable);
    }


    @Test
    @DisplayName("Should return client when ID exists")
    void getClientById_ShouldReturnClient_WhenIdExists() {

        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);
        ClientResponseDto responseDto = new ClientResponseDto();

        when(validatorEntities.validate(id, clientRepository, "Client")).thenReturn(client);
        when(clientMapper.toResponseDto(client)).thenReturn(responseDto);

        ClientResponseDto result = clientService.getClientById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, clientRepository, "Client");
    }


    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID not exists")
    void getClientById_ShouldThrowException_WhenIdDoesNotExists(){

        UUID id = UUID.randomUUID();
        String expectedMessage = "Client not found";

        when(validatorEntities.validate(id, clientRepository,"Client"))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        assertThatThrownBy(() -> clientService.getClientById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(validatorEntities).validate(id,clientRepository,"Client");
        verifyNoInteractions(clientMapper);

    }


    @Test
    @DisplayName("Should return page of clients when searching by name")
    void getClientByName_ShouldReturnPage_WhenNameExists() {

        String name = "Maria";
        Pageable pageable = PageRequest.of(0, 10);
        Client client = new Client();
        ClientResponseDto responseDto = new ClientResponseDto();
        Page<Client> clientPage = new PageImpl<>(List.of(client));

        when(clientRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(clientPage);
        when(clientMapper.toResponseDto(client)).thenReturn(responseDto);

        Page<ClientResponseDto> result = clientService.getClientByNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should create client successfully")
    void createClient_ShouldReturnDto_WhenSuccessful() {

        CreateClientDto createDto = new CreateClientDto();
        createDto.setCpf("12345678900");
        Client client = new Client();
        Client savedClient = new Client();
        savedClient.setId(UUID.randomUUID());
        ClientResponseDto responseDto = new ClientResponseDto();

        when(clientRepository.existsByCpf(createDto.getCpf())).thenReturn(false);
        when(clientMapper.toEntity(createDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(savedClient);
        when(clientMapper.toResponseDto(savedClient)).thenReturn(responseDto);

        ClientResponseDto result = clientService.createClient(createDto);

        assertThat(result).isNotNull();
        verify(clientRepository).save(client);
    }

    @Test
    @DisplayName("Should throw exception when CPF already exists")
    void createClient_ShouldThrowException_WhenCpfExists() {

        CreateClientDto createDto = new CreateClientDto();
        createDto.setCpf("12345678900");

        when(clientRepository.existsByCpf(createDto.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> clientService.createClient(createDto))
                .isInstanceOf(CpfAlreadyExistsException.class)
                .hasMessage("This CPF already exists");

        verify(clientRepository, never()).save(any());
    }


    @Test
    @DisplayName("Should update client successfully")
    void updateClient_ShouldReturnUpdatedDto_WhenSuccessful() {

        UUID id = UUID.randomUUID();
        UpdateClientDto updateDto = new UpdateClientDto();
        UpdateAddressDto addressDto = new UpdateAddressDto();
        updateDto.setAddress(addressDto);
        Client client = new Client();
        client.setId(id);
        Address address = new Address();
        client.setAddress(address);
        Client savedClient = new Client();
        ClientResponseDto responseDto = new ClientResponseDto();

        when(validatorEntities.validate(id, clientRepository, "Client")).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(savedClient);
        when(clientMapper.toResponseDto(savedClient)).thenReturn(responseDto);

        ClientResponseDto result = clientService.updateClient(id, updateDto);

        assertThat(result).isNotNull();
        verify(clientMapper).updateClientFromDto(updateDto, client);
        verify(addressMapper).updateAddressFromDto(addressDto, address);
        verify(clientRepository).save(client);
    }


    @Test
    @DisplayName("Should delete client when medical appointments not exist")
    void deleteClient_ShouldDelete_WhenNoAppointments() {

        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);

        when(validatorEntities.validate(id, clientRepository, "Client")).thenReturn(client);
        when(medicalAppointmentRepository.existsByClient(client)).thenReturn(false);

        clientService.deleteClient(id);

        verify(clientRepository).delete(client);
    }

    @Test
    @DisplayName("Should throw BusinessException when trying to delete client with appointments")
    void deleteClient_ShouldThrowException_WhenClientHasAppointments() {

        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);

        when(validatorEntities.validate(id, clientRepository, "Client")).thenReturn(client);
        when(medicalAppointmentRepository.existsByClient(client)).thenReturn(true);

        assertThatThrownBy(() -> clientService.deleteClient(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot delete this client because it is being used by medical appointments");

        verify(clientRepository, never()).delete(any());
    }
}