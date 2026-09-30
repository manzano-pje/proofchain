/**
 * ============================================================
 * PARTICIPANT SERVICE
 * ============================================================
 * Encapsula chamadas HTTP para o módulo de participante.
 *
 * Endpoint real identificado:
 *   POST /api/v1/participants/register
 *
 * LIMITAÇÃO:
 * O backend não possui endpoints de listagem, busca por ID,
 * atualização ou exclusão. Os métodos correspondentes NÃO
 * foram implementados para evitar invenção de contratos.
 * ============================================================
 */

import type { ParticipantRequest } from '../types/Participant.types'

const PARTICIPANT_URL = `${import.meta.env.VITE_API_URL.replace(/\/$/, '')}/participants`

export const participantService = {
  /** Cria participante; telefone opcional é enviado como null quando vazio. */
  async create(token: string, payload: ParticipantRequest): Promise<void> {
    const requestPayload = {
      ...payload,
      phone: payload.phone.replace(/\D/g, '') || null,
    }

    const response = await fetch(`${PARTICIPANT_URL}/register`, {
      method: 'POST',
      headers: {
    'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(requestPayload),
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => null)
      throw new Error(
        errorData?.message ?? `Erro ao cadastrar participante (${response.status})`,
      )
    }
  },
}
