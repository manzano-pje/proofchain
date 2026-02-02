package com.proofchain.service;

// Serviço uitlizado pelo Spring Security no login

import com.proofchain.exceptions.ResourceNotFoundException;
import com.proofchain.identities.User;
import com.proofchain.repository.UserRepository;
import com.proofchain.user.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
//@AllArgsConstructor

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)  {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não localizado"));
        return new UserDetailsImpl(user);
    }
}


    /*
🔧 Versão mais otimizada (sênior):
- Query trazendo User + Institution
- Cache por email
Motivo: menos acessos ao banco
*/





