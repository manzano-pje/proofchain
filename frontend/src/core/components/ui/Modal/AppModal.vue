<script setup lang="ts">
defineProps<{
  isOpen: boolean
}>()

const emit = defineEmits<{
  (e: 'close'): void
}>()
</script>

<template>
  <Teleport to="body">
    <div
      v-if="isOpen"
      class="modal-backdrop"
      role="dialog"
      aria-modal="true"
      @click.self="emit('close')"
    >
      <div class="modal-content">
        <header class="modal-content__header">
          <slot name="header" />
          <button
            type="button"
            class="modal-content__close"
            aria-label="Fechar modal"
            @click="emit('close')"
          >
            ✕
          </button>
        </header>

        <div class="modal-content__body">
          <slot />
        </div>

        <footer v-if="$slots.footer" class="modal-content__footer">
          <slot name="footer" />
        </footer>
      </div>
    </div>
  </Teleport>
</template>

<style scoped src="./AppModal.css"></style>
