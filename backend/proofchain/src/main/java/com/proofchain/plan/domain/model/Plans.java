package com.proofchain.plan.domain.model;

import com.proofchain.featurePlan.domain.model.FeaturePlan;
import com.proofchain.identities.enums.BillingType;
import com.proofchain.subscription.Subscriptions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_plans")
public class Plans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private Integer durationDays;
    private boolean isActive;
    private BillingType billingType;            // MANUAL, RECURRING
    private Integer monthlyCertificateLimit;    // Limite de certificados mensais

    @OneToMany(mappedBy = "idPlan")
    private List<FeaturePlan> featurePlans = new ArrayList<>(); // Lista de features

    @OneToMany
    List<Subscriptions> subscriptionsList;
}
