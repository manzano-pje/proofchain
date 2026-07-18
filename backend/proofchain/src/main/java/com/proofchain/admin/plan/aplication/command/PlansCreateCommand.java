package com.proofchain.admin.plan.aplication.command;

import com.proofchain.admin.subscription.domain.enuns.BillingType;
import com.proofchain.admin.plan.interfaces.dto.request.CreatePlanRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlansCreateCommand {

    private final String name;
    private final double price;
    private final Integer durationDays;
    private final BillingType billingType; // MANUAL, RECURRING
    private final Integer monthlyCertificateLimit;
    private final boolean isActive;

    public PlansCreateCommand(CreatePlanRequest dto){
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.durationDays = dto.getDurationDays();
        this.billingType = dto.getBillingType();
        this.monthlyCertificateLimit = dto.getMonthlyCertificateLimit();
        this.isActive = dto.isActive();
    }
}
