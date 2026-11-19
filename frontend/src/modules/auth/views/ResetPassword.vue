<!--
=========================================================
Project.......: ProofChain
Module........: Auth
Feature.......: Password Recovery
File..........: ResetPassword.vue
Version.......: 1.0.0

Description...:
Redefine a senha após validar o código recebido por e-mail.

Responsibilities:
- Validar a nova senha e sua confirmação.
- Enviar a nova senha e o código ao backend.
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
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import Modal from '@/core/components/ui/Modal/Modal.vue'
import RecoveryLayout from '@/modules/auth/components/RecoveryLayout.vue'
import { passwordRecoveryService } from '@/modules/auth/services/PasswordRecovery.service'

defineOptions({ name: 'ResetPasswordView' })

const EMAIL_STORAGE_KEY = 'proofchain.password-recovery.email'
const TOKEN_STORAGE_KEY = 'proofchain.password-recovery.token'
const router = useRouter()
const email = ref(sessionStorage.getItem(EMAIL_STORAGE_KEY) ?? '')
const token = ref(sessionStorage.getItem(TOKEN_STORAGE_KEY) ?? '')
const password = ref('')
const confirmPassword = ref('')
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const isSubmitting = ref(false)
const passwordError = ref('')
const confirmPasswordError = ref('')
const showModal = ref(false)
const modalType = ref<'success' | 'error'>('error')
const modalTitle = ref('')
const modalMessage = ref('')

onMounted(() => {
  if (!email.value) {
    void router.replace({ name: 'requestPasswordRecovery' })
  } else if (!token.value) {
    void router.replace({ name: 'validateRecoveryCode' })
  }
})

function openModal(type: 'success' | 'error', title: string, message: string): void {
  modalType.value = type
  modalTitle.value = title
  modalMessage.value = message
  showModal.value = true
}

function closeModal(): void {
  showModal.value = false
}

function validateForm(): boolean {
  passwordError.value = ''
  confirmPasswordError.value = ''

  if (password.value.length < 8) {
    passwordError.value = 'A senha deve possuir no mínimo 8 caracteres.'
  }

  if (!confirmPassword.value) {
    confirmPasswordError.value = 'Confirme sua nova senha.'
  } else if (password.value !== confirmPassword.value) {
    confirmPasswordError.value = 'As senhas informadas não coincidem.'
  }

  return !passwordError.value && !confirmPasswordError.value
}

async function handleSubmit(): Promise<void> {
  if (isSubmitting.value || !validateForm()) return

  isSubmitting.value = true
  try {
    await passwordRecoveryService.updatePassword(email.value, token.value, password.value)
    sessionStorage.removeItem(EMAIL_STORAGE_KEY)
    sessionStorage.removeItem(TOKEN_STORAGE_KEY)
    openModal('success', 'Senha alterada', 'Sua senha foi alterada com sucesso.')
  } catch (error) {
    const message =
      error instanceof Error
        ? error.message
        : 'Não foi possível concluir a operação. Tente novamente.'
    openModal('error', 'Não foi possível alterar a senha', message)
  } finally {
    isSubmitting.value = false
  }
}

function returnToLogin(): void {
  closeModal()
  void router.replace({ name: 'login' })
}
</script>

<template>
  <RecoveryLayout>
    <template #header>
      <header class="login__header">
        <h2 id="reset-password-title">Crie uma nova senha</h2>
        <p>Escolha uma senha com pelo menos 8 caracteres.</p>
      </header>
    </template>

    <form class="login__form" novalidate @submit.prevent="handleSubmit">
      <div class="login__field">
        <label for="new-password">Nova senha</label>
        <div class="login__input-wrap">
          <input
            id="new-password"
            v-model="password"
            :type="showPassword ? 'text' : 'password'"
            autocomplete="new-password"
            :aria-invalid="Boolean(passwordError)"
            :aria-describedby="passwordError ? 'new-password-error' : undefined"
            @input="passwordError = ''"
          />
          <button
            class="login__password-toggle"
            type="button"
            :aria-label="showPassword ? 'Ocultar senha' : 'Mostrar senha'"
            @click="showPassword = !showPassword"
          >
            {{ showPassword ? 'Ocultar' : 'Mostrar' }}
          </button>
        </div>
        <p v-if="passwordError" id="new-password-error" class="login__error" aria-live="polite">
          {{ passwordError }}
        </p>
      </div>

      <div class="login__field">
        <label for="confirm-password">Confirmar nova senha</label>
        <div class="login__input-wrap">
          <input
            id="confirm-password"
            v-model="confirmPassword"
            :type="showConfirmPassword ? 'text' : 'password'"
            autocomplete="new-password"
            :aria-invalid="Boolean(confirmPasswordError)"
            :aria-describedby="confirmPasswordError ? 'confirm-password-error' : undefined"
            @input="confirmPasswordError = ''"
          />
          <button
            class="login__password-toggle"
            type="button"
            :aria-label="showConfirmPassword ? 'Ocultar senha' : 'Mostrar senha'"
            @click="showConfirmPassword = !showConfirmPassword"
          >
            {{ showConfirmPassword ? 'Ocultar' : 'Mostrar' }}
          </button>
        </div>
        <p
          v-if="confirmPasswordError"
          id="confirm-password-error"
          class="login__error"
          aria-live="polite"
        >
          {{ confirmPasswordError }}
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
        {{ isSubmitting ? 'Alterando...' : 'Alterar senha' }}
      </BaseButton>

      <RouterLink class="recovery__back-link" :to="{ name: 'validateRecoveryCode' }">
        Voltar para o código
      </RouterLink>
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
          @click="returnToLogin"
        >
          Ir para o login
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
