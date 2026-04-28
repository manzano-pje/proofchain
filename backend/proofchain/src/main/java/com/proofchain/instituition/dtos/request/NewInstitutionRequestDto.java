package com.proofchain.instituition.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewInstitutionRequestDto {

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
    private int idPlan;
}
