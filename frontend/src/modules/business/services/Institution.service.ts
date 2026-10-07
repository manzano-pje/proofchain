import type {
  InstitutionEditableInitialData,
  InstitutionReadonlyData,
  InstitutionUpdatePayload,
} from '@/modules/business/views/admin/institution/Types'

export interface InstitutionRecord extends InstitutionReadonlyData, InstitutionEditableInitialData {
  id: number
  active: boolean
}

const URL = `${import.meta.env.VITE_API_URL}/institution`

/**
 * Cria um multipart sem definir manualmente `Content-Type`: o navegador gera o
 * boundary necessário ao transmitir os campos e os arquivos.
 */
function buildUpdateFormData(payload: InstitutionUpdatePayload): FormData {
  const formData = new FormData()

  formData.append('postalCode', payload.postalCode)
  formData.append('phone', payload.phone)
  formData.append('address', payload.address)
  formData.append('number', String(payload.number))
  formData.append('complement', payload.complement)
  formData.append('neighborhood', payload.neighborhood)
  formData.append('city', payload.city)
  formData.append('state', payload.state)
  formData.append('removeLogo', String(payload.removeLogo))
  formData.append('removeSignature', String(payload.removeSignature))

  if (payload.logo) formData.append('logo', payload.logo, payload.logo.name)
  if (payload.signature) {
    formData.append('signature', payload.signature, payload.signature.name)
  }

  return formData
}

async function readResponse(response: Response): Promise<string> {
  const responseText = await response.text()

  if (response.ok) return responseText

  try {
    const responseBody: unknown = JSON.parse(responseText)
    if (typeof responseBody === 'object' && responseBody !== null) {
      const body = responseBody as Record<string, unknown>
      for (const key of ['message', 'detail', 'error', 'description']) {
        if (typeof body[key] === 'string' && body[key]) return body[key] as string
      }
    }
  } catch {
    if (responseText.trim()) throw new Error(responseText.trim())
  }

  throw new Error('Não foi possível concluir a operação da instituição.')
}

export const institutionService = {
  async getCurrent(token: string): Promise<InstitutionRecord> {
    const response = await fetch(`${URL}/get`, {
      headers: { Authorization: `Bearer ${token}` },
    })
    const responseText = await readResponse(response)
    return JSON.parse(responseText) as InstitutionRecord
  },

  /**
   * PATCH /api/v1/institution/update
   * Envia os dados cadastrais e arquivos em multipart/form-data.
   * Campos textuais: postalCode, phone, address, number, complement,
   * neighborhood, city, state, removeLogo e removeSignature.
   * Campos de arquivo opcionais: logo e signature.
   */
  async updateCurrent(token: string, payload: InstitutionUpdatePayload): Promise<void> {
    const response = await fetch(`${URL}/update`, {
      method: 'PATCH',
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: buildUpdateFormData(payload),
    })
    await readResponse(response)
  },
}
