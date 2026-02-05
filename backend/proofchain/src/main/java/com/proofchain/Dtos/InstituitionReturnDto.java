package com.proofchain.Dtos;

import com.proofchain.identities.Instituition;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InstituitionReturnDto {
        UUID idInstituition;
        Long userInstituition;
        String nameInstituition;
        String cnpj;
        String emailInstituition;
        String addressInstituition;
        int numberInstituition;
        String complementInstituition;
        String neighborhoodInstituition;
        String cityInstituition;

        @Size(min = 2, max = 2)
        String stateInstituition;

        @Pattern(regexp = "\\d{5}-\\d{3}",message = "O cep deve ser no fornato XXXXX-XXX")
        String postalCodeInstituition;

        @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX")
        String phoneInstituition;

        public InstituitionReturnDto(Instituition instituition) {
        }


}
