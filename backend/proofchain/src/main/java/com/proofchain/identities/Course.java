package com.proofchain.identities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "tb_courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCourse;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    @Size(max = 200)
    private String description;

    /////RELACIONAMENTO /////

    // Instituition
    @ManyToOne
    @JoinColumn(name = "instituition_id",
                nullable = false)
    private Instituition instituition;

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
