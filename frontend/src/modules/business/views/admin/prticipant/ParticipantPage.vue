<!--
=========================================================
Projeto.......: ProofChain
Módulo........: Business / Participantes
Funcionalidade: Administração de participantes
Arquivo.......: ParticipantPage.vue
Versão........: 1.0.0

Descrição:
Página administrativa responsável pela consulta, filtragem,
paginação, cadastro, edição, visualização e exclusão de
participantes da instituição.

Responsabilidades:
- Carregar participantes pelo endpoint de resumo.
- Consultar as turmas para identificar o curso mais recente.
- Filtrar participantes por nome, e-mail, CPF, curso e situação.
- Controlar a paginação da grade.
- Abrir o formulário de cadastro, edição e visualização.
- Abrir e controlar o modal de confirmação de exclusão.
- Executar a exclusão do participante mediante confirmação.
- Recarregar a listagem após operações de persistência.
- Exibir mensagens de sucesso e erro ao usuário.

Dependências:
- Auth.store
- Participant.service
- ParticipantForm.vue
- BaseButton

Metodologia:
BEM
=========================================================
-->

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import ParticipantForm from './ParticipantForm.vue'
import { participantService } from '@/modules/business/services/Participant.service'
import {
  emptyParticipant,
  type CourseClassAssignment,
  type ParticipantGridItem,
  type ParticipantRequest,
  type ParticipantSummary,
} from '@/modules/business/types/Participant.types'

/* ============================================================
   Dependências
   ============================================================ */
const authStore = useAuthStore()

/* ============================================================
   Estado
   ============================================================ */
const participants = ref<ParticipantGridItem[]>([])
const courseAssignments = ref<CourseClassAssignment[]>([])

const isLoading = ref(false)
const isSubmitting = ref(false)

const isFormOpen = ref(false)
const isDeleteModalOpen = ref(false)

const feedbackMessage = ref('')
const feedbackType = ref<'success' | 'error'>('success')

const searchTerm = ref('')
const courseFilter = ref('')
const statusFilter = ref<'all' | 'active' | 'inactive'>('all')

const currentPage = ref(1)
const pageSize = 5

const formModel = ref<ParticipantRequest>(emptyParticipant())
const formMode = ref<'create' | 'edit' | 'view'>('create')

const editingParticipantId = ref<number | null>(null)
const selectedParticipant = ref<ParticipantGridItem | null>(null)

/* ============================================================
   Helpers
   ============================================================ */

/**
 * Exibe uma mensagem de feedback ao usuário.
 */
const showFeedback = (
  message: string,
  type: 'success' | 'error',
): void => {
  feedbackMessage.value = message
  feedbackType.value = type
}

/**
 * Fecha a mensagem de feedback atualmente exibida.
 */
const closeFeedback = (): void => {
  feedbackMessage.value = ''
}

/**
 * Obtém o access token da sessão atual.
 *
 * Quando não existe token, informa ao usuário que a sessão
 * expirou e interrompe a operação solicitada.
 */
const getAccessToken = (): string | null => {
  const token = authStore.session?.accessToken

  if (!token) {
    showFeedback(
      'Sua sessão expirou. Entre novamente para continuar.',
      'error',
    )
  }

  return token ?? null
}

/**
 * Identifica o curso mais recente associado ao participante.
 *
 * A consulta depende dos participantes retornados pelo endpoint
 * de courseclass. Enquanto o endpoint não disponibilizar essa
 * associação, o resultado permanece como "—".
 */
const latestCourseForParticipant = (participantId?: number): string => {
  if (participantId == null) return '—'

  // O endpoint courseclass atualmente pode não retornar os
  // participantes associados à turma. Quando essa informação
  // estiver disponível, este mapeamento será utilizado.
  const latestAssignment = courseAssignments.value
    .filter((assignment) => assignment.participants?.some((participant) =>
      typeof participant === 'number'
        ? participant === participantId
        : participant.id === participantId,
    ))
    .sort((left, right) => {
      const leftDate = new Date(
        left.createAt ?? left.updateAt ?? 0,
      ).getTime()

      const rightDate = new Date(
        right.createAt ?? right.updateAt ?? 0,
      ).getTime()

      return rightDate - leftDate
    })[0]

  return latestAssignment?.course?.name ?? '—'
}

