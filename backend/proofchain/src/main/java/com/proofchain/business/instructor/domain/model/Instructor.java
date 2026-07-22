package com.proofchain.business.instructor.domain.model;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInstructor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    private String specialty;
    private Instant hiringDate;
    private boolean isActive;
    private Instant creatAt;
    private Instant deletedAt;

}
