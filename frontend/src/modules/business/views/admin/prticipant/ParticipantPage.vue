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
- Manter registros criados nesta sessão enquanto o GET não existe.
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
import { emptyParticipant, type CourseClassAssignment, type ParticipantGridItem, type ParticipantRequest } from '@/modules/business/types/Participant.types'

const authStore = useAuthStore()
const participants = ref<ParticipantGridItem[]>([])
const courseAssignments = ref<CourseClassAssignment[]>([])
const isLoading = ref(false)
const isFormOpen = ref(false)
const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')
const searchTerm = ref('')
const courseFilter = ref('')
const statusFilter = ref<'all' | 'active' | 'inactive'>('all')
const currentPage = ref(1)
const pageSize = 5
const formModel = ref<ParticipantRequest>(emptyParticipant())

const getAccessToken = (): string | null => {
  const token = authStore.session?.accessToken
  if (!token) errorMessage.value = 'Sua sessão expirou. Entre novamente para continuar.'
  return token ?? null
}

const latestCourseForParticipant = (participantId?: number): string => {
  if (participantId == null) return '—'

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
  formModel.value = emptyParticipant()
  errorMessage.value = ''
  successMessage.value = ''
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
    errorMessage.value = error instanceof Error ? error.message : 'Não foi possível carregar as turmas.'
  }
}

const loadParticipants = async (): Promise<void> => {
  // Ativar quando GET /api/v1/participants/list for disponibilizado.
  // Resposta vazia ([]) deve permanecer como estado vazio, sem mensagem de erro.
  // const token = getAccessToken()
  // if (!token) return
  // participants.value = await participantService.getAll(token)
}

const handleSubmit = async (payload: ParticipantRequest): Promise<void> => {
  errorMessage.value = ''
  successMessage.value = ''
  const token = getAccessToken()
  if (!token) return

  isSubmitting.value = true
  try {
    await participantService.create(token, payload)
    const localKey = globalThis.crypto?.randomUUID?.() ?? `session-${Date.now()}`
    participants.value = [
      {
        ...payload,
        clientKey: localKey,
        latestCourseName: '—',
      },
      ...participants.value,
    ]
    currentPage.value = 1
    successMessage.value = 'Participante cadastrado com sucesso.'
    closeForm()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Não foi possível salvar o participante.'
  } finally {
    isSubmitting.value = false
  }
}

// Ações apenas visuais enquanto PATCH e DELETE não estiverem disponíveis.
// Futuramente, receberão o id real retornado pelo GET e chamarão os endpoints.
const onUpdatePlaceholder = (_participant: ParticipantGridItem): void => {}
const onDeletePlaceholder = (_participant: ParticipantGridItem): void => {}

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

    <div v-if="successMessage" class="participant-page__feedback participant-page__feedback--success" role="status">
      {{ successMessage }}
    </div>
    <div v-if="errorMessage" class="participant-page__feedback participant-page__feedback--error" role="alert">
      {{ errorMessage }}
    </div>

    <section class="participant-page__filters" aria-label="Filtros de participantes">
      <label class="participant-page__search-wrap">
        <span class="participant-page__sr-only">Buscar participantes</span>
        <input
          v-model="searchTerm"
          class="participant-page__search"
          type="search"
          placeholder="Buscar por nome, e-mail ou CPF..."
          @input="currentPage = 1"
        />
      </label>
      <select v-model="courseFilter" class="participant-page__filter" aria-label="Filtrar por curso" @change="currentPage = 1">
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
            <td>{{ participant.latestCourseName }}</td>
            <td>
              <span :class="['participant-page__status', participant.isActive ? 'participant-page__status--active' : 'participant-page__status--inactive']">
                {{ participant.isActive ? 'Ativo' : 'Inativo' }}
              </span>
            </td>
            <td class="participant-page__actions">
              <button type="button" class="participant-page__action" title="Atualização será integrada quando PATCH estiver disponível" aria-label="Atualizar participante" @click="onUpdatePlaceholder(participant)">✎</button>
              <button type="button" class="participant-page__action participant-page__action--danger" title="Exclusão será integrada quando DELETE estiver disponível" aria-label="Excluir participante" @click="onDeletePlaceholder(participant)">×</button>
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
      <div v-if="isFormOpen" class="participant-page__modal-backdrop" @click.self="closeForm" @keydown.esc.stop.prevent="closeForm">
        <section class="participant-page__modal" role="dialog" aria-modal="true" aria-labelledby="participant-modal-title">
          <header class="participant-page__modal-header">
            <h2 id="participant-modal-title">Novo Participante</h2>
            <button type="button" class="participant-page__modal-close" aria-label="Fechar" @click="closeForm">×</button>
          </header>
          <div class="participant-page__modal-body">
            <ParticipantForm
              mode="create"
              :initial-data="formModel"
              :submitting="isSubmitting"
              @submit="handleSubmit"
              @cancel="closeForm"
            />
          </div>
        </section>
      </div>
    </Teleport>
  </main>
</template>

<style scoped src="./ParticipantPage.css"></style>
