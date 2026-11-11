<!--
=========================================================
Projeto.......: ProofChain
Módulo........: Business / Instrutores
Funcionalidade: Administração de instrutores
Arquivo.......: InstructorPage.vue
Versão........: 1.0.0

Descrição:
Página administrativa para consulta, cadastro, visualização,
alteração de situação e exclusão de instrutores.

Responsabilidades:
- Consultar instrutores da instituição autenticada.
- Filtrar e paginar a listagem.
- Cadastrar instrutor associando usuário e curso existentes.
- Consultar os dados completos de um instrutor.
- Ativar ou desativar instrutor.
- Excluir instrutor mediante confirmação.
- Exibir feedback de operações.

Dependências:
- Auth.store
- Instructor.service
- Instructor.types
- BaseButton
- InstructorPage.css

Observações:
- A instituição é identificada pelo backend através do token.
- O backend exige os IDs do usuário e do curso no cadastro.
- A listagem atual do backend retorna somente instrutores ativos.
- O handler de atualização aplica somente o campo `active`.
=========================================================
-->

<template>
  <main class="instructor-page">
    <!-- Cabeçalho -->
    <header class="instructor-page__header">
      <div>
        <h1 class="instructor-page__title">Instrutores</h1>
        <p class="instructor-page__subtitle">
          Gerencie os instrutores vinculados à sua instituição.
        </p>
      </div>

      <BaseButton type="button" @click="openCreateForm">
        Novo Instrutor
      </BaseButton>
    </header>

    <!-- Feedback -->
    <div
      v-if="feedback"
      class="instructor-page__feedback"
      :class="`instructor-page__feedback--${feedback.type}`"
      role="status"
      aria-live="polite"
    >
      <span>{{ feedback.message }}</span>
      <button
        type="button"
        class="instructor-page__feedback-close"
        aria-label="Fechar mensagem"
        @click="dismissFeedback"
      >
        ×
      </button>
    </div>

    <!-- Pesquisa -->
    <section class="instructor-page__toolbar">
      <input
        v-model="searchTerm"
        class="instructor-page__search"
        type="search"
        placeholder="Buscar por nome ou e-mail..."
        aria-label="Buscar instrutores por nome ou e-mail"
      />
      <span class="instructor-page__counter">
        {{ filteredInstructors.length }}
        {{ filteredInstructors.length === 1 ? 'instrutor' : 'instrutores' }}
      </span>
    </section>

    <!-- Conteúdo -->
    <section class="instructor-page__content" aria-label="Lista de instrutores">
      <div v-if="isLoading" class="instructor-page__state" role="status">
        Carregando instrutores...
      </div>

      <div v-else-if="filteredInstructors.length === 0" class="instructor-page__state">
        <strong>Nenhum instrutor encontrado.</strong>
        <span v-if="searchTerm">
          Tente alterar o termo informado na busca.
        </span>
        <span v-else>
          Cadastre um instrutor para começar.
        </span>
      </div>

      <div v-else class="instructor-page__table-wrapper">
        <table class="instructor-page__table">
          <thead>
            <tr>
              <th scope="col">Instrutor</th>
              <th scope="col">E-mail</th>
              <th scope="col" class="instructor-page__actions-heading">
                Ações
              </th>
            </tr>
          </thead>

          <tbody>
            <tr
              v-for="instructor in paginatedInstructors"
              :key="instructor.id"
            >
              <td>
                <span class="instructor-page__person-name">
                  {{ instructor.name }}
                </span>
              </td>
              <td>{{ instructor.email }}</td>
              <td class="instructor-page__actions">
                <button
                  type="button"
                  class="instructor-page__icon-button"
                  title="Visualizar instrutor"
                  :aria-label="`Visualizar ${instructor.name}`"
                  @click="openDetails(instructor)"
                >
                  <span aria-hidden="true">◉</span>
                </button>

                <button
                  type="button"
                  class="instructor-page__icon-button"
                  title="Desativar instrutor"
                  :aria-label="`Desativar ${instructor.name}`"
                  :disabled="isSubmitting"
                  @click="changeStatus(instructor, false)"
                >
                  <span aria-hidden="true">⏸</span>
                </button>

                <button
                  type="button"
                  class="instructor-page__icon-button instructor-page__icon-button--danger"
                  title="Excluir instrutor"
                  :aria-label="`Excluir ${instructor.name}`"
                  :disabled="isSubmitting"
                  @click="openDeleteConfirmation(instructor)"
                >
                  <span aria-hidden="true">⌫</span>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>

    <!-- Paginação -->
    <nav
      v-if="!isLoading && totalPages > 1"
      class="instructor-page__pagination"
      aria-label="Paginação de instrutores"
    >
      <button
        type="button"
        :disabled="currentPage === 1"
        @click="currentPage--"
      >
        Anterior
      </button>

      <span>Página {{ currentPage }} de {{ totalPages }}</span>

      <button
        type="button"
        :disabled="currentPage === totalPages"
        @click="currentPage++"
      >
        Próxima
      </button>
    </nav>

    <!-- Modal de cadastro -->
    <Teleport to="body">
      <div
        v-if="isFormOpen"
        class="instructor-page__overlay"
        @click.self="closeForm"
      >
        <section
          class="instructor-page__modal"
          role="dialog"
          aria-modal="true"
          aria-labelledby="instructor-form-title"
        >
          <header class="instructor-page__modal-header">
            <div>
              <h2 id="instructor-form-title">Novo Instrutor</h2>
              <p>
                Associe um usuário já cadastrado a um curso da instituição.
              </p>
            </div>

            <button
              type="button"
              class="instructor-page__modal-close"
              aria-label="Fechar formulário"
              @click="closeForm"
            >
              ×
            </button>
          </header>

          <form class="instructor-page__form" @submit.prevent="handleCreate">
            <div class="instructor-page__notice">
              Selecione um usuário cadastrado e um curso disponível.
            </div>

            <div
              v-if="isFormOptionsLoading"
              class="instructor-page__state"
              role="status"
            >
              Carregando usuários e cursos...
            </div>

            <div v-else-if="formOptionsError" class="instructor-page__options-error" role="alert">
              <span>{{ formOptionsError }}</span>
              <button type="button" @click="loadFormOptions">
                Tentar novamente
              </button>
            </div>

            <template v-else>
              <div class="instructor-page__field">
                <label for="instructor-user-id">
                  Usuário <span aria-hidden="true">*</span>
                </label>
                <select
                  id="instructor-user-id"
                  v-model.number="form.id_User"
                  required
                  :disabled="availableUsers.length === 0"
                >
                  <option :value="null" disabled>Selecione um usuário</option>
                  <option
                    v-for="user in availableUsers"
                    :key="user.id"
                    :value="user.id"
                  >
                    {{ user.name }} — {{ user.email }}
                  </option>
                </select>
                <small v-if="availableUsers.length === 0">
                  Não há usuários ativos disponíveis para cadastro.
                </small>
              </div>

              <div class="instructor-page__field">
                <label for="instructor-course-id">
                  Curso <span aria-hidden="true">*</span>
                </label>
                <select
                  id="instructor-course-id"
                  v-model.number="form.id_course"
                  required
                  :disabled="courses.length === 0"
                >
                  <option :value="null" disabled>Selecione um curso</option>
                  <option
                    v-for="course in courses"
                    :key="course.id"
                    :value="course.id"
                  >
                    {{ course.name }}
                  </option>
                </select>
                <small v-if="courses.length === 0">
                  Não há cursos disponíveis para cadastro.
                </small>
              </div>
            </template>

            <footer class="instructor-page__form-actions">
              <BaseButton
                type="button"
                variant="secondary"
                :disabled="isSubmitting"
                @click="closeForm"
              >
                Cancelar
              </BaseButton>

              <BaseButton
                type="submit"
                :loading="isSubmitting"
                :disabled="
                  isSubmitting
                  || isFormOptionsLoading
                  || Boolean(formOptionsError)
                  || !isFormValid
                "
              >
                Cadastrar Instrutor
              </BaseButton>
            </footer>
          </form>
        </section>
      </div>

      <!-- Modal de detalhes -->
      <div
        v-if="isDetailsOpen"
        class="instructor-page__overlay"
        @click.self="closeDetails"
      >
        <section
          class="instructor-page__modal"
          role="dialog"
          aria-modal="true"
          aria-labelledby="instructor-details-title"
        >
          <header class="instructor-page__modal-header">
            <div>
              <h2 id="instructor-details-title">Dados do Instrutor</h2>
              <p>Informações retornadas pelo cadastro.</p>
            </div>

            <button
              type="button"
              class="instructor-page__modal-close"
              aria-label="Fechar detalhes"
              @click="closeDetails"
            >
              ×
            </button>
          </header>

          <div v-if="isDetailsLoading" class="instructor-page__state">
            Carregando dados...
          </div>

          <dl v-else-if="selectedInstructor" class="instructor-page__details">
            <div>
              <dt>Nome</dt>
              <dd>{{ selectedInstructor.name }}</dd>
            </div>
            <div>
              <dt>E-mail</dt>
              <dd>{{ selectedInstructor.email }}</dd>
            </div>
            <div>
              <dt>Curso</dt>
              <dd>{{ selectedInstructor.course || '—' }}</dd>
            </div>
            <div>
              <dt>Situação</dt>
              <dd>
                {{ selectedInstructor.isActive ? 'Ativo' : 'Inativo' }}
              </dd>
            </div>
            <div>
              <dt>Data de cadastro</dt>
              <dd>{{ formatDate(selectedInstructor.creatAt) }}</dd>
            </div>
            <div>
              <dt>Data de exclusão</dt>
              <dd>{{ formatDate(selectedInstructor.deletedAt) }}</dd>
            </div>
          </dl>

          <footer class="instructor-page__form-actions">
            <BaseButton type="button" @click="closeDetails">
              Fechar
            </BaseButton>
          </footer>
        </section>
      </div>

      <!-- Modal de confirmação de exclusão -->
      <div
        v-if="isDeleteOpen"
        class="instructor-page__overlay"
        @click.self="closeDeleteConfirmation"
      >
        <section
          class="instructor-page__modal instructor-page__modal--compact"
          role="alertdialog"
          aria-modal="true"
          aria-labelledby="instructor-delete-title"
          aria-describedby="instructor-delete-description"
        >
          <h2 id="instructor-delete-title">Excluir instrutor</h2>

          <p id="instructor-delete-description">
            Confirma a exclusão de
            <strong>{{ instructorToDelete?.name }}</strong>?
          </p>

          <p class="instructor-page__warning">
            A operação será enviada ao backend.
          </p>

          <footer class="instructor-page__form-actions">
            <BaseButton
              type="button"
              variant="secondary"
              :disabled="isSubmitting"
              @click="closeDeleteConfirmation"
            >
              Cancelar
            </BaseButton>

            <BaseButton
              type="button"
              :loading="isSubmitting"
              :disabled="isSubmitting"
              @click="confirmDelete"
            >
              Confirmar exclusão
            </BaseButton>
          </footer>
        </section>
      </div>
    </Teleport>
  </main>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import { courseService } from '@/modules/business/services/Course.service'
