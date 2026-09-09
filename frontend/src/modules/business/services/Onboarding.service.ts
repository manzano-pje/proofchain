import type { OnboardingRequest } from '../types/OnboardingRequest'

// Serviço preparado para futura integração com backend
export const onboardingService = {
  async create(request: OnboardingRequest): Promise<{ success: boolean }> {
    // Simula chamada assíncrona
    console.log('Dados enviados:', request)
    return new Promise((resolve) => {
      setTimeout(() => resolve({ success: true }), 1000)
    })
  },
}
