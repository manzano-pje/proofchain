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
        this(participant.id(),
                participant.name(),
                participant.email(),
                participant.phone(),
                participant.cpf(),
                participant.address(),
                participant.number(),
                participant.complement(),
                participant.neighborhood(),
                participant.city(),
                participant.state(),
                participant.postalCode(),
                participant.createdAt(),
                participant.deletedAt(),
                participant.isActive()
        );


    }
}
