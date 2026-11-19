// =========================================================
// Project.......: ProofChain
// Module........: Auth
// File..........: PasswordRecovery.service.ts
// Version.......: 1.0.0
//
// Description...:
// Serviço de comunicação para recuperação de senha.
// Encaminha as requisições para os endpoints do módulo Auth.
// =========================================================

const AUTH_URL = `${import.meta.env.VITE_API_URL.replace(/\/$/, '')}/auth`

interface ApiError {
  message?: unknown
  detail?: unknown
  error?: unknown
  description?: unknown
}

function getResponseMessage(responseText: string, fallback: string): string {
  const trimmedResponse = responseText.trim()
  if (!trimmedResponse) return fallback

  try {
    const body: unknown = JSON.parse(trimmedResponse)

    if (typeof body === 'string' && body.trim()) return body.trim()

    if (typeof body === 'object' && body !== null) {
      const errorBody = body as ApiError
      for (const key of ['message', 'detail', 'error', 'description'] as const) {
        const message = errorBody[key]
        if (typeof message === 'string' && message.trim()) return message.trim()
      }
    }
  } catch {
    return trimmedResponse
  }

  return fallback
}

async function sendRequest(path: string, init: RequestInit, fallback: string): Promise<void> {
  const response = await fetch(`${AUTH_URL}${path}`, init)
  const responseText = await response.text()

  if (!response.ok) {
    throw new Error(getResponseMessage(responseText, fallback))
  }
}

export const passwordRecoveryService = {
  /** PATCH /api/v1/auth/reset/{email} */
  async requestCode(email: string): Promise<void> {
    await sendRequest(
      `/reset/${encodeURIComponent(email)}`,
      { method: 'PATCH' },
      'Não foi possível solicitar o código de recuperação.',
    )
  },

  /** POST /api/v1/auth/reset/validate */
  async validateCode(email: string, token: string): Promise<void> {
    await sendRequest(
      '/reset/validate',
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, token }),
      },
      'Não foi possível validar o código informado.',
    )
  },

  /** PATCH /api/v1/auth/newPassword */
  async updatePassword(email: string, token: string, password: string): Promise<void> {
    await sendRequest(
      '/newPassword',
      {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, token, password }),
      },
      'Não foi possível alterar a senha.',
    )
  },
}
