package com.proofchain.shared.security;

import com.proofchain.user.domain.model.User;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * UserDetailsImpl
 *
 * Responsabilidade:
 * Atua como adaptador entre a entidade de usuário da aplicação e o contrato
 * exigido pelo Spring Security para autenticação e autorização.
 *
 * Função no sistema:
 * Implementa a interface UserDetails, fornecendo as informações necessárias
 * para o processo de autenticação e controle de acesso.
 *
 * Dados expostos:
 * - Identificador do usuário (id)
 * - E-mail utilizado como username
 * - Senha criptografada
 * - Perfis de acesso (roles: ROLE_USER, ROLE_ADMIN, ROLE_SUPER_ADMIN)
 * - Contexto institucional (multi-tenant)
 * - Status da conta (ativo/inativo)
 *
 * Fluxo de utilização:
 * 1. O UserDetailsService carrega o usuário do banco de dados
 * 2. A entidade User é convertida para UserDetailsImpl
 * 3. O Spring Security utiliza esta estrutura para autenticação
 * 4. As informações são usadas em autorização e validação de acesso
 *
 * Integração no sistema:
 * Esta classe também fornece base para:
 * - Geração de tokens JWT
 * - Validação de permissões
 * - Controle de acesso por tenant
 */
@Configuration
@Getter
@EnableWebSecurity
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final String role;
    private final Long institutionId;
    private final boolean active;

    public UserDetailsImpl(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole().name();
        this.institutionId = user.getInstitution().getId();
        this.active = user.isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + role
                )
        );
    }

  @Override
  public Long getId() {
      return id;

  }
    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

}
