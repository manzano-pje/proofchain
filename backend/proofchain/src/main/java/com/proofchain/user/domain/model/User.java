package com.proofchain.user.domain.model;

import com.proofchain.institution.domain.model.Institution;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

import static java.time.Instant.now;

/**
 * TODO (ProofChain):
 * Refatorar esta entidade para adequação completa ao padrão arquitetural do projeto.
 *
 * Alterações previstas:
 * - Substituir o método estático update() por um método de instância update(...);
 * - Centralizar as alterações de estado na própria entidade (Rich Domain Model);
 * - Remover a atribuição manual de createAt, deixando o Hibernate gerenciar o campo;
 * - Avaliar padronização dos campos createAt/updateAt para createdAt/updatedAt;
 * - Incluir validações de domínio diretamente nos métodos create() e update().
 */

/**
 * User
 *
 * Função no sistema:
 * Representa o usuário da plataforma ProofChain, armazenando suas informações
 * cadastrais, credenciais de autenticação, perfil de acesso e vínculo com a
 * instituição à qual pertence.
 *
 * Estrutura atual:
 * Entidade de domínio persistida via JPA, servindo como base para autenticação,
 * autorização e identificação do usuário dentro da arquitetura multi-tenant.
 *
 * Fluxo:
 * 1. Usuário é criado pela camada de aplicação
 * 2. Entidade é persistida no banco de dados
 * 3. UserDetailsService utiliza esta entidade durante a autenticação
 * 4. JwtService gera tokens contendo suas informações
 * 5. Demais módulos utilizam esta entidade como referência do usuário autenticado
 *
 * Integração no sistema:
 * Integrada com Institution, Spring Security, JwtService e todos os módulos que
 * necessitam identificar o usuário autenticado.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_users")
public class User {

    /*
     * =========================================================
     * IDENTIFICAÇÃO
     * =========================================================
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @CreationTimestamp
    private Instant createAt;

    @UpdateTimestamp
    private Instant updateAt;

    private boolean isActive;

    /*
     * =========================================================
     * FÁBRICA DE CRIAÇÃO
     * =========================================================
     */

    /**
     * Cria uma nova instância de usuário.
     *
     * Nota de decisão:
     * Atualmente este método atua como fábrica de criação da entidade.
     * Futuramente concentrará também todas as validações e regras de negócio
     * referentes ao cadastro de usuários.
     *
     * @param name nome do usuário
     * @param email e-mail utilizado para autenticação
     * @param password senha criptografada
     * @param role perfil de acesso
     * @param institution instituição proprietária do usuário
     * @return nova entidade User
     */
    public static User create(String name,
                              String email,
                              String password,
                              UserRole role,
                              Institution institution) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setInstitution(institution);
        user.setPassword(password);
        user.setCreateAt(now());
        user.setRole(role);
        user.setActive(true);

        return user;
    }

    /*
     * =========================================================
     * ATUALIZAÇÃO
     * =========================================================
     */

    /**
     * Cria uma nova instância de usuário para atualização.
     *
     * Nota de decisão:
     * Este método será substituído futuramente por um método de instância,
     * conforme padrão arquitetural definido para o ProofChain.
     *
     * @param id identificador do usuário
     * @param name nome atualizado
     * @param email e-mail do usuário
     * @param role perfil de acesso
     * @param isActive status de ativação
     * @return instância de User contendo os dados informados
     */
    public static User update(Long id,
                              String name,
                              String email,
                              UserRole role,
                              Boolean isActive) {

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        user.setActive(isActive);

        return user;
    }

    /*
     * =========================================================
     * RELACIONAMENTOS
     * =========================================================
     */

    /**
     * Instituição proprietária do usuário.
     *
     * Em ambiente multi-tenant, todo usuário pertence exatamente
     * a uma instituição.
     */
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;
}