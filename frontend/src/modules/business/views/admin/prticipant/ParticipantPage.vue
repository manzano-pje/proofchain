<!--
=========================================================
Project.......: ProofChain
Module........: Business / Participants
Feature.......: Participant administration
File..........: ParticipantPage.vue
Version.......: 1.0.0

Description...:
Grade administrativa com busca, filtros, paginação e cadastro.

Responsibilities:
- Consultar cursos recentes via courseclass.
- Carregar a grade pelo endpoint de resumo e manter o ID apenas internamente.
- Abrir modal central para cadastro.
- Deixar ações de edição/exclusão prontas apenas visualmente.

Dependencies..:
- Auth.store
- Participant.service
- ParticipantForm.vue
- BaseButton
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

const authStore = useAuthStore()
const participants = ref<ParticipantGridItem[]>([])
const courseAssignments = ref<CourseClassAssignment[]>([])
const isLoading = ref(false)
const isFormOpen = ref(false)
const isSubmitting = ref(false)
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

const showFeedback = (message: string, type: 'success' | 'error'): void => {
  feedbackMessage.value = message
  feedbackType.value = type
}

const closeFeedback = (): void => {
  feedbackMessage.value = ''
}

const getAccessToken = (): string | null => {
  const token = authStore.session?.accessToken
  if (!token) showFeedback('Sua sessão expirou. Entre novamente para continuar.', 'error')
  return token ?? null
}

const latestCourseForParticipant = (participantId?: number): string => {
  if (participantId == null) return '—'

  // CourseClassReturn currently omits participants; this mapping becomes effective
  // when the existing endpoint includes participant IDs in its response.
  const latestAssignment = courseAssignments.value
    .filter((assignment) => assignment.participants?.some((participant) =>
      typeof participant === 'number' ? participant === participantId : participant.id === participantId,
    ))
    .sort((left, right) => {
      const leftDate = new Date(left.createAt ?? left.updateAt ?? 0).getTime()
      const rightDate = new Date(right.createAt ?? right.updateAt ?? 0).getTime()
      return rightDate - leftDate
    })[0]

  return latestAssignment?.course?.name ?? '—'
}

const courseOptions = computed(() => [...new Set(
  courseAssignments.value
    .map((assignment) => assignment.course?.name)
    .filter((name): name is string => Boolean(name)),
)])
const hasParticipantCourseData = computed(() =>
  participants.value.some((participant) => participant.latestCourseName !== '—'),
)

const filteredParticipants = computed(() => {
  const search = searchTerm.value.trim().toLocaleLowerCase()
  return participants.value.filter((participant) => {
    const matchesSearch = !search || [participant.name, participant.email, participant.cpf]
      .some((value) => value.toLocaleLowerCase().includes(search))
    const matchesCourse = !courseFilter.value || participant.latestCourseName === courseFilter.value
    const matchesStatus = statusFilter.value === 'all'
      || (statusFilter.value === 'active' && participant.isActive)
      || (statusFilter.value === 'inactive' && !participant.isActive)
    return matchesSearch && matchesCourse && matchesStatus
  })
})

const totalPages = computed(() => Math.max(1, Math.ceil(filteredParticipants.value.length / pageSize)))
const paginatedParticipants = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredParticipants.value.slice(start, start + pageSize)
})

const openCreateForm = (): void => {
  formMode.value = 'create'
  editingParticipantId.value = null
  formModel.value = emptyParticipant()
  closeFeedback()
  isFormOpen.value = true
}

const closeForm = (): void => {
  isFormOpen.value = false
}

const loadCourseAssignments = async (): Promise<void> => {
  const token = getAccessToken()
  if (!token) return

  try {
    courseAssignments.value = await participantService.getCourseClassAssignments(token)
    participants.value = participants.value.map((participant) => ({
      ...participant,
      latestCourseName: latestCourseForParticipant(participant.id),
    }))
  } catch (error) {
    showFeedback(error instanceof Error ? error.message : 'Não foi possível carregar as turmas.', 'error')
  }
}

