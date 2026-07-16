package com.proofchain.business.model;

//import com.proofchain.auth.AuthenticationStatus;
//import com.proofchain.admin.model.domain.institution.Institution;
//import com.proofchain.business.participant.Participant;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.Instant;
//
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@ToString
//@Table(name = "tb_certificate_authentication")
//public class CertificateAuthentication {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // Data/hora da autenticação
//    @Column(nullable = false)
//    private Instant authenticationDate;
//
//    // Status da autenticação (opcional: ex. SUCESSO, FALHA, REVOGADO)
//    @Enumerated(EnumType.STRING)
//    private AuthenticationStatus authenticationStatus;
//
//    // Qual certificado foi autenticado
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "certificate_id")
//    private Certificate certificate;
//
//    // Qual instituição realizou ou registrou a autenticação (opcional, mas útil para auditoria)
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "institution_id")
//    private Institution institution;
//
//    // Qual usuário autenticou (participante)
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "participant_id")
//    private Participant participant;
//
//}
