package com.proofchain.admin.subscription.domain.model;

import com.proofchain.admin.institution.domain.model.Institution;
import com.proofchain.admin.payment.Payments;
import com.proofchain.admin.plan.domain.model.Plans;
import com.proofchain.admin.subscription.domain.enuns.BillingType;
import com.proofchain.admin.subscription.domain.enuns.StatusSubscription;
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
    private Long id;
//    private Long idInstituition;
//    private Long idPlan;

    @Enumerated(EnumType.STRING)
    private StatusSubscription status;    // Status da assinatura PENDING, ACTIVE, EXPIRED, CANCELED

    @Enumerated(EnumType.STRING)
    private BillingType billingType;        // tipo de cobrança MANUAL OU RECOURRING

    private Instant currentPeriodStarts;    // Data do início da assinatura do pacote atual
    private Instant currentPeriodEnd;       // Data do término da assinatura do pacote atual
    private Instant nextBillingAt;          // Data da próxima cobrannça
    private Instant schudledPlanChangeAt;   // Data para mudança do pacore

    // Integração Mercado Pago

    private String mercadoPagoPreapprovalId;    //
    private String mercadoPagoCustommerId;      //

    @ManyToOne
    @JoinColumn(name = "institution_id")
    private Institution institution;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plans plans;

    @ManyToOne
    @JoinColumn(name = "schedulePlan_id")
    private Plans schedulePlan; // mudança no próximo ciclo

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payments payments;

}
