package com.proofchain.user.domain.model;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.proofchain.course.domain.model.Course;
import com.proofchain.identities.enums.UserRole;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.user.infrastructure.repository.UserRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

import static java.time.Instant.now;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
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
    @UpdateTimestamp
    private Instant updateAt;
    private boolean isActive;


    public static User create(String name,
                              String email,
                              String password,
                              UserRole role,
                              Institution institution
                              ) {

        User user = new User();
        user.setName(name);
        user.setInstitution(institution);
        user.setPassword(password);
        user.setCreateAt(now());
        user.setActive(true);
        return user;
    }





    /////// RELACIONAMENTO ///////

    // EM Instituition
    @ManyToOne
    @JoinColumn(name = "instituition_id")
    private Institution institution;

}
