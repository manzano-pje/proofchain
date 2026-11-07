/*
=========================================================
Projeto.......: ProofChain
Módulo........: Business / Instrutores
Arquivo.......: Instructor.service.ts
Descrição.....: Integração HTTP com /api/v1/instructor.
=========================================================
*/
import { authenticatedFetch } from '@/modules/auth/services/AuthenticatedFetch'
import type {
  Instructor,
  InstructorCreatePayload,
  InstructorSummary,
  InstructorUpdatePayload,
} from '../types/Instructor.types'

const API_URL = `${import.meta.env.VITE_API_URL.replace(/\/$/, '')}/instructor`

async function readArray<T>(response: Response): Promise<T[]> {
  if (response.status === 204 || response.status === 404) return []
  const body = await response.text()
  if (!body.trim()) return []
  const parsed: unknown = JSON.parse(body)
  if (Array.isArray(parsed)) return parsed as T[]
  if (typeof parsed === 'object' && parsed !== null && 'content' in parsed && Array.isArray(parsed.content)) {
    return parsed.content as T[]
  }
  return []
}

export const instructorService = {
  /** GET /api/v1/instructor/list — backend retorna apenas instrutores ativos. */
  async getAll(token: string): Promise<InstructorSummary[]> {
    const response = await authenticatedFetch(`${API_URL}/list`, {}, token)
    if (!response.ok && response.status !== 404) {
      throw new Error(`Não foi possível carregar os instrutores (${response.status}).`)
    }
    return readArray<InstructorSummary>(response)
  },

  /** GET /api/v1/instructor/get/{id} */
  async getById(id: number, token: string): Promise<Instructor> {
    const response = await authenticatedFetch(`${API_URL}/get/${id}`, {}, token)
    if (!response.ok) throw new Error(`Não foi possível consultar o instrutor (${response.status}).`)
    return response.json() as Promise<Instructor>
  },

  /** POST /api/v1/instructor/register */
  async create(token: string, payload: InstructorCreatePayload): Promise<void> {
    const response = await authenticatedFetch(`${API_URL}/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    }, token)
    if (!response.ok) {
      const errorData = await response.json().catch(() => null)
      throw new Error(errorData?.message ?? `Não foi possível cadastrar o instrutor (${response.status}).`)
    }
  },

  /** PATCH /api/v1/instructor/update/{id} — no momento, somente active é aplicado no handler. */
  async update(token: string, id: number, payload: InstructorUpdatePayload): Promise<void> {
    const response = await authenticatedFetch(`${API_URL}/update/${id}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    }, token)
    if (!response.ok) throw new Error(`Não foi possível atualizar o instrutor (${response.status}).`)
  },

  /** DELETE /api/v1/instructor/delete/{id} */
  async delete(token: string, id: number): Promise<void> {
    const response = await authenticatedFetch(`${API_URL}/delete/${id}`, { method: 'DELETE' }, token)
    if (!response.ok) throw new Error(`Não foi possível excluir o instrutor (${response.status}).`)
  },
}
