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

import type {
  CourseClassAssignment,
  ParticipantRequest,
  ParticipantResponse,
} from '../types/Participant.types'

const API_URL = import.meta.env.VITE_API_URL.replace(/\/$/, '')
const PARTICIPANT_URL = `${API_URL}/participants`
const COURSE_CLASS_URL = `${API_URL}/couseClass`

export const participantService = {
  /**
   * Integração preparada para GET /participants/list.
   * A API ainda não oferece esse endpoint; 404 é tratado como lista vazia.
   */
  async getAll(token: string): Promise<ParticipantResponse[]> {
    const response = await fetch(`${PARTICIPANT_URL}/list`, {
      headers: { Authorization: `Bearer ${token}` },
    })

    if (response.status === 404) return []
    if (!response.ok) {
      throw new Error(`Não foi possível carregar participantes (${response.status}).`)
    }
    return response.json() as Promise<ParticipantResponse[]>
  },

  /** Lista as associações de turma para identificar o curso mais recente por participante. */
  async getCourseClassAssignments(token: string): Promise<CourseClassAssignment[]> {
    const response = await fetch(`${COURSE_CLASS_URL}/list`, {
      headers: { Authorization: `Bearer ${token}` },
    })

    if (response.status === 404) return []
    if (!response.ok) {
      throw new Error(`Não foi possível carregar as turmas (${response.status}).`)
    }
    return response.json() as Promise<CourseClassAssignment[]>
  },

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
