package com.proofchain.user.domain.model;

/**
 * UserRole
 *
 * Função no sistema:
 * Define os perfis de acesso disponíveis na plataforma ProofChain,
 * determinando o nível de autorização concedido a cada usuário durante
 * o processo de autenticação e autorização.
 *
 * Estrutura atual:
 * Enum utilizada pelo domínio da aplicação, persistida no banco de dados
 * através de EnumType.STRING e integrada ao Spring Security para concessão
 * de permissões.
 *
 * Fluxo:
 * 1. O perfil é atribuído ao usuário durante seu cadastro
 * 2. O perfil é persistido na entidade User
 * 3. UserDetailsImpl converte o perfil em GrantedAuthority
 * 4. O Spring Security utiliza a autoridade nas validações de acesso
 * 5. Os controladores aplicam restrições através de @PreAuthorize
 *
 * Integração no sistema:
 * Utilizado pelas entidades de domínio, UserDetailsImpl, JwtService,
 * AuthenticationManager e pelas regras de autorização implementadas pelo
 * Spring Security.
 */
public enum UserRole {

    /*
     * =========================================================
     * PERFIS DE ACESSO
     * =========================================================
     */

    // Perfil responsável pela administração global da plataforma.
    SUPER_ADMIN,

    // Perfil responsável pela administração de uma instituição.
    ADMIN,

    // Perfil padrão dos usuários da plataforma.
    USER,

    // Perfil padrão dos instrutores da plataforma.
    INSTRUCTOR
}