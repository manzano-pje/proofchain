package com.proofchain.shared.security;

import com.proofchain.user.domain.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementação da interface UserDetails do Spring Security.
 *
 * Esta classe atua como um adaptador entre a entidade User da aplicação
 * e o mecanismo de autenticação do Spring Security.
 *
 * Sua responsabilidade é fornecer ao Spring Security as informações
 * necessárias para autenticação e autorização, como:
 *
 * - Identificador do usuário (id);
 * - E-mail utilizado para login;
 * - Senha criptografada;
 * - Perfil de acesso (ROLE_USER, ROLE_ADMIN, ROLE_SUPER_ADMIN);
 * - Instituição à qual o usuário pertence;
 * - Status de ativação da conta.
 *
 * Durante o processo de autenticação, o Spring Security não trabalha
 * diretamente com a entidade User, mas sim com objetos que implementam
 * a interface UserDetails. Por esse motivo, esta classe converte os
 * dados da entidade User para o formato esperado pelo framework.
 *
 * Além disso, as informações armazenadas nesta classe serão utilizadas
 * posteriormente na geração e validação dos tokens JWT e no controle
 * de acesso multi-tenant da aplicação.
 */

@Getter
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
