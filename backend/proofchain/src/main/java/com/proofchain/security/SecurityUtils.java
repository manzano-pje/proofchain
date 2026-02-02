package com.proofchain.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    // Retorna o ID da instituição do usuário logado
    public static UUID getInstitutionId() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        Claims claims = (Claims) auth.getDetails();

        return UUID.fromString(
                claims.get("institution_id", String.class)
        );
    }

    // Retorna o ID do usuário logado
    public static UUID getUserId() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        return UUID.fromString(auth.getName());
    }
}
