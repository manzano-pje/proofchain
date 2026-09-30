/**
 * ============================================================
 * PARTICIPANT TYPES
 * ============================================================
 * Tipos derivados do contrato real do backend.
 * 
 * Referência: backend/.../participant/interfaces/dto/request/ParticipantRequest.java
 * 
 * LIMITAÇÃO CONHECIDA:
 * O backend não possui endpoints de listagem, atualização ou exclusão.
 * Portanto, não há Response DTO definido para retorno de dados.
 * ============================================================
 */

/** Payload de criação de participante — espelha ParticipantRequest.java */
export interface ParticipantRequest {
  name: string;
  email: string;
  phone: string;
  cpf: string;
  address: string;
  number: number | null;
  complement: string;
  neighborhood: string;
  city: string;
  state: string;
  postalCode: string;
  isActive: boolean;
}

/** Dados exibidos na grade; clientKey mantém a chave da linha sem expor o ID. */
export interface ParticipantGridItem extends ParticipantRequest {
  id?: number;
  clientKey: string;
  latestCourseName: string;
}

/** Estrutura consumida do endpoint existente de turmas para obter o curso mais recente. */
export interface CourseClassAssignment {
  id: number;
  course?: { id: number; name: string } | null;
  participants?: Array<{ id: number } | number> | null;
  createAt?: string | null;
  updateAt?: string | null;
  isActive?: boolean;
}

/** Estado inicial do formulário (modo create) */
export const emptyParticipant = (): ParticipantRequest => ({
  name: '',
  email: '',
  phone: '',
  cpf: '',
  address: '',
  number: null,
  complement: '',
  neighborhood: '',
  city: '',
  state: '',
  postalCode: '',
  isActive: true,
});