const loadParticipants = async (showErrors = true): Promise<void> => {
  const token = getAccessToken()
  if (!token) return

  try {
    // 404/204 e corpos vazios são convertidos pelo service em uma lista vazia.
    const records = await participantService.getAll(token)
    const persistedParticipants = records.map((record: ParticipantSummary) => ({
      ...emptyParticipant(),
      ...record,
      phone: record.phone ?? '',
      id: record.id,
      clientKey: `participant-${record.id}`,
      latestCourseName: record.latestCourseName || latestCourseForParticipant(record.id),
    }))
    const sessionParticipants = participants.value.filter((participant) =>
      participant.id == null
      && !persistedParticipants.some((record) => record.cpf && record.cpf === participant.cpf),
    )
    participants.value = [...sessionParticipants, ...persistedParticipants]
    currentPage.value = 1
  } catch (error) {
    if (showErrors) {
      showFeedback(error instanceof Error
        ? error.message
        : 'Não foi possível carregar participantes.', 'error')
    }
  }
}

const handleSubmit = async (payload: ParticipantRequest): Promise<void> => {
  closeFeedback()
  const token = getAccessToken()
  if (!token) return

  isSubmitting.value = true
  try {
    if (formMode.value === 'edit' && editingParticipantId.value != null) {
      await participantService.update(token, editingParticipantId.value, payload)
    } else {
      await participantService.create(token, payload)
      const localKey = globalThis.crypto?.randomUUID?.() ?? `session-${Date.now()}`
      participants.value = [
        { ...payload, clientKey: localKey, latestCourseName: '—' },
        ...participants.value,
      ]
    }
    currentPage.value = 1
    showFeedback(
      formMode.value === 'edit' ? 'Participante atualizado com sucesso.' : 'Participante cadastrado com sucesso.',
      'success',
    )
    void loadParticipants(false)
  } catch (error) {
    showFeedback(error instanceof Error ? error.message : 'Não foi possível salvar o participante.', 'error')
  } finally {
    isSubmitting.value = false
  }
}

const onUpdate = async (participant: ParticipantGridItem): Promise<void> => {
  if (participant.id == null) {
    showFeedback('Não foi possível identificar o participante para edição.', 'error')
    return
  }

  const token = getAccessToken()
  if (!token) return

  closeFeedback()
  try {
    formModel.value = await participantService.getById(participant.id, token)
    editingParticipantId.value = participant.id
    formMode.value = 'edit'
    isFormOpen.value = true
  } catch (error) {
    showFeedback(error instanceof Error
      ? error.message
      : 'Não foi possível carregar os dados do participante.', 'error')
  }
}

const onDelete = async (participant: ParticipantGridItem): Promise<void> => {
  if (participant.id == null) {
    showFeedback('Não foi possível identificar o participante para exclusão.', 'error')
    return
  }
  if (!globalThis.confirm(`Deseja excluir o participante ${participant.name}?`)) return

  const token = getAccessToken()
  if (!token) return

  closeFeedback()
  try {
    await participantService.delete(token, participant.id)
    participants.value = participants.value.filter((item) => item.id !== participant.id)
    showFeedback('Participante excluído com sucesso.', 'success')
  } catch (error) {
    showFeedback(error instanceof Error
      ? error.message
      : 'Não foi possível excluir o participante.', 'error')
  }
}

const openParticipantView = async (participant: ParticipantGridItem): Promise<void> => {
  formMode.value = 'view'
  formModel.value = { ...participant }
  isFormOpen.value = true

  if (participant.id == null) return
  const token = getAccessToken()
  if (!token) return

  try {
    formModel.value = await participantService.getById(participant.id, token)
  } catch (error) {
    showFeedback(error instanceof Error
      ? error.message
      : 'Não foi possível carregar os dados do participante.', 'error')
  }
}

onMounted(async () => {
  isLoading.value = true
  await Promise.all([loadCourseAssignments(), loadParticipants()])
  isLoading.value = false
})
</script>

