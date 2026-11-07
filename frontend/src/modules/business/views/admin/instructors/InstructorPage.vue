<!--
=========================================================
Projeto.......: ProofChain
Módulo........: Business / Instrutores
Funcionalidade: Administração de instrutores
Arquivo.......: InstructorPage.vue
Versão........: 1.0.0

Descrição:
Consulta, cadastro, visualização, ativação/desativação e exclusão
de instrutores vinculados à instituição autenticada.

Contrato:
- POST   /api/v1/instructor/register
- GET    /api/v1/instructor/list
- GET    /api/v1/instructor/get/{id}
- PATCH  /api/v1/instructor/update/{id}
- DELETE /api/v1/instructor/delete/{id}

Observações:
- O tenant é obtido pelo backend a partir da sessão autenticada.
- O cadastro recebe os IDs do usuário e do curso existentes.
- A listagem do backend retorna somente instrutores ativos.
- A edição disponibilizada pelo backend altera somente o status active.
=========================================================
-->
<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import { instructorService } from '@/modules/business/services/Instructor.service'
import type {
  Instructor,
  InstructorCreatePayload,
  InstructorUpdatePayload,
} from '@/modules/business/types/Instructor.types'

const authStore = useAuthStore()

/* Estado da página */
const instructors = ref<Instructor[]>([])
const isLoading = ref(false)
const isSubmitting = ref(false)
const isFormOpen = ref(false)
const isDeleteModalOpen = ref(false)
const selectedInstructor = ref<Instructor | null>(null)
const formMode = ref<'create' | 'view'>('create')
const searchTerm = ref('')
const currentPage = ref(1)
const pageSize = 10

/* Formulário de associação: IDs de usuário e curso já cadastrados. */
const form = ref<InstructorCreatePayload>({
  id_User: null,
  id_Institution: null,
  id_course: null,
})

type Feedback = { message: string; type: 'success' | 'error' }
const feedback = ref<Feedback | null>(null)

const notify = (message: string, type: Feedback['type'] = 'success') => {
  feedback.value = { message, type }
}
const dismissFeedback = () => { feedback.value = null }

const requireToken = (): string | null => {
  const token = authStore.session?.accessToken
  if (!token) notify('Sua sessão expirou. Entre novamente para continuar.', 'error')
  return token ?? null
}

