package com.proofchain.identiies;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Sequencia number for human use
    @Column(
            name = "certificate_number",
            insertable = false,
            updatable = false,
            unique = true
    )
    private Long certificateNumber;

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
    private Instituition instituition;

    // Instrutor
    @ManyToOne(optional = false)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    // Participant
    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id")
    private Participant participant;
}