<template>
  <main class="participant-page">
    <header class="participant-page__header">
      <div>
        <h1 class="participant-page__title">Participantes</h1>
        <p class="participant-page__subtitle">Gerencie os participantes e seus cursos.</p>
      </div>
      <BaseButton type="button" variant="primary" @click="openCreateForm">
        + Novo Participante
      </BaseButton>
    </header>

    <section class="participant-page__filters" aria-label="Filtros de participantes">
      <label class="participant-page__search-wrap">
        <span class="participant-page__sr-only">Buscar participantes</span>
        <input
          v-model="searchTerm"
          class="participant-page__search"
          type="search"
          placeholder="Buscar por nome ou e-mail"
          @input="currentPage = 1"
        />
      </label>
      <select v-model="courseFilter" class="participant-page__filter" aria-label="Filtrar por curso" :disabled="!hasParticipantCourseData" @change="currentPage = 1">
        <option value="">Todos os cursos</option>
        <option v-for="course in courseOptions" :key="course" :value="course">{{ course }}</option>
      </select>
      <select v-model="statusFilter" class="participant-page__filter" aria-label="Filtrar por situação" @change="currentPage = 1">
        <option value="all">Todas as situações</option>
        <option value="active">Ativo</option>
        <option value="inactive">Inativo</option>
      </select>
    </section>

    <div v-if="isLoading" class="participant-page__state" role="status">Carregando participantes...</div>
    <div v-else class="participant-page__table-wrap">
      <table class="participant-page__table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>E-mail</th>
            <th>Telefone</th>
            <th>Curso mais recente</th>
            <th>Situação</th>
            <th class="participant-page__actions-heading">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="participant in paginatedParticipants" :key="participant.clientKey">
            <td>{{ participant.name }}</td>
            <td>{{ participant.email }}</td>
            <td>{{ participant.phone || '—' }}</td>
            <td :title="participant.latestCourseName === '—' ? 'O endpoint courseclass ainda não retorna os participantes associados.' : undefined">
              {{ participant.latestCourseName }}
            </td>
            <td>
              <span :class="['participant-page__status', participant.isActive ? 'participant-page__status--active' : 'participant-page__status--inactive']">
                {{ participant.isActive ? 'Ativo' : 'Inativo' }}
              </span>
            </td>
            <td class="participant-page__actions">
              <button type="button" class="participant-page__action" title="Visualizar participante" aria-label="Visualizar participante" @click="openParticipantView(participant)">◉</button>
              <button type="button" class="participant-page__action" title="Editar participante" aria-label="Editar participante" @click="onUpdate(participant)">✎</button>
              <button type="button" class="participant-page__action participant-page__action--danger" title="Excluir participante" aria-label="Excluir participante" @click="onDelete(participant)">×</button>
            </td>
          </tr>
          <tr v-if="paginatedParticipants.length === 0">
            <td colspan="6" class="participant-page__empty">Nenhum participante encontrado.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <footer class="participant-page__table-footer">
      <span>{{ filteredParticipants.length }} participante(s)</span>
      <nav v-if="totalPages > 1" class="participant-page__pagination" aria-label="Paginação">
        <button type="button" :disabled="currentPage === 1" @click="currentPage--">‹</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button type="button" :disabled="currentPage === totalPages" @click="currentPage++">›</button>
      </nav>
    </footer>

    <Teleport to="body">
      <div
        v-if="isFormOpen || feedbackMessage"
        class="participant-page__modal-backdrop"
        @click.self="!feedbackMessage && closeForm()"
        @keydown.esc.stop.prevent="!feedbackMessage && closeForm()"
      >
        <section
          v-if="feedbackMessage"
          class="participant-page__feedback-modal"
          role="alertdialog"
          aria-modal="true"
          aria-labelledby="participant-feedback-title"
        >
          <h2 id="participant-feedback-title">{{ feedbackType === 'error' ? 'Não foi possível concluir' : 'Concluído' }}</h2>
          <p>{{ feedbackMessage }}</p>
          <button type="button" class="participant-page__feedback-ok" @click="closeFeedback">OK</button>
        </section>
        <section v-show="isFormOpen && !feedbackMessage" class="participant-page__modal" role="dialog" aria-modal="true" aria-labelledby="participant-modal-title">
          <header class="participant-page__modal-header">
            <button type="button" class="participant-page__modal-close" aria-label="Fechar" @click="closeForm">×</button>
          </header>
          <div class="participant-page__modal-body">
            <ParticipantForm
              :mode="formMode"
              :initial-data="formModel"
              :submitting="isSubmitting"
              @submit="handleSubmit"
              @validation-error="showFeedback('Confira os campos obrigatórios e corrija os dados destacados.', 'error')"
              @cancel="closeForm"
            />
          </div>
        </section>
      </div>
    </Teleport>
  </main>
</template>

<style scoped src="./ParticipantPage.css"></style>
