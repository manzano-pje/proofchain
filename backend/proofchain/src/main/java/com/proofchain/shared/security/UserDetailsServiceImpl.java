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
 * UserDetailsServiceImpl
 *
 * Função no sistema:
 * Responsável por carregar os dados do usuário durante o processo de autenticação,
 * servindo como ponte entre a camada de persistência e o Spring Security.
 *
 * Estrutura atual:
 * Implementação da interface UserDetailsService do Spring Security.
 * Realiza consulta ao banco de dados via UserRepository e converte a entidade User
 * para UserDetailsImpl.
 *
 * Fluxo:
 * 1. Spring Security inicia processo de autenticação
 * 2. Username (email) é fornecido ao método loadUserByUsername
 * 3. Repositório busca usuário no banco de dados
 * 4. Usuário é convertido para UserDetailsImpl
 * 5. Objeto é retornado para o AuthenticationManager
 *
 * Integração no sistema:
 * Utilizado diretamente pelo AuthenticationManager durante login e geração de JWT,
 * sendo parte essencial do fluxo de autenticação do ProofChain.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /*
     * =========================================================
     * DEPENDÊNCIAS
     * =========================================================
     */
    private final UserRepository userRepository;

    /*
     * =========================================================
     * CARREGAMENTO DE USUÁRIO
     * =========================================================
     */

    /**
     * Carrega um usuário pelo e-mail para autenticação.
     *
     * @param email identificador utilizado no login
     * @return UserDetails representando o usuário autenticável
     * @throws UsernameNotFoundException se o usuário não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return new UserDetailsImpl(user);
    }
}