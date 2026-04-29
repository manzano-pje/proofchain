package com.proofchain.institution.dtos.response;

import com.proofchain.institution.Institution;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InstitutionReturn(
        Long idInstitution,
        String nameInstitution,
        String cnpj,
        String emailInstitution,
        String addressInstitution,
        int numberInstitution,
        String complementInstitution,
        String neighborhoodInstitution,
        String cityInstitution,

        @Size(min = 2, max = 2)
        String stateInstitution,

        @Pattern(regexp = "\\d{5}-\\d{3}",message = "O cep deve ser no fornato XXXXX-XXX")
        String postalCodeInstitution,

        @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX")
        String phoneInstitution
   ){
        public InstitutionReturn(Institution institution){
                this(institution.getId(),
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
                        institution.getPhone()
                );
        }
}