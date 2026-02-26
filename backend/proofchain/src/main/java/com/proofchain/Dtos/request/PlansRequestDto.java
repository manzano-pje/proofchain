package com.proofchain.Dtos.request;

import com.proofchain.identities.enums.BillingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlansRequestDto {

    private String name;
    private double price;
    private boolean isActive;
    private BillingType billingType;
    private Integer monthlyCertificateLimit;
    private Instant createdAt;
}
