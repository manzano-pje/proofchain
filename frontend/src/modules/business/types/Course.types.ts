/*
=========================================================
Project.......: ProofChain
Module........: Business / Courses
Feature.......: Course contracts
File..........: Course.types.ts
Version.......: 1.0.0

Description...:
Define os modelos usados pela interface e pela integração com a API.

Responsibilities:
- Representar dados retornados para um curso.
- Tipar payloads de criação e atualização.
- Compartilhar o modo de exibição do formulário.

Notes.........:
Manter estes campos alinhados a CourseResponse e FullCourseResponse.
O ID é necessário para as operações de leitura e edição por registro.
=========================================================
*/

export interface Course {
  id: number
  name: string
  description: string
  hours: number
  institutionId?: number // presente no response completo, mas não editável
  courseClass?: unknown
  certificates?: unknown
  participants?: unknown
}

/** Payload para criação de curso (POST /api/v1/course/register) */
export interface CourseCreatePayload {
  name: string
  description: string
  hours: number
}

/** Payload para atualização de curso (PATCH /api/v1/course/update/{id}) */
export interface CourseUpdatePayload {
  id: number
  name: string
  description: string
  hours: number
}

/** Modos do formulário */
export type FormMode = 'create' | 'edit' | 'view'
