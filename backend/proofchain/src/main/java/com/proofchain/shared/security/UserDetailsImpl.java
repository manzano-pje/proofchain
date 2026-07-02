package com.proofchain.shared.security;

import com.proofchain.user.domain.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * UserDetailsImpl
 *
 * Função no sistema:
 * Atua como adaptador entre a entidade de domínio User e o contrato UserDetails exigido pelo Spring Security,
 * permitindo que o sistema utilize autenticação e autorização baseadas em roles e contexto multi-tenant.
 *
 * Estrutura atual:
 * Implementação de UserDetails baseada na entidade User.
 * Expõe informações essenciais para autenticação, autorização e geração de JWT.
 *
 * Fluxo:
 * 1. UserDetailsService carrega a entidade User do banco de dados
 * 2. User é convertido para UserDetailsImpl
 * 3. Spring Security utiliza esta implementação para autenticação
 * 4. Authorities são aplicadas para controle de acesso
 * 5. Dados são reutilizados no JWT e no SecurityContext
 *
 * Integração no sistema:
 * Base da camada de segurança do ProofChain, utilizada por AuthenticationManager,
 * JwtService e JwtAuthenticationFilter.
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    /*
     * =========================================================
     * DADOS DE AUTENTICAÇÃO
     * =========================================================
     */

    private final Long id;
    private final String email;
    private final String password;

    /*
     * =========================================================
     * CONTEXTO DE AUTORIZAÇÃO
     * =========================================================
     */

    private final String role;
    private final Long institutionId;

    /*
     * =========================================================
     * STATUS DA CONTA
     * =========================================================
     */

    private final boolean active;

    /**
     * Nota de decisão:
     * A conversão direta da entidade User para UserDetailsImpl mantém a camada de segurança desacoplada
     * da lógica de persistência, mas ainda permite acesso controlado aos dados necessários para autenticação.
     *
     * @param user entidade de domínio representando o usuário autenticável
     */
    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole().name();
        this.institutionId = user.getInstitution().getId();
        this.active = user.isActive();
    }

    /*
     * =========================================================
     * AUTORIDADES
     * =========================================================
     */

    /**
     * Retorna as permissões do usuário no formato exigido pelo Spring Security.
     *
     * @return coleção de authorities baseadas no role do usuário
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role)
        );
    }

    /**
     * Retorna o identificador utilizado como username no sistema de autenticação.
     *
     * @return email do usuário
     */
    @Override
    public String getUsername() {
        return email;
    }

    /*
     * =========================================================
     * STATUS DE CONTA
     * =========================================================
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}