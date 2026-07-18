package com.proofchain.admin.institution.interfaces.dtos.response;

import com.proofchain.admin.institution.domain.model.Institution;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record InstitutionResponse(
    Long id,
    String name,
    String cnpj,
    String email,
    String address,
    Integer number,
    String complement,
    String neighborhood,
    String city,
    @Size(min = 2, max = 2)
    String state,
    @Pattern(regexp = "\\d{5}-\\d{3}",message = "O cep deve ser no fornato XXXXX-XXX")
    String postalCode,
    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX")
    String phone,
    Instant createdAt,
    Instant DeletedAt,
    Boolean active
   ){
    public static InstitutionResponse from(Institution institution){
           return new InstitutionResponse(
                    institution.getId(),
                    institution.getName(),
                    institution.getCnpj(),
                    institution.getEmail(),
                    institution.getAddress(),
                    institution.getNumber(),
                    institution.getComplement(),
                    institution.getNeighborhood(),
                    institution.getCity(),
                    institution.getState(),
                    institution.getPostalCode(),
                    institution.getPhone(),
                    institution.getCreatedAt(),
                    institution.getDeletedAt(),
                    institution.getActive()
            );
    }
}