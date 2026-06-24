package com.proofchain.shared.security;

import com.proofchain.user.domain.exception.UserNotFoundException;
import com.proofchain.user.domain.model.User;
import com.proofchain.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementação da interface UserDetailsService do Spring Security.
 *
 * Responsável por localizar e carregar um usuário durante o processo
 * de autenticação da aplicação.
 *
 * Quando uma tentativa de login é realizada, o Spring Security chama
 * automaticamente o método loadUserByUsername(), passando o identificador
 * utilizado no login (neste caso, o e-mail do usuário).
 *
 * Esta classe consulta o banco de dados através do UserRepository,
 * localiza o usuário e o converte para UserDetailsImpl, formato
 * compreendido pelo Spring Security.
 *
 * Caso o usuário não seja encontrado, uma UsernameNotFoundException
 * é lançada, interrompendo o processo de autenticação.
 *
 * Importante:
 * Esta classe não valida a senha do usuário.
 * A validação da senha é realizada posteriormente pelo
 * AuthenticationManager utilizando o PasswordEncoder configurado
 * na aplicação.
 *
 * No contexto do ProofChain, esta classe representa o ponto de entrada
 * entre o banco de dados e o mecanismo de autenticação do Spring Security,
 * sendo responsável por fornecer as informações necessárias para geração
 * de tokens JWT e controle de acesso baseado em papéis (roles).
 */

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {

            User user = userRepository
                    .findByEmail(email)
                    .orElseThrow(UserNotFoundException::new);

            return new UserDetailsImpl(user);
    }
}
