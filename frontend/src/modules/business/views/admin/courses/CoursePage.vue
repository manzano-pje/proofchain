<!--
=========================================================
Project.......: ProofChain
Module........: Business / Courses
Feature.......: Course administration
File..........: CoursePage.vue
Version.......: 1.0.0

Description...:
Página administrativa para consultar e manter os cursos da instituição.

Responsibilities:
- Carregar, filtrar e paginar cursos.
- Orquestrar consulta individual e submissão do formulário.
- Sincronizar o modal de criação com a query `form=create`.

Dependencies..:
- Auth.store
- Course.service
- Formcourse.vue
- BaseButton

Methodology...:
BEM
=========================================================
-->

<template>
  <div class="course-page">
    <!-- Cabeçalho -->
    <header class="course-page__header">
      <h1 class="course-page__title">Cursos</h1>
      <BaseButton class="Buttom" @click="openCreateForm">Novo Curso</BaseButton>
    </header>

    <!-- Pesquisa -->
    <div class="course-page__filters">
      <input
        v-model="searchTerm"
        type="search"
        class="course-page__search"
        placeholder="Procurar por um curso..."
        aria-label="Pesquisar cursos"
      />
    </div>

    <!-- Loading -->
    <div v-if="isLoading" class="course-page__loading">
      Carregando cursos...
    </div>

    <!-- Tabela -->
    <table v-else class="course-page__table">
      <thead>
        <tr>
          <th>Nome</th>
          <th>Descrição</th>
          <th>Carga horária</th>
          <th class="course-page__actions-header">Ações</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="course in paginatedCourses" :key="course.id">
          <td>{{ course.name }}</td>
          <td>{{ course.description }}</td>
          <td>{{ course.hours }}h</td>
          <td class="course-page__actions">
            <button
              class="course-page__action-btn"
              title="Visualizar"
              @click="openViewForm(course)"
            >
              👁
            </button>
            <button
              class="course-page__action-btn"
              title="Editar"
              @click="openEditForm(course)"
            >
              ✏️
            </button>
            <button
              class="course-page__action-btn"
              title="Excluir"
              @click="handleDeleteCourse(course)"
            >
              🗑
            </button>
          </td>
        </tr>
        <tr v-if="paginatedCourses.length === 0">
          <td colspan="4" class="course-page__empty">
            Nenhum curso encontrado.
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Paginação -->
    <nav
      v-if="totalPages > 1"
      class="course-page__pagination"
      aria-label="Paginação de cursos"
    >
      <button
        class="course-page__page-btn"
        :disabled="currentPage === 1"
        @click="currentPage--"
      >
        ‹
      </button>
      <span class="course-page__page-info">
        Página {{ currentPage }} de {{ totalPages }}
      </span>
      <button
        class="course-page__page-btn"
        :disabled="currentPage === totalPages"
        @click="currentPage++"
      >
        ›
      </button>
    </nav>

    <!-- Teleport mantém o backdrop acima do layout administrativo. -->
    <Teleport to="body">
      <div v-if="isFormOpen" class="course-page__form-overlay">
        <div class="course-page__form-container">
          <FormCourse
            :initial-data="selectedCourse"
            :mode="formMode"
            :submitting="isSubmitting"
            @submit="handleFormSubmit"
            @cancel="closeForm"
          />
        </div>
      </div>

      <!-- Modal de confirmação de exclusão -->
      <div
        v-if="isDeleteModalOpen"
        class="course-page__delete-overlay"
        @click.self="closeDeleteModal"
      >
        <section
          class="course-page__delete-modal"
          role="alertdialog"
          aria-modal="true"
          aria-labelledby="course-delete-title"
          aria-describedby="course-delete-description"
        >
          <h2
            id="course-delete-title"
            class="course-page__delete-title"
          >
            Excluir curso
          </h2>

          <p
            id="course-delete-description"
            class="course-page__delete-message"
          >
            Tem certeza que deseja excluir
            <strong>{{ selectedCourse?.name }}</strong>?
          </p>

          <p class="course-page__delete-warning">
            Essa ação não poderá ser desfeita.
          </p>

          <div class="course-page__delete-actions">
            <button
              type="button"
              class="course-page__delete-cancel"
              :disabled="isSubmitting"
              @click="closeDeleteModal"
            >
              Cancelar
            </button>

            <button
              type="button"
              class="course-page__delete-confirm"
              :disabled="isSubmitting"
              @click="confirmDeleteCourse"
            >
              {{ isSubmitting ? 'Excluindo...' : 'Excluir' }}
            </button>
          </div>
        </section>
      </div>
    </Teleport>

    <Teleport to="body">
      <div v-if="feedbackMessage" class="course-page__feedback-backdrop">
        <section class="course-page__feedback-modal" role="alertdialog" aria-modal="true" aria-labelledby="course-feedback-title">
          <h2 id="course-feedback-title">{{ feedbackType === 'error' ? 'Não foi possível concluir' : 'Concluído' }}</h2>
          <p>{{ feedbackMessage }}</p>
          <button type="button" class="course-page__feedback-ok" @click="closeFeedback">OK</button>
        </section>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import FormCourse from './Formcourse.vue'
import { courseService } from '@/modules/business/services/Course.service'
import type {
  Course,
  CourseCreatePayload,
  CourseUpdatePayload,
  FormMode,
} from '@/modules/business/types/Course.types'

/* ============================================================
  ESTADO E SESSÃO
  Token obrigatório: as rotas de cursos exigem autenticação.
  ============================================================ */
const courses = ref<Course[]>([])
const authStore = useAuthStore()
const route = useRoute()
const router = useRouter()
const isLoading = ref(false)
const isSubmitting = ref(false)
const feedbackMessage = ref('')
const feedbackType = ref<'success' | 'error'>('success')

