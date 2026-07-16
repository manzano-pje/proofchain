package com.proofchain.business.couseClass.domain.model;

import com.proofchain.business.course.domain.model.Course;
import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@ToString
@Table(name = "tb_instructors")
public class CourseClass {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long number;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @CreationTimestamp
    private Instant createAt;

    @UpdateTimestamp
    private Instant updateAt;

    private boolean isActive;
    /////// RELACIONAMENTO ///////

    // EM Instituition
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;
}
