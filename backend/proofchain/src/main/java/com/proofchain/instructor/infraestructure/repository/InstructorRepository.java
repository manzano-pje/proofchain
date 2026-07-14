package com.proofchain.instructor.infraestructure.repository;

import com.proofchain.course.domain.model.Course;
import com.proofchain.instructor.domain.model.Instructor;
import com.proofchain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


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
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    boolean existsByUser_IdAndCourse_IdAndInstitution_DeletedAtIsNull(
            Long userId,
            Long courseId,
            Long institutionId);

}
