package com.proofchain.identiies;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
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

    @Column(nullable = false, unique = true)
    private String nameInstituition;

    @Column(nullable = false)
    @CNPJ
    private String cnpjInstituition;

    @Column(nullable = false)
    @Size(min = 3, message = "O campo rua precisa ter no mínimo 3 caracteres!")
    private String addressInstituition;

    @Column
    private int numberInstituition;

    @Column
    private String complementInstituition;

    @Column(nullable = false)
    private String bairroInstituition;

    @Column(nullable = false)
    private String cityInstituition;

    @Column(nullable = false)
    @Size(min = 2, max = 2)
    private String stateInstituition;

    @Column(nullable = false)
    @Pattern(regexp = "\\d{5}-\\d{3}",message = "O cep deve ser no fornato XXXXX-XXX")
    private String postalCodeInstituition;

    @Column(nullable = false)
    @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX")
    private String phoneInstituition;

    @Email
    @Column(nullable = false)
    private String emailInstituition;


    ///// RELACIONAMENTO /////

    // Courses
    @OneToMany(mappedBy = "instituition")
    private List<Course> courses;

    // Certificates
}
