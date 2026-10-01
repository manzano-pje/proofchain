package com.proofchain.business.couseClass.infraestructure.repository;

import com.proofchain.business.couseClass.domain.model.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * CourseRepository
 *
 * Função no sistema:
 * Responsável por fornecer operações de persistência e consulta da entidade Course,
 * incluindo suporte a isolamento por instituição (multi-tenant lógico).
 *
 * Estrutura atual:
 * Interface de repositório baseada em Spring Data JPA.
 * Expõe métodos de consulta filtrados por institutionId para garantir escopo de tenant.
 *
 * Fluxo:
 * 1. Camada de aplicação solicita operações de leitura ou escrita
 * 2. Spring Data executa queries derivadas automaticamente
 * 3. Retorna entidades Course ou estados booleanos conforme necessidade
 *
 * Integração no sistema:
 * Utilizado pelos handlers da camada de aplicação (CreateCourseHandler, UpdateCourseHandler,
 * ListAllCourseHandler, ListOneCourseHandler) para acesso ao banco de dados.
 */

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {

    @Query("""
        SELECT DISTINCT cc
        FROM CourseClass cc
        LEFT JOIN FETCH cc.participants p
        LEFT JOIN FETCH cc.user u
        LEFT JOIN FETCH cc.course c
        WHERE cc.institution.id = :institutionId
          AND cc.institution.deletedAt IS NULL
        ORDER BY u.name ASC, c.name ASC
        """)
    List<CourseClass> findAllByInstitution_IdAndInstitution_DeletedAtIsNullOrderByUser_NameAscCourse_NameAsc(@Param("institutionId") Long institutionId);

    boolean existsByIdAndCourse_IdAndInstitution_IdAndInstitution_DeletedAtIsNull(
            Long userId,
            Long courseId,
            Long institutionId);
}
