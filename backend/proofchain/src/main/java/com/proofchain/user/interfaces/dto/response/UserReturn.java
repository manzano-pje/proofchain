package com.proofchain.user.interfaces.dto.response;

import com.proofchain.identities.enums.UserRole;
import com.proofchain.user.domain.model.User;

import java.time.Instant;


public record UserReturn(

    String name,
    String email,
    UserRole role,
    boolean active,
    Instant createAt,
    Instant updateAt
){
    public UserReturn(User user)
    {
        this(
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreateAt(),
                user.getUpdateAt()
        );
    }
}
