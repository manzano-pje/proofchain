import { afterEach, expect, it, vi } from 'vitest'
import type { ParticipantRequest } from '../types/Participant.types'

const apiUrl = 'http://localhost:8080/api/v1'

afterEach(() => {
  vi.unstubAllGlobals()
  vi.unstubAllEnvs()
  vi.resetModules()
})

it('sends the backend participant fields on update and the participant id on delete', async () => {
  vi.stubEnv('VITE_API_URL', apiUrl)
  const fetchMock = vi.fn<typeof fetch>().mockResolvedValue(
    new Response(null, { status: 200 }),
  )
  vi.stubGlobal('fetch', fetchMock)

  const { participantService } = await import('./Participant.service')
  const token = 'test-access-token'
  const id = 42
  const payload: ParticipantRequest = {
    name: 'Ana Silva',
    email: 'ana@example.com',
    phone: '(11) 98765-4321',
    cpf: '52998224725',
    address: 'Rua das Flores',
    number: 123,
    complement: 'Bloco B',
    neighborhood: 'Centro',
    city: 'Sao Paulo',
    state: 'SP',
    postalCode: '01001-000',
    isActive: false,
  }

  await participantService.update(token, id, payload)
  await participantService.delete(token, id)

  expect(fetchMock).toHaveBeenNthCalledWith(1, `${apiUrl}/participants/update/${id}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ ...payload, phone: '11987654321' }),
  })
  expect(fetchMock).toHaveBeenNthCalledWith(2, `${apiUrl}/participants/delete/${id}`, {
    method: 'DELETE',
    headers: { Authorization: `Bearer ${token}` },
  })
})
