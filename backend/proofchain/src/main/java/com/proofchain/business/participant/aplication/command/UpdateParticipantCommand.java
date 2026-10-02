package com.proofchain.business.participant.aplication.command;

import com.proofchain.business.participant.interfaces.dto.request.ParticipantRequest;
import com.proofchain.business.participant.interfaces.dto.request.ParticipantUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateParticipantCommand {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^[1-9]{2}(9\\d{8}|[2-5]\\d{7})$")
    private String phone;

    @NotBlank
    @CPF
    private String cpf;

    @NotBlank
    private String address;

    private Long number;
    private String complement;

    @NotBlank
    private String neighborhood;

    @NotBlank
    private String city;

    @NotBlank
    @Size(min = 2, max = 2)
    private String state;

    @NotBlank
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O cep deve ser no fornato XXXXX-XXX")
    private String postalCode;

    private boolean isActive;

    public UpdateParticipantCommand(Long id, ParticipantUpdate dto) {
        this.id = id;
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.phone = dto.getPhone();
        this.cpf = dto.getCpf();
        this.address = dto.getAddress();
        this.number = dto.getNumber();
        this.complement = dto.getComplement();
        this.neighborhood = dto.getNeighborhood();
        this.city = dto.getCity();
        this.state = dto.getState();
        this.postalCode = dto.getPostalCode();
        this.isActive = dto.isActive();
    }
}
