package com.proofchain.user.domain.model;

import com.proofchain.institution.domain.model.Institution;
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
        user.setEmail((email));
        user.setInstitution(institution);
        user.setPassword(password);
        user.setCreateAt(now());
        user.setRole(role);
        user.setActive(true);
        return user;
    }

    public static User update(Long id,
                              String name,
                              String email,
                              UserRole role,
                              Boolean isActive
    ) {

        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        user.setActive(isActive);
        return user;
    }


    /////// RELACIONAMENTO ///////

    // EM Instituition
    @ManyToOne
    @JoinColumn(name = "instituition_id")
    private Institution institution;

}
