/*
=========================================================
Projeto.......: ProofChain
Módulo........: Business / Instrutores
Arquivo.......: Instructor.types.ts
Versão........: 1.0.0

Descrição:
Contratos TypeScript alinhados aos DTOs do módulo Instructor
do backend.

Observação:
A listagem resumida e a consulta individual possuem respostas
diferentes. Não presumir que os campos do detalhe estejam
presentes na listagem.
=========================================================
*/

/** Item retornado por GET /api/v1/instructor/list. */
export interface InstructorSummary {
  id: number
  name: string
  email: string
}

/** Resposta de GET /api/v1/instructor/get/{id}. */
export interface Instructor {
  idInstructor: number
  name: string
  email: string
  course: string
  isActive: boolean
  creatAt: string
  deletedAt: string | null
}

/** Payload de POST /api/v1/instructor/register. */
export interface InstructorCreatePayload {
  id_User: number
  id_Institution: number | null
  id_course: number
}

/** Payload aceito por PATCH /api/v1/instructor/update/{id}. */
export interface InstructorUpdatePayload {
  specialty?: string
  hiringDate?: string
  active: boolean
}
