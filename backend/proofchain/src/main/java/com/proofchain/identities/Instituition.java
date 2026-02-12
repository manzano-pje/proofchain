package com.proofchain.identities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "tb_institutions")
public class Instituition {
    @Id
    @GeneratedValue
    private UUID idInstituition;

    @Column(
            name = "user_number",
            insertable = false,
            updatable = false,
            unique = true
    )
    private Long userInstituition;

    @Column(unique = true)
    @Size(min = 5, max = 100)
    private String nameInstituition;

    @Column(nullable = false)
    @CNPJ
    private String cnpj;

    @Email
    @Column(nullable = false)
    private String emailInstituition;

    private String addressInstituition;
    private Integer numberInstituition;
    private String complementInstituition;
    private String neighborhoodInstituition;
    private String cityInstituition;

    @Size(min = 2, max = 2)
    private String stateInstituition;

    @Pattern(regexp = "\\d{5}-\\d{3}",message = "O cep deve ser no fornato XXXXX-XXX")
    private String postalCodeInstituition;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX")
    private String phoneInstituition;


    ///// RELACIONAMENTO /////

    // Courses
    @OneToMany(mappedBy = "instituition",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @ToString.Exclude
    private List<Course> listCourses;

    // Useres
    @OneToMany(mappedBy = "instituition",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    private List<User> listUsers;

    // Instructor
    @OneToMany(mappedBy = "instituition",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    private List<Instructor> listInstructors;

    // Participants
    @OneToMany(mappedBy = "instituition",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    private List<Participant> listParticipants;

    // Instructor
    @OneToMany(mappedBy = "instituition")
    @ToString.Exclude
    private List<Certificate> listCertificates;
}
