package com.proofchain.Dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InstituitionRequestDto(
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
) {
}
