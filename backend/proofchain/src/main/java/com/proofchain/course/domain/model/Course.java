package com.proofchain.course.domain.model;

import com.proofchain.certificate.model.Certificate;
import com.proofchain.shared.domain.model.BaseEntity;
import com.proofchain.shared.exception.ValidationException;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.instructor.Instructor;
import com.proofchain.participant.Participant;
import com.proofchain.plataform.domain.text.textNormalize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_courses")
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Size(max = 200)
    private String description;

    @Column(nullable = false)
    private int hours;


    public static Course create(String name,
                                String description,
                                int hours,
                                Institution institution) {

        if (hours <= 0) {
            throw new ValidationException("Horas devem ser maior que zero");
        }

        if (institution == null) {
            throw new ValidationException("Instituição obrigatória");
        }

        Course course = new Course();
        course.name = textNormalize.normalize(name);
        course.description = textNormalize.normalize(description);
        course.hours = hours;
        course.institution = institution;
        return course;
    }

    public void updateCourse(String name, String description, int hours) {
        this.name = textNormalize.normalize(name);
        this.description = textNormalize.normalize(description);
        this.hours = hours;
    }

    /////RELACIONAMENTO /////

    // Instituition
    @ManyToOne
    @JoinColumn(name = "institution_id",
                nullable = false)
    private Institution institution;

    // Instrutor
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

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

}
