package com.proofchain.business.model;

import com.proofchain.business.course.domain.model.Course;
import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.business.couseClass.domain.model.CourseClass;
import com.proofchain.business.participant.Participant;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(
        name = "tb_certificates",
        uniqueConstraints = {
                @UniqueConstraint(columnNames =
                        {"course_id", "participant_id"})
        }
)

public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cryptographic proof (off-chain / on-chain)
    @Column(nullable = false, unique = true, length = 64)
    private String  hash;
    private Instant issuedDate;         // Data de emissão
    private Instant initialDateCourse;  // Data do início do curso
    private Instant finishDateCourse;   // Data do final do curso
    private Instant revokedDate;        // Data em que o certificado foi cancelado
    private Instant expiredDate;        // Data em que o certificado expira

    ///// RELACIONAMENTO /////

    // Course
    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    // Instituição
    @ManyToOne(optional = false)
    @JoinColumn(name = "institution_id")
    private Institution institution;

    // Instrutor
    @ManyToOne(optional = false)
    @JoinColumn(name = "instructor_id")
    private CourseClass courseClass;

    // Participant
    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id")
    private Participant participant;
}

