package com.proofchain.business.participant.interfaces.dto.response;

public record ParticipantSumaryResponse(
        Long id,
        String name,
        String email,
        String phone,
        boolean isActive
) {
}
