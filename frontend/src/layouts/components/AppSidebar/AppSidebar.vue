<script setup lang="ts">
import type { Component } from 'vue'
import {
  Award,
  BookOpen,
  Building2,
  ChevronLeft,
  ChevronRight,
  CircleHelp,
  GraduationCap,
  LayoutDashboard,
  LogOut,
  Search,
  Settings,
  Sparkles,
  Users,
  UsersRound,
} from 'lucide-vue-next'

export interface MenuItem {
  id: string
  label: string
  icon: string
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
  appVersion?: string
}>()

const emit = defineEmits<{
  (e: 'select-menu', id: string): void
  (e: 'toggle-collapse'): void
  (e: 'logout'): void
}>()

const iconsByItemId: Record<string, Component> = {
  overview: LayoutDashboard,
  participants: UsersRound,
  courses: BookOpen,
  classes: CircleHelp,
  instructors: GraduationCap,
  certificates: Award,
  'generate-certificates': Sparkles,
  validate: Search,
  institution: Building2,
  users: Users,
  settings: Settings,
}

const iconFor = (itemId: string): Component => iconsByItemId[itemId] || CircleHelp
</script>

<template>
  <aside :class="['sidebar', { 'sidebar--collapsed': isCollapsed }]">
    <nav class="sidebar__nav" aria-label="Navegação do sistema">
      <button
        type="button"
        class="sidebar__toggle"
        :aria-label="isCollapsed ? 'Expandir menu' : 'Recolher menu'"
        :title="isCollapsed ? 'Expandir menu' : 'Recolher menu'"
        @click="emit('toggle-collapse')"
      >
        <ChevronRight v-if="isCollapsed" :size="18" :stroke-width="2" aria-hidden="true" />
        <ChevronLeft v-else :size="18" :stroke-width="2" aria-hidden="true" />
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
              <component
                :is="iconFor(item.id)"
                class="sidebar__icon"
                :size="18"
                :stroke-width="2"
                aria-hidden="true"
              />
              <span class="sidebar__text">{{ item.label }}</span>
              <span v-if="item.badge" class="sidebar__badge">{{ item.badge }}</span>
            </button>
          </li>
        </ul>
      </div>

      <ul class="sidebar__list sidebar__list--logout">
        <li class="sidebar__item">
          <button
            type="button"
            class="sidebar__link sidebar__logout"
            title="Sair"
            @click="emit('logout')"
          >
            <LogOut class="sidebar__icon" :size="18" :stroke-width="2" aria-hidden="true" />
            <span class="sidebar__text">Sair</span>
          </button>
        </li>
      </ul>
    </nav>

    <div v-if="!isCollapsed" class="sidebar__footer">
      <span>ProofChain</span>
      <span v-if="appVersion">v{{ appVersion }}</span>
    </div>
  </aside>
</template>

<style scoped src="./AppSidebar.css"></style>
