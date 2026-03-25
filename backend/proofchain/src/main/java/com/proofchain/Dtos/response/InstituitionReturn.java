package com.proofchain.Dtos.response;

import com.proofchain.identities.Instituition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;


public record InstituitionReturn(
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
        public InstituitionReturn(Instituition instituition){
                this(instituition.getId(),
                        instituition.getName(),
                        instituition.getCnpj(),
                        instituition.getEmail(),
                        instituition.getAddress(),
                        instituition.getNumber(),
                        instituition.getComplement(),
                        instituition.getNeighborhood(),
                        instituition.getCity(),
                        instituition.getState(),
                        instituition.getPostalCode(),
                        instituition.getPhone()
                );
        }
}