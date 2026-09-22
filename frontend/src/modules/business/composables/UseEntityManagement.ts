/** Estado e ações da tela administrativa de gestão de entidades. */

import { computed, onMounted, reactive, ref } from 'vue'
import entityService from '../services/EntityService'
import type {
  ActionFeedback,
  CreateEntityDTO,
  EntityFormModel,
  EntityItem,
  EntityQueryParams,
  UpdateEntityDTO,
} from '../types/Entity.types'
import { USER_ROLES } from '../types/Entity.types'

/* Validation and list configuration. */

/** Expressão regular de validação de e-mail (RFC 5322 simplificada). */
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/

/** Comprimento mínimo aceito para o campo `name`. */
const NAME_MIN_LENGTH = 3

/** Comprimento mínimo aceito para a senha de um novo usuário. */
const PASSWORD_MIN_LENGTH = 8

/** Quantidade máxima de registros mantidos na mini-tabela. */
const RECENT_ITEMS_LIMIT = 8

/**
 * Retorna o estado inicial (limpo) do formulário de cadastro.
 */
const createInitialFormState = (): EntityFormModel => ({
  name: '',
  email: '',
  role: 'Usuário',
  status: 'Ativo',
  password: '',
})

/**
 * Composable de gestão de entidades administrativas do ProofChain.
 *
 * @returns Todo o estado reativo e os handlers consumidos pela view.
 */
