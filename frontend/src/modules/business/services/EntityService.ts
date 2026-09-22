/** Mock-ready CRUD service for administrative entities. */

// import api from '@/core/services/api'
import type {
  CreateEntityDTO,
  EntityItem,
  EntityQueryParams,
  PaginatedResponse,
  UpdateEntityDTO,
} from '../types/Entity.types'

/* --------------------------------------------------------------------------
 * 1. CONFIGURAÇÃO DO MOCK
 * ------------------------------------------------------------------------ */

/** Latência simulada de rede (ms) aplicada a todas as operações mockadas. */
const MOCK_LATENCY_MS = 550

/** Quantidade máxima de registros considerados "recentes" na mini-tabela. */
const RECENT_ITEMS_LIMIT = 5

/**
 * Resolve após o tempo informado, simulando latência de rede.
 * @param ms Tempo em milissegundos.
 */
const simulateLatency = (ms: number = MOCK_LATENCY_MS): Promise<void> =>
  new Promise((resolve) => setTimeout(resolve, ms))

/**
 * Gera um UUID v4 com fallback para ambientes sem `crypto.randomUUID`.
 * @returns Identificador único em formato string.
 */
const generateId = (): string => {
  if (typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function') {
    return crypto.randomUUID()
  }
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (char) => {
    const random = (Math.random() * 16) | 0
    const value = char === 'x' ? random : (random & 0x3) | 0x8
    return value.toString(16)
  })
}

/**
 * Base de dados em memória utilizada exclusivamente em modo MOCK.
 * [LOGIC SWITCH]: Substituir por dados reais assim que o backend responder.
 */
let mockDatabase: EntityItem[] = [
  {
    id: 'b1f0a6c4-1c2e-4a77-9d31-0a1f2c3d4e50',
    name: 'Ana Beatriz Carvalho',
    email: 'ana.carvalho@proofchain.io',
    role: 'Admin',
    status: 'Ativo',
    createdAt: '2024-11-02T09:14:00.000Z',
    lastAccess: '2025-01-18T14:32:00.000Z',
    totalIssuedCertificates: 482,
  },
  {
    id: 'e4a6d0f8-5f6b-4daa-c064-3d4e5f607183',
    name: 'Rafael Nogueira Pires',
    email: 'rafael.pires@proofchain.io',
    role: 'Usuário',
    status: 'Inativo',
    createdAt: '2024-08-13T07:25:00.000Z',
    lastAccess: '2024-12-28T10:40:00.000Z',
    totalIssuedCertificates: 96,
  },
  {
    id: 'f5b7e1a9-6a7c-4ebb-d175-4e5f60718294',
    name: 'Juliana Ferraz Andrade',
    email: 'juliana.andrade@proofchain.io',
    role: 'Admin',
    status: 'Ativo',
    createdAt: '2024-12-01T13:58:00.000Z',
    lastAccess: '2025-01-18T07:22:00.000Z',
    totalIssuedCertificates: 310,
  },
  {
    id: 'a6c8f2b0-7b8d-4fcc-e286-5f60718293a5',
    name: 'Bruno Tavares Siqueira',
    email: 'bruno.siqueira@proofchain.io',
    role: 'Usuário',
    status: 'Ativo',
    createdAt: '2024-09-27T15:33:00.000Z',
    lastAccess: '2025-01-15T17:49:00.000Z',
    totalIssuedCertificates: 154,
  },
  {
    id: 'b7d9a3c1-8c9e-4add-f397-60718293a4b6',
    name: 'Patrícia Almeida Rocha',
    email: 'patricia.rocha@proofchain.io',
    role: 'Admin',
    status: 'Ativo',
    createdAt: '2025-01-12T10:06:00.000Z',
    lastAccess: '2025-01-12T10:06:00.000Z',
    totalIssuedCertificates: 0,
  },
  {
    id: 'c8e0b4d2-9d0f-4bee-a408-718293a4b5c7',
    name: 'Eduardo Lima Barreto',
    email: 'eduardo.barreto@proofchain.io',
    role: 'Admin',
    status: 'Inativo',
    createdAt: '2024-07-19T18:21:00.000Z',
    lastAccess: '2024-11-30T09:15:00.000Z',
    totalIssuedCertificates: 42,
  },
]

/* --------------------------------------------------------------------------
 * 2. SERVIÇO
 * ------------------------------------------------------------------------ */

