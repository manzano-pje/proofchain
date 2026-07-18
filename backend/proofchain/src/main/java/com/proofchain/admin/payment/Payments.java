package com.proofchain.admin.payment;

import com.proofchain.admin.subscription.domain.model.Subscriptions;
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
@Table(name="tb_payment")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Subscriptions subscriptions;

    private String mercadoPagoPaymentId;
    private String mercadoPagoPreappovalId;
    private double amount;
    private StatusPayment status;
    private Instant paidAt;

    @Lob
    private String rawPayload; // JSON completo do webhook

    private Instant createdAt;
}
