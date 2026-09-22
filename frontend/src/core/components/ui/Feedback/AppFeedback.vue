<script setup lang="ts">
defineProps<{
  type: 'success' | 'error' | 'info'
  message: string
}>()

const emit = defineEmits<{
  (e: 'close'): void
}>()
</script>

<template>
  <div :class="['admin-feedback', `admin-feedback--${type}`]" role="status">
    <span class="admin-feedback__message">{{ message }}</span>
    <button
      type="button"
      class="admin-feedback__close"
      aria-label="Fechar aviso"
      @click="emit('close')"
    >
      ✕
    </button>
  </div>
</template>

<style scoped src="./AppFeedback.css"></style>
