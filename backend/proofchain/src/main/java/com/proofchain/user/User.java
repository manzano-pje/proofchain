package com.proofchain.user;

import com.proofchain.identities.enums.UserRole;
import com.proofchain.instituition.Instituition;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//    @Column(nullable = false)
    private String name;

//    @Column(nullable = false, unique = true)
    private String email;

//    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
    private UserRole role;


    @CreationTimestamp
    private Instant createAt;

    @CreationTimestamp
    private Instant updateAt;
    private boolean isActive;

    /////// RELACIONAMENTO ///////

    // EM Instituition
    @ManyToOne
    @JoinColumn(name = "instituition_id")
    private Instituition instituition;

}
