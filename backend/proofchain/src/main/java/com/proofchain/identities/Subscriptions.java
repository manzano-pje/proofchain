package com.proofchain.identities;

import com.proofchain.identities.enums.StatusSubscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


/*
 * Subscrição indica
 * Qual plano está ativo
 * Quando começou
 * Quando expira
 * Se está ativo, cancelado ou expirado
 * Se é recorrente ou não
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tb_subscriptions")
public class Subscriptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSubscription;
//    private Long idInstituition;
//    private Long idPlan;

    @Enumerated(EnumType.STRING)
    private StatusSubscription StatusSubscription;
    private Instant startsAt;
    private Instant expiresAt;
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "id_instituition")
    private Instituition instituition;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private Plans plans;
}
