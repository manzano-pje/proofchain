package com.proofchain.participant;

import com.proofchain.course.domain.model.Course;
import com.proofchain.institution.domain.model.Institution;
import com.proofchain.user.domain.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
@Table(name = "tb_participants")
public class Participant extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Email
    private String email;

    @Pattern(regexp = "^[1-9]{2}(9\\d{8}|[2-5]\\d{7})$")
    private String phone;

    @Column(nullable = false, unique = true)
    @CPF
    private String cpf;

    @Column(nullable = false)
    private String address;

    private Long number;
    private String complement;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    @Size(min = 2, max = 2)
    private String state;

    @Column(nullable = false)
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O cep deve ser no fornato XXXXX-XXX")
    private String postalCode;


    /////// RELACIONAMENTO //////

    // Course
    @ManyToMany(mappedBy = "participants")
    private List<Course> courses;

    // EM Instituition
    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;
}
