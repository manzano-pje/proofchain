/** Shared domain contracts for administrative entity management. */

import type { AuthRole } from '@/modules/auth/types/Auth.types'

/* --------------------------------------------------------------------------
 * 1. ENUMS E UNION TYPES DE DOMÍNIO
 * ------------------------------------------------------------------------ */

/**
 * Papéis de acesso disponíveis na plataforma ProofChain.
 */
export type UserRole = 'Admin' | 'Usuário'

/**
 * Estados possíveis de um registro administrativo.
 */
export type EntityStatus = 'Ativo' | 'Inativo'

/**
 * Lista imutável de papéis — útil para popular selects e validar payloads.
 */
export const USER_ROLES: readonly UserRole[] = ['Admin', 'Usuário'] as const

/**
 * Lista imutável de estados — útil para popular selects e validar payloads.
 */
export const ENTITY_STATUSES: readonly EntityStatus[] = ['Ativo', 'Inativo'] as const

/* --------------------------------------------------------------------------
 * 2. ENTIDADE PRINCIPAL E DTOs
 * ------------------------------------------------------------------------ */

/**
 * Representa um registro administrativo completo (ex.: Usuário, Participante,
 * Instrutor) já persistido, com metadados de auditoria e vínculos.
 */
export interface EntityItem {
  /** Identificador único do registro (UUID). */
  id: string
  /** Nome completo / razão social da entidade. */
  name: string
  /** E-mail institucional de contato. */
  email: string
  /** Função de acesso na plataforma. */
  role: UserRole
  /** Estado atual do registro. */
  status: EntityStatus
  /** Data de criação em formato ISO 8601. */
  createdAt: string
  /** Data do último acesso em formato ISO 8601. */
  lastAccess: string
  /** Total de certificados emitidos associados a este registro. */
  totalIssuedCertificates: number
}

/** Payload de criação do usuário; a senha não é retornada pela API. */
export type CreateEntityDTO = Pick<EntityItem, 'name' | 'email' | 'role'> & {
  password: string
}

/**
 * Payload de atualização parcial — todos os campos editáveis são opcionais.
 */
export type UpdateEntityDTO = Partial<CreateEntityDTO>

/**
 * Modelo reativo do formulário de cadastro (campos controlados pela UI).
 */
export interface EntityFormModel {
  name: string
  email: string
  role: UserRole | ''
  status: EntityStatus
  password: string
}

/* --------------------------------------------------------------------------
 * 3. TIPAGEM DO MENU LATERAL
 * ------------------------------------------------------------------------ */

/**
 * Categorias macro do menu lateral administrativo.
 */
export type MenuCategory = 'DASHBOARD' | 'GESTÃO' | 'CERTIFICADOS' | 'ADMINISTRAÇÃO'

/**
 * Item navegável do menu lateral.
 */
export interface MenuItemConfig {
  /** Identificador estável do item (usado para controle de estado ativo). */
  id: string
  /** Rótulo exibido ao usuário. */
  label: string
  /** Glifo/ícone representativo. */
  icon: string
  /** Rota de destino no Vue Router. */
  route: string
  /** Categoria a que o item pertence. */
  category: MenuCategory
  /** Contador opcional exibido como badge. */
  badge?: number
  /** Indica se o item está temporariamente indisponível. */
  disabled?: boolean
  allowedRoles?: readonly AuthRole[]
}

/**
 * Agrupamento de itens por categoria — estrutura renderizada na sidebar.
 */
export interface MenuGroupConfig {
  /** Categoria macro do grupo. */
  category: MenuCategory
  /** Rótulo do agrupador. */
  label: string
  /** Itens pertencentes ao grupo. */
  items: MenuItemConfig[]
}

/* --------------------------------------------------------------------------
 * 4. ENVELOPES DE RESPOSTA / FILTROS / FEEDBACK
 * ------------------------------------------------------------------------ */

/**
 * Envelope padrão de resposta paginada da API ProofChain.
 */
export interface PaginatedResponse<T> {
  data: T[]
  total: number
  page: number
  perPage: number
  hasNext: boolean
}

/**
 * Parâmetros de consulta aceitos pela listagem administrativa.
 */
export interface EntityQueryParams {
  search?: string
  role?: UserRole | 'Todos'
  status?: EntityStatus | 'Todos'
  page?: number
  perPage?: number
}

