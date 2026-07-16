package com.proofchain.admin.plan.interfaces.dto.request;

import com.proofchain.admin.subscription.BillingType;
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
    private Integer durationDays;
    @NonNull
    private BillingType billingType; // MANUAL, RECURRING
    @NonNull
    private Integer monthlyCertificateLimit;
    @NonNull
    private boolean isActive;
}
