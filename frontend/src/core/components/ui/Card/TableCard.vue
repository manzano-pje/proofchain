<script setup lang="ts">
defineProps<{
  title: string
  description?: string
  totalCount?: number
  filteredCount?: number
  isLoading?: boolean
  hasResults?: boolean
}>()
</script>

<template>
  <section class="table-card">
    <header class="table-card__header">
      <div class="table-card__heading">
        <h2 class="table-card__title">{{ title }}</h2>
        <p v-if="description" class="table-card__description">{{ description }}</p>
      </div>

      <span v-if="filteredCount !== undefined" class="table-card__counter">
        {{ filteredCount }} de {{ totalCount || filteredCount }}
      </span>
    </header>

    <div class="table-card__body">
      <div v-if="isLoading" class="table-card__state">
        <span class="table-card__spinner" aria-hidden="true"></span>
        Carregando registros...
      </div>

      <div v-else-if="!hasResults" class="table-card__state">
        Nenhum registro encontrado para os critérios informados.
      </div>

      <table v-else class="table-card__table">
        <thead class="table-card__thead">
          <tr>
            <slot name="head" />
          </tr>
        </thead>
        <tbody class="table-card__tbody">
          <slot name="rows" />
        </tbody>
      </table>
    </div>

    <footer v-if="$slots.footer" class="table-card__footer">
      <slot name="footer" />
    </footer>
  </section>
</template>

<style scoped src="./TableCard.css"></style>
