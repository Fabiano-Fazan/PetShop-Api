package com.petshop.api.domain.validator;

import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.*;
import com.petshop.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidatorEntities {
    private final ClientRepository clientRepository;
    private final AnimalRepository animalRepository;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final SaleRepository saleRepository;
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final VeterinarianRepository veterinarianRepository;
    private final FinancialRepository financialRepository;
    private final VeterinarianCategoryRepository veterinarianCategoryRepository;
    private final MonetaryTypeRepository monetaryTypeRepository;
    private final FinancialPaymentRepository financialPaymentRepository;


    public Client validateClient (UUID id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    public Animal validateAnimal (UUID id){
        return animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found"));
    }

    public Product validateProduct (UUID id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public ProductCategory validateProductCategory (UUID id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public Sale validateSale (UUID id){
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
    }

    public MedicalAppointment validateMedicalAppointment (UUID id){
        return medicalAppointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical appointment not found"));
    }

    public Veterinarian validateVeterinarian (UUID id){
        return veterinarianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));
    }

    public VeterinarianCategory validateVeterinarianCategory (UUID id){
        return veterinarianCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian category not found"));
    }

    public Financial validateFinancial (UUID id){
        return financialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial not found"));
    }

    public FinancialPayment validateFinancialPayment (UUID id){
        return financialPaymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Financial payment not found"));
    }

    public MonetaryType validateMonetaryType (UUID id){
        return monetaryTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monetary type not found"));
    }
}
