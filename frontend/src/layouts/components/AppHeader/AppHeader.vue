<script setup lang="ts">
export interface UserProfile {
  name: string
  role: string
  avatarInitials: string
}

defineProps<{
  title: string
  subtitle?: string
  currentUser: UserProfile
  searchQuery?: string
}>()

const emit = defineEmits<{
  (e: 'update:searchQuery', value: string): void
}>()
</script>

<template>
  <header class="top-header">
    <div class="top-header__title-block">
      <h1 class="top-header__title">{{ title }}</h1>
      <p v-if="subtitle" class="top-header__subtitle">{{ subtitle }}</p>
    </div>

    <div class="top-header__actions">
      <div v-if="searchQuery !== undefined" class="top-header__search">
        <span class="top-header__search-icon" aria-hidden="true">🔍</span>
        <input
          :value="searchQuery"
          type="search"
          class="top-header__search-input"
          placeholder="Buscar..."
          aria-label="Busca global"
          @input="emit('update:searchQuery', ($event.target as HTMLInputElement).value)"
        />
      </div>

      <div class="top-header__profile">
        <div class="top-header__avatar" aria-hidden="true">
          {{ currentUser.avatarInitials }}
        </div>
        <div class="top-header__profile-info">
          <strong class="top-header__profile-name">{{ currentUser.name }}</strong>
          <span class="top-header__profile-role">{{ currentUser.role }}</span>
        </div>
      </div>
    </div>
  </header>
</template>

<style scoped src="./AppHeader.css"></style>
