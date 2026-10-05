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

    <!-- Formulário -->
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

      <!-- Modal de exclusão -->
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
          <h2 id="course-delete-title" class="course-page__delete-title">
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

      <!-- Modal de feedback -->
      <div v-if="feedback" class="course-page__feedback-backdrop">
        <section
          class="course-page__feedback-modal"
          role="alertdialog"
          aria-modal="true"
          aria-labelledby="course-feedback-title"
        >
          <h2 id="course-feedback-title">
            {{ feedback.type === 'error' ? 'Não foi possível concluir' : 'Concluído' }}
          </h2>
          <p>{{ feedback.message }}</p>
          <button
            type="button"
            class="course-page__feedback-ok"
            @click="dismissFeedback"
          >
            OK
          </button>
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
   Dependências
   ============================================================ */
const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

/* ============================================================
   Estado
   ============================================================ */
const courses = ref<Course[]>([])
const isLoading = ref(false)
const isSubmitting = ref(false)

const searchTerm = ref('')
const currentPage = ref(1)
const pageSize = 10

const isFormOpen = ref(false)
const formMode = ref<FormMode>('create')
const selectedCourse = ref<Course | null>(null)
const isDeleteModalOpen = ref(false)

type Feedback = { message: string; type: 'success' | 'error' }
const feedback = ref<Feedback | null>(null)

/* ============================================================
   Helpers
   ============================================================ */
const notify = (message: string, type: Feedback['type'] = 'success') => {
  feedback.value = { message, type }
}

const dismissFeedback = () => {
  feedback.value = null
}

/**
 * Devolve o access token da sessão ou notifica o usuário e retorna `null`.
 */
const requireToken = (): string | null => {
  const token = authStore.session?.accessToken
  if (!token) {
    notify('Sua sessão expirou. Entre novamente para continuar.', 'error')
    return null
  }
  return token
}

/* ============================================================
   Filtro e paginação
   ============================================================ */
const filteredCourses = computed(() => {
  const term = searchTerm.value.trim().toLowerCase()
  if (!term) return courses.value
  return courses.value.filter((course) =>
    course.name.toLowerCase().includes(term),
  )
})

const totalPages = computed(() =>
  Math.max(1, Math.ceil(filteredCourses.value.length / pageSize)),
)

const paginatedCourses = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredCourses.value.slice(start, start + pageSize)
})

// Reset automático de página sempre que o filtro muda.
watch(searchTerm, () => {
  currentPage.value = 1
})

/* ============================================================
   Leitura
   ============================================================ */
const loadCourses = async () => {
  const token = requireToken()
  if (!token) return

  isLoading.value = true
  try {
    courses.value = await courseService.getAll(token)
  } catch (err) {
    console.error(err)
    notify('Não foi possível carregar os cursos.', 'error')
  } finally {
    isLoading.value = false
  }
}

/* ============================================================
   Formulário — abertura / fechamento
   ============================================================ */
const openForm = (mode: FormMode, course: Course | null = null) => {
  formMode.value = mode
  selectedCourse.value = course
  isFormOpen.value = true
}

const openCreateForm = () => openForm('create')

/**
 * `edit` e `view` compartilham a mesma rotina: abrir e hidratar com o
 * registro completo retornado pela API.
 */
const openExistingCourseForm = async (mode: 'edit' | 'view', course: Course) => {
  const token = requireToken()
  if (!token) return

  openForm(mode)
  try {
    selectedCourse.value = await courseService.getById(course.id, token)
  } catch (err) {
    console.error(err)
    notify('Não foi possível carregar o curso.', 'error')
    closeForm()
  }
}

const openEditForm = (course: Course) => openExistingCourseForm('edit', course)
const openViewForm = (course: Course) => openExistingCourseForm('view', course)

const closeForm = () => {
  isFormOpen.value = false
  selectedCourse.value = null

  // Remove `form=create` da URL sem empilhar histórico.
  if (route.query.form === 'create') {
    const { form: _discarded, ...rest } = route.query
    void router.replace({ query: rest })
  }
}

// Query `form=create` na URL abre o modal (deep-link).
watch(
  () => route.query.form,
  (form) => {
    if (form === 'create') {
      openCreateForm()
    } else if (isFormOpen.value && formMode.value === 'create') {
      // Alguém removeu a flag da URL: fecha o modal correspondente.
      isFormOpen.value = false
      selectedCourse.value = null
    }
  },
  { immediate: true },
)

/* ============================================================
   Submissão
   ============================================================ */
const persistCourse = async (
  token: string,
  payload: CourseCreatePayload | CourseUpdatePayload,
): Promise<string> => {
  if (formMode.value === 'create') {
    await courseService.create(token, payload as CourseCreatePayload)
    return 'Curso cadastrado com sucesso.'
  }

  const updatePayload = payload as CourseUpdatePayload
  await courseService.update(token, updatePayload.id, updatePayload)
  return 'Curso atualizado com sucesso.'
}

const resetListState = () => {
  searchTerm.value = ''
  currentPage.value = 1
}

const handleFormSubmit = async (
  payload: CourseCreatePayload | CourseUpdatePayload,
) => {
  const token = requireToken()
  if (!token) return

  isSubmitting.value = true
  try {
    const successMessage = await persistCourse(token, payload)

    // 1) Fecha o modal (e limpa o `form=create` da URL).
    closeForm()
    // 2) Recomeça a listagem do zero.
    resetListState()
    await loadCourses()
    // 3) Só então notifica.
    notify(successMessage)
  } catch (err) {
    // Mantém o formulário aberto para o usuário corrigir e reenviar.
    console.error(err)
    notify('Não foi possível salvar o curso.', 'error')
  } finally {
    isSubmitting.value = false
  }
}

/* ============================================================
   Exclusão
   ============================================================ */
const handleDeleteCourse = (course: Course) => {
  selectedCourse.value = course
  isDeleteModalOpen.value = true
}

const closeDeleteModal = () => {
  isDeleteModalOpen.value = false
  selectedCourse.value = null
}

const confirmDeleteCourse = async () => {
  const token = requireToken()
  if (!token || !selectedCourse.value) return

  isSubmitting.value = true
  try {
    await courseService.delete(token, selectedCourse.value.id)

    closeDeleteModal()
    resetListState()
    await loadCourses()
    notify('Curso excluído com sucesso.')
  } catch (err) {
    console.error(err)
    notify('Não foi possível excluir o curso.', 'error')
  } finally {
    isSubmitting.value = false
  }
}

/* ============================================================
   Ciclo de vida
   ============================================================ */
onMounted(loadCourses)
</script>

<style scoped>
@import './CoursePage.css';
</style>
