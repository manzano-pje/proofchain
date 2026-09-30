package com.proofchain.business.participant.interfaces.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public record ParticipantResponse(
        Long id,
        String name,
        String email,
        String phone,
        String cpf,
        String address,
        Long number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String postalCode,
        Instant createdAt,
        Instant deletedAt,
        boolean isActive
) {
    public ParticipantResponse(ParticipantResponse participant) {
        this.id    = participant.id();
        this.name  = participant.name();
        this.email = participant.email();
        this.phone = participant.phone();
        this.cpf   = participant.cpf();
        this.address = participant.address();
        this.number = participant.number();
        this.complement = participant.complement();
        this.neighborhood = participant.neighborhood();
        this.city = participant.city();
        this.state = participant.state();
        this.postalCode = participant.postalCode();
        this.createdAt = participant.createdAt();
        this.deletedAt = participant.deletedAt();
        this.isActive = participant.isActive();

    }
}
