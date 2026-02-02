package com.proofchain.Dtos;

import com.proofchain.identities.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserReturnDto {

    private UUID idUser;
    private String name;
    private String email;
    private UserRole role;
    private boolean active;
    private Instant createAt;
    private Instant updateAt;
}
