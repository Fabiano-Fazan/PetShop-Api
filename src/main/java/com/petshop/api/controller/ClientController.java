package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateClientDto;
import com.petshop.api.dto.request.UpdateClientDto;
import com.petshop.api.dto.response.ClientResponseDto;
import com.petshop.api.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientResponseDto>> getAllClients(Pageable pageable) {
        Page<ClientResponseDto> allClients = clientService.getAllClients(pageable);
        return ResponseEntity.ok(allClients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(@PathVariable UUID id) {
        ClientResponseDto clientById = clientService.getClientById(id);
        return ResponseEntity.ok(clientById);
    }

    @GetMapping("/name")
    public ResponseEntity<Page<ClientResponseDto>> getClientByName(@RequestParam String name, Pageable pageable) {
        Page<ClientResponseDto> clientByName = clientService.getClientByNameContainingIgnoreCase(name,pageable);
        return ResponseEntity.ok(clientByName);
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(@Valid @RequestBody CreateClientDto clientDTO) {
        ClientResponseDto createdClient = clientService.createClient(clientDTO);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable UUID id, @Valid @RequestBody UpdateClientDto updateClientDTO) {
        ClientResponseDto updatedClient = clientService.updateClient(id, updateClientDTO);
        return ResponseEntity.ok(updatedClient);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
