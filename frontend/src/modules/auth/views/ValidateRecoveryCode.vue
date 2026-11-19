<!--
=========================================================
Project.......: ProofChain
Module........: Auth
Feature.......: Password Recovery
File..........: ValidateRecoveryCode.vue
Version.......: 1.0.0

Description...:
Valida o código de recuperação recebido pelo usuário.

Responsibilities:
- Validar e enviar o código ao backend.
- Manter o e-mail e o código para a etapa de redefinição.
- Apresentar os resultados pelo Modal global.

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
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import Modal from '@/core/components/ui/Modal/Modal.vue'
import RecoveryLayout from '@/modules/auth/components/RecoveryLayout.vue'
import { passwordRecoveryService } from '@/modules/auth/services/PasswordRecovery.service'

defineOptions({ name: 'ValidateRecoveryCodeView' })

const EMAIL_STORAGE_KEY = 'proofchain.password-recovery.email'
const TOKEN_STORAGE_KEY = 'proofchain.password-recovery.token'
const router = useRouter()
const email = ref(sessionStorage.getItem(EMAIL_STORAGE_KEY) ?? '')
const token = ref('')
const tokenError = ref('')
const isSubmitting = ref(false)
const showModal = ref(false)
const modalType = ref<'success' | 'error'>('error')
const modalTitle = ref('')
const modalMessage = ref('')
const maskedEmail = computed(() => {
  const [name, domain] = email.value.split('@')
  if (!name || !domain) return email.value
  return `${name.slice(0, 2)}***@${domain}`
})

onMounted(() => {
  if (!email.value) void router.replace({ name: 'requestPasswordRecovery' })
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

async function handleSubmit(): Promise<void> {
  tokenError.value = ''

  if (!/^\d{6}$/.test(token.value)) {
    tokenError.value = 'Informe o código de 6 dígitos recebido por e-mail.'
    return
  }
  if (isSubmitting.value) return

  isSubmitting.value = true
  try {
    await passwordRecoveryService.validateCode(email.value, token.value)
    sessionStorage.setItem(TOKEN_STORAGE_KEY, token.value)
    closeModal()
    void router.push({ name: 'resetPassword' })
  } catch (error) {
    const message =
      error instanceof Error
        ? error.message
        : 'Não foi possível validar o código informado. Tente novamente.'
    openModal('error', 'Código não validado', message)
  } finally {
    isSubmitting.value = false
  }
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
          Informe o código de verificação enviado para
          <strong class="recovery__email">{{ maskedEmail }}</strong>.
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

      <BaseButton
        type="submit"
        variant="primary"
        class="login__buttom"
        size="lg"
        full-width
        :loading="isSubmitting"
      >
        {{ isSubmitting ? 'Validando...' : 'Validar código' }}
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
          variant="secondary"
          class="modal__button modal__button--secondary"
          @click="closeModal"
        >
          Tentar novamente
        </BaseButton>
        <BaseButton
          type="button"
          variant="primary"
          class="modal__button modal__button--primary"
          @click="requestAnotherCode"
        >
          Solicitar novo código
        </BaseButton>
      </template>
    </Modal>
  </RecoveryLayout>
</template>
