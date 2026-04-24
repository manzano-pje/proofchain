package com.proofchain.certificate.model;

import com.proofchain.course.domain.model.Course;
import com.proofchain.instituition.Institution;
import com.proofchain.instructor.Instructor;
import com.proofchain.participant.Participant;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(
        name = "tb_certificates",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"course_id", "participant_id"})
        }
)

public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Cryptographic proof (off-chain / on-chain)
    @Column(nullable = false, unique = true, length = 64)
    private String hash;

    private LocalDate issuedDate;
    private LocalDate initialDateCourse;
    private LocalDate finishDateCourse;
    private LocalDate revokedDate;
    private LocalDate expiredDate;

    ///// RELACIONAMENTO /////

    // Course
    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    // Instituição
    @ManyToOne(optional = false)
    @JoinColumn(name = "instituition_id")
    private Institution institution;

    // Instrutor
    @ManyToOne(optional = false)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    // Participant
    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id")
    private Participant participant;
}

