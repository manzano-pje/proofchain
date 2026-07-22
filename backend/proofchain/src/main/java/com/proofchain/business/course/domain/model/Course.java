package com.proofchain.business.course.domain.model;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.business.couseClass.domain.model.CourseClass;
import com.proofchain.business.model.Certificate;
import com.proofchain.business.participant.Participant;
import com.proofchain.shared.domain.model.BaseEntity;
import com.proofchain.shared.exception.BusinessException;
import com.proofchain.shared.util.textNormalize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Course
 *
 * Função no sistema:
 * Representa a entidade de domínio responsável por modelar um curso dentro da plataforma ProofChain.
 * Centraliza regras de negócio e invariantes do domínio relacionadas a criação, atualização e relacionamento
 * com instituição, instrutores e participantes.
 *
 * Estrutura atual:
 * Entidade JPA mapeada para tabela tb_courses.
 * Implementa regras de validação de domínio diretamente na entidade para garantir consistência do estado.
 * Estende BaseEntity para compartilhamento de atributos comuns.
 *
 * Fluxo:
 * 1. Curso é criado via factory method Course.create
 * 2. Regras de domínio são validadas na criação
 * 3. Curso pode ser atualizado via updateCourse mantendo invariantes
 * 4. Persistência ocorre via repository na camada de infraestrutura
 *
 * Integração no sistema:
 * Utilizada pelos handlers da camada de aplicação (CreateCourseHandler, UpdateCourseHandler)
 * e persistida via CourseRepository.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(
        name = "tb_courses",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "institution_id"}
                )
        }
)
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Size(max = 200)
    private String description;

    @Column(nullable = false)
    private int hours;

    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    // Instrutor
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id")
    private CourseClass courseClass;

    // Certificates
    @OneToMany(mappedBy = "course")
    private List<Certificate> certificates;

    @ManyToMany
    @JoinTable(
            name = "course_participant",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;

    /*
     * =========================================================
     * FACTORY METHOD
     * =========================================================
     */

    public static Course create(String name,
                                String description,
                                int hours,
                                Institution institution) {

        validateCoreFields(name, description, hours);
        validateInstitution(institution);

        Course course = new Course();
        course.name = textNormalize.normalize(name);
        course.description = textNormalize.normalize(description);
        course.hours = hours;
        course.institution = institution;

        return course;
    }

    /*
     * =========================================================
     * DOMAIN BEHAVIOR
     * =========================================================
     */

    public void updateCourse(String name, String description, int hours) {

        validateCoreFields(name, description, hours);

        this.name = textNormalize.normalize(name);
        this.description = textNormalize.normalize(description);
        this.hours = hours;
    }

    /*
     * =========================================================
     * DOMAIN VALIDATIONS (INVARIANTS)
     * =========================================================
     */

    private static void validateCoreFields(String name, String description, int hours) {

        if (name == null || name.isBlank()) {
            throw new BusinessException("Nome do curso é obrigatório");
        }

        if (description == null || description.isBlank()) {
            throw new BusinessException("Descrição do curso é obrigatória");
        }

        if (hours <= 0) {
            throw new BusinessException("Horas devem ser maior que zero");
        }
    }

    private static void validateInstitution(Institution institution) {

        if (institution == null) {
            throw new BusinessException("Instituição obrigatória");
        }
    }
}