/* ============================================================
   Filtro e paginação
   ============================================================ */

/**
 * Lista de cursos disponíveis para o filtro da grade.
 */
const courseOptions = computed(() => [...new Set(
  courseAssignments.value
    .map((assignment) => assignment.course?.name)
    .filter((name): name is string => Boolean(name)),
)])

/**
 * Indica se existem dados de curso associados aos participantes.
 */
const hasParticipantCourseData = computed(() =>
  participants.value.some(
    (participant) => participant.latestCourseName !== '—',
  ),
)

/**
 * Aplica os filtros de busca, curso e situação.
 */
const filteredParticipants = computed(() => {
  const search = searchTerm.value.trim().toLocaleLowerCase()

  return participants.value.filter((participant) => {
    const matchesSearch =
      !search
      || [participant.name, participant.email, participant.cpf]
        .some((value) => value.toLocaleLowerCase().includes(search))

    const matchesCourse =
      !courseFilter.value
      || participant.latestCourseName === courseFilter.value

    const matchesStatus =
      statusFilter.value === 'all'
      || (
        statusFilter.value === 'active'
        && participant.isActive
      )
      || (
        statusFilter.value === 'inactive'
        && !participant.isActive
      )

    return matchesSearch && matchesCourse && matchesStatus
  })
})

/**
 * Calcula a quantidade total de páginas da grade.
 */
const totalPages = computed(() =>
  Math.max(
    1,
    Math.ceil(filteredParticipants.value.length / pageSize),
  ),
)

/**
 * Retorna somente os participantes correspondentes à página atual.
 */
const paginatedParticipants = computed(() => {
  const start = (currentPage.value - 1) * pageSize

  return filteredParticipants.value.slice(
    start,
    start + pageSize,
  )
})

/* ============================================================
   Formulário — abertura / fechamento
   ============================================================ */

/**
 * Abre o formulário para criação de um novo participante.
 */
const openCreateForm = (): void => {
  formMode.value = 'create'
  editingParticipantId.value = null
  formModel.value = emptyParticipant()

  closeFeedback()

  isFormOpen.value = true
}

/**
 * Fecha o formulário de participante.
 */
const closeForm = (): void => {
  isFormOpen.value = false
}

/* ============================================================
   Leitura
   ============================================================ */

/**
 * Carrega as turmas utilizadas para identificar o curso mais
 * recente de cada participante.
 */
const loadCourseAssignments = async (): Promise<void> => {
  const token = getAccessToken()

  if (!token) return

  try {
    courseAssignments.value =
      await participantService.getCourseClassAssignments(token)

    participants.value = participants.value.map((participant) => ({
      ...participant,
      latestCourseName: latestCourseForParticipant(participant.id),
    }))
  } catch (error) {
    showFeedback(
      error instanceof Error
        ? error.message
        : 'Não foi possível carregar as turmas.',
      'error',
    )
  }
}

/**
 * Carrega os participantes persistidos no backend.
 *
 * Registros que ainda não possuem ID podem permanecer
 * temporariamente na sessão até que a persistência seja
 * confirmada pelo backend.
 */
const loadParticipants = async (
  showErrors = true,
): Promise<void> => {
  const token = getAccessToken()

  if (!token) return

  try {
    // Respostas 404/204 e corpos vazios são convertidos
    // pelo serviço em uma lista vazia.
    const records = await participantService.getAll(token)

    const persistedParticipants = records.map(
      (record: ParticipantSummary) => ({
        ...emptyParticipant(),
        ...record,
        phone: record.phone ?? '',
        id: record.id,
        clientKey: `participant-${record.id}`,
        latestCourseName:
          record.latestCourseName
          || latestCourseForParticipant(record.id),
      }),
    )

    const sessionParticipants = participants.value.filter(
      (participant) =>
        participant.id == null
        && !persistedParticipants.some(
          (record) =>
            record.cpf
            && record.cpf === participant.cpf,
        ),
    )

    participants.value = [
      ...sessionParticipants,
      ...persistedParticipants,
    ]

    currentPage.value = 1
  } catch (error) {
    if (showErrors) {
      showFeedback(
        error instanceof Error
          ? error.message
          : 'Não foi possível carregar participantes.',
        'error',
      )
    }
  }
}

