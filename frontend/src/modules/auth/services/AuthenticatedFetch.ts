import { useAuthStore } from '../stores/Auth.store'
import type { LoginResponse } from '../types/Auth.types'

const API_BASE_URL = import.meta.env.VITE_API_URL.replace(/\/$/, '')

let refreshPromise: Promise<void> | null = null

function isLoginResponse(value: unknown): value is LoginResponse {
  if (typeof value !== 'object' || value === null) return false

  const response = value as Record<string, unknown>
  const user = response.user

  return (
    typeof response.token === 'string' &&
    response.token.length > 0 &&
    typeof response.refreshToken === 'string' &&
    response.refreshToken.length > 0 &&
    typeof response.expiresIn === 'number' &&
    Number.isFinite(response.expiresIn) &&
    response.expiresIn > 0 &&
    typeof user === 'object' &&
    user !== null &&
    typeof (user as Record<string, unknown>).role === 'string'
  )
}

async function refreshSession(): Promise<void> {
  if (refreshPromise) return refreshPromise

  const authStore = useAuthStore()
  const refreshToken = authStore.session?.refreshToken

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
      authStore.clearSession()
      throw new Error('Sessão expirada. Faça login novamente.')
    }

    if (!response.ok) {
      throw new Error(`Não foi possível renovar a sessão (${response.status}).`)
    }

    const body: unknown = await response.json()

    if (!isLoginResponse(body)) {
      authStore.clearSession()
      throw new Error('O servidor retornou dados inválidos ao renovar a sessão.')
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

  if (authStore.session?.refreshToken && authStore.session.expiresAt <= Date.now()) {
    await refreshSession()
    accessToken = authStore.session?.accessToken
  }

  if (!accessToken) {
    authStore.clearSession()
    throw new Error('Não foi possível obter um token de acesso.')
  }

  let response = await sendRequest(accessToken)

  if (response.status !== 401) return response

  await refreshSession()

  accessToken = authStore.session?.accessToken
  if (!accessToken) {
    authStore.clearSession()
    throw new Error('Não foi possível obter um token de acesso.')
  }

  response = await sendRequest(accessToken)

  if (response.status === 401) {
    authStore.clearSession()
  }

  return response
}
