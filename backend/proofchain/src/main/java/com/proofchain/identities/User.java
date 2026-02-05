package com.proofchain.identities;

import com.proofchain.identities.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "user_number",
            insertable = false,
            updatable = false,
            unique = true
    )
    private Long userNumber;

//    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;


    @CreationTimestamp
    private Instant createAt;

    @CreationTimestamp
    private Instant updateAt;
    private boolean isActive;

    /////// RELACIONAMENTO ///////

    // EM Instituition
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Instituition instituition;


}
