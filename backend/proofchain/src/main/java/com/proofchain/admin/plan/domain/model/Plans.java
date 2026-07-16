package com.proofchain.admin.plan.domain.model;

import com.proofchain.admin.featurePlan.domain.model.FeaturePlan;
import com.proofchain.admin.subscription.BillingType;
import com.proofchain.admin.subscription.Subscriptions;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate created_at;

    @OneToMany(mappedBy = "idPlan")
    private List<FeaturePlan> featurePlans = new ArrayList<>(); // Lista de features

    @OneToMany
    List<Subscriptions> subscriptionsList;
}
