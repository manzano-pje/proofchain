/**
 * Serviço de Cursos.
 * Encapsula as chamadas HTTP para o backend, seguindo o padrão dos
 * demais services do projeto (ex.: Institution.service.ts).
 *
 * A URL base é obtida de VITE_API_URL e não contém duplicação de /api/v1.
 */

import type { Course, CourseCreatePayload, CourseUpdatePayload } from '../types/Course.types'

const API_URL = import.meta.env.VITE_API_URL
const BASE_PATH = '/api/v1/course'

export const courseService = {
  /**
   * GET /api/v1/course/list
   * Retorna todos os cursos da instituição do usuário autenticado.
   */
  async getAll(): Promise<Course[]> {
    const response = await fetch(`${API_URL}${BASE_PATH}/list`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        // O token JWT é gerenciado pelo interceptor global do projeto.
        // Não é necessário adicionar manualmente.
      },
      credentials: 'include', // preserva cookies de sessão, se houver
    })

    if (!response.ok) {
      throw new Error('Não foi possível carregar os cursos.')
    }

    return response.json() as Promise<Course[]>
  },

  /**
   * GET /api/v1/course/list/{id}
   * Retorna um curso específico.
   */
  async getById(id: number): Promise<Course> {
    const response = await fetch(`${API_URL}${BASE_PATH}/list/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    })

    if (!response.ok) {
      throw new Error('Não foi possível carregar o curso.')
    }

    return response.json() as Promise<Course>
  },

  /**
   * POST /api/v1/course/register
   * Cria um novo curso.
   */
  async create(payload: CourseCreatePayload): Promise<Course> {
    const response = await fetch(`${API_URL}${BASE_PATH}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(payload),
    })

    if (!response.ok) {
      throw new Error('Não foi possível salvar o curso.')
    }

    return response.json() as Promise<Course>
  },

  /**
   * PATCH /api/v1/course/update/{id}
   * Atualiza um curso existente.
   */
  async update(id: number, payload: CourseUpdatePayload): Promise<Course> {
    const response = await fetch(`${API_URL}${BASE_PATH}/update/${id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(payload),
    })

    if (!response.ok) {
      throw new Error('Não foi possível atualizar o curso.')
    }

    return response.json() as Promise<Course>
  },

  /**
   * Método de exclusão preparado, mas NÃO chamado nesta tarefa.
   * O backend atual não possui endpoint DELETE de curso validado.
   *
   * async delete(id: number): Promise<void> {
   *   const response = await fetch(`${API_URL}${BASE_PATH}/delete/${id}`, {
   *     method: 'DELETE',
   *     headers: { 'Content-Type': 'application/json' },
   *     credentials: 'include',
   *   })
   *   if (!response.ok) throw new Error('Não foi possível excluir o curso.')
   * },
   */
}