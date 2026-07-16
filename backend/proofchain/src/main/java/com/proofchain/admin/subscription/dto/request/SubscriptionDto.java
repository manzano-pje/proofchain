package com.proofchain.admin.subscription.dto.request;

import com.proofchain.admin.subscription.StatusSubscription;
import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.plan.domain.model.Plans;
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
    private Institution institution;
    private Plans plans;

}
