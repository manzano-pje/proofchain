package com.proofchain.identities;

import com.proofchain.identities.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_login")
@Getter
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Email
    private String email;
    private UserRole usrRole;
    private boolean isActive;

    /////// RELACIONAMENTO ///////
    // user

}
