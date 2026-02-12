package com.proofchain.Dtos;

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
public class NewInstituitionRequestDto {

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
}