/**
 * Feedback transiente exibido ao operador após uma ação.
 */
export interface ActionFeedback {
  type: 'success' | 'error' | 'info'
  message: string
}

/* --------------------------------------------------------------------------
 * 5. CONSTANTE DE NAVEGAÇÃO (SIDEBAR ADMINISTRATIVA)
 * ------------------------------------------------------------------------ */

/**
 * Estrutura completa do menu lateral administrativo do ProofChain.
 * [LOGIC SWITCH]: Para adicionar novas entidades, basta incluir um novo
 * MenuItemConfig no grupo correspondente — a sidebar renderiza dinamicamente.
 */
export const ADMIN_MENU: readonly MenuGroupConfig[] = [
  {
    category: 'DASHBOARD',
    label: 'Dashboard',
    items: [
      {
        id: 'overview',
        label: 'Visão Geral',
        icon: '◈',
        route: '/admin/dashboard',
        category: 'DASHBOARD',
        allowedRoles: ['Super_admin', 'Admin', 'user'],
      },
    ],
  },
  {
    category: 'GESTÃO',
    label: 'Gestão',
    items: [
      {
        id: 'participants',
        label: 'Participantes',
        icon: '👥',
        route: '/admin/participantes',
        category: 'GESTÃO',
      },
      {
        id: 'courses',
        label: 'Cursos',
        icon: '📚',
        route: '/admin/cursos',
        category: 'GESTÃO',
      },
      {
        id: 'classes',
        label: 'Turmas',
        icon: '🗂️',
        route: '/admin/turmas',
        category: 'GESTÃO',
      },
      {
        id: 'instructors',
        label: 'Instrutores',
        icon: '🎓',
        route: '/admin/instrutores',
        category: 'GESTÃO',
      },
    ],
  },
  {
    category: 'CERTIFICADOS',
    label: 'Certificados',
    items: [
      {
        id: 'certificates',
        label: 'Certificados',
        icon: '📜',
        route: '/admin/certificados',
        category: 'CERTIFICADOS',
        allowedRoles: ['Super_admin', 'Admin', 'user'],
      },
      {
        id: 'generate-certificates',
        label: 'Gerar Certificados',
        icon: '✨',
        route: '/admin/certificados/gerar',
        category: 'CERTIFICADOS',
        allowedRoles: ['Super_admin', 'Admin'],
      },
      {
        id: 'validate',
        label: 'Validar',
        icon: '🔎',
        route: '/admin/certificados/validar',
        category: 'CERTIFICADOS',
        allowedRoles: ['Super_admin', 'Admin', 'user'],
      },
    ],
  },
  {
    category: 'ADMINISTRAÇÃO',
    label: 'Administração',
    items: [
      {
        id: 'institution',
        label: 'Instituição',
        icon: '🏛️',
        route: '/admin/instituicao',
        category: 'ADMINISTRAÇÃO',
        allowedRoles: ['Super_admin', 'Admin'],
      },
      {
        id: 'users',
        label: 'Usuários',
        icon: '👤',
        route: '/admin/usuarios',
        category: 'ADMINISTRAÇÃO',
        allowedRoles: ['Super_admin', 'Admin'],
      },
      {
        id: 'settings',
        label: 'Configurações',
        icon: '⚙️',
        route: '/admin/configuracoes',
        category: 'ADMINISTRAÇÃO',
        allowedRoles: ['Super_admin', 'Admin'],
      },
    ],
  },
] as const

export function getAdminMenuForRole(role: AuthRole): readonly MenuGroupConfig[] {
  return ADMIN_MENU.map((group) => ({
    ...group,
    items: group.items.filter((item) => !item.allowedRoles || item.allowedRoles.includes(role)),
  })).filter((group) => group.items.length > 0)
}

export function canAccessAdminRoute(path: string, role: AuthRole): boolean {
  if (path === '/admin' || path === '/institutionAdmin') return true

  const normalizedPath = path.replace(/^\/institutionAdmin(?=\/)/, '/admin')

  return ADMIN_MENU.some((group) =>
    group.items.some(
      (item) =>
        item.route === normalizedPath && (!item.allowedRoles || item.allowedRoles.includes(role)),
    ),
  )
}

