package com.proofchain.subscription;

import com.proofchain.identities.enums.BillingType;
import com.proofchain.identities.enums.StatusSubscription;
import com.proofchain.institution.Institution;
import com.proofchain.payment.Payments;
import com.proofchain.plan.Plans;
import com.proofchain.usageCounter.UsageCounters;
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
    private StatusSubscription statusSubscription;  // Status da assinatura PENDING, ACTIVE, EXPIRED, CANCELED

    @Enumerated(EnumType.STRING)
    private BillingType billingType;        // tipo de cobrança MANUAL OU RECOURRING

    private Instant currentPeriodStarts;    // Data do início da assinatura do pacote atual
    private Instant currentPeriodEnd;       // Data do término da assinatura do pacote atual
    private Instant nextBillingAt;          //
    private Instant schudledPlanChangeAt;   // Data para mudança do pacore

    // Integração Mercado Pago

    private String mercadoPagoPreapprovalId;    //
    private String mercadoPagoCustommerId;      //
    private Instant createdAt;                  // Data de criação da assinatura
    private Instant canceledAt;                 // Data de cancelamento da assinatura

    @ManyToOne
    @JoinColumn(name = "id_institution")
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "id_plan")
    private Plans plans;

    @ManyToOne
    @JoinColumn(name = "id_schedulePlan")
    private Plans schedulePlan; // mudança no próximo ciclo

    @ManyToOne
    @JoinColumn(name = "id_payment")
    private Payments payments;

    @OneToOne
    private UsageCounters usageCounters;
}