import { instructorService } from '@/modules/business/services/Instructor.service'
import { userService } from '@/modules/business/services/User.service'
import type { Course } from '@/modules/business/types/Course.types'
import type {
  Instructor,
  InstructorCreatePayload,
  InstructorSummary,
} from '@/modules/business/types/Instructor.types'
import type { UserOption } from '@/modules/business/types/User.types'

/* ============================================================
   Dependências
   ============================================================ */
const authStore = useAuthStore()

/* ============================================================
   Estado da página
   ============================================================ */
const instructors = ref<InstructorSummary[]>([])
const users = ref<UserOption[]>([])
const courses = ref<Course[]>([])
const isLoading = ref(false)
const isSubmitting = ref(false)
const isFormOptionsLoading = ref(false)
const formOptionsError = ref('')

const searchTerm = ref('')
const currentPage = ref(1)
const pageSize = 10

const isFormOpen = ref(false)
const isDetailsOpen = ref(false)
const isDetailsLoading = ref(false)
const isDeleteOpen = ref(false)

const selectedInstructor = ref<Instructor | null>(null)
const instructorToDelete = ref<InstructorSummary | null>(null)

const form = ref({
  id_User: null as number | null,
  id_course: null as number | null,
})

type Feedback = {
  message: string
  type: 'success' | 'error'
}
const feedback = ref<Feedback | null>(null)

