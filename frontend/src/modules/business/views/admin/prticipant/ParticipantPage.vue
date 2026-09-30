<!--
  ============================================================
  PARTICIPANT PAGE
  ============================================================
  Página administrativa de participantes.

  LIMITAÇÃO CRÍTICA DO BACKEND:
  O backend atual expõe APENAS o endpoint de criação
  (POST /api/v1/participants/register).

  Endpoints de listagem, busca por ID, atualização e exclusão
  NÃO EXISTEM. Portanto, as funcionalidades de listagem,
  pesquisa, paginação, edição, visualização e exclusão NÃO
  podem ser implementadas sem inventar contratos.

  Esta página implementa apenas a criação de participantes.
  A estrutura visual está preparada para expansão futura
  quando os endpoints forem disponibilizados no backend.
  ============================================================
-->

<script setup lang="ts">
import { ref } from 'vue'
import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import ParticipantForm from './ParticipantForm.vue'
import { participantService } from '../../../services/Participant.service'
import type { ParticipantRequest } from '../../../types/Participant.types'

// ============================================================
// ESTADO
// ============================================================
const authStore = useAuthStore()
const isFormOpen = ref(false)
const isSubmitting = ref(false)
const successMessage = ref('')
const errorMessage = ref('')

// ============================================================
// AÇÕES
// ============================================================
const openCreateForm = () => {
  successMessage.value = ''
  errorMessage.value = ''
  isFormOpen.value = true
};

const closeForm = () => {
  isFormOpen.value = false
};

const handleSubmit = async (payload: ParticipantRequest) => {
  errorMessage.value = ''
  successMessage.value = ''
  const token = authStore.session?.accessToken
  if (!token) {
    errorMessage.value = 'Sua sessão expirou. Entre novamente para continuar.'
    return
  }

  isSubmitting.value = true

  try {
    await participantService.create(token, payload)
    successMessage.value = 'Participante cadastrado com sucesso.'
    isFormOpen.value = false
  } catch (error) {
    console.error('[ParticipantPage] Erro ao cadastrar participante:', error)
    errorMessage.value = error instanceof Error
      ? error.message
      : 'Não foi possível salvar o participante.'
  } finally {
    isSubmitting.value = false
  }
};

// ============================================================
// EXCLUSÃO — PREPARADA MAS NÃO IMPLEMENTADA
// ============================================================
// O backend NÃO possui endpoint DELETE.
// A função abaixo está preparada para quando o endpoint existir.
//
// const handleDeleteParticipant = async (id: number) => {
//   // Implementar quando DELETE /api/v1/participants/{id} existir.
//   //
//   // await participantService.delete(id);
//   //
//   // await loadParticipants();
// };
</script>

<template>
  <div class="participant-page">
    <!-- ========================================================
         CABEÇALHO
         ======================================================== -->
    <header class="participant-page__header">
      <h1 class="participant-page__title">Participantes</h1>
        <BaseButton
        v-if="!isFormOpen"
        type="button"
          variant="primary"
        @click="openCreateForm"
      >
          + Novo Participante
        </BaseButton>
    </header>

    <!-- ========================================================
         FEEDBACK
         ======================================================== -->
    <div
      v-if="successMessage"
      class="participant-page__feedback participant-page__feedback--success"
      role="status"
    >
      {{ successMessage }}
    </div>

    <div
      v-if="errorMessage"
      class="participant-page__feedback participant-page__feedback--error"
      role="alert"
    >
      {{ errorMessage }}
    </div>

    <!-- ========================================================
         FORMULÁRIO
         ======================================================== -->
    <section v-if="isFormOpen" class="participant-page__form-container">
      <h2 class="participant-page__form-title">Novo Participante</h2>
      <ParticipantForm
        mode="create"
        :submitting="isSubmitting"
        @submit="handleSubmit"
        @cancel="closeForm"
      />
    </section>

    <!-- ========================================================
         ESTADO VAZIO / LIMITAÇÃO
         ======================================================== -->
    <section v-else class="participant-page__empty">
      <p class="participant-page__empty-text">
        Cadastro de participantes
      </p>
      <p class="participant-page__empty-note">
        O cadastro está disponível. A listagem ainda depende da implementação
        de um endpoint de consulta no backend.
      </p>
    </section>
  </div>
</template>
