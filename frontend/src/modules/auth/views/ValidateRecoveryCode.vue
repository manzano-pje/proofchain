<!--
=========================================================
Project.......: ProofChain
Module........: Auth
Feature.......: Password Recovery
File..........: ValidateRecoveryCode.vue
Version.......: 1.0.0

Description...:
Apresenta a etapa de entrada do código recebido por e-mail.

Responsibilities:
- Validar o formato do código informado.
- Orientar o usuário sobre o estado da integração.
- Apresentar mensagens pelo Modal global.

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
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import Modal from '@/core/components/ui/Modal/Modal.vue'
import RecoveryLayout from '@/modules/auth/components/RecoveryLayout.vue'
import { RECOVERY_EMAIL_STORAGE_KEY } from '@/modules/auth/services/PasswordRecovery.service'

defineOptions({ name: 'ValidateRecoveryCodeView' })

const router = useRouter()
const email = ref(sessionStorage.getItem(RECOVERY_EMAIL_STORAGE_KEY) ?? '')
const token = ref('')
const tokenError = ref('')
const showModal = ref(false)
const modalType = ref<'success' | 'warning' | 'error'>('warning')
const modalTitle = ref('')
const modalMessage = ref('')
const maskedEmail = computed(() => {
  const [name, domain] = email.value.split('@')
  if (!name || !domain) return email.value
  return `${name.slice(0, 2)}***@${domain}`
})

function openModal(type: 'success' | 'warning' | 'error', title: string, message: string): void {
  modalType.value = type
  modalTitle.value = title
  modalMessage.value = message
  showModal.value = true
}

function closeModal(): void {
  showModal.value = false
}

function handleSubmit(): void {
  tokenError.value = ''

  if (!/^\d{6}$/.test(token.value)) {
    tokenError.value = 'Informe o código de 6 dígitos recebido por e-mail.'
    return
  }

  // TODO: Conectar à validação do backend após ela deixar de alterar a senha.
  openModal(
    'warning',
    'Validação indisponível',
    'Não foi possível validar o código neste momento. Tente novamente mais tarde.',
  )
}

function requestAnotherCode(): void {
  closeModal()
  void router.push({ name: 'requestPasswordRecovery' })
}
</script>

<template>
  <RecoveryLayout>
    <template #header>
      <header class="login__header">
        <h2 id="recovery-code-title">Confira seu e-mail</h2>
        <p class="recovery__description">
          Se houver uma conta associada a <strong class="recovery__email">{{ maskedEmail }}</strong>,
          enviaremos um código de recuperação.
        </p>
      </header>
    </template>

    <form class="login__form" novalidate @submit.prevent="handleSubmit">
      <div class="login__field">
        <label for="recovery-code">Código de verificação</label>
        <div class="login__input-wrap">
          <input
            id="recovery-code"
            v-model="token"
            type="text"
            inputmode="numeric"
            autocomplete="one-time-code"
            maxlength="6"
            placeholder="000000"
            :aria-invalid="Boolean(tokenError)"
            :aria-describedby="tokenError ? 'recovery-code-error' : undefined"
            @input="
              token = token.replace(/\D/g, '').slice(0, 6);
              tokenError = ''
            "
          />
        </div>
        <p v-if="tokenError" id="recovery-code-error" class="login__error" aria-live="polite">
          {{ tokenError }}
        </p>
      </div>

      <BaseButton type="submit" variant="primary" class="login__buttom" size="lg" full-width>
        Validar código
      </BaseButton>

      <RouterLink class="recovery__back-link" :to="{ name: 'requestPasswordRecovery' }">
        Solicitar outro código
      </RouterLink>
      <RouterLink class="recovery__back-link" :to="{ name: 'login' }">
        Voltar para o login
      </RouterLink>
    </form>

    <Modal :visible="showModal" :type="modalType" :title="modalTitle" @close="closeModal">
      <p>{{ modalMessage }}</p>

      <template #actions>
        <BaseButton
          type="button"
          variant="primary"
          class="modal__button modal__button--primary"
          @click="requestAnotherCode"
        >
          Solicitar novo código
        </BaseButton>
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
