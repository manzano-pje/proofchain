import type {
  InstitutionEditableInitialData,
  InstitutionReadonlyData,
  InstitutionUpdatePayload,
} from '@/modules/business/views/admin/institution/Types'

export interface InstitutionRecord extends InstitutionReadonlyData, InstitutionEditableInitialData {
  id: number
  active: boolean
}

const URL = `${import.meta.env.VITE_API_URL}/institution/me`

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
    const response = await fetch(URL, {
      headers: { Authorization: `Bearer ${token}` },
    })
    const responseText = await readResponse(response)
    return JSON.parse(responseText) as InstitutionRecord
  },

  async updateCurrent(token: string, payload: InstitutionUpdatePayload): Promise<void> {
    const response = await fetch(URL, {
      method: 'PATCH',
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        postalCode: payload.postalCode,
        phone: payload.phone,
        address: payload.address,
        number: payload.number,
        complement: payload.complement,
        neighborhood: payload.neighborhood,
        city: payload.city,
        state: payload.state,
      }),
    })
    await readResponse(response)
  },
}
