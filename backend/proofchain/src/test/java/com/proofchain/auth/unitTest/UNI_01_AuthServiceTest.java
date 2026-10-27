
/**
 * Testes unitários do serviço de autenticação.
 *
 * Referência: família ISO/IEC 25000 (SQuaRE), com atenção à
 * adequação funcional e à segurança previstas na ISO/IEC 25010.
 *
 * Escopo:
 * Verificar o login com credenciais válidas, o tratamento das
 * exceções de autenticação, a emissão de access e refresh tokens,
 * a renovação de tokens e a não exposição de dados sensíveis.
 *
 * Estes testes não representam certificação ou conformidade integral
 * com as normas citadas.
 */

package com.proofchain.auth.unitTest;

import com.proofchain.auth.dtos.AuthRequest;
import com.proofchain.auth.dtos.AuthResponse;
import com.proofchain.auth.dtos.RefreshRequest;
import com.proofchain.auth.dtos.UserSummaryDto;
import com.proofchain.auth.service.AuthService;
import com.proofchain.shared.exception.ForbiddenException;
import com.proofchain.shared.exception.UnauthorizedException;
import com.proofchain.shared.exception.messages.AuthMessages;
import com.proofchain.shared.security.JwtService;
import com.proofchain.shared.security.UserDetailsImpl;
import com.proofchain.shared.security.UserDetailsServiceImpl;
import com.proofchain.user.domain.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @Mock private UserDetailsServiceImpl userDetailsService;
    @InjectMocks private AuthService authService;

    private UserDetailsImpl buildUserDetails(Long id, Long institutionId, UserRole role) {
        UserDetailsImpl user = mock(UserDetailsImpl.class);
        when(user.getId()).thenReturn(id);
        when(user.getUsername()).thenReturn("user@example.com");
        when(user.getInstitutionId()).thenReturn(institutionId);
        when(user.getRole()).thenReturn(role.name());
        return user;
    }

    @Test
    @DisplayName("UNI_01.01 - Deve autenticar usuário com credenciais válidas")
    void shouldAuthenticateUserWithValidCredentials() {
        AuthRequest request = new AuthRequest("user@example.com", "senha-correta");
        UserDetailsImpl userDetails = buildUserDetails(1L, 100L, UserRole.ADMIN);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh-token");
        when(jwtService.getExpirationInSeconds()).thenReturn(900L);

        AuthResponse response = authService.login(request);

        assertAll(
                () -> assertEquals("access-token", response.token()),
                () -> assertEquals("refresh-token", response.refreshToken()),
                () -> assertEquals(900L, response.expiresIn())
        );
    }

    @Test
    @DisplayName("UNI_01.02 - Deve rejeitar login com credenciais inválidas")
    void shouldRejectLoginWithInvalidCredentials() {
        AuthRequest request = new AuthRequest("user@example.com", "senha-errada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad credentials"));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> authService.login(request));

        assertEquals(AuthMessages.INVALID_CREDENTIALS, ex.getMessage());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("UNI_01.03 - Deve rejeitar login quando usuário não existe")
    void shouldRejectLoginWhenUserDoesNotExist() {
        AuthRequest request = new AuthRequest("missing@example.com", "senha");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new InternalAuthenticationServiceException("user not found"));

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> authService.login(request));

        assertEquals(AuthMessages.INVALID_CREDENTIALS, ex.getMessage());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("UNI_01.04 - Deve rejeitar login quando conta expirada")
    void shouldRejectLoginWhenAccountIsExpired() {
        AuthRequest request = new AuthRequest("user@example.com", "senha");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AccountExpiredException("account expired"));

        AccountExpiredException ex = assertThrows(AccountExpiredException.class,
                () -> authService.login(request));

        assertEquals(AuthMessages.ACCOUNT_EXPIRED, ex.getMessage());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("UNI_01.05 - Deve rejeitar login quando conta bloqueada")
    void shouldRejectLoginWhenAccountIsLocked() {
        AuthRequest request = new AuthRequest("user@example.com", "senha");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new LockedException("locked"));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> authService.login(request));

        assertEquals(AuthMessages.ACCESS_DENIED, ex.getMessage());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("UNI_01.06 - Não deve expor senha na resposta")
    void shouldNotExposePasswordInResponse() {
        AuthRequest request = new AuthRequest("user@example.com", "senha-correta");
        UserDetailsImpl userDetails = buildUserDetails(1L, 100L, UserRole.ADMIN);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh-token");
        when(jwtService.getExpirationInSeconds()).thenReturn(900L);

        AuthResponse response = authService.login(request);

        assertAll(
                () -> assertFalse(response.token().contains("senha-correta")),
                () -> assertFalse(response.refreshToken().contains("senha-correta")),
                () -> assertFalse(response.user().toString().contains("senha-correta"))
        );
    }

    @Test
    @DisplayName("UNI_01.07 - Deve retornar resumo do usuário correto no login")
    void shouldReturnCorrectUserSummaryOnLogin() {
        AuthRequest request = new AuthRequest("user@example.com", "senha-correta");
        UserDetailsImpl userDetails = buildUserDetails(1L, 100L, UserRole.ADMIN);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh-token");
        when(jwtService.getExpirationInSeconds()).thenReturn(900L);

        AuthResponse response = authService.login(request);
        UserSummaryDto user = response.user();

        assertAll(
                () -> assertEquals(1L, user.id()),
                () -> assertEquals("user@example.com", user.name()),
                () -> assertEquals(UserRole.ADMIN.toString(), user.role()),
                () -> assertEquals(100L, user.tenantId())
        );
    }

    @Test
    @DisplayName("UNI_01.08 - Deve renovar tokens com refresh token válido")
    void shouldRefreshTokensWithValidRefreshToken() {

        RefreshRequest request = new RefreshRequest("refresh-valido");
        UserDetailsImpl userDetails = buildUserDetails(1L, 100L, UserRole.ADMIN);

        when(userDetails.isEnabled()).thenReturn(true);
        when(jwtService.extractUsername("refresh-valido")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.validateRefreshToken("refresh-valido", userDetails)).thenReturn(true);
        when(jwtService.generateToken(userDetails)).thenReturn("novo-access");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("novo-refresh");
        when(jwtService.getExpirationInSeconds()).thenReturn(900L);

        AuthResponse response = authService.refreshToken(request);

        assertAll(
                () -> assertEquals("novo-access", response.token()),
                () -> assertEquals("novo-refresh", response.refreshToken())
        );
    }

    @Test
    @DisplayName("UNI_01.09 - Deve rejeitar access token usado como refresh token")
    void shouldRejectAccessTokenUsedAsRefreshToken() {
        RefreshRequest request = new RefreshRequest("access-token-como-refresh");

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.isEnabled()).thenReturn(true);  // UNI_01.09

        when(jwtService.extractUsername("access-token-como-refresh")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.validateRefreshToken("access-token-como-refresh", userDetails)).thenReturn(false);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> authService.refreshToken(request));

        assertEquals(AuthMessages.INVALID_CREDENTIALS, ex.getMessage());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("UNI_01.10 - Deve rejeitar refresh token expirado")
    void shouldRejectExpiredRefreshToken() {
        RefreshRequest request = new RefreshRequest("refresh-expirado");
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.isEnabled()).thenReturn(true);  

        when(jwtService.extractUsername("refresh-expirado")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.validateRefreshToken("refresh-expirado", userDetails)).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.refreshToken(request));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("UNI_01.11 - Deve rejeitar refresh token com assinatura inválida")
    void shouldRejectRefreshTokenWithInvalidSignature() {
        RefreshRequest request = new RefreshRequest("refresh-assinatura-invalida");

        when(jwtService.extractUsername("refresh-assinatura-invalida"))
                .thenThrow(new UnauthorizedException(AuthMessages.INVALID_CREDENTIALS));

        assertThrows(UnauthorizedException.class,
                () -> authService.refreshToken(request));

        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("UNI_01.12 - Deve rejeitar refresh quando usuário está desabilitado")
    void shouldRejectRefreshWhenUserIsDisabled() {
        RefreshRequest request = new RefreshRequest("refresh-valido");
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.isEnabled()).thenReturn(false);

        when(jwtService.extractUsername("refresh-valido")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class,
                () -> authService.refreshToken(request));

        assertEquals(AuthMessages.INVALID_CREDENTIALS, ex.getMessage());
        verify(jwtService, never()).validateRefreshToken(any(), any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("UNI_01.13 - Deve preservar usuário e tenant no novo token")
    void shouldPreserveUserAndTenantInNewToken() {
        RefreshRequest request = new RefreshRequest("refresh-valido");
        UserDetailsImpl userDetails = buildUserDetails(7L, 100L, UserRole.INSTRUCTOR);

        when(userDetails.isEnabled()).thenReturn(true);
        when(jwtService.extractUsername("refresh-valido")).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        when(jwtService.validateRefreshToken("refresh-valido", userDetails)).thenReturn(true);
        when(jwtService.generateToken(userDetails)).thenReturn("novo-access");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("novo-refresh");
        when(jwtService.getExpirationInSeconds()).thenReturn(900L);

        AuthResponse response = authService.refreshToken(request);

        assertAll(
                () -> assertEquals(7L, response.user().id()),
                () -> assertEquals(100L, response.user().tenantId()),
                () -> assertEquals(UserRole.INSTRUCTOR.toString(), response.user().role())
        );
        verify(jwtService).generateToken(userDetails);
    }
}
