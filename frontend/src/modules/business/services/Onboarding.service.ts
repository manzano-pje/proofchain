// =========================================================
// Project.......: ProofChain
// Module........: Business
// File..........: Onboarding.service.ts
// Version.......: 1.0.0
//
// Description...:
// Serviço responsável pelo cadastro inicial da instituição.
// Encaminha os dados do onboarding para a API em formato JSON.
// =========================================================

import type { OnboardingRequest } from '../types/OnboardingRequest'

const ONBOARDING_URL = 'http://localhost:8080/api/v1/institution/register'

export const onboardingService = {
  // ---------- CADASTRO DA INSTITUIÇÃO ----------
  async create(request: OnboardingRequest): Promise<String> {
    const response = await fetch(ONBOARDING_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    })

    const message = await response.text()

    if (!response.ok) {
      console.error('Status:', response.status)
      console.error('Resposta do backend:', message)

      throw new Error(message || 'Não foi possível criar a instituição')
    }

    return message
  },
}
