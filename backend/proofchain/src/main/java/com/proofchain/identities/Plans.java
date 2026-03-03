package com.proofchain.identities;

import com.proofchain.identities.enums.BillingType;
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
    private boolean isActive;
    private Instant createdAt;
    private BillingType billingType;
    private Integer monthlyCertificateLimit;

    @OneToMany(mappedBy = "idPlan")
    private List<FeaturePlan> featurePlans = new ArrayList<>();

    @OneToMany
    List<Subscriptions> subscriptionsList;

}
