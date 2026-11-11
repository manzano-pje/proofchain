import { authenticatedFetch } from '@/modules/auth/services/AuthenticatedFetch'
import type { UserOption } from '../types/User.types'

const USER_URL = `${import.meta.env.VITE_API_URL.replace(/\/$/, '')}/user`

function isUserOption(value: unknown): value is UserOption {
  if (typeof value !== 'object' || value === null) return false

  const user = value as Record<string, unknown>

  return (
    typeof user.id === 'number' &&
    Number.isFinite(user.id) &&
    typeof user.name === 'string' &&
    typeof user.email === 'string' &&
    typeof user.role === 'string' &&
    typeof user.active === 'boolean'
  )
}

async function getErrorMessage(response: Response): Promise<string> {
  const body = await response.json().catch(() => null) as
    | { message?: string; detail?: string }
    | null

  return body?.message ?? body?.detail
    ?? `Não foi possível carregar os usuários (${response.status}).`
}

export const userService = {
  /** GET /api/v1/user/list */
  async getAll(token: string): Promise<UserOption[]> {
    const response = await authenticatedFetch(
      `${USER_URL}/list`,
      { method: 'GET' },
      token,
    )

    if (!response.ok) {
      throw new Error(await getErrorMessage(response))
    }

    const users: unknown = await response.json()

    if (!Array.isArray(users) || !users.every(isUserOption)) {
      throw new Error('A API retornou dados inválidos para a lista de usuários.')
    }

    return users
  },
}
