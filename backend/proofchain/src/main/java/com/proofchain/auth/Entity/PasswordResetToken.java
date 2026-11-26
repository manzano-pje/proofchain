package com.proofchain.auth.Entity;

import com.proofchain.user.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private String password;
    private Instant expirationDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public PasswordResetToken() {

    }

    public boolean isexpirated() {
        return !this.getExpirationDate().isAfter(Instant.now());
    }
}