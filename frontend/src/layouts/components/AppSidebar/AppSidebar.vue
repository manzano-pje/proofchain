<script setup lang="ts">
export interface MenuItem {
  id: string
  label: string
  icon: string
  route: string
  badge?: string | number
  disabled?: boolean
}

export interface MenuGroup {
  category: string
  label: string
  items: readonly MenuItem[]
}

defineProps<{
  menuGroups: readonly MenuGroup[]
  activeItemId: string
  isCollapsed?: boolean
}>()

const emit = defineEmits<{
  (e: 'select-menu', id: string): void
  (e: 'toggle-collapse'): void
  (e: 'logout'): void
}>()
</script>

<template>
  <aside :class="['sidebar', { 'sidebar--collapsed': isCollapsed }]">
    <div class="sidebar__brand">
      <a href="#" class="sidebar__brand-link">
        <slot name="brand">
          <img src="@/assets/images/logo/logo_adm_horizontal_black.svg" alt="Logo" />
        </slot>
      </a>
    </div>

    <nav class="sidebar__nav" aria-label="Navegação do sistema">
      <button
        type="button"
        class="sidebar__toggle"
        aria-label="Recolher menu"
        @click="emit('toggle-collapse')"
      >
        ☰
      </button>

      <div v-for="group in menuGroups" :key="group.category" class="sidebar__group">
        <span class="sidebar__group-label">{{ group.label }}</span>

        <ul class="sidebar__list">
          <li v-for="item in group.items" :key="item.id" class="sidebar__item">
            <button
              type="button"
              class="sidebar__link"
              :class="{
                'sidebar__link--active': activeItemId === item.id,
                'sidebar__link--disabled': item.disabled,
              }"
              :disabled="item.disabled"
              :aria-current="activeItemId === item.id ? 'page' : undefined"
              @click="emit('select-menu', item.id)"
            >
              <span class="sidebar__icon" aria-hidden="true">{{ item.icon }}</span>
              <span class="sidebar__text">{{ item.label }}</span>
              <span v-if="item.badge" class="sidebar__badge">{{ item.badge }}</span>
            </button>
          </li>
        </ul>
      </div>
    </nav>

    <div class="sidebar__footer">
      <button type="button" class="sidebar__logout" @click="emit('logout')">
        <span class="sidebar__icon" aria-hidden="true">⎋</span>
        <span class="sidebar__text">Sair</span>
      </button>
    </div>
  </aside>
</template>

<style scoped src="./AppSidebar.css"></style>
