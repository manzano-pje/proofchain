package com.proofchain.identities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_usage_counters")
public class UsageCounters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsageCounter;

    @OneToOne
    private Subscriptions Subscriptions;
    private Integer certificatesIssues;
    private Integer monthLimit;
    private Instant periodStart;
    private Instant periodEnd;
    private Instant updatedAt;
}
