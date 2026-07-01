package com.proofchain.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * JwtService (ProofChain)

 * Responsável por:
 * - Gerar JWT (access token)
 * - Extrair claims
 * - Validar token

 * NÃO é responsável por:
 * - Autenticação de usuário
 * - Regras de negócio
 * - Acesso a banco de dados
 */
@Service
public class JwtService {

    /*
     * =========================================================
     * CONFIGURAÇÕES
     * =========================================================
     */

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration}")
    private Long expiration;

    @Value("${security.jwt.refresh-expiration}")
    private Long refreshExpiration;

    /*
     * =========================================================
     * CLAIMS CUSTOMIZADOS (MULTI-TENANT)
     * =========================================================
     */

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_INSTITUTION_ID = "institutionId";
    private static final String CLAIM_ROLE = "role";

    /*
     * =========================================================
     * SIGNING KEY
     * =========================================================
     */

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
     * =========================================================
     * GERAÇÃO DE TOKEN
     * =========================================================
     */

    public String generateToken(UserDetailsImpl user) {

        Instant now = Instant.now();
        Instant expirationTime = now.plusMillis(expiration);

        return Jwts.builder()
                .claims(buildClaims(user))
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    private Map<String, Object> buildClaims(UserDetailsImpl user) {
        return Map.of(
                CLAIM_USER_ID, user.getId(),
                CLAIM_INSTITUTION_ID, user.getInstitutionId(),
                CLAIM_ROLE, user.getRole()
        );
    }

    /*
     * =========================================================
     * EXTRAÇÃO DE CLAIMS
     * =========================================================
     */

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get(CLAIM_USER_ID, Long.class);
    }

    public Long extractInstitutionId(String token) {
        return extractAllClaims(token).get(CLAIM_INSTITUTION_ID, Long.class);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get(CLAIM_ROLE, String.class);
    }

    public Claims extractClaims(String token) {
        return extractAllClaims(token);
    }

    /*
     * =========================================================
     * VALIDAÇÃO
     * =========================================================
     */

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }
}