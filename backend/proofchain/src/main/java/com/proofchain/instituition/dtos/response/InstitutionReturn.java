package com.proofchain.instituition.dtos.response;

import com.proofchain.instituition.Institution;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record InstitutionReturn(
        Long idInstituition,
        String nameInstituition,
        String cnpj,
        String emailInstituition,
        String addressInstituition,
        int numberInstituition,
        String complementInstituition,
        String neighborhoodInstituition,
        String cityInstituition,

        @Size(min = 2, max = 2)
        String stateInstituition,

        @Pattern(regexp = "\\d{5}-\\d{3}",message = "O cep deve ser no fornato XXXXX-XXX")
        String postalCodeInstituition,

        @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX")
        String phoneInstituition
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