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