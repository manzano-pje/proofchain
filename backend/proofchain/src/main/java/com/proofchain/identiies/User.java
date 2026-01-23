package com.proofchain.identiies;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(
            name = "user_number",
            insertable = false,
            updatable = false,
            unique = true
    )
    private Long userNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private Instant createAt;

    @CreationTimestamp
    private Instant updateAt;
    private boolean isActive;

    /////// RELACIONAMENTO ///////

//    // participant
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private Participant participant;
//
//    // instructor
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private Instructor instructor;


  // course
  // certificate
//    participanes
//    instrutores
//    login
//
//@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//@JoinTable(
//        name = "student_course",                        // nome da tabela intermediária
//        joinColumns = @JoinColumn(name = "student_id"), // FK do dono
//        inverseJoinColumns = @JoinColumn(name = "course_id")
//)
//private Set<Course> courses = new HashSet<>();

}
