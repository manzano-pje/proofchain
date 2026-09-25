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

const URL = `${import.meta.env.VITE_API_URL}/api/v1`

export const onboardingService = {
  // ---------- CADASTRO DA INSTITUIÇÃO ----------
  async create(request: OnboardingRequest): Promise<string> {
    const response = await fetch(URL, {
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
