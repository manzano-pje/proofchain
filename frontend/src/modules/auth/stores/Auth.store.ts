import { computed, ref } from 'vue'
import { defineStore } from 'pinia'

import type { AuthRole, AuthSession, LoginResponse } from '../types/Auth.types'
import { normalizeAuthRole } from '../types/Auth.types'

const SESSION_STORAGE_KEY = 'proofchain.auth.session'

function readStoredSession(): AuthSession | null {
  const storedSession = localStorage.getItem(SESSION_STORAGE_KEY)

  if (!storedSession) return null

  try {
    const session = JSON.parse(storedSession) as AuthSession

    if (!session.accessToken || !session.user || session.expiresAt <= Date.now()) {
      localStorage.removeItem(SESSION_STORAGE_KEY)
      return null
    }

    return session
  } catch {
    localStorage.removeItem(SESSION_STORAGE_KEY)
    return null
  }
}

export const useAuthStore = defineStore('auth', () => {
  const session = ref<AuthSession | null>(readStoredSession())

  const isAuthenticated = computed(() => Boolean(session.value?.accessToken))
  const user = computed(() => session.value?.user ?? null)
  const role = computed<AuthRole | null>(() => session.value?.user.role ?? null)

  function setSession(response: LoginResponse): void {
    const normalizedRole = normalizeAuthRole(response.user.role)

    if (!normalizedRole) {
      throw new Error('A role recebida pelo backend não é suportada.')
    }

    const nextSession: AuthSession = {
      accessToken: response.accessToken,
      ...(response.refreshToken ? { refreshToken: response.refreshToken } : {}),
      expiresAt: Date.now() + response.expiresIn * 1000,
      user: {
        id: response.user.id,
        name: response.user.name,
        email: response.user.email,
        role: normalizedRole,
        tenantId: response.user.tenantId,
        permissions: response.user.permissions,
      },
    }

    session.value = nextSession
    localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(nextSession))
  }

  function clearSession(): void {
    session.value = null
    localStorage.removeItem(SESSION_STORAGE_KEY)
  }

  return {
    session,
    isAuthenticated,
    user,
    role,
    setSession,
    clearSession,
  }
})
