package com.proofchain.admin.usageCounter;

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
    private Long id;

    @OneToOne
    private com.proofchain.admin.subscription.Subscriptions Subscriptions;
    private Integer certificatesIssues;     // Número de certificados emitidos
    private Integer courses;                // Número de cursos cadastrados
    private Integer instructors;            // Número de instrutores cadastrados
//    private Integer monthLimit;
    private Instant periodStart;            // Início do período
    private Instant periodEnd;              // Final do período
//    private Instant updatedAt;              //
}
