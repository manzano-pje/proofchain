/**
 * Contratos locais do formulário de edição da Instituição.
 * Mantidos fora de `Entity.types.ts` porque o domínio Institution ainda
 * não possui tipo global no projeto.
 */

/** Campos imutáveis nesta tela (§6). Não entram no payload de atualização. */
export interface InstitutionReadonlyData {
  name: string
  cnpj: string
  email: string
}

/** Modelo reativo do formulário — todos os campos como string para simplificar máscaras. */
export interface InstitutionEditableFormModel {
  postalCode: string
  phone: string
  address: string
  number: string
  complement: string
  neighborhood: string
  city: string
  state: string
}

/**
 * Valores iniciais opcionais, aceitos como número ou string para `number`
 * (a entidade backend expõe `number: Integer`, mas a UI trabalha com string).
 */
export interface InstitutionEditableInitialData {
  postalCode?: string
  phone?: string
  address?: string
  number?: string | number
  complement?: string
  neighborhood?: string
  city?: string
  state?: string
}

/**
 * Payload emitido em `submit`. O serviço envia os campos de endereço,
 * contato e imagens como multipart/form-data.
 *
 * NOTA: `id`, `institutionId`, `name`, `cnpj` e `email` NÃO fazem parte
 * deste payload. A instituição é identificada pelo backend a partir do JWT.
 */
export interface InstitutionUpdatePayload {
  postalCode: string
  phone: string
  address: string
  /** Número do endereço como inteiro (coerente com o modelo backend). */
  number: number
  complement: string
  neighborhood: string
  city: string
  state: string
  /** Logotipo final já cortado, ou `null` quando nenhum arquivo novo foi selecionado. */
  logo: File | null
  /** Assinatura final já cortada, ou `null` quando nenhum arquivo novo foi selecionado. */
  signature: File | null
  /** `true` quando o usuário removeu o logotipo existente sem enviar outro. */
  removeLogo: boolean
  /** `true` quando o usuário removeu a assinatura existente sem enviar outra. */
  removeSignature: boolean
}

export type InstitutionFieldErrors = Partial<
  Record<keyof InstitutionEditableFormModel, string>
>