/* ============================================================
   Submissão
   ============================================================ */

/**
 * Salva um participante novo ou atualiza um participante existente.
 */

const handleSubmit = async (
  payload: ParticipantRequest,
): Promise<void> => {
  closeFeedback()

  const token = getAccessToken()

  if (!token) return

  isSubmitting.value = true

  try {
    if (
      formMode.value === 'edit'
      && editingParticipantId.value != null
    ) {
      await participantService.update(
        token,
        editingParticipantId.value,
        payload,
      )
    } else {
      await participantService.create(token, payload)
    }

    // Fecha o formulário antes de atualizar a listagem.
    closeForm()

    // Reinicia a paginação para exibir a listagem atualizada.
    currentPage.value = 1

    // Recarrega os dados persistidos pelo backend.
    await loadParticipants(false)

    showFeedback(
      formMode.value === 'edit'
        ? 'Participante atualizado com sucesso.'
        : 'Participante cadastrado com sucesso.',
      'success',
    )
  } catch (error) {
    // Mantém o formulário aberto para que o usuário possa
    // corrigir os dados e tentar novamente.
    showFeedback(
      error instanceof Error
        ? error.message
        : 'Não foi possível salvar o participante.',
      'error',
    )
  } finally {
    isSubmitting.value = false
  }
}


/* ============================================================
   Edição
   ============================================================ */

/**
 * Abre o formulário de edição com os dados completos
 * retornados pela API.
 */
const onUpdate = async (
  participant: ParticipantGridItem,
): Promise<void> => {
  if (participant.id == null) {
    showFeedback(
      'Não foi possível identificar o participante para edição.',
      'error',
    )
    return
  }

  const token = getAccessToken()

  if (!token) return

  closeFeedback()

  try {
    formModel.value =
      await participantService.getById(
        participant.id,
        token,
      )

    editingParticipantId.value = participant.id
    formMode.value = 'edit'
    isFormOpen.value = true
  } catch (error) {
    showFeedback(
      error instanceof Error
        ? error.message
        : 'Não foi possível carregar os dados do participante.',
      'error',
    )
  }
}

/* ============================================================
   Exclusão
   ============================================================ */

/**
 * Seleciona o participante e abre o modal de confirmação.
 *
 * A exclusão não é executada nesta etapa.
 */
const onDelete = (
  participant: ParticipantGridItem,
): void => {
  console.log('onDelete executado:', participant)

  if (participant.id == null) {
    console.log('Participante sem ID')
    showFeedback(
      'Não foi possível identificar o participante para exclusão.',
      'error',
    )
    return
  }

  selectedParticipant.value = participant
  isDeleteModalOpen.value = true

  console.log('Modal:', isDeleteModalOpen.value)
  console.log('Selecionado:', selectedParticipant.value)
}

/**
 * Fecha o modal de confirmação de exclusão e limpa
 * o participante selecionado.
 */
const closeDeleteModal = (): void => {
  isDeleteModalOpen.value = false
  selectedParticipant.value = null
}

/**
 * Confirma a exclusão do participante selecionado.
 *
 * O registro somente é removido após a confirmação do backend.
 * Depois da exclusão, a listagem é recarregada para manter a
 * interface sincronizada com os dados persistidos.
 */
const confirmDeleteParticipant = async (): Promise<void> => {
  const token = getAccessToken()

  if (!token || !selectedParticipant.value) return

  isSubmitting.value = true

  try {
    await participantService.delete(
      token,
      selectedParticipant.value.id as number,
    )

    closeDeleteModal()

    searchTerm.value = ''
    courseFilter.value = ''
    statusFilter.value = 'all'
    currentPage.value = 1

    await loadParticipants()

    showFeedback(
      'Participante excluído com sucesso.',
      'success',
    )
  } catch (error) {
    showFeedback(
      error instanceof Error
        ? error.message
        : 'Não foi possível excluir o participante.',
      'error',
    )
  } finally {
    isSubmitting.value = false
  }
}

