<script setup lang="ts">
defineProps<{
  title: string
  description?: string
  isEditing?: boolean
  isSubmitting?: boolean
  isValid?: boolean
  submitLabel?: string
}>()

const emit = defineEmits<{
  (e: 'submit'): void
  (e: 'cancel'): void
}>()
</script>

<template>
  <section class="form-card">
    <header class="form-card__header">
      <div class="form-card__heading">
        <h2 class="form-card__title">{{ title }}</h2>
        <p v-if="description" class="form-card__description">{{ description }}</p>
      </div>

      <div v-if="$slots.metrics" class="form-card__metrics">
        <slot name="metrics" />
      </div>
    </header>

    <form class="form-card__form" @submit.prevent="emit('submit')">
      <div class="form-card__grid">
        <slot />
      </div>

      <footer class="form-card__actions">
        <button
          v-if="isEditing"
          type="button"
          class="form-card__button form-card__button--ghost"
          @click="emit('cancel')"
        >
          Cancelar
        </button>

        <button
          type="submit"
          class="form-card__button form-card__button--primary"
          :disabled="!isValid || isSubmitting"
        >
          <span v-if="isSubmitting" class="form-card__spinner" aria-hidden="true"></span>
          {{ isSubmitting ? 'Processando...' : submitLabel || 'Salvar' }}
        </button>
      </footer>
    </form>
  </section>
</template>

<style scoped src="./FormCard.css"></style>