/* ============================================================
   Computed
   ============================================================ */
const availableUsers = computed(() =>
  users.value.filter((user) =>
    user.active
    && !instructors.value.some(
      (instructor) => instructor.email.toLocaleLowerCase() === user.email.toLocaleLowerCase(),
    ),
  ),
)

const isFormValid = computed(() =>
  availableUsers.value.some((user) => user.id === form.value.id_User)
  && courses.value.some((course) => course.id === form.value.id_course),
)

const filteredInstructors = computed(() => {
  const term = searchTerm.value.trim().toLocaleLowerCase()

  if (!term) return instructors.value

  return instructors.value.filter((instructor) =>
    [instructor.name, instructor.email]
      .some((value) => value.toLocaleLowerCase().includes(term)),
  )
})

const totalPages = computed(() =>
  Math.max(1, Math.ceil(filteredInstructors.value.length / pageSize)),
)

const paginatedInstructors = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredInstructors.value.slice(start, start + pageSize)
})

watch(searchTerm, () => {
  currentPage.value = 1
})

/* ============================================================
   Helpers
   ============================================================ */

/** Exibe uma mensagem de feedback. */
const notify = (
  message: string,
  type: Feedback['type'] = 'success',
): void => {
  feedback.value = { message, type }
}

/** Fecha a mensagem de feedback. */
const dismissFeedback = (): void => {
  feedback.value = null
}

