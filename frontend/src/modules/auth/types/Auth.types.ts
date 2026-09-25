export type AuthRole = 'Super_admin' | 'Admin' | 'user'

export interface AuthUser {
  id: number | string
  name: string
  email: string
  role: AuthRole
  tenantId?: number | string | null
  permissions?: string[]
}

export interface LoginResponse {
  token: string
  refreshToken?: string
  expiresIn: number
  user: {
    id: number | string
    name: string
    email: string
    role: string
    tenantId?: number | string | null
  }
}

export interface AuthSession {
  accessToken: string
  refreshToken?: string
  expiresAt: number
  user: AuthUser
}

export function normalizeAuthRole(role: string): AuthRole | null {
  const normalizedRole = role.trim().toUpperCase().replace('-', '_')

  if (normalizedRole === 'SUPER_ADMIN' || normalizedRole === 'SUPERADMIN') {
    return 'Super_admin'
  }

  if (normalizedRole === 'ADMIN') {
    return 'Admin'
  }

  if (normalizedRole === 'USER' || normalizedRole === 'USUARIO' || normalizedRole === 'USUÁRIO') {
    return 'user'
  }

  return null
}
