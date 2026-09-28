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
 * Payload emitido em `submit`. O serviço envia os campos de endereço e
 * contato como JSON; os campos de logo aguardam suporte no backend.
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
  /** Arquivo final já cortado, ou `null` quando nenhuma logo nova foi selecionada. */
  logo: File | null
  /** `true` quando o usuário removeu a logo existente sem enviar outra. */
  removeLogo: boolean
}

export type InstitutionFieldErrors = Partial<
  Record<keyof InstitutionEditableFormModel, string>
>
