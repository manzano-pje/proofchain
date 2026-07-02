package com.proofchain.user.domain.exception;

import com.proofchain.shared.exception.BaseException;

/**
 * UserRegisteredException
 *
 * Função no sistema:
 * Representa a exceção lançada quando é identificada a tentativa de cadastrar
 * um usuário já existente na instituição (tenant) atual.
 *
 * Estrutura atual:
 * Exceção de domínio especializada, derivada de BaseException, responsável
 * por padronizar a resposta HTTP e a mensagem apresentada ao cliente.
 *
 * Fluxo:
 * 1. Handler ou Service verifica existência do usuário
 * 2. É identificado um cadastro duplicado
 * 3. UserRegisteredException é lançada
 * 4. GlobalExceptionHandler intercepta a exceção
 * 5. Resposta HTTP 409 (Conflict) é retornada ao cliente
 *
 * Integração no sistema:
 * Utilizada durante operações de criação de usuários para garantir a
 * integridade dos dados e evitar registros duplicados dentro da mesma
 * instituição.
 */
public class UserRegisteredException extends BaseException {

    /*
     * =========================================================
     * CONSTRUTOR
     * =========================================================
     */

    /**
     * Cria uma exceção indicando que o usuário já possui cadastro na instituição.
     */
    public UserRegisteredException() {
        super("Usuário já cadastrado", 409);
    }
}