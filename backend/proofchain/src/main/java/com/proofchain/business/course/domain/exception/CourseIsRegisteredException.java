package com.proofchain.business.course.domain.exception;

import com.proofchain.shared.exception.BaseException;

/**
 * CourseIsRegisteredException
 *
 * Função no sistema:
 * Representa uma exceção de regra de negócio lançada quando ocorre tentativa de criação
 * ou atualização de um curso com nome já existente dentro do mesmo contexto de instituição (tenant).
 *
 * Estrutura atual:
 * Exceção customizada que estende BaseException.
 * Define mensagem padrão e código HTTP de conflito (409).
 *
 * Fluxo:
 * 1. Handler/Service identifica duplicidade de curso
 * 2. Exceção é lançada
 * 3. GlobalExceptionHandler captura e transforma em resposta HTTP adequada
 *
 * Integração no sistema:
 * Utilizada na camada de domínio e aplicação para garantir integridade de dados
 * no módulo de cursos.
 */
public class CourseIsRegisteredException extends BaseException {

    public CourseIsRegisteredException() {
        super("Curso já cadastrado", 409);
    }
}