<!--
=========================================================
Project.......: ProofChain
Component.....: Modal
File..........: Modal.vue
Version.......: 1.0.0

Description...:
Modal global e reutilizável para feedback visual da plataforma.

Responsibility:
Apresentar estados de sucesso, atenção e erro sem conhecer
regras de negócio ou controlar ações da tela consumidora.

Dependencies..:
- BaseIcon
- Modal.css

Methodology...:
BEM

=========================================================
-->

<script setup lang="ts">
import { computed, useId } from 'vue'

import BaseIcon from '@/core/components/base/BaseIcon/BaseIcon.vue'

import './Modal.css'

defineOptions({
  name: 'ProofChainModal',
})

/* ======================================================
   TIPOS
====================================================== */

type ModalType = 'success' | 'warning' | 'error'

interface ModalProps {
  visible: boolean
  type: ModalType
  title: string
}

const props = defineProps<ModalProps>()

const emit = defineEmits<{
  (event: 'close'): void
}>()

/*
 * Os slots mantêm o conteúdo e as ações sob responsabilidade
 * da tela consumidora, permitindo múltiplas ações no futuro.
 */
defineSlots<{
  default?: () => unknown
  actions?: () => unknown
}>()

/* ======================================================
   ESTADO VISUAL
   Mapeia o tipo recebido para as classes e o ícone do modal.
====================================================== */

const modalClasses = computed(() => ['modal', `modal--${props.type}`])

const iconLabels: Record<ModalType, string> = {
  success: 'Sucesso',
  warning: 'Atenção',
  error: 'Erro',
}

const iconLabel = computed(() => iconLabels[props.type])
const modalTitleId = useId()

const handleClose = (): void => {
  emit('close')
}
</script>

<template>
  <Transition name="modal">
    <div v-if="visible" class="modal__overlay">
      <section
        :class="modalClasses"
        role="dialog"
        aria-modal="true"
        :aria-labelledby="modalTitleId"
      >
        <!-- ======================================================
             CABEÇALHO
             Status visual, título e fechamento explícito.
        ======================================================= -->
        <header class="modal__header">
          <div class="modal__icon" :aria-label="iconLabel">
            <BaseIcon name="status" size="lg" :aria-label="iconLabel">
              <svg
                v-if="type === 'success'"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2.5"
                stroke-linecap="round"
                stroke-linejoin="round"
                aria-hidden="true"
              >
                <path d="m5 12 4 4L19 6" />
              </svg>
              <svg
                v-else-if="type === 'warning'"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2.5"
                stroke-linecap="round"
                stroke-linejoin="round"
                aria-hidden="true"
              >
                <path d="M12 8v4" />
                <path d="M12 16h.01" />
                <path
                  d="M10.3 3.8 2.6 17a2 2 0 0 0 1.7 3h15.4a2 2 0 0 0 1.7-3L13.7 3.8a2 2 0 0 0-3.4 0Z"
                />
              </svg>
              <svg
                v-else
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2.5"
                stroke-linecap="round"
                stroke-linejoin="round"
                aria-hidden="true"
              >
                <circle cx="12" cy="12" r="9" />
                <path d="m15 9-6 6" />
                <path d="m9 9 6 6" />
              </svg>
            </BaseIcon>
          </div>

          <h2 :id="modalTitleId" class="modal__title">{{ title }}</h2>

          <button class="modal__close" type="button" aria-label="Fechar" @click="handleClose">
            <BaseIcon name="close" size="sm" aria-label="Fechar">
              <svg
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                aria-hidden="true"
              >
                <path d="m6 6 12 12" />
                <path d="m18 6-12 12" />
              </svg>
            </BaseIcon>
          </button>
        </header>

        <!-- ======================================================
             CONTEÚDO
             O slot padrão recebe a mensagem da tela consumidora.
        ======================================================= -->
        <div class="modal__body">
          <slot />
        </div>

        <!-- ======================================================
             AÇÕES
             O slot permite uma ou várias ações definidas pela tela.
        ======================================================= -->
        <footer v-if="$slots.actions" class="modal__actions">
          <slot name="actions" />
        </footer>
      </section>
    </div>
  </Transition>
</template>
