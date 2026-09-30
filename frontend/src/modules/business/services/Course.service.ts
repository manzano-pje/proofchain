/*
=========================================================
Project.......: ProofChain
Module........: Business / Courses
Feature.......: Course API integration
File..........: Course.service.ts
Version.......: 1.0.0

Description...:
Centraliza as chamadas HTTP do módulo de cursos.

Responsibilities:
- Montar os endpoints relativos a `/api/v1/course`.
- Encaminhar o token Bearer nas rotas protegidas.
- Converter respostas JSON e sinalizar respostas HTTP inválidas.

Dependencies..:
- VITE_API_URL
- Course.types

Notes.........:
VITE_API_URL já inclui `/api/v1`; a base deste serviço acrescenta `/course`.
=========================================================
*/

import type { Course, CourseCreatePayload, CourseUpdatePayload } from '../types/Course.types'

const COURSE_URL = `${import.meta.env.VITE_API_URL.replace(/\/$/, '')}/course`

/* ============================================================
  CONTRATO DE LEITURA
  Confirmar com o backend se lista vazia retorna [] ou status 404.
  ============================================================ */
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
  async create(token: string, payload: CourseCreatePayload): Promise<void> {
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
    // O endpoint responde 201 Created sem corpo; não tentar ler JSON.
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
