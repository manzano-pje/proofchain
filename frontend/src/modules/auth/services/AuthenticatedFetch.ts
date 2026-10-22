import { useAuthStore } from '../stores/Auth.store'
import type { LoginResponse } from '../types/Auth.types'

const API_BASE_URL = import.meta.env.VITE_API_URL.replace(/\/$/, '')

let refreshPromise: Promise<void> | null = null

function isLoginResponse(value: unknown): value is LoginResponse {
  if (typeof value !== 'object' || value === null) return false

  const response = value as Record<string, unknown>
  const user = response.user

  if (typeof user !== 'object' || user === null) return false

  const userResponse = user as Record<string, unknown>

  return (
    typeof response.token === 'string' &&
    response.token.length > 0 &&
    typeof response.refreshToken === 'string' &&
    response.refreshToken.length > 0 &&
    typeof response.expiresIn === 'number' &&
    Number.isFinite(response.expiresIn) &&
    response.expiresIn > 0 &&
    (typeof userResponse.id === 'string' || typeof userResponse.id === 'number') &&
    typeof userResponse.name === 'string' &&
    typeof userResponse.email === 'string' &&
    typeof userResponse.role === 'string'
  )
}

async function refreshSession(): Promise<void> {
  if (refreshPromise) return refreshPromise

  const authStore = useAuthStore()
  const sessionAtStart = authStore.session
  const refreshToken = sessionAtStart?.refreshToken

  if (!refreshToken) {
    authStore.clearSession()
    throw new Error('Sessão expirada. Faça login novamente.')
  }

  refreshPromise = (async () => {
    const response = await fetch(`${API_BASE_URL}/auth/refresh`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken }),
    })

    if (response.status === 401 || response.status === 403) {
      if (authStore.session?.refreshToken === refreshToken) {
        authStore.clearSession()
      }
      throw new Error('Sessão expirada. Faça login novamente.')
    }

    if (!response.ok) {
      throw new Error(`Não foi possível renovar a sessão (${response.status}).`)
    }

    const body: unknown = await response.json()

    if (!isLoginResponse(body)) {
      if (authStore.session?.refreshToken === refreshToken) {
        authStore.clearSession()
      }
      throw new Error('O servidor retornou dados inválidos ao renovar a sessão.')
    }

    if (authStore.session?.refreshToken !== refreshToken) {
      if (authStore.session) return
      throw new Error('Sessão encerrada durante a renovação. Faça login novamente.')
    }

    // Atualiza também o refresh token, pois o backend emite um novo.
    authStore.setSession(body)
  })().finally(() => {
    refreshPromise = null
  })

  return refreshPromise
}

export async function authenticatedFetch(
  input: RequestInfo | URL,
  init: RequestInit = {},
  fallbackAccessToken?: string,
): Promise<Response> {
  const authStore = useAuthStore()

  const sendRequest = (token: string): Promise<Response> => {
    const headers = new Headers(init.headers)
    headers.set('Authorization', `Bearer ${token}`)

    return fetch(input, { ...init, headers })
  }

  let accessToken = authStore.session?.accessToken ?? fallbackAccessToken

  if (!accessToken) {
    authStore.clearSession()
    throw new Error('Sessão expirada. Faça login novamente.')
  }

  const currentSession = authStore.session
  if (currentSession && currentSession.expiresAt <= Date.now()) {
    if (!currentSession.refreshToken) {
      authStore.clearSession()
      throw new Error('Sessão expirada. Faça login novamente.')
    }

    await refreshSession()
    accessToken = authStore.session?.accessToken
  }

  if (!accessToken) {
    authStore.clearSession()
    throw new Error('Não foi possível obter um token de acesso.')
  }

  const sentAccessToken = accessToken
  let response = await sendRequest(sentAccessToken)

  if (response.status !== 401) return response

  const latestAccessToken = authStore.session?.accessToken
  if (latestAccessToken && latestAccessToken !== sentAccessToken) {
    accessToken = latestAccessToken
  } else {
    await refreshSession()
    accessToken = authStore.session?.accessToken
  }

  if (!accessToken) {
    authStore.clearSession()
    throw new Error('Não foi possível obter um token de acesso.')
  }

  response = await sendRequest(accessToken)

  if (response.status === 401 && authStore.session?.accessToken === accessToken) {
    authStore.clearSession()
  }

  return response
}
