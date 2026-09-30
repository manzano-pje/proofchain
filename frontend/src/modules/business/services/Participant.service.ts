/*
=========================================================
Project.......: ProofChain
Module........: Business / Participants
Feature.......: Participant API integration
File..........: Participant.service.ts
Version.......: 1.0.0

Description...:
Encapsula as chamadas HTTP do módulo de participantes.

Responsibilities:
- Integrar resumo, detalhe e cadastro com autenticação Bearer.
- Consultar turmas para obter o curso associado mais recente, se o DTO permitir.
- Tratar resposta vazia como coleção vazia, sem erro de interface.
=========================================================
*/

import type {
  CourseClassAssignment,
  ParticipantRequest,
  ParticipantResponse,
  ParticipantSummary,
} from '../types/Participant.types'

const API_URL = import.meta.env.VITE_API_URL.replace(/\/$/, '')
const PARTICIPANT_CREATE_URL = `${API_URL}/participants/register`
const PARTICIPANT_SUMMARY_URL = `${API_URL}/participants/listSumary`
const PARTICIPANT_DETAILS_URL = (id: number): string => `${API_URL}/participants/listOne/${id}`
const COURSE_CLASS_URL = `${API_URL}/couseClass`

export const participantService = {
  /**
   * GET /api/v1/participantslistSumary
   * 404, 204 e corpo vazio representam a grade sem registros.
   */
  async getAll(token: string): Promise<ParticipantSummary[]> {
    const response = await fetch(PARTICIPANT_SUMMARY_URL, {
      headers: { Authorization: `Bearer ${token}` },
    })

    if (response.status === 404 || response.status === 204) return []
    if (!response.ok) {
      throw new Error(`Não foi possível carregar participantes (${response.status}).`)
    }
    return readArrayResponse<ParticipantSummary>(response)
  },

  /** GET /api/v1/participantslistOne/{id} */
  async getById(id: number, token: string): Promise<ParticipantResponse> {
    const response = await fetch(PARTICIPANT_DETAILS_URL(id), {
      headers: { Authorization: `Bearer ${token}` },
    })
    if (!response.ok) {
      throw new Error(`Não foi possível carregar o participante (${response.status}).`)
    }
    return response.json() as Promise<ParticipantResponse>
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
    return readArrayResponse<CourseClassAssignment>(response)
  },

  /** Cria participante; telefone opcional é enviado como null quando vazio. */
  async create(token: string, payload: ParticipantRequest): Promise<void> {
    const requestPayload = {
      ...payload,
      phone: payload.phone.replace(/\D/g, '') || null,
    }

    const response = await fetch(PARTICIPANT_CREATE_URL, {
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

async function readArrayResponse<T>(response: Response): Promise<T[]> {
  if (response.status === 204) return []
  const body = await response.text()
  if (!body.trim()) return []

  const parsedBody: unknown = JSON.parse(body)
  return Array.isArray(parsedBody) ? parsedBody as T[] : []
}
