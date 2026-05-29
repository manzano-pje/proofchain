package com.proofchain.shared.exception;

public class SubscriptionInactiveException extends BusinessException{
    public SubscriptionInactiveException(String message){
        super(message);
    }
//    Assinatura vencida
//    Plano cancelado
//    Pagamento pendente
//    Plano suspenso
}
