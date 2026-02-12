package com.proofchain.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static java.lang.String.valueOf;

public class SecurityUtils {

    // Retorna o ID da instituição do usuário logado
    public static Long getInstitutionId() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        Claims claims = (Claims) auth.getDetails();

        return Long.valueOf(
                claims.get("institution_id", String.class)
        );
    }

    // Retorna o ID do usuário logado
    public static Long getUserId() {
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        return Long.valueOf(auth.getName());
    }

}
