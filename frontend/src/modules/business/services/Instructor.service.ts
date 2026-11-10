/*
=========================================================
Projeto.......: ProofChain
Módulo........: Business / Instrutores
Arquivo.......: Instructor.service.ts
Versão........: 1.0.0

Descrição:
Centraliza as chamadas HTTP do módulo de instrutores.

Dependências:
- authenticatedFetch
- Instructor.types

Observação:
VITE_API_URL deve conter a URL base da API, incluindo /api/v1,
conforme o padrão utilizado pelos serviços do frontend.
=========================================================
*/

import { authenticatedFetch } from '@/modules/auth/services/AuthenticatedFetch'
import type {
  Instructor,
  InstructorCreatePayload,
  InstructorSummary,
  InstructorUpdatePayload,
} from '../types/Instructor.types'

const API_URL = import.meta.env.VITE_API_URL.replace(/\/$/, '')
const INSTRUCTOR_URL = `${API_URL}/instructor`

function isInstructorSummary(value: unknown): value is InstructorSummary {
  if (typeof value !== 'object' || value === null) return false

  const instructor = value as Record<string, unknown>

  return (
    typeof instructor.id === 'number' &&
    Number.isFinite(instructor.id) &&
    typeof instructor.name === 'string' &&
    typeof instructor.email === 'string'
  )
}

/**
 * Aceita uma lista simples ou uma resposta paginada com `content`.
 * Respostas bem-sucedidas fora desses formatos são tratadas como erro.
 */
async function readArrayResponse(response: Response): Promise<InstructorSummary[]> {
  if (response.status === 204) return []

  const body = await response.text()
  if (!body.trim()) {
    throw new Error('A API retornou uma resposta vazia ao listar os instrutores.')
  }

  const parsed: unknown = JSON.parse(body)
  let items: unknown[]

  if (Array.isArray(parsed)) {
    items = parsed
  } else if (typeof parsed === 'object' && parsed !== null && 'content' in parsed) {
    const content = parsed.content
    if (!Array.isArray(content)) {
      throw new Error('A API retornou um formato inválido para a lista de instrutores.')
    }
    items = content
  } else {
    throw new Error('A API retornou um formato inválido para a lista de instrutores.')
  }

  if (!items.every(isInstructorSummary)) {
    throw new Error('A API retornou dados inválidos para a lista de instrutores.')
  }

  return items
}

/** Extrai a mensagem enviada pela API, quando disponível. */
async function getErrorMessage(
  response: Response,
  defaultMessage: string,
): Promise<string> {
  const body = await response.json().catch(() => null) as
    | { message?: string; detail?: string }
    | null

  return body?.message ?? body?.detail
    ?? `${defaultMessage} (${response.status}).`
}

export const instructorService = {
  /**
   * Lista os instrutores.
   *
   * GET /api/v1/instructor/list
   *
   * Atenção: o handler atual do backend retorna somente instrutores
   * ativos. A tela não consegue listar inativos sem ajuste no backend.
   */
  async getAll(token: string): Promise<InstructorSummary[]> {
    const response = await authenticatedFetch(
      `${INSTRUCTOR_URL}/list`,
      {},
      token,
    )

    if (response.status === 204) return []

    if (!response.ok) {
      throw new Error(
        await getErrorMessage(response, 'Não foi possível carregar os instrutores'),
      )
    }

    return readArrayResponse(response)
  },

  /** GET /api/v1/instructor/get/{id} */
  async getById(id: number, token: string): Promise<Instructor> {
    const response = await authenticatedFetch(
      `${INSTRUCTOR_URL}/get/${id}`,
      {},
      token,
    )

    if (!response.ok) {
      throw new Error(
        await getErrorMessage(response, 'Não foi possível consultar o instrutor'),
      )
    }

    return response.json() as Promise<Instructor>
  },

  /** POST /api/v1/instructor/register */
  async create(
    payload: InstructorCreatePayload,
    token: string,
  ): Promise<void> {
    const response = await authenticatedFetch(
      `${INSTRUCTOR_URL}/register`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      },
      token,
    )

    if (!response.ok) {
      throw new Error(
        await getErrorMessage(response, 'Não foi possível cadastrar o instrutor'),
      )
    }
  },

  /** PATCH /api/v1/instructor/update/{id} */
  async update(
    id: number,
    payload: InstructorUpdatePayload,
    token: string,
  ): Promise<void> {
    const response = await authenticatedFetch(
      `${INSTRUCTOR_URL}/update/${id}`,
      {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      },
      token,
    )

    if (!response.ok) {
      throw new Error(
        await getErrorMessage(response, 'Não foi possível atualizar o instrutor'),
      )
    }
  },

  /** DELETE /api/v1/instructor/delete/{id} */
  async delete(id: number, token: string): Promise<void> {
    const response = await authenticatedFetch(
      `${INSTRUCTOR_URL}/delete/${id}`,
      { method: 'DELETE' },
      token,
    )

    if (!response.ok) {
      throw new Error(
        await getErrorMessage(response, 'Não foi possível excluir o instrutor'),
      )
    }
  },
}
