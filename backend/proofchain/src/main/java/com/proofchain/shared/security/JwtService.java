package com.proofchain.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço responsável por todas as operações envolvendo JSON Web Token (JWT).
 *
 * Responsabilidades:
 *
 * - geração de Access Token
 * - leitura de Claims
 * - validação
 * - controle de expiração
 *
 * Este serviço NÃO realiza autenticação de usuários.
 * Sua única responsabilidade é manipular tokens JWT.
 */
@Service
public class JwtService {

    /*
     * =========================================================
     * Configurações
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
     * Claims
     * =========================================================
     */

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_INSTITUTION_ID = "institutionId";
    private static final String CLAIM_ROLE = "role";

    /*
     * =========================================================
     * Core
     * =========================================================
     */

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
    /*
     * =========================================================
     * GERAÇÃO DO TOKEN
     * =========================================================
     */

    public String generateToken(
            Long userId,
            Long institutionId,
            String role,
            String username
    ){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(buildClaims(userId, institutionId, role, username))
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /*
     * =========================================================
     * BUILD DE CLAIMS
     * =========================================================
     */

    private Map<String, Object> buildClaims(
            Long userId,
            Long institutionId,
            String role,
            String username
    ){
        return Map.of(
                CLAIM_USER_ID, userId,
                CLAIM_INSTITUTION_ID, institutionId,
                CLAIM_ROLE, role,
                CLAIM_USERNAME, username
        );
    }

    /*
     * =========================================================
     * LEITURA DO TOKEN
     * =========================================================
     */

    /**
     * Realiza o parsing do JWT.
     *
     * Durante esse processo o JJWT:
     * - valida a assinatura;
     * - verifica a integridade do token;
     * - retorna o payload (Claims).
     */
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public

}