export function useEntityManagement() {
  /* State. */

  /** Registros recentes exibidos na mini-tabela. */
  const recentItems = ref<EntityItem[]>([])

  /** Registro atualmente selecionado para detalhamento no modal. */
  const selectedItem = ref<EntityItem | null>(null)

  /** Controla a visibilidade do modal centralizado. */
  const isModalOpen = ref(false)

  /** Controla o estado de carregamento da listagem. */
  const isLoading = ref(false)

  /** Controla o estado de submissão do formulário. */
  const isSubmitting = ref(false)

  /** Termo de busca global do header. */
  const searchQuery = ref('')

  /** Mensagem de feedback transiente (sucesso/erro). */
  const feedback = ref<ActionFeedback | null>(null)

  /** Indica se o item em edição está sendo manipulado no formulário. */
  const editingId = ref<string | null>(null)

  /** Modelo reativo do formulário de cadastro. */
  const formData = reactive<EntityFormModel>(createInitialFormState())

  /** Opções disponíveis para os selects do formulário. */
  const roleOptions = USER_ROLES

  /* Derived state. */

  /**
   * Valida os campos obrigatórios do formulário.
   * [LOGIC SWITCH]: Alterar as regras de validação e mapeamento de campos para
   * Cursos/Turmas/Participantes — basta substituir as checagens abaixo pelas
   * regras específicas da nova entidade.
   */
  const isFormValid = computed<boolean>(() => {
    const name = formData.name.trim()
    const email = formData.email.trim()

    return (
      name.length >= NAME_MIN_LENGTH &&
      EMAIL_REGEX.test(email) &&
      formData.role !== '' &&
      (editingId.value !== null || formData.password.length >= PASSWORD_MIN_LENGTH)
    )
  })

  /** Total de registros atualmente carregados na listagem recente. */
  const totalCount = computed<number>(() => recentItems.value.length)

  /** Total consolidado de certificados emitidos pelos registros listados. */
  const totalIssuedCertificates = computed<number>(() =>
    recentItems.value.reduce((acc, item) => acc + item.totalIssuedCertificates, 0),
  )

  /**
   * Lista recente filtrada pelo termo de busca global do header.
   */
  const filteredItems = computed<EntityItem[]>(() => {
    const term = searchQuery.value.trim().toLowerCase()
    if (!term) return recentItems.value

    return recentItems.value.filter(
      (item) =>
        item.name.toLowerCase().includes(term) ||
        item.email.toLowerCase().includes(term) ||
        item.role.toLowerCase().includes(term) ||
        item.status.toLowerCase().includes(term),
    )
  })

  /** Indica se há resultados para exibir na mini-tabela. */
  const hasResults = computed<boolean>(() => filteredItems.value.length > 0)

  /** Rótulo dinâmico do botão primário do formulário. */
  const submitLabel = computed<string>(() =>
    editingId.value ? 'Salvar Alterações' : '+ Cadastrar',
  )

  /* Helpers. */

  /**
   * Formata uma data ISO 8601 para o padrão brasileiro (dd/mm/aaaa hh:mm).
   * @param isoString Data em formato ISO 8601.
   * @returns Data formatada ou `—` quando inválida.
   */
  const formatDateTime = (isoString: string): string => {
    if (!isoString) return '—'
    const date = new Date(isoString)
    if (Number.isNaN(date.getTime())) return '—'

    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date)
  }

  /**
   * Exibe uma mensagem de feedback e a remove automaticamente após 4s.
   * @param type Tipo do feedback.
   * @param message Conteúdo textual da mensagem.
   */
  const pushFeedback = (type: ActionFeedback['type'], message: string): void => {
    feedback.value = { type, message }
    window.setTimeout(() => {
      feedback.value = null
    }, 4000)
  }

  /** Limpa manualmente a mensagem de feedback. */
  const clearFeedback = (): void => {
    feedback.value = null
  }

  /**
   * Restaura o formulário ao estado inicial e encerra o modo de edição.
   */
  const resetForm = (): void => {
    Object.assign(formData, createInitialFormState())
    editingId.value = null
  }

  /* Data loading. */

  /**
   * Carrega os registros recentes a partir do serviço de integração.
   * [LOGIC SWITCH]: Para outras entidades, trocar `entityService.fetchRecentItems`
   * pelo método correspondente (fetchCourses, fetchClasses, etc.).
   */
  const loadRecentItems = async (): Promise<void> => {
    isLoading.value = true
    try {
      const items = await entityService.fetchRecentItems(RECENT_ITEMS_LIMIT)
      recentItems.value = items
    } catch (error) {
      pushFeedback(
        'error',
        error instanceof Error ? error.message : 'Falha ao carregar os registros recentes.',
      )
    } finally {
      isLoading.value = false
    }
  }

  /**
   * Recarrega a listagem completa aplicando filtros (uso futuro na tela cheia).
   * @param params Parâmetros de consulta opcionais.
   */
  const loadAllItems = async (params: EntityQueryParams = {}): Promise<EntityItem[]> => {
    isLoading.value = true
    try {
      const response = await entityService.fetchAllItems(params)
      recentItems.value = response.data
      return response.data
    } catch (error) {
      pushFeedback(
        'error',
        error instanceof Error ? error.message : 'Falha ao carregar a listagem.',
      )
      return []
    } finally {
      isLoading.value = false
    }
  }

  /* Actions. */

  /**
   * Valida o formulário e persiste um novo registro (ou salva a edição em
   * andamento), atualizando o estado local em seguida.
   */
  const handleCreate = async (): Promise<void> => {
    // [LOGIC SWITCH]: Alterar as regras de validação e mapeamento de campos
    // para Cursos/Turmas/Participantes.
    if (!isFormValid.value) {
      pushFeedback('error', 'Preencha corretamente todos os campos obrigatórios.')
      return
    }

    isSubmitting.value = true

    const payload: CreateEntityDTO = {
      name: formData.name.trim(),
      email: formData.email.trim().toLowerCase(),
      role: formData.role as CreateEntityDTO['role'],
      password: formData.password,
    }

    try {
      if (editingId.value) {
        const updatePayload: UpdateEntityDTO = { ...payload }
        if (!updatePayload.password) delete updatePayload.password

        const updated = await entityService.updateItem(editingId.value, updatePayload)
        recentItems.value = recentItems.value.map((item) =>
          item.id === updated.id ? updated : item,
        )
        pushFeedback('success', `"${updated.name}" atualizado com sucesso.`)
      } else {
        const created = await entityService.createItem(payload)
        recentItems.value = [created, ...recentItems.value].slice(0, RECENT_ITEMS_LIMIT)
        pushFeedback('success', `"${created.name}" cadastrado com sucesso.`)
      }
      resetForm()
    } catch (error) {
      pushFeedback(
        'error',
        error instanceof Error ? error.message : 'Não foi possível concluir a operação.',
      )
    } finally {
      isSubmitting.value = false
    }
  }

  /**
   * Abre o modal centralizado com os detalhes do item selecionado.
   * @param item Registro alvo do detalhamento.
   */
  const handleOpenModal = (item: EntityItem): void => {
    selectedItem.value = { ...item }
    isModalOpen.value = true
  }

  /**
   * Fecha o modal e limpa o item selecionado.
   */
  const handleCloseModal = (): void => {
    isModalOpen.value = false
    selectedItem.value = null
  }

  /**
   * Carrega um item no formulário superior e habilita o modo de edição.
   * @param item Registro a ser editado.
   */
  const handleEdit = (item: EntityItem): void => {
    editingId.value = item.id
    formData.name = item.name
    formData.email = item.email
    formData.role = item.role
    formData.password = ''
    handleCloseModal()
    pushFeedback('info', `Editando "${item.name}". Altere os campos e salve.`)
  }

  /**
   * Desativa um registro após confirmação do operador e atualiza o estado local.
   * @param id Identificador único (UUID) do registro.
   */
  const handleDelete = async (id: string): Promise<void> => {
    const target = recentItems.value.find((item) => item.id === id)
    const label = target ? `"${target.name}"` : 'este registro'

    const confirmed = window.confirm(
      `Tem certeza que deseja excluir ${label}? Esta ação desativará o registro.`,
    )
    if (!confirmed) return

    try {
      const deactivated = await entityService.deleteItem(id)
      recentItems.value = recentItems.value.map((item) =>
        item.id === deactivated.id ? deactivated : item,
      )
      if (selectedItem.value?.id === deactivated.id) {
        handleCloseModal()
      }
      pushFeedback('success', `${label} foi desativado com sucesso.`)
    } catch (error) {
      pushFeedback(
        'error',
        error instanceof Error ? error.message : 'Não foi possível excluir o registro.',
      )
    }
  }

  /**
   * Cancela o modo de edição e limpa o formulário.
   */
  const handleCancelEdit = (): void => {
    resetForm()
    clearFeedback()
  }

  /**
   * Navega para a listagem completa (placeholder de roteamento).
   */
  const handleViewFullList = (): void => {
    // TODO: [ROUTER INTEGRATION] - Substituir por router.push('/admin/usuarios')
    pushFeedback('info', 'Redirecionando para a listagem completa de registros...')
  }

  /* Lifecycle. */

  onMounted(async () => {
    await loadRecentItems()
  })

  /* Public API. */

  return {
    // State
    recentItems,
    selectedItem,
    isModalOpen,
    isLoading,
    isSubmitting,
    searchQuery,
    feedback,
    editingId,
    formData,

    // Options
    roleOptions,

    // Computed
    isFormValid,
    totalCount,
    totalIssuedCertificates,
    filteredItems,
    hasResults,
    submitLabel,

    // Helpers
    formatDateTime,
    clearFeedback,

    // Handlers
    loadRecentItems,
    loadAllItems,
    handleCreate,
    handleOpenModal,
    handleCloseModal,
    handleEdit,
    handleDelete,
    handleCancelEdit,
    handleViewFullList,
  }
}

export default useEntityManagement
