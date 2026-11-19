<!--
=========================================================
Project.......: ProofChain
Module........: Auth
Feature.......: Password Recovery
File..........: ResetPassword.vue
Version.......: 1.0.0

Description...:
Apresenta o formulário para definir uma nova senha.

Responsibilities:
- Validar a nova senha e sua confirmação.
- Impedir o envio enquanto a validação do código não estiver integrada.
- Apresentar mensagens pelo Modal global.

Dependencies..:
- RecoveryLayout
- BaseButton
- Modal
- Login.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
import { ref } from 'vue'

import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import Modal from '@/core/components/ui/Modal/Modal.vue'
import RecoveryLayout from '@/modules/auth/components/RecoveryLayout.vue'

defineOptions({ name: 'ResetPasswordView' })

const password = ref('')
const confirmPassword = ref('')
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const passwordError = ref('')
const confirmPasswordError = ref('')
const showModal = ref(false)
const modalTitle = ref('')
const modalMessage = ref('')

function openModal(title: string, message: string): void {
  modalTitle.value = title
  modalMessage.value = message
  showModal.value = true
}

function closeModal(): void {
  showModal.value = false
}

function handleSubmit(): void {
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

  if (passwordError.value || confirmPasswordError.value) return

  // TODO: Integrar PATCH /api/v1/user/reset-password quando o endpoint exigir o código validado.
  openModal(
    'Redefinição indisponível',
    'Não foi possível concluir a redefinição agora. Tente novamente mais tarde.',
  )
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

      <BaseButton type="submit" variant="primary" class="login__buttom" size="lg" full-width>
        Alterar senha
      </BaseButton>

      <RouterLink class="recovery__back-link" :to="{ name: 'validateRecoveryCode' }">
        Voltar para o código
      </RouterLink>
      <RouterLink class="recovery__back-link" :to="{ name: 'login' }">
        Voltar para o login
      </RouterLink>
    </form>

    <Modal :visible="showModal" type="warning" :title="modalTitle" @close="closeModal">
      <p>{{ modalMessage }}</p>

      <template #actions>
        <BaseButton
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