export const entityService = {
  /**
   * Recupera os registros mais recentes para exibição na mini-tabela.
   *
   * @param limit Quantidade máxima de registros retornados (default: 5).
   * @returns Lista ordenada por `createdAt` decrescente.
   */
  async fetchRecentItems(limit: number = RECENT_ITEMS_LIMIT): Promise<EntityItem[]> {
    // TODO: [BACKEND INTEGRATION] - Descomentar e ajustar o endpoint quando a API REST estiver disponível
    // const response = await api.get<EntityItem[]>('/api/v1/admin/entities/recent', {
    //   params: { limit },
    // });
    // return response.data;

    await simulateLatency()
    return [...mockDatabase]
      .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
      .slice(0, limit)
  },

  /**
   * Recupera a listagem completa paginada, aplicando filtros de busca,
   * papel e status.
   *
   * @param params Parâmetros de consulta (busca, papel, status, paginação).
   * @returns Envelope paginado com os registros filtrados.
   */
  async fetchAllItems(params: EntityQueryParams = {}): Promise<PaginatedResponse<EntityItem>> {
    // TODO: [BACKEND INTEGRATION] - Descomentar e ajustar o endpoint quando a API REST estiver disponível
    // const response = await api.get<PaginatedResponse<EntityItem>>('/api/v1/admin/entities', {
    //   params: {
    //     search: params.search,
    //     role: params.role,
    //     status: params.status,
    //     page: params.page ?? 1,
    //     per_page: params.perPage ?? 20,
    //   },
    // });
    // return response.data;

    await simulateLatency()

    const { search = '', role = 'Todos', status = 'Todos', page = 1, perPage = 20 } = params

    const term = search.trim().toLowerCase()

    const filtered = mockDatabase.filter((item) => {
      const matchesSearch =
        term.length === 0 ||
        item.name.toLowerCase().includes(term) ||
        item.email.toLowerCase().includes(term)
      const matchesRole = role === 'Todos' || item.role === role
      const matchesStatus = status === 'Todos' || item.status === status
      return matchesSearch && matchesRole && matchesStatus
    })

    const start = (page - 1) * perPage
    const data = filtered.slice(start, start + perPage)

    return {
      data,
      total: filtered.length,
      page,
      perPage,
      hasNext: start + perPage < filtered.length,
    }
  },

  /**
   * Busca os detalhes completos de um registro pelo seu identificador.
   *
   * @param id Identificador único (UUID) do registro.
   * @returns O registro encontrado.
   * @throws Error quando o registro não existe na base.
   */
  async getItemById(id: string): Promise<EntityItem> {
    // TODO: [BACKEND INTEGRATION] - Descomentar e ajustar o endpoint quando a API REST estiver disponível
    // const response = await api.get<EntityItem>(`/api/v1/admin/entities/${id}`);
    // return response.data;

    await simulateLatency()

    const found = mockDatabase.find((item) => item.id === id)
    if (!found) {
      throw new Error(`Registro não encontrado para o id "${id}".`)
    }
    return { ...found }
  },

  /**
   * Cria um novo registro administrativo.
   *
   * @param payload Dados validados do formulário de cadastro.
   * @returns O registro recém-criado, já com metadados de auditoria.
   */
  async createItem(payload: CreateEntityDTO): Promise<EntityItem> {
    // TODO: [BACKEND INTEGRATION] - Descomentar e ajustar o endpoint quando a API REST estiver disponível
    // const response = await api.post<EntityItem>('/api/v1/admin/entities', payload);
    // return response.data;

    await simulateLatency()

    const now = new Date().toISOString()

    const created: EntityItem = {
      id: generateId(),
      name: payload.name.trim(),
      email: payload.email.trim().toLowerCase(),
      role: payload.role,
      status: 'Ativo',
      createdAt: now,
      lastAccess: now,
      totalIssuedCertificates: 0,
    }

    mockDatabase = [created, ...mockDatabase]
    return { ...created }
  },

  /**
   * Atualiza parcialmente um registro existente.
   *
   * @param id Identificador único (UUID) do registro.
   * @param payload Campos a serem atualizados.
   * @returns O registro atualizado.
   * @throws Error quando o registro não existe na base.
   */
  async updateItem(id: string, payload: UpdateEntityDTO): Promise<EntityItem> {
    // TODO: [BACKEND INTEGRATION] - Descomentar e ajustar o endpoint quando a API REST estiver disponível
    // const response = await api.patch<EntityItem>(`/api/v1/admin/entities/${id}`, payload);
    // return response.data;

    await simulateLatency()

    const index = mockDatabase.findIndex((item) => item.id === id)
    if (index === -1) {
      throw new Error(`Não foi possível atualizar: registro "${id}" inexistente.`)
    }

    const currentItem = mockDatabase[index]

    if (!currentItem) {
      throw new Error(`Não foi possível atualizar: registro "${id}" inexistente.`)
    }

    const updated: EntityItem = {
      ...currentItem,
      ...payload,
    }

    mockDatabase = [...mockDatabase.slice(0, index), updated, ...mockDatabase.slice(index + 1)]

    return { ...updated }
  },

  /**
   * Desativa (soft delete) um registro administrativo. O registro permanece
   * na base com status `Inativo` para preservar a trilha de auditoria.
   *
   * @param id Identificador único (UUID) do registro.
   * @returns O registro desativado.
   * @throws Error quando o registro não existe na base.
   */
  async deleteItem(id: string): Promise<EntityItem> {
    // TODO: [BACKEND INTEGRATION] - Descomentar e ajustar o endpoint quando a API REST estiver disponível
    // await api.delete(`/api/v1/admin/entities/${id}`);
    // const response = await api.get<EntityItem>(`/api/v1/admin/entities/${id}`);
    // return response.data;

    await simulateLatency()

    const index = mockDatabase.findIndex((item) => item.id === id)
    if (index === -1) {
      throw new Error(`Não foi possível excluir: registro "${id}" inexistente.`)
    }

    const currentItem = mockDatabase[index]

    if (!currentItem) {
      throw new Error(`Não foi possível excluir: registro "${id}" inexistente.`)
    }

    const deactivated: EntityItem = {
      ...currentItem,
      status: 'Inativo',
    }

    mockDatabase = [...mockDatabase.slice(0, index), deactivated, ...mockDatabase.slice(index + 1)]

    return { ...deactivated }
  },
}

export default entityService