/**
 * Obtém o token da sessão atual.
 * Interrompe a operação quando não existe token.
 */
const getAccessToken = (): string | null => {
  const token = authStore.session?.accessToken

  if (!token) {
    notify('Sua sessão expirou. Entre novamente para continuar.', 'error')
    return null
  }

  return token
}

/** Formata uma data ISO para o padrão brasileiro. */
const formatDate = (value?: string | null): string => {
  if (!value) return '—'

  const date = new Date(value)

  if (Number.isNaN(date.getTime())) return '—'

  return new Intl.DateTimeFormat('pt-BR').format(date)
}

/* ============================================================
   Consulta da listagem
   ============================================================ */

/** Carrega os instrutores retornados pelo endpoint de listagem. */
const loadInstructors = async (): Promise<void> => {
  const token = getAccessToken()
  if (!token) return

  isLoading.value = true

  try {
    instructors.value = await instructorService.getAll(token)
    currentPage.value = 1
  } catch (error) {
    notify(
      error instanceof Error
        ? error.message
        : 'Não foi possível carregar os instrutores.',
      'error',
    )
  } finally {
    isLoading.value = false
  }
}

/* ============================================================
   Cadastro
   ============================================================ */

/** Abre o formulário e limpa os campos. */
const openCreateForm = (): void => {
  form.value = {
    id_User: null,
    id_course: null,
  }
  formOptionsError.value = ''

  dismissFeedback()
  isFormOpen.value = true
  void loadFormOptions()
}

/** Carrega as opções válidas de usuário e curso para o cadastro. */
const loadFormOptions = async (): Promise<void> => {
  const token = getAccessToken()
  if (!token) return

  isFormOptionsLoading.value = true
  formOptionsError.value = ''

  try {
    const [availableUsers, availableCourses] = await Promise.all([
      userService.getAll(token),
      courseService.getAll(token),
    ])

    users.value = availableUsers
    courses.value = availableCourses
  } catch (error) {
    formOptionsError.value = error instanceof Error
      ? error.message
      : 'Não foi possível carregar usuários e cursos.'
  } finally {
    isFormOptionsLoading.value = false
  }
}

