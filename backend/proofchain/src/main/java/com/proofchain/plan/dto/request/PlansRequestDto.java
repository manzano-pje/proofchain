package com.proofchain.plan.dto.request;

import com.proofchain.identities.enums.BillingType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlansRequestDto {

    @NonNull
    private String name;
    @NonNull
    private double price;
    @NonNull
    private int durationDays;
    @NonNull
    private boolean isActive;
    @NonNull
    private BillingType billingType; // MANUAL, RECURRING
    @NonNull
    private Integer monthlyCertificateLimit;
}
