package com.proofchain.auth.dtos;

import java.util.List;

public record UserSummaryDto(
        Long id,
        String name,
        String email,
        String role,
        Long tenantId
) {
}
