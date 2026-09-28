<template>
  <div class="course-page">
    <!-- Cabeçalho -->
    <header class="course-page__header">
      <h1 class="course-page__title">Cursos</h1>
      <BaseButton @click="openCreateForm">+ Novo Curso</BaseButton>
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

    <!-- Feedback -->
    <div v-if="errorMessage" class="course-page__feedback course-page__feedback--error">
      {{ errorMessage }}
    </div>
    <div v-if="successMessage" class="course-page__feedback course-page__feedback--success">
      {{ successMessage }}
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

    <!-- Formulário (modal ou inline) -->
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
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import FormCourse from './FormCourse.vue'
import { courseService } from '@/modules/business/services/Course.service'
import type {
  Course,
  CourseCreatePayload,
  CourseUpdatePayload,
  FormMode,
} from '@/modules/business/types/Course.types'

/* ============================================================
   ESTADO
   ============================================================ */
const courses = ref<Course[]>([])
const isLoading = ref(false)
const isSubmitting = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const searchTerm = ref('')
const currentPage = ref(1)
const pageSize = 10

const isFormOpen = ref(false)
const formMode = ref<FormMode>('create')
const selectedCourse = ref<Course | null>(null)

/* ============================================================
   COMPUTADOS
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
   CARREGAMENTO
   ============================================================ */
const loadCourses = async () => {
  isLoading.value = true
  errorMessage.value = ''
  try {
    courses.value = await courseService.getAll()
  } catch (err) {
    errorMessage.value = 'Não foi possível carregar os cursos.'
    console.error(err)
  } finally {
    isLoading.value = false
  }
}

/* ============================================================
   ABERTURA DO FORMULÁRIO
   ============================================================ */
const openCreateForm = () => {
  formMode.value = 'create'
  selectedCourse.value = null
  isFormOpen.value = true
}

const openEditForm = async (course: Course) => {
  formMode.value = 'edit'
  selectedCourse.value = null
  isFormOpen.value = true
  try {
    selectedCourse.value = await courseService.getById(course.id)
  } catch (err) {
    errorMessage.value = 'Não foi possível carregar o curso.'
    console.error(err)
    isFormOpen.value = false
  }
}

const openViewForm = async (course: Course) => {
  formMode.value = 'view'
  selectedCourse.value = null
  isFormOpen.value = true
  try {
    selectedCourse.value = await courseService.getById(course.id)
  } catch (err) {
    errorMessage.value = 'Não foi possível carregar o curso.'
    console.error(err)
    isFormOpen.value = false
  }
}

const closeForm = () => {
  isFormOpen.value = false
  selectedCourse.value = null
  successMessage.value = ''
}

/* ============================================================
   SUBMIT DO FORMULÁRIO
   ============================================================ */
const handleFormSubmit = async (payload: CourseCreatePayload | CourseUpdatePayload) => {
  isSubmitting.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    if (formMode.value === 'create') {
      await courseService.create(payload as CourseCreatePayload)
      successMessage.value = 'Curso cadastrado com sucesso.'
    } else if (formMode.value === 'edit') {
      const updatePayload = payload as CourseUpdatePayload
      await courseService.update(updatePayload.id, updatePayload)
      successMessage.value = 'Curso atualizado com sucesso.'
    }

    await loadCourses()
    closeForm()
  } catch (err) {
    errorMessage.value = 'Não foi possível salvar o curso.'
    console.error(err)
  } finally {
    isSubmitting.value = false
  }
}

/* ============================================================
   EXCLUSÃO — preparada, mas não executada
   ============================================================ */
const handleDeleteCourse = (_course: Course) => {
  // O endpoint DELETE ainda não existe no backend.
  // Quando estiver disponível, descomentar:
  //
  // await courseService.delete(course.id)
  // await loadCourses()
  //
  // Por enquanto, nenhuma requisição é feita.
  alert('A exclusão de cursos estará disponível em breve.')
}

/* ============================================================
   CICLO DE VIDA
   ============================================================ */
onMounted(loadCourses)
</script>

<style scoped>
@import './CoursePage.css';
</style>