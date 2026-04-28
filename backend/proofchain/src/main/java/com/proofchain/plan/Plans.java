package com.proofchain.plan;

import com.proofchain.featurePlan.FeaturePlan;
import com.proofchain.identities.enums.BillingType;
import com.proofchain.subscription.Subscriptions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
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
    private int id;

    private String name;
    private double price;
    private int durationDays;
    private boolean isActive;
    private Instant createdAt;
    private BillingType billingType;            // MANUAL, RECURRING
    private Integer monthlyCertificateLimit;    // Limite de certificados mensais

    @OneToMany(mappedBy = "idPlan")
    private List<FeaturePlan> featurePlans = new ArrayList<>(); // Lista de features

    @OneToMany
    List<Subscriptions> subscriptionsList;

}
