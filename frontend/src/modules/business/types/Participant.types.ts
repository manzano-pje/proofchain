/*
=========================================================
Project.......: ProofChain
Module........: Business / Participants
Feature.......: Participant data contracts
File..........: Participant.types.ts
Version.......: 1.0.0

Description...:
Tipa o payload de criação e as estruturas internas da grade.

Notes.........:
O POST atual não devolve ID. clientKey identifica apenas linhas locais da sessão.
ParticipantResponse está preparado para o futuro GET, mas não é consumido ainda.
=========================================================
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

/** Campos consumidos pelo endpoint de resumo usado na grade. */
export interface ParticipantSummary {
  id: number;
  name: string;
  email: string;
  phone?: string | null;
  isActive: boolean;
  latestCourseName?: string | null;
}

/** Resposta do endpoint de detalhe; o ID é interno e não é apresentado como campo. */
export interface ParticipantResponse extends ParticipantRequest {
  id: number;
  latestCourseName?: string | null;
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
