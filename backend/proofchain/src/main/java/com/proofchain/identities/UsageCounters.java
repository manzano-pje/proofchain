package com.proofchain.identities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name = "id_subscription")
    private Subscriptions Subscriptions;

    private Long usageCount;
}
