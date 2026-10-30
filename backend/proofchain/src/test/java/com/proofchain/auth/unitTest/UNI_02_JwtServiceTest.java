
    /**
     * Testes unitários do serviço de emissão e validação de tokens JWT.
     *
     * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
     * segurança e à confiabilidade previstas na ISO/IEC 25010.
     *
     * Escopo:
     * Verificar a geração de access e refresh tokens, a extração de
     * claims, a validação de assinatura, a expiração e a diferenciação
     * entre os tipos de token.
     *
     * Estes testes não representam certificação ou conformidade integral
     * com as normas citadas.
     */

package com.proofchain.auth.unitTest;

import com.proofchain.shared.security.JwtService;
import com.proofchain.shared.security.UserDetailsImpl;
import com.proofchain.user.domain.model.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UNI_02_JwtServiceTest {

    private static final String SECRET =
            "chavedetestesuficientementelongaparaHS256xxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static final Long EXPIRATION = 900_000L;
    private static final Long REFRESH_EXPIRATION = 604_800_000L;

    private JwtService jwtService;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", REFRESH_EXPIRATION);

        userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getInstitutionId()).thenReturn(100L);
        when(userDetails.getRole()).thenReturn(UserRole.ADMIN.toString());
    }

    private JwtService buildServiceWithExpiration(long expiration, long refresh) {
        JwtService service = new JwtService();
        ReflectionTestUtils.setField(service, "secret", SECRET);
        ReflectionTestUtils.setField(service, "expiration", expiration);
        ReflectionTestUtils.setField(service, "refreshExpiration", refresh);
        return service;
    }

    @Test
    @DisplayName("UNI_02.01 - Deve gerar access token com claims esperadas")
    void shouldGenerateAccessTokenWithExpectedClaims() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("user@example.com", jwtService.extractUsername(token));
        assertEquals(100L, jwtService.extractInstitutionId(token));
    }

    @Test
    @DisplayName("UNI_02.02 - Deve gerar refresh token com claims esperadas")
    void shouldGenerateRefreshTokenWithExpectedClaims() {
        String refresh = jwtService.generateRefreshToken(userDetails);

        assertNotNull(refresh);
        assertEquals("user@example.com", jwtService.extractUsername(refresh));
    }

    @Test
    @DisplayName("UNI_02.03 - Deve extrair username do token")
    void shouldExtractUsernameFromToken() {
        String token = jwtService.generateToken(userDetails);
        assertEquals("user@example.com", jwtService.extractUsername(token));
    }

    @Test
    @DisplayName("UNI_02.04 - Deve extrair institutionId do token")
    void shouldExtractInstitutionIdFromToken() {
        String token = jwtService.generateToken(userDetails);
        assertEquals(100L, jwtService.extractInstitutionId(token));
    }

    @Test
    @DisplayName("UNI_02.05 - Deve validar token com sucesso")
    void shouldValidateTokenSuccessfully() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    @DisplayName("UNI_02.06 - Deve rejeitar token expirado")
    void shouldRejectExpiredToken() {
        JwtService expiredService = buildServiceWithExpiration(-1_000L, -1_000L);
        String expired = expiredService.generateToken(userDetails);

        assertThrows(ExpiredJwtException.class,
                () -> jwtService.extractUsername(expired));
    }

    @Test
    @DisplayName("UNI_02.07 - Deve rejeitar token malformado")
    void shouldRejectMalformedToken() {
        assertThrows(JwtException.class,
                () -> jwtService.extractUsername("token.malformado.aqui"));
    }

    @Test
    @DisplayName("UNI_02.08 - Deve rejeitar token com assinatura inválida")
    void shouldRejectTokenWithInvalidSignature() {
        JwtService other = new JwtService();
        ReflectionTestUtils.setField(other, "secret",
                "chavedetestesuficientementelongaparaHS256xxxxxxxxxxxxxxxxxxxxxxxxxxy");
        ReflectionTestUtils.setField(other, "expiration", EXPIRATION);
        ReflectionTestUtils.setField(other, "refreshExpiration", REFRESH_EXPIRATION);
        String forged = other.generateToken(userDetails);

        assertThrows(JwtException.class,
                () -> jwtService.extractUsername(forged));
    }

    @Test
    @DisplayName("UNI_02.09 - Deve rejeitar token de tipo incorreto")
    void shouldRejectTokenOfIncorrectType() {
        String access = jwtService.generateToken(userDetails);
        assertFalse(jwtService.validateRefreshToken(access, userDetails));

        String refresh = jwtService.generateRefreshToken(userDetails);
        assertFalse(jwtService.validateToken(refresh, userDetails));
    }

    @Test
    @DisplayName("UNI_02.10 - Deve retornar expiração em segundos")
    void shouldReturnExpirationInSeconds() {

        assertEquals(900L, jwtService.getExpirationInSeconds());
    }

}
