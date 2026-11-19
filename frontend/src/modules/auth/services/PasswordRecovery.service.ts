// =========================================================
// Project.......: ProofChain
// Module........: Auth
// File..........: PasswordRecovery.service.ts
// Version.......: 1.0.0
//
// Description...:
// Serviço de comunicação para recuperação de senha.
// Os contratos são mantidos isolados da apresentação das telas.
// =========================================================

const USER_URL = `${import.meta.env.VITE_API_URL.replace(/\/$/, '')}/user`

export const RECOVERY_EMAIL_STORAGE_KEY = 'proofchain.password-recovery.email'

async function ensureSuccessfulResponse(response: Response): Promise<void> {
  if (!response.ok) {
    throw new Error(`A solicitação de recuperação falhou (${response.status}).`)
  }
}

export const passwordRecoveryService = {
  /** PATCH /api/v1/user/reset/{email} */
  async requestCode(email: string): Promise<void> {
    const response = await fetch(`${USER_URL}/reset/${encodeURIComponent(email)}`, {
      method: 'PATCH',
    })

    // O endpoint atual usa 404 para e-mails inexistentes; não diferencie esse caso na interface.
    if (response.status === 404) return

    await ensureSuccessfulResponse(response)
  },

  /**
   * POST /api/v1/user/reset/validate
   *
   * TODO: Conectar à tela quando o endpoint validar o código sem alterar a senha.
   * Atualmente o handler tenta gravar a senha durante a validação.
   */
  async validateCode(email: string, token: string): Promise<void> {
    const response = await fetch(`${USER_URL}/reset/validate`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, token }),
    })

    await ensureSuccessfulResponse(response)
  },
}