/** Fecha o formulário. */
const closeForm = (): void => {
  isFormOpen.value = false
}

/** Valida e envia o cadastro para a API. */
const handleCreate = async (): Promise<void> => {
  if (!isFormValid.value) {
    notify('Selecione um usuário e um curso válidos.', 'error')
    return
  }

  const token = getAccessToken()
  if (!token) return

  const payload: InstructorCreatePayload = {
    id_User: Number(form.value.id_User),
    id_course: Number(form.value.id_course),
  }

  isSubmitting.value = true

  try {
    await instructorService.create(payload, token)
    closeForm()
    await loadInstructors()
    notify('Instrutor cadastrado com sucesso.')
  } catch (error) {
    notify(
      error instanceof Error
        ? error.message
        : 'Não foi possível cadastrar o instrutor.',
      'error',
    )
  } finally {
    isSubmitting.value = false
  }
}

/* ============================================================
   Visualização
   ============================================================ */

/** Consulta os dados completos do instrutor selecionado. */
const openDetails = async (
  instructor: InstructorSummary,
): Promise<void> => {
  const token = getAccessToken()
  if (!token) return

  selectedInstructor.value = null
  isDetailsOpen.value = true
  isDetailsLoading.value = true

  try {
    selectedInstructor.value = await instructorService.getById(
      instructor.id,
      token,
    )
  } catch (error) {
    isDetailsOpen.value = false
    notify(
      error instanceof Error
        ? error.message
        : 'Não foi possível consultar o instrutor.',
      'error',
    )
  } finally {
    isDetailsLoading.value = false
  }
}

/** Fecha o modal de detalhes. */
const closeDetails = (): void => {
  isDetailsOpen.value = false
  selectedInstructor.value = null
}

/* ============================================================
   Situação
   ============================================================ */

/**
 * Atualiza a situação do instrutor.
 *
 * O endpoint PATCH aceita o campo `active`. O handler atual
 * aplica somente esse campo; specialty e hiringDate não são
 * alterados por esta tela.
 */
const changeStatus = async (
  instructor: InstructorSummary,
  active: boolean,
): Promise<void> => {
  const token = getAccessToken()
  if (!token) return

  isSubmitting.value = true

  try {
    await instructorService.update(
      instructor.id,
      { active },
      token,
    )

    await loadInstructors()

    notify(
      active
        ? 'Instrutor ativado com sucesso.'
        : 'Instrutor desativado com sucesso.',
    )
  } catch (error) {
    notify(
      error instanceof Error
        ? error.message
        : 'Não foi possível atualizar a situação do instrutor.',
      'error',
    )
  } finally {
    isSubmitting.value = false
  }
}

/* ============================================================
   Exclusão
   ============================================================ */

/** Abre a confirmação de exclusão. */
const openDeleteConfirmation = (
  instructor: InstructorSummary,
): void => {
  instructorToDelete.value = instructor
  isDeleteOpen.value = true
}

/** Fecha a confirmação de exclusão. */
const closeDeleteConfirmation = (): void => {
  isDeleteOpen.value = false
  instructorToDelete.value = null
}

/** Confirma e executa a exclusão do instrutor. */
const confirmDelete = async (): Promise<void> => {
  const token = getAccessToken()
  const instructor = instructorToDelete.value

  if (!token || !instructor) return

  isSubmitting.value = true

  try {
    await instructorService.delete(instructor.id, token)
    closeDeleteConfirmation()
    await loadInstructors()
    notify('Instrutor excluído com sucesso.')
  } catch (error) {
    notify(
      error instanceof Error
        ? error.message
        : 'Não foi possível excluir o instrutor.',
      'error',
    )
  } finally {
    isSubmitting.value = false
  }
}

/* ============================================================
   Inicialização
   ============================================================ */
onMounted(loadInstructors)
</script>

<style scoped>
@import './InstructorPage.css';
</style>
