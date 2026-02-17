package com.proofchain.Dtos.request;

import com.proofchain.identities.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    private boolean isActive;
}