const searchTerm = ref('')
const currentPage = ref(1)
const pageSize = 10

const isFormOpen = ref(false)
const formMode = ref<FormMode>('create')
const selectedCourse = ref<Course | null>(null)

const getAccessToken = (): string | null => {
  const token = authStore.session?.accessToken
  if (!token) {
    showFeedback('Sua sessão expirou. Entre novamente para continuar.', 'error')
    return null
  }
  return token
}

const showFeedback = (message: string, type: 'success' | 'error'): void => {
  feedbackMessage.value = message
  feedbackType.value = type
}

const closeFeedback = (): void => {
  feedbackMessage.value = ''
}

/* ============================================================
  FILTRO E PAGINAÇÃO
  ============================================================ */
const filteredCourses = computed(() => {
  const term = searchTerm.value.trim().toLowerCase()
  if (!term) return courses.value
  return courses.value.filter((c) =>
    c.name.toLowerCase().includes(term),
  )
})

const totalPages = computed(() =>
  Math.max(1, Math.ceil(filteredCourses.value.length / pageSize)),
)

const paginatedCourses = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredCourses.value.slice(start, start + pageSize)
})

/* Reset da página ao pesquisar */
const setSearchTerm = (value: string) => {
  searchTerm.value = value
  currentPage.value = 1
}

/* ============================================================
  LEITURA DA API
  O contrato de GET /course/list deve corresponder ao tipo Course.
  ============================================================ */
const loadCourses = async () => {
  closeFeedback()
  const token = getAccessToken()
  if (!token) return

  isLoading.value = true
  try {
    courses.value = await courseService.getAll(token)
  } catch (err) {
    showFeedback('Não foi possível carregar os cursos.', 'error')
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

/* ============================================================
  ABERTURA E FECHAMENTO DO FORMULÁRIO
  A query `form=create` é removida ao fechar o modal.
  ============================================================ */
const openCreateForm = () => {
  formMode.value = 'create'
  selectedCourse.value = null
  isFormOpen.value = true
}

const openEditForm = async (course: Course) => {
  const token = getAccessToken()
  if (!token) return

  formMode.value = 'edit'
  selectedCourse.value = null
  isFormOpen.value = true
  try {
    selectedCourse.value = await courseService.getById(course.id, token)
  } catch (err) {
    showFeedback('Não foi possível carregar o curso.', 'error')
    console.error(err)
    isFormOpen.value = false
  }
}

const openViewForm = async (course: Course) => {
  const token = getAccessToken()
  if (!token) return

  formMode.value = 'view'
  selectedCourse.value = null
  isFormOpen.value = true
  try {
    selectedCourse.value = await courseService.getById(course.id, token)
  } catch (err) {
    showFeedback('Não foi possível carregar o curso.', 'error')
    console.error(err)
    isFormOpen.value = false
  }
}

const closeForm = () => {
  isFormOpen.value = false
  selectedCourse.value = null
  if (route.query.form === 'create') {
    const query = { ...route.query }
    delete query.form
    void router.replace({ query })
  }
}

watch(
  () => route.query.form,
  (form) => {
    if (form === 'create') {
      openCreateForm()
    } else if (formMode.value === 'create') {
      isFormOpen.value = false
      selectedCourse.value = null
    }
  },
  { immediate: true },
)

/* ============================================================
  SUBMISSÃO E ATUALIZAÇÃO DA LISTA
  Após POST/PATCH bem-sucedido, fechar o modal e reler os dados.
  ============================================================ */
const handleFormSubmit = async (payload: CourseCreatePayload | CourseUpdatePayload) => {
  closeFeedback()
  const token = getAccessToken()
  if (!token) return

  isSubmitting.value = true

  try {
    let confirmationMessage = ''

    if (formMode.value === 'create') {
      await courseService.create(token, payload as CourseCreatePayload)
      confirmationMessage = 'Curso cadastrado com sucesso.'
    } else if (formMode.value === 'edit') {
      const updatePayload = payload as CourseUpdatePayload
      await courseService.update(token, updatePayload.id, updatePayload)
      confirmationMessage = 'Curso atualizado com sucesso.'
    }
      searchTerm.value = ''
      currentPage.value = 1
      await loadCourses()
      if (!feedbackMessage.value) showFeedback(confirmationMessage, 'success')
    } catch (err) {
      showFeedback('Não foi possível salvar o curso.', 'error')
      console.error(err)
    } finally {
      isSubmitting.value = false
  }
}

/* ============================================================
  EXCLUSÃO — aguardando endpoint
  Necessário confirmar contrato e autorização antes de habilitar.
  ============================================================ */
  const isDeleteModalOpen = ref(false)

  const handleDeleteCourse = (course: Course) => {
    selectedCourse.value = course
    isDeleteModalOpen.value = true
  }

  const confirmDeleteCourse = async () => {
    const token = getAccessToken()
    if (!token || !selectedCourse.value) return

    isSubmitting.value = true

    try {
      await courseService.delete(token, selectedCourse.value.id)

      isDeleteModalOpen.value = false
      selectedCourse.value = null

      searchTerm.value = ''
      currentPage.value = 1

      await loadCourses()

      showFeedback('Curso excluído com sucesso.', 'success')
    } catch (err) {
      showFeedback('Não foi possível excluir o curso.', 'error')
      console.error(err)
    } finally {
      isSubmitting.value = false
    }
  }

  const closeDeleteModal = () => {
    isDeleteModalOpen.value = false
    selectedCourse.value = null
  }

/* ============================================================
   CICLO DE VIDA
   ============================================================ */
onMounted(loadCourses)
</script>

<style scoped>
@import './CoursePage.css';
</style>
