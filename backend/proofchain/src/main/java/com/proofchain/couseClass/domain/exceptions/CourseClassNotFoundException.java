package com.proofchain.couseClass.domain.exceptions;

import com.proofchain.shared.exception.BaseException;

/**
 * CourseNotFoundException
 *
 * Função no sistema:
 * Representa uma exceção de regra de negócio lançada quando um curso não é encontrado
 * dentro do contexto de uma instituição (tenant).
 *
 * Estrutura atual:
 * Exceção customizada que estende BaseException.
 * Define mensagem padrão e código HTTP 404 (Not Found).
 *
 * Fluxo:
 * 1. Handler ou Service realiza consulta de curso por ID e instituição
 * 2. Nenhum registro é encontrado
 * 3. Exceção é lançada para interromper o fluxo
 * 4. GlobalExceptionHandler converte para resposta HTTP padronizada
 *
 * Integração no sistema:
 * Utilizada na camada de domínio e aplicação para controle de integridade de consultas
 * no módulo de cursos.
 */
public class CourseClassNotFoundException extends BaseException {

    public CourseClassNotFoundException(String message) {

        super(message, 404);
    }
}