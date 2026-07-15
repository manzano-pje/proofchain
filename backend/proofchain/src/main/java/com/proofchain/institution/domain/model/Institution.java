package com.proofchain.institution.domain.model;

import com.proofchain.certificate.model.Certificate;
import com.proofchain.course.domain.model.Course;
import com.proofchain.institution.interfaces.dtos.request.InstitutionRequest;
import com.proofchain.couseClass.domain.model.Instructor;
import com.proofchain.participant.Participant;
import com.proofchain.subscription.Subscriptions;
import com.proofchain.user.domain.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "tb_institutions")
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Size(min = 5, max = 100)
    private String name;

//    @Column(nullable = false)
    @CNPJ
    private String cnpj;

    @Email
//    @Column(nullable = false)
    private String email;

    private String address;
    private Integer number;
    private String complement;
    private String neighborhood;
    private String city;

    @Size(min = 2, max = 2)
    private String state;

    @Pattern(regexp = "\\d{5}-\\d{3}",message = "O cep deve ser no fornato XXXXX-XXX")
    private String postalCode;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX")
    private String phone;
    private Instant createdAt;
    private Instant deletedAt;
    private Boolean active;

    ///// RELACIONAMENTO /////

    // Courses
    @OneToMany(mappedBy = "institution",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @ToString.Exclude
    private List<Course> listCourses = new ArrayList<>();

    // Subscriptions
    @OneToMany(mappedBy = "institution",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    private List<Subscriptions> subscriptions = new ArrayList<>();

    // Useres
    @OneToMany(mappedBy = "institution",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    private List<User> listUsers = new ArrayList<>();

    // Instructor
    @OneToMany(mappedBy = "institution",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    private List<Instructor> listInstructors = new ArrayList<>();

    // Participants
    @OneToMany(mappedBy = "institution",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @ToString.Exclude
    private List<Participant> listParticipants = new ArrayList<>();

    // Certificate
    @OneToMany(mappedBy = "institution")
    @ToString.Exclude
    private List<Certificate> listCertificates = new ArrayList<>();

    // UPDATE

    public void updateFrom(InstitutionRequest request) {
        this.name = request.name();
        this.address = request.address();
        this.number = request.number();
        this.complement = request.complement();
        this.neighborhood = request.neighborhood();
        this.city = request.city();
        this.state = request.state();
        this.postalCode = request.postalCode();
        this.phone = request.phone();
    }

}
