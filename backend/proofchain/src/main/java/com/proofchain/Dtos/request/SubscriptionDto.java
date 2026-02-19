package com.proofchain.Dtos.request;

import com.proofchain.identities.Instituition;
import com.proofchain.identities.Plans;
import com.proofchain.identities.enums.StatusSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionDto {

    private StatusSubscription statusSubscription;
    private Instant startsAt;
    private Instant expiresAt;
    private Instant createdAt;
    private Instituition instituition;
    private Plans plans;

}
