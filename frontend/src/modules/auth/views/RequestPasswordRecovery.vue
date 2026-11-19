<!--
=========================================================
Project.......: ProofChain
Module........: Auth
Feature.......: Password Recovery
File..........: RequestPasswordRecovery.vue
Version.......: 1.0.0

Description...:
Solicita o envio de um código de recuperação por e-mail.

Responsibilities:
- Validar o endereço de e-mail informado.
- Solicitar o código ao backend.
- Apresentar o resultado pelo Modal global.

Dependencies..:
- RecoveryLayout
- BaseButton
- Modal
- PasswordRecovery.service

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import Modal from '@/core/components/ui/Modal/Modal.vue'
import RecoveryLayout from '@/modules/auth/components/RecoveryLayout.vue'
import {
  passwordRecoveryService,
  RECOVERY_EMAIL_STORAGE_KEY,
} from '@/modules/auth/services/PasswordRecovery.service'

defineOptions({ name: 'RequestPasswordRecoveryView' })

const router = useRouter()
const email = ref(sessionStorage.getItem(RECOVERY_EMAIL_STORAGE_KEY) ?? '')
const emailError = ref('')
const isSubmitting = ref(false)
const showModal = ref(false)
const modalType = ref<'success' | 'warning' | 'error'>('error')
const modalTitle = ref('')
const modalMessage = ref('')
const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/

function openModal(type: 'success' | 'warning' | 'error', title: string, message: string): void {
  modalType.value = type
  modalTitle.value = title
  modalMessage.value = message
  showModal.value = true
}

function closeModal(): void {
  showModal.value = false
}

function validateForm(): boolean {
  emailError.value = ''

  if (!emailPattern.test(email.value.trim())) {
    emailError.value = 'Informe um e-mail válido.'
    return false
  }

  return true
}

async function handleSubmit(): Promise<void> {
  if (isSubmitting.value || !validateForm()) return

  isSubmitting.value = true

  try {
    const normalizedEmail = email.value.trim()
    await passwordRecoveryService.requestCode(normalizedEmail)
    sessionStorage.setItem(RECOVERY_EMAIL_STORAGE_KEY, normalizedEmail)
    openModal(
      'success',
      'Solicitação realizada',
      'Se houver uma conta associada ao e-mail informado, você receberá as instruções de recuperação.',
    )
  } catch {
    openModal(
      'error',
      'Não foi possível solicitar a recuperação',
      'Não foi possível concluir a operação. Tente novamente.',
    )
  } finally {
    isSubmitting.value = false
  }
}

function continueToCodeValidation(): void {
  closeModal()
  void router.push({ name: 'validateRecoveryCode' })
}
</script>

<template>
  <RecoveryLayout>
    <template #header>
      <header class="login__header">
        <h2 id="recovery-title">Esqueceu sua senha?</h2>
        <p>Informe seu e-mail para receber as instruções de recuperação.</p>
      </header>
    </template>

    <form class="login__form" novalidate @submit.prevent="handleSubmit">
      <div class="login__field">
        <label for="recovery-email">E-mail</label>
        <div class="login__input-wrap">
          <span class="login__field-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
              <rect x="3" y="5" width="18" height="14" rx="2" />
              <path d="m3 7 9 6 9-6" />
            </svg>
          </span>
          <input
            id="recovery-email"
            v-model="email"
            type="email"
            autocomplete="email"
            placeholder="seu@email.com"
            :aria-invalid="Boolean(emailError)"
            :aria-describedby="emailError ? 'recovery-email-error' : undefined"
            @input="emailError = ''"
          />
        </div>
        <p
          v-if="emailError"
          id="recovery-email-error"
          class="login__error"
          aria-live="polite"
        >
          {{ emailError }}
        </p>
      </div>

      <BaseButton
        type="submit"
        variant="primary"
        class="login__buttom"
        size="lg"
        full-width
        :loading="isSubmitting"
      >
        {{ isSubmitting ? 'Enviando...' : 'Enviar código' }}
      </BaseButton>

      <RouterLink class="recovery__back-link" :to="{ name: 'login' }">
        Voltar para o login
      </RouterLink>
    </form>

    <Modal :visible="showModal" :type="modalType" :title="modalTitle" @close="closeModal">
      <p>{{ modalMessage }}</p>

      <template #actions>
        <BaseButton
          v-if="modalType === 'success'"
          type="button"
          variant="primary"
          class="modal__button modal__button--primary"
          @click="continueToCodeValidation"
        >
          Continuar
        </BaseButton>
        <BaseButton
          v-else
          type="button"
          variant="secondary"
          class="modal__button modal__button--secondary"
          @click="closeModal"
        >
          Fechar
        </BaseButton>
      </template>
    </Modal>
  </RecoveryLayout>
</template>
