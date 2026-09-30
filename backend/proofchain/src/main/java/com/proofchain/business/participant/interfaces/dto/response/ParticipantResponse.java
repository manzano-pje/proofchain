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
        Instant DeletedAt,
        boolean isActive
) {
}
