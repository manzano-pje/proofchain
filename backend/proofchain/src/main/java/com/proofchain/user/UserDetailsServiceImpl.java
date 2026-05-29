package com.proofchain.user;

// Serviço uitlizado pelo Spring Security no login

import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                        new UserNotFoundException());
        return new UserDetailsImpl(user);
    }
}
    /*
🔧 Versão mais otimizada (sênior):
- Query trazendo User + Instituition
- Cache por email
Motivo: menos acessos ao banco
*/





