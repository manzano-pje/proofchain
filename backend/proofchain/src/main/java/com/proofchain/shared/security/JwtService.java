package com.proofchain.shared.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * JwtService
 *
 * Função no sistema:
 * Responsável por centralizar a geração, validação e extração de informações de tokens JWT
 * utilizados no fluxo de autenticação do sistema ProofChain.
 *
 * Estrutura atual:
 * Service de segurança baseado na biblioteca jjwt.
 * Suporta claims customizados para arquitetura multi-tenant.
 * Atua como componente central de criptografia e leitura de tokens.
 *
 * Fluxo:
 * 1. Usuário é autenticado via AuthService
 * 2. JwtService gera token contendo claims do usuário
 * 3. Token é assinado com chave secreta
 * 4. Em requisições futuras, token é validado via JwtAuthenticationFilter
 * 5. Claims são extraídas para contexto de segurança e autorização
 *
 * Integração no sistema:
 * Utilizado por AuthService (geração) e JwtAuthenticationFilter (validação e leitura).
 */
@Service
public class JwtService {

    public void JwtService() {
        System.out.println("\n\n=========================================================");
        System.out.println("JWT SECRET = [" + secret + "]");
        System.out.println("=========================================================\n\n");
    }

    /*
     * =========================================================
     * CONFIGURAÇÕES
     * =========================================================
     */

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.access-token.expiration}")
    private Long expiration;

    @Value("${security.jwt-token.refresh-expiration}")
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

    /**
     * Nota de decisão:
     * A chave é derivada de Base64 para suportar compatibilidade com secrets gerados externamente
     * (ex: environment variables / vault / CI/CD pipelines).
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode("6Uvp/nAn/1VLi0E0gflxhYpSEBmPOjxOlalD5fRdO+E=");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /*
     * =========================================================
     * GERAÇÃO DE TOKEN
     * =========================================================
     */

    /**
     * Gera um JWT assinado contendo informações do usuário autenticado.
     *
     * @param user usuário autenticado contendo dados necessários para claims
     * @return token JWT assinado
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

    /**
     * Constrói claims customizados do token JWT.
     *
     * @param user usuário autenticado
     * @return mapa de claims
     */
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

    /**
     * Extrai todos os claims do token JWT.
     *
     * @param token token JWT
     * @return claims do token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extrai o username (subject) do token.
     *
     * @param token JWT
     * @return username
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrai o ID do usuário do token.
     *
     * @param token JWT
     * @return userId
     */
    public Long extractUserId(String token) {
        return extractAllClaims(token).get(CLAIM_USER_ID, Long.class);
    }

    /**
     * Extrai o ID da instituição do token (multi-tenant).
     *
     * @param token JWT
     * @return institutionId
     */
    public Long extractInstitutionId(String token) {
        return extractAllClaims(token).get(CLAIM_INSTITUTION_ID, Long.class);
    }

    /**
     * Extrai o papel (role) do usuário no sistema.
     *
     * @param token JWT
     * @return role do usuário
     */
    public String extractRole(String token) {
        return extractAllClaims(token).get(CLAIM_ROLE, String.class);
    }

    /**
     * Retorna todos os claims do token.
     *
     * @param token JWT
     * @return claims completos
     */
    public Claims extractClaims(String token) {
        return extractAllClaims(token);
    }

    /*
     * =========================================================
     * VALIDAÇÃO
     * =========================================================
     */

    /**
     * Valida se o token é válido para o usuário informado.
     *
     * @param token JWT
     * @param userDetails usuário autenticado
     * @return true se válido, false caso contrário
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Verifica se o token expirou.
     *
     * @param token JWT
     * @return true se expirado
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    @PostConstruct
    public void testSecret() {
        System.out.println("SECRET LENGTH: " + secret.length());
        System.out.println("SECRET VALUE: " + secret);
    }
}