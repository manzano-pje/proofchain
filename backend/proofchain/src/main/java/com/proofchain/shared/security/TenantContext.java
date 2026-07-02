package com.proofchain.shared.security;

/**
 * TenantContext
 *
 * Função no sistema:
 * Responsável por armazenar e isolar o identificador de instituição (tenantId)
 * durante o ciclo de vida de uma requisição HTTP na plataforma ProofChain.
 *
 * Estrutura atual:
 * Utiliza ThreadLocal para manter o contexto de tenant por thread, garantindo
 * isolamento de dados em ambiente multi-tenant.
 *
 * Fluxo:
 * 1. JwtAuthenticationFilter extrai institutionId do JWT
 * 2. TenantContext armazena o institutionId na thread atual
 * 3. Camadas de aplicação e infraestrutura utilizam o tenantId para filtragem
 * 4. Ao final da requisição, o contexto é limpo para evitar vazamento de dados
 *
 * Integração no sistema:
 * Utilizado transversalmente por handlers, services e repositories para garantir
 * isolamento de dados entre instituições (multi-tenant lógico).
 */
public class TenantContext {

    /*
     * =========================================================
     * CONTEXTO DE TENANT (THREAD LOCAL)
     * =========================================================
     */

    /**
     * Instância ThreadLocal responsável por armazenar o ID da instituição
     * de forma isolada por thread.
     */
    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

    /*
     * =========================================================
     * DEFINIÇÃO DE TENANT
     * =========================================================
     */

    /**
     * Define o identificador da instituição para a thread atual.
     *
     * @param institutionId ID da instituição extraído do JWT
     */
    public static void setInstitutionId(Long institutionId) {
        CURRENT_TENANT.set(institutionId);
    }

    /*
     * =========================================================
     * RECUPERAÇÃO DE TENANT
     * =========================================================
     */

    /**
     * Recupera o identificador da instituição associado à thread atual.
     *
     * @return institutionId ativo na thread ou null se inexistente
     */
    public static Long getInstitutionId() {
        return CURRENT_TENANT.get();
    }

    /*
     * =========================================================
     * LIMPEZA DE CONTEXTO
     * =========================================================
     */

    /**
     * Remove completamente o tenantId da thread atual.
     * Deve ser chamado obrigatoriamente ao final do ciclo da requisição
     * para evitar vazamento de dados em ambientes com thread pooling.
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}