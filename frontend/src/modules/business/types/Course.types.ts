/**
 * Tipos para o módulo de Cursos.
 * Refletem o contrato real do backend (CourseResponse / FullCourseResponse).
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