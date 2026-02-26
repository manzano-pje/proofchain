package com.proofchain.Dtos.response;

import com.proofchain.identities.User;
import com.proofchain.identities.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserReturnDto {

    private String name;
    private String email;
    private UserRole role;
    private boolean active;
    private Instant createAt;
    private Instant updateAt;

    public UserReturnDto(User user) {}
}
