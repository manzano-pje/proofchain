/**
 * Serviço de Cursos.
 * Encapsula as chamadas HTTP para o backend, seguindo o padrão dos
 * demais services do projeto (ex.: Institution.service.ts).
 *
 * VITE_API_URL já inclui /api/v1; este serviço acrescenta apenas /course.
 */

import type { Course, CourseCreatePayload, CourseUpdatePayload } from '../types/Course.types'

const COURSE_URL = `${import.meta.env.VITE_API_URL.replace(/\/$/, '')}/course`

export const courseService = {
  /**
   * GET /api/v1/course/list
   * Retorna todos os cursos da instituição do usuário autenticado.
   */
  async getAll(token: string): Promise<Course[]> {
    const response = await fetch(`${COURSE_URL}/list`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
      credentials: 'include',
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
  async getById(id: number, token: string): Promise<Course> {
    const response = await fetch(`${COURSE_URL}/list/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
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
  async create(token: string, payload: CourseCreatePayload): Promise<Course> {
    const response = await fetch(`${COURSE_URL}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
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
  async update(token: string, id: number, payload: CourseUpdatePayload): Promise<Course> {
    const response = await fetch(`${COURSE_URL}/update/${id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
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
  *   const response = await fetch(`${COURSE_URL}/delete/${id}`, {
   *     method: 'DELETE',
   *     headers: { 'Content-Type': 'application/json' },
   *     credentials: 'include',
   *   })
   *   if (!response.ok) throw new Error('Não foi possível excluir o curso.')
   * },
   */
}
