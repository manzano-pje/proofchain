package com.proofchain.instructor;

import com.proofchain.course.domain.model.Course;
import com.proofchain.institution.Institution;
import com.proofchain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@ToString
@Table(name = "tb_instructors")
public class Instructor  {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long number;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private Course course;

    /////// RELACIONAMENTO ///////

    // EM Instituition
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;
}
