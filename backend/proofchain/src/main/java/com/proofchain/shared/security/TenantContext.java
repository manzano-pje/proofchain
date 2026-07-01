package com.proofchain.shared.security;

/**
 * Componente responsável por armazenar e gerenciar o identificador da
 * instituição (Tenant ID) de forma segura durante o ciclo de vida de uma
 * requisição HTTP na plataforma ProofChain.
 * * Utiliza o conceito de ThreadLocal para garantir o isolamento completo
 * dos dados em cenários Multi-Tenant, prevenindo que uma requisição acesse
 * ou modifique informações pertencentes a outra instituição (Data Leak).
 * * Responsabilidades:
 * - Reter o 'institutionId' associado ao token do usuário na thread atual;
 * - Disponibilizar o ID da instituição para as camadas de serviço e repositório;
 * - Garantir a limpeza da memória ao fim do ciclo da requisição.
 */
public class TenantContext {

    // Instância ThreadLocal que encapsula o ID da instituição individual por Thread
    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();

    /**
     * Define o identificador da instituição para a thread atual.
     * Este método será invocado pelo filtro de segurança (JwtAuthenticationFilter)
     * logo após validar o token com sucesso.
     * * @public
     * @param institutionId O ID da instituição extraído do JWT.
     */
    public static void setInstitutionId(Long institutionId) {
        CURRENT_TENANT.set(institutionId);
    }

    /**
     * Recupera o identificador da instituição associado à thread atual.
     * Este método será amplamente utilizado pelas classes de serviço e pelas
     * queries customizadas para filtrar e isolar os registros no banco de dados.
     * * @public
     * @return Long O ID da instituição ativa na thread, ou null se não houver.
     */
    public static Long getInstitutionId() {
        return CURRENT_TENANT.get();
    }

    /**
     * Limpa completamente o valor armazenado no ThreadLocal para a thread atual.
     * ATENÇÃO: É obrigatório chamar este método no bloco 'finally' do filtro
     * de interceptação para evitar vazamentos de memória (Memory Leaks) e a
     * contaminação de dados entre threads reutilizadas pelo pool do servidor.
     * * @public
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}