const filteredInstructors = computed(() => {
  const term = searchTerm.value.trim().toLocaleLowerCase()
  if (!term) return instructors.value
  return instructors.value.filter((item) =>
    [item.name, item.email, item.course ?? '']
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

watch(searchTerm, () => { currentPage.value = 1 })

const loadInstructors = async () => {
  const token = requireToken()
  if (!token) return
  isLoading.value = true
  try {
    instructors.value = (await instructorService.getAll(token)).map((item) => ({\n      idInstructor: item.id,\n      name: item.name,\n      email: item.email,\n      course: '',\n      isActive: true,\n      creatAt: '',\n      deletedAt: null,\n    }))
  } catch (error) {
    notify(error instanceof Error ? error.message : 'Não foi possível carregar os instrutores.', 'error')
  } finally {
    isLoading.value = false
  }
}

const openCreateForm = () => {
  formMode.value = 'create'
  form.value = {
    id_User: null,
    id_Institution: authStore.user?.tenantId ?? null,
    id_course: null,
  }
  feedback.value = null
  isFormOpen.value = true
}
const closeForm = () => {
  isFormOpen.value = false
  selectedInstructor.value = null
}
const openView = async (instructor: Instructor) => {
  const token = requireToken()
  if (!token) return
  try {
    selectedInstructor.value = await instructorService.getById(instructor.idInstructor, token)
    formMode.value = 'view'
    isFormOpen.value = true
  } catch (error) {
    notify(error instanceof Error ? error.message : 'Não foi possível consultar o instrutor.', 'error')
  }
}

const handleCreate = async () => {
  const token = requireToken()
  if (!token) return
  if (!form.value.id_User || !form.value.id_course) {
    notify('Informe o ID do usuário e o ID do curso.', 'error')
    return
  }
  isSubmitting.value = true
  try {
    await instructorService.create(token, {
      id_User: Number(form.value.id_User),
      id_Institution: authStore.user?.tenantId ?? null,
      id_course: Number(form.value.id_course),
    })
    closeForm()
    await loadInstructors()
    notify('Instrutor cadastrado com sucesso.')
  } catch (error) {
    notify(error instanceof Error ? error.message : 'Não foi possível cadastrar o instrutor.', 'error')
  } finally {
    isSubmitting.value = false
  }
}

const toggleStatus = async (instructor: Instructor) => {
  const token = requireToken()
  if (!token) return
  isSubmitting.value = true
  try {
    await instructorService.update(token, instructor.idInstructor, {
      active: !instructor.isActive,
    })
    await loadInstructors()
    notify(instructor.isActive ? 'Instrutor desativado com sucesso.' : 'Instrutor ativado com sucesso.')
  } catch (error) {
    notify(error instanceof Error ? error.message : 'Não foi possível atualizar o status.', 'error')
  } finally {
    isSubmitting.value = false
  }
}

const askDelete = (instructor: Instructor) => {
  selectedInstructor.value = instructor
  isDeleteModalOpen.value = true
}
const closeDelete = () => {
  isDeleteModalOpen.value = false
  selectedInstructor.value = null
}
const confirmDelete = async () => {
  const token = requireToken()
  if (!token || !selectedInstructor.value) return
  isSubmitting.value = true
  try {
    await instructorService.delete(token, selectedInstructor.value.idInstructor)
    closeDelete()
    await loadInstructors()
    notify('Instrutor excluído com sucesso.')
  } catch (error) {
    notify(error instanceof Error ? error.message : 'Não foi possível excluir o instrutor.', 'error')
  } finally {
    isSubmitting.value = false
  }
}

const formatDate = (value?: string | null) => {
  if (!value) return '—'
  const date = new Date(value)
  return Number.isNaN(date.getTime()) ? '—' : new Intl.DateTimeFormat('pt-BR').format(date)
}

onMounted(loadInstructors)
</script>

<template>
  <main class="instructor-page">
    <header class="instructor-page__header">
      <div>
        <h1 class="instructor-page__title">Instrutores</h1>
        <p class="instructor-page__subtitle">Gerencie os instrutores vinculados à sua instituição.</p>
      </div>
      <BaseButton type="button" variant="primary" @click="openCreateForm">
        + Novo Instrutor
      </BaseButton>
    </header>

    <div v-if="feedback" class="instructor-page__feedback" :class="`instructor-page__feedback--${feedback.type}`" role="status">
      <span>{{ feedback.message }}</span>
      <button type="button" aria-label="Fechar aviso" @click="dismissFeedback">×</button>
    </div>

    <section class="instructor-page__filters" aria-label="Filtros de instrutores">
      <label class="instructor-page__search-wrap">
        <span class="instructor-page__sr-only">Buscar instrutores</span>
        <input v-model="searchTerm" class="instructor-page__search" type="search" placeholder="Buscar por nome, e-mail ou curso">
      </label>
      <span class="instructor-page__counter">{{ filteredInstructors.length }} instrutor(es)</span>
    </section>

    <div v-if="isLoading" class="instructor-page__state" role="status">Carregando instrutores...</div>
    <div v-else-if="!filteredInstructors.length" class="instructor-page__state">
      Nenhum instrutor encontrado.
    </div>
    <div v-else class="instructor-page__table-wrap">
      <table class="instructor-page__table">
        <thead>
          <tr>
            <th>Instrutor</th>
            <th>Curso</th>
            <th>Cadastro</th>
            <th>Situação</th>
            <th class="instructor-page__actions-heading">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="instructor in paginatedInstructors" :key="instructor.idInstructor">
            <td>
              <strong>{{ instructor.name }}</strong>
              <span class="instructor-page__email">{{ instructor.email }}</span>
            </td>
            <td>{{ instructor.course || '—' }}</td>
            <td>{{ formatDate(instructor.creatAt) }}</td>
            <td>
              <span class="instructor-page__status" :class="instructor.isActive ? 'instructor-page__status--active' : 'instructor-page__status--inactive'">
                {{ instructor.isActive ? 'Ativo' : 'Inativo' }}
              </span>
            </td>
            <td class="instructor-page__actions">
              <button type="button" class="instructor-page__action" title="Visualizar" aria-label="Visualizar instrutor" @click="openView(instructor)">◉</button>
              <button type="button" class="instructor-page__action" :title="instructor.isActive ? 'Desativar' : 'Ativar'" :aria-label="instructor.isActive ? 'Desativar instrutor' : 'Ativar instrutor'" :disabled="isSubmitting" @click="toggleStatus(instructor)">
                {{ instructor.isActive ? '⏸' : '▶' }}
              </button>
              <button type="button" class="instructor-page__action instructor-page__action--delete" title="Excluir" aria-label="Excluir instrutor" :disabled="isSubmitting" @click="askDelete(instructor)">⌫</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <footer v-if="!isLoading && filteredInstructors.length" class="instructor-page__pagination">
      <button type="button" :disabled="currentPage <= 1" @click="currentPage--">Anterior</button>
      <span>Página {{ currentPage }} de {{ totalPages }}</span>
      <button type="button" :disabled="currentPage >= totalPages" @click="currentPage++">Próxima</button>
    </footer>

    <div v-if="isFormOpen" class="instructor-page__overlay" @click.self="closeForm">
      <section class="instructor-page__modal" role="dialog" aria-modal="true" aria-labelledby="instructor-form-title">
        <header class="instructor-page__modal-header">
          <div>
            <h2 id="instructor-form-title">{{ formMode === 'create' ? 'Novo Instrutor' : 'Dados do Instrutor' }}</h2>
            <p>{{ formMode === 'create' ? 'Associe um usuário existente a um curso da instituição.' : 'Informações retornadas pela API.' }}</p>
          </div>
          <button type="button" class="instructor-page__close" aria-label="Fechar" @click="closeForm">×</button>
        </header>

        <form v-if="formMode === 'create'" class="instructor-page__form" @submit.prevent="handleCreate">
          <div class="instructor-page__notice">
            O usuário e o curso devem estar previamente cadastrados na instituição. A instituição é identificada pela sessão autenticada.
          </div>
          <div class="instructor-page__field">
            <label for="instructor-user-id">ID do usuário <span>*</span></label>
            <input id="instructor-user-id" v-model.number="form.id_User" type="number" min="1" required>
            <small>Informe o identificador do usuário já cadastrado.</small>
          </div>
          <div class="instructor-page__field">
            <label for="instructor-course-id">ID do curso <span>*</span></label>
            <input id="instructor-course-id" v-model.number="form.id_course" type="number" min="1" required>
            <small>Informe o identificador de um curso pertencente à instituição.</small>
          </div>
          <div class="instructor-page__field">
            <label>Instituição</label>
            <input :value="authStore.user?.tenantId ?? 'Identificada pela sessão'" type="text" readonly>
          </div>
          <footer class="instructor-page__form-actions">
            <BaseButton type="button" variant="secondary" @click="closeForm">Cancelar</BaseButton>
            <BaseButton type="submit" :loading="isSubmitting" :disabled="isSubmitting || !form.id_User || !form.id_course">Cadastrar Instrutor</BaseButton>
          </footer>
        </form>

        <div v-else-if="selectedInstructor" class="instructor-page__details">
          <div><span>Nome</span><strong>{{ selectedInstructor.name }}</strong></div>
          <div><span>E-mail</span><strong>{{ selectedInstructor.email }}</strong></div>
          <div><span>Curso</span><strong>{{ selectedInstructor.course || '—' }}</strong></div>
          <div><span>Situação</span><strong>{{ selectedInstructor.isActive ? 'Ativo' : 'Inativo' }}</strong></div>
          <div><span>Data de cadastro</span><strong>{{ formatDate(selectedInstructor.creatAt) }}</strong></div>
          <div><span>Exclusão</span><strong>{{ formatDate(selectedInstructor.deletedAt) }}</strong></div>
          <footer class="instructor-page__form-actions">
            <BaseButton type="button" @click="closeForm">Fechar</BaseButton>
          </footer>
        </div>
      </section>
    </div>

    <div v-if="isDeleteModalOpen && selectedInstructor" class="instructor-page__overlay" @click.self="closeDelete">
      <section class="instructor-page__confirm" role="alertdialog" aria-modal="true" aria-labelledby="instructor-delete-title">
        <h2 id="instructor-delete-title">Excluir instrutor</h2>
        <p>Confirma a exclusão de <strong>{{ selectedInstructor.name }}</strong>?</p>
        <p class="instructor-page__warning">Essa operação será enviada ao backend.</p>
        <footer class="instructor-page__form-actions">
          <BaseButton type="button" variant="secondary" :disabled="isSubmitting" @click="closeDelete">Cancelar</BaseButton>
          <BaseButton type="button" :loading="isSubmitting" :disabled="isSubmitting" @click="confirmDelete">Confirmar exclusão</BaseButton>
        </footer>
      </section>
    </div>
  </main>
</template>

<style scoped>
.instructor-page { width: 100%; color: var(--color-text, #202938); }
.instructor-page__header { display:flex; align-items:center; justify-content:space-between; gap:1rem; margin-bottom:1.5rem; }
.instructor-page__title { margin:0; font-size:1.65rem; font-weight:700; }
.instructor-page__subtitle { margin:.4rem 0 0; color:var(--color-text-muted, #667085); }
.instructor-page__filters { display:flex; align-items:center; justify-content:space-between; gap:1rem; margin:1rem 0; }
.instructor-page__search-wrap { flex:1; max-width:32rem; }
.instructor-page__search { width:100%; min-height:2.7rem; padding:.65rem .85rem; border:1px solid #d0d5dd; border-radius:.5rem; font:inherit; }
.instructor-page__counter { color:#667085; font-size:.9rem; }
.instructor-page__table-wrap { overflow-x:auto; border:1px solid #eaecf0; border-radius:.75rem; background:#fff; }
.instructor-page__table { width:100%; border-collapse:collapse; text-align:left; }
.instructor-page__table th,.instructor-page__table td { padding:.9rem 1rem; border-bottom:1px solid #eaecf0; vertical-align:middle; }
.instructor-page__table th { color:#667085; background:#f9fafb; font-size:.8rem; font-weight:600; }
.instructor-page__table td strong,.instructor-page__email { display:block; }
.instructor-page__email { margin-top:.25rem; color:#667085; font-size:.85rem; }
.instructor-page__actions-heading,.instructor-page__actions { text-align:right; white-space:nowrap; }
.instructor-page__action { min-width:2rem; min-height:2rem; margin-left:.3rem; border:1px solid #eaecf0; border-radius:.4rem; background:#fff; cursor:pointer; }
.instructor-page__action:disabled { opacity:.5; cursor:not-allowed; }
.instructor-page__action--delete { color:#b42318; }
.instructor-page__status { display:inline-block; padding:.25rem .6rem; border-radius:99px; font-size:.8rem; font-weight:600; }
.instructor-page__status--active { color:#067647; background:#ecfdf3; }
.instructor-page__status--inactive { color:#b42318; background:#fef3f2; }
.instructor-page__state { padding:3rem 1rem; color:#667085; text-align:center; }
.instructor-page__pagination { display:flex; justify-content:flex-end; align-items:center; gap:1rem; padding:1rem 0; color:#667085; }
.instructor-page__pagination button { padding:.45rem .8rem; border:1px solid #d0d5dd; border-radius:.4rem; background:#fff; cursor:pointer; }
.instructor-page__pagination button:disabled { opacity:.45; cursor:not-allowed; }
.instructor-page__feedback { display:flex; justify-content:space-between; align-items:center; gap:1rem; padding:.8rem 1rem; margin:1rem 0; border-radius:.5rem; }
.instructor-page__feedback button,.instructor-page__close { border:0; background:transparent; font-size:1.3rem; cursor:pointer; }
.instructor-page__feedback--success { color:#067647; background:#ecfdf3; }
.instructor-page__feedback--error { color:#b42318; background:#fef3f2; }
.instructor-page__overlay { position:fixed; inset:0; z-index:1000; display:grid; place-items:center; padding:1rem; background:rgb(16 24 40 / 55%); }
.instructor-page__modal,.instructor-page__confirm { width:min(100%,34rem); max-height:90vh; overflow:auto; padding:1.5rem; border-radius:.8rem; background:#fff; box-shadow:0 1rem 3rem rgb(16 24 40 / 20%); }
.instructor-page__modal-header { display:flex; justify-content:space-between; gap:1rem; margin-bottom:1.5rem; }
.instructor-page__modal-header h2,.instructor-page__confirm h2 { margin:0; }
.instructor-page__modal-header p { margin:.4rem 0 0; color:#667085; font-size:.9rem; }
.instructor-page__form { display:grid; gap:1.1rem; }
.instructor-page__notice { padding:.8rem; border-radius:.5rem; color:#344054; background:#f2f4f7; font-size:.88rem; line-height:1.5; }
.instructor-page__field { display:grid; gap:.4rem; }
.instructor-page__field label { font-weight:600; font-size:.9rem; }
.instructor-page__field label span { color:#b42318; }
.instructor-page__field input { width:100%; min-height:2.7rem; padding:.6rem .75rem; border:1px solid #d0d5dd; border-radius:.45rem; font:inherit; }
.instructor-page__field input[readonly] { background:#f9fafb; color:#667085; }
.instructor-page__field small { color:#667085; }
.instructor-page__form-actions { display:flex; justify-content:flex-end; gap:.7rem; margin-top:1rem; }
.instructor-page__details { display:grid; gap:1rem; }
.instructor-page__details div { display:grid; gap:.2rem; padding-bottom:.7rem; border-bottom:1px solid #eaecf0; }
.instructor-page__details span { color:#667085; font-size:.82rem; }
.instructor-page__warning { color:#b42318; font-size:.9rem; }
.instructor-page__sr-only { position:absolute; width:1px; height:1px; padding:0; margin:-1px; overflow:hidden; clip:rect(0,0,0,0); white-space:nowrap; border:0; }
@media (max-width: 640px) {
 .instructor-page__header,.instructor-page__filters { align-items:stretch; flex-direction:column; }
 .instructor-page__header :deep(button) { width:100%; }
 .instructor-page__form-actions { flex-direction:column-reverse; }
}
</style>
