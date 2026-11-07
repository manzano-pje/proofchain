/*
=========================================================
Projeto.......: ProofChain
Módulo........: Business / Instrutores
Arquivo.......: Instructor.types.ts
Descrição.....: Contratos alinhados aos DTOs do backend.
=========================================================
*/

/** Resposta resumida de GET /api/v1/instructor/list. */
export interface InstructorSummary {
  id: number
  name: string
  email: string
}

/** Resposta detalhada de GET /api/v1/instructor/get/{id}. */
export interface Instructor {
  idInstructor: number
  name: string
  email: string
  course: string
  isActive: boolean
  creatAt: string
  deletedAt: string | null
}

/** DTO de POST /api/v1/instructor/register. */
export interface InstructorCreatePayload {
  id_User: number | null
  id_Institution: number | null
  id_course: number | null
}

/** DTO de PATCH /api/v1/instructor/update/{id}. */
export interface InstructorUpdatePayload {
  specialty?: string
  hiringDate?: string
  active: boolean
}
