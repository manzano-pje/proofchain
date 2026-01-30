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

    ///// GETTERS e SETTERS /////


    public UUID getIdInstituition() {
        return idInstituition;
    }

    public void setIdInstituition(UUID idInstituition) {
        this.idInstituition = idInstituition;
    }

    public Long getUserInstituition() {
        return userInstituition;
    }

    public void setUserInstituition(Long userInstituition) {
        this.userInstituition = userInstituition;
    }

    public String getNameInstituition() {
        return nameInstituition;
    }

    public void setNameInstituition(String nameInstituition) {
        this.nameInstituition = nameInstituition;
    }

    public @CNPJ String getCnpjInstituition() {
        return cnpjInstituition;
    }

    public void setCnpjInstituition(@CNPJ String cnpjInstituition) {
        this.cnpjInstituition = cnpjInstituition;
    }

    public @Size(min = 3, message = "O campo rua precisa ter no mínimo 3 caracteres!") String getAddressInstituition() {
        return addressInstituition;
    }

    public void setAddressInstituition(@Size(min = 3, message = "O campo rua precisa ter no mínimo 3 caracteres!") String addressInstituition) {
        this.addressInstituition = addressInstituition;
    }

    public int getNumberInstituition() {
        return numberInstituition;
    }

    public void setNumberInstituition(int numberInstituition) {
        this.numberInstituition = numberInstituition;
    }

    public String getComplementInstituition() {
        return complementInstituition;
    }

    public void setComplementInstituition(String complementInstituition) {
        this.complementInstituition = complementInstituition;
    }

    public String getBairroInstituition() {
        return bairroInstituition;
    }

    public void setBairroInstituition(String bairroInstituition) {
        this.bairroInstituition = bairroInstituition;
    }

    public String getCityInstituition() {
        return cityInstituition;
    }

    public void setCityInstituition(String cityInstituition) {
        this.cityInstituition = cityInstituition;
    }

    public @Size(min = 2, max = 2) String getStateInstituition() {
        return stateInstituition;
    }

    public void setStateInstituition(@Size(min = 2, max = 2) String stateInstituition) {
        this.stateInstituition = stateInstituition;
    }

    public @Pattern(regexp = "\\d{5}-\\d{3}", message = "O cep deve ser no fornato XXXXX-XXX") String getPostalCodeInstituition() {
        return postalCodeInstituition;
    }

    public void setPostalCodeInstituition(@Pattern(regexp = "\\d{5}-\\d{3}", message = "O cep deve ser no fornato XXXXX-XXX") String postalCodeInstituition) {
        this.postalCodeInstituition = postalCodeInstituition;
    }

    public @Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX") String getPhoneInstituition() {
        return phoneInstituition;
    }

    public void setPhoneInstituition(@Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "O telefone deve estar no formato (XX) XXXXX-XXXX") String phoneInstituition) {
        this.phoneInstituition = phoneInstituition;
    }

    public @Email String getEmailInstituition() {
        return emailInstituition;
    }

    public void setEmailInstituition(@Email String emailInstituition) {
        this.emailInstituition = emailInstituition;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
