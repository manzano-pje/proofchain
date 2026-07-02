package com.proofchain.user.infrastructure.repository;

import com.proofchain.user.domain.model.User;
import com.proofchain.user.domain.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository
 *
 * Função no sistema:
 * Responsável pela persistência e recuperação das entidades User,
 * fornecendo operações de consulta e validação utilizadas pelos casos
 * de uso da plataforma ProofChain.
 *
 * Estrutura atual:
 * Repositório baseado em Spring Data JPA, contendo consultas derivadas
 * para operações de autenticação, gerenciamento de usuários e isolamento
 * entre instituições (multi-tenant).
 *
 * Fluxo:
 * 1. Os Handlers solicitam operações de persistência ou consulta
 * 2. O Spring Data gera automaticamente as implementações
 * 3. As entidades User são recuperadas ou persistidas
 * 4. Os resultados retornam para a camada de aplicação
 *
 * Integração no sistema:
 * Utilizado pelos Handlers de usuários, UserDetailsServiceImpl,
 * AuthService e demais componentes responsáveis pelo gerenciamento
 * de usuários da plataforma.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * =========================================================
     * CONSULTAS
     * =========================================================
     */

    /**
     * Localiza um usuário pelo e-mail pertencente a uma instituição.
     *
     * @param email e-mail do usuário
     * @param id identificador da instituição
     * @return usuário encontrado, caso exista
     */
    Optional<User> findByEmailAndInstitution_Id(String email, Long id);

    /**
     * Localiza um usuário pelo identificador dentro de uma instituição.
     *
     * @param id identificador do usuário
     * @param institutionId identificador da instituição
     * @return usuário encontrado, caso exista
     */
    Optional<User> findByIdAndInstitution_Id(Long id, Long institutionId);

    /**
     * Localiza um usuário pelo e-mail.
     *
     * Utilizado principalmente pelo processo de autenticação.
     *
     * @param email e-mail do usuário
     * @return usuário encontrado, caso exista
     */
    Optional<User> findByEmail(String email);

    /**
     * Lista todos os usuários ativos pertencentes a uma instituição.
     *
     * @param institutionId identificador da instituição
     * @return lista de usuários encontrados
     */
    List<User> findAllByInstitution_IdAndInstitution_DeletedAtIsNull(Long institutionId);

    /*
     * =========================================================
     * VALIDAÇÕES
     * =========================================================
     */

    /**
     * Verifica se um usuário pertence à instituição informada.
     *
     * @param id identificador do usuário
     * @param institutionId identificador da instituição
     * @return true quando o usuário existir
     */
    boolean existsByIdAndInstitutionId(Long id, Long institutionId);

    /**
     * Verifica se já existe um usuário com o mesmo nome
     * dentro da instituição.
     *
     * @param name nome do usuário
     * @param institutionId identificador da instituição
     * @return true quando existir
     */
    boolean existsByNameAndInstitutionId(String name, Long institutionId);

    /**
     * Verifica se já existe um usuário cadastrado com o e-mail informado.
     *
     * @param email e-mail do usuário
     * @return true quando existir
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se existe algum usuário associado ao perfil informado.
     *
     * @param userRole perfil de acesso
     * @return true quando existir
     */
    boolean existsByRole(UserRole userRole);

}