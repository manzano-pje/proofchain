package com.proofchain.instructor;

import com.proofchain.course.domain.model.Course;
import com.proofchain.instituition.Instituition;
import com.proofchain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@ToString
@Table(name = "tb_instructors")
public class Instructor extends User {

    @Column(nullable = false)
    private Long instructorNumber;

    @Column(nullable = false, unique = true)
    private Long idCourse;


    /////// RELACIONAMENTO ///////

    // course
    @OneToMany(mappedBy = "instructor")
    private List<Course> courses;

    // EM Instituition
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Instituition instituition;
}
