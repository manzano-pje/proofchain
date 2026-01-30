package com.proofchain.user;


import com.proofchain.identities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Classe que ensina o Spring Security como é o user
public class UserDetailsImpl implements UserDetails {

    private User user;

    public UserDetailsImpl(User user){
        this.user = user;
    }

    // Define o papel do usuário
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(
                 new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    // Login feito por e-mail
    @Override
    public String getUsername(){
        return user.getEmail();
    }

    // Senha criptografada
    @Override
    public String getPassword(){
        return user.getPassword();
    }

    // Usuário ativo logado
    @Override
    public boolean isEnabled(){
        return user.isActive();
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

    // Permite acessar a entidade User original
    public User getUser() {
        return user;
    }
}


/*
🔧 Versão mais otimizada (sênior):
- Trabalhar com permissions em vez de role
- Cache de UserDetails
Motivo: autorização mais granular e performance
*/