/* ============================================================
   Visualização
   ============================================================ */

/**
 * Abre o formulário no modo somente visualização.
 *
 * Quando o participante possui ID persistido, os dados completos
 * são carregados novamente pela API.
 */
const openParticipantView = async (
  participant: ParticipantGridItem,
): Promise<void> => {
  formMode.value = 'view'
  formModel.value = { ...participant }
  isFormOpen.value = true

  if (participant.id == null) return

  const token = getAccessToken()

  if (!token) return

  try {
    formModel.value =
      await participantService.getById(
        participant.id,
        token,
      )
  } catch (error) {
    showFeedback(
      error instanceof Error
        ? error.message
        : 'Não foi possível carregar os dados do participante.',
      'error',
    )
  }
}

/* ============================================================
   Ciclo de vida
   ============================================================ */

onMounted(async () => {
  isLoading.value = true

  await Promise.all([
    loadCourseAssignments(),
    loadParticipants(),
  ])

  isLoading.value = false
})
</script>

<template>
  <main class="participant-page">
    <!-- Cabeçalho -->
    <header class="participant-page__header">
      <div>
        <h1 class="participant-page__title">Participantes</h1>
        <p class="participant-page__subtitle">
          Gerencie os participantes e seus cursos.
        </p>
      </div>

      <BaseButton
        type="button"
        variant="primary"
        @click="openCreateForm"
      >
        + Novo Participante
      </BaseButton>
    </header>

    <!-- Filtros -->
    <section
      class="participant-page__filters"
      aria-label="Filtros de participantes"
    >
      <label class="participant-page__search-wrap">
        <span class="participant-page__sr-only">
          Buscar participantes
        </span>

        <input
          v-model="searchTerm"
          class="participant-page__search"
          type="search"
          placeholder="Buscar por nome ou e-mail"
          @input="currentPage = 1"
        />
      </label>

      <select
        v-model="courseFilter"
        class="participant-page__filter"
        aria-label="Filtrar por curso"
        :disabled="!hasParticipantCourseData"
        @change="currentPage = 1"
      >
        <option value="">Todos os cursos</option>

        <option
          v-for="course in courseOptions"
          :key="course"
          :value="course"
        >
          {{ course }}
        </option>
      </select>

      <select
        v-model="statusFilter"
        class="participant-page__filter"
        aria-label="Filtrar por situação"
        @change="currentPage = 1"
      >
        <option value="all">Todas as situações</option>
        <option value="active">Ativo</option>
        <option value="inactive">Inativo</option>
      </select>
    </section>

    <!-- Carregamento -->
    <div
      v-if="isLoading"
      class="participant-page__state"
      role="status"
    >
      Carregando participantes...
    </div>

    <!-- Tabela -->
    <div
      v-else
      class="participant-page__table-wrap"
    >
      <table class="participant-page__table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>E-mail</th>
            <th>Telefone</th>
            <th>Curso mais recente</th>
            <th>Situação</th>
            <th class="participant-page__actions-heading">
              Ações
            </th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="participant in paginatedParticipants"
            :key="participant.clientKey"
          >
            <td>{{ participant.name }}</td>
            <td>{{ participant.email }}</td>
            <td>{{ participant.phone || '—' }}</td>

            <td
              :title="
                participant.latestCourseName === '—'
                  ? 'O endpoint courseclass ainda não retorna os participantes associados.'
                  : undefined
              "
            >
              {{ participant.latestCourseName }}
            </td>

            <td>
              <span
                :class="[
                  'participant-page__status',
                  participant.isActive
                    ? 'participant-page__status--active'
                    : 'participant-page__status--inactive',
                ]"
              >
                {{ participant.isActive ? 'Ativo' : 'Inativo' }}
              </span>
            </td>

            <td class="participant-page__actions">
              <button
                type="button"
                class="participant-page__action"
                title="Visualizar participante"
                aria-label="Visualizar participante"
                @click="openParticipantView(participant)"
              >
                ◉
              </button>

              <button
                type="button"
                class="participant-page__action"
                title="Editar participante"
                aria-label="Editar participante"
                @click="onUpdate(participant)"
              >
                ✎
              </button>

              <button
                type="button"
                class="participant-page__action participant-page__action--danger"
                title="Excluir participante"
                aria-label="Excluir participante"
                @click="onDelete(participant)"
              >
                ×
              </button>
            </td>
          </tr>

          <tr v-if="paginatedParticipants.length === 0">
            <td
              colspan="6"
              class="participant-page__empty"
            >
              Nenhum participante encontrado.
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Paginação -->
    <footer class="participant-page__table-footer">
      <span>
        {{ filteredParticipants.length }} participante(s)
      </span>

      <nav
        v-if="totalPages > 1"
        class="participant-page__pagination"
        aria-label="Paginação"
      >
        <button
          type="button"
          :disabled="currentPage === 1"
          @click="currentPage--"
        >
          ‹
        </button>

        <span>
          {{ currentPage }} / {{ totalPages }}
        </span>

        <button
          type="button"
          :disabled="currentPage === totalPages"
          @click="currentPage++"
        >
          ›
        </button>
      </nav>
    </footer>

    <!-- Modais -->
    <Teleport to="body">
      <!-- Modal de formulário -->
      <div
        v-if="isFormOpen"
        class="participant-page__modal-backdrop"
        @click.self="closeForm"
        @keydown.esc.stop.prevent="closeForm"
      >
        <section
          class="participant-page__modal"
          role="dialog"
          aria-modal="true"
          aria-labelledby="participant-modal-title"
        >
          <header class="participant-page__modal-header">
            <button
              type="button"
              class="participant-page__modal-close"
              aria-label="Fechar"
              @click="closeForm"
            >
              ×
            </button>
          </header>

          <div class="participant-page__modal-body">
            <ParticipantForm
              :mode="formMode"
              :initial-data="formModel"
              :submitting="isSubmitting"
              @submit="handleSubmit"
              @validation-error="
                showFeedback(
                  'Confira os campos obrigatórios e corrija os dados destacados.',
                  'error',
                )
              "
              @cancel="closeForm"
            />
          </div>
        </section>
      </div>

      <!-- Modal de exclusão -->
      <div
        v-if="isDeleteModalOpen"
        class="participant-page__delete-overlay"
        @click.self="closeDeleteModal"
      >
        <section
          class="participant-page__delete-modal"
          role="alertdialog"
          aria-modal="true"
          aria-labelledby="participant-delete-title"
          aria-describedby="participant-delete-description"
        >
          <h2
            id="participant-delete-title"
            class="participant-page__delete-title"
          >
            Excluir participante
          </h2>

          <p
            id="participant-delete-description"
            class="participant-page__delete-message"
          >
            Tem certeza que deseja excluir
            <strong>
              {{ selectedParticipant?.name }}
            </strong>?
          </p>

          <p class="participant-page__delete-warning">
            Essa ação não poderá ser desfeita.
          </p>

          <div class="participant-page__delete-actions">
            <button
              type="button"
              class="participant-page__delete-cancel"
              :disabled="isSubmitting"
              @click="closeDeleteModal"
            >
              Cancelar
            </button>

            <button
              type="button"
              class="participant-page__delete-confirm"
              :disabled="isSubmitting"
              @click="confirmDeleteParticipant"
            >
              {{ isSubmitting ? 'Excluindo...' : 'Excluir' }}
            </button>
          </div>
        </section>
      </div>

      <!-- Modal de feedback -->
      <div
        v-if="feedbackMessage"
        class="participant-page__feedback-backdrop"
      >
        <section
          class="participant-page__feedback-modal"
          role="alertdialog"
          aria-modal="true"
          aria-labelledby="participant-feedback-title"
        >
          <h2 id="participant-feedback-title">
            {{
              feedbackType === 'error'
                ? 'Não foi possível concluir'
                : 'Concluído'
            }}
          </h2>

          <p>{{ feedbackMessage }}</p>

          <button
            type="button"
            class="participant-page__feedback-ok"
            @click="closeFeedback"
          >
            OK
          </button>
        </section>
      </div>
    </Teleport>
  </main>
</template>

<style scoped src="./ParticipantPage.css"></style>
