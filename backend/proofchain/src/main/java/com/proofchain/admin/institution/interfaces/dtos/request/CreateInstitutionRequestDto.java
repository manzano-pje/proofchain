package com.proofchain.admin.institution.interfaces.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateInstitutionRequestDto {

    @Size(min = 5, max = 100)
    private String name;
    @CNPJ
    private String cnpj;
    @Size(min = 5, max = 30)
    private String userName;
    @Email
    private String email;
    @Size(min=8)
    private String password;
    @NonNull
    private Long idPlan;
}
