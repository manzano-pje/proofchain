package com.proofchain.user.domain.exception;

import com.proofchain.shared.exception.BaseException;

/**
 * UserNotFoundException
 *
 * Função no sistema:
 * Representa a exceção lançada quando um usuário não é localizado durante
 * uma operação da plataforma ProofChain.
 *
 * Estrutura atual:
 * Exceção de domínio especializada, derivada de BaseException, responsável
 * por padronizar a resposta HTTP e a mensagem apresentada ao cliente.
 *
 * Fluxo:
 * 1. Handler ou Service realiza consulta ao usuário
 * 2. Usuário não é localizado
 * 3. UserNotFoundException é lançada
 * 4. GlobalExceptionHandler intercepta a exceção
 * 5. Resposta HTTP 404 é retornada ao cliente
 *
 * Integração no sistema:
 * Utilizada pelas camadas de aplicação e domínio durante operações de consulta,
 * atualização e exclusão de usuários.
 */
public class UserNotFoundException extends BaseException {

    /*
     * =========================================================
     * CONSTRUTOR
     * =========================================================
     */

    /**
     * Cria uma exceção indicando que o usuário solicitado não foi encontrado.
     */
    public UserNotFoundException() {
        super("Usuário não encontrado", 404);
    }
}