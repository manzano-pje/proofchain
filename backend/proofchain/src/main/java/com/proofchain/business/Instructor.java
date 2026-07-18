package com.proofchain.business;

import com.proofchain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInstructor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String bio;
    private String specialty;
    private Instant hiringDate;
    private boolean isActive;

}
