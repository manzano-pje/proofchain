package com.proofchain.institution.interfaces.dtos.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InstitutionRequest(
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
        String phone
) {
}
