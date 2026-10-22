import type {
  InstitutionEditableInitialData,
  InstitutionReadonlyData,
  InstitutionUpdatePayload,
} from '@/modules/business/views/admin/institution/Types'
import { authenticatedFetch } from '@/modules/auth/services/AuthenticatedFetch'

export interface InstitutionRecord extends InstitutionReadonlyData, InstitutionEditableInitialData {
  id: number
  active: boolean
  logoUrl: string | null
  signatureUrl: string | null
}

const URL = `${import.meta.env.VITE_API_URL}/institution`

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
    const response = await authenticatedFetch(`${URL}/get`, { method: 'GET' }, token)
    const responseText = await readResponse(response)
    return JSON.parse(responseText) as InstitutionRecord
  },

  async updateCurrent(token: string, payload: InstitutionUpdatePayload): Promise<void> {
    const { logo, signature, ...data } = payload

    const body = new FormData()
    body.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }))

    if (logo) body.append('logo', logo)
    if (signature) body.append('signature', signature)

    const response = await authenticatedFetch(`${URL}/update`, { method: 'PATCH', body }, token)
    await readResponse(response)
  },
}
