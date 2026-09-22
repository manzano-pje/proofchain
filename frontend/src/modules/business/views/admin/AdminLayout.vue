<script setup lang="ts">
import { ref } from 'vue'
import AppSidebar, { type MenuGroup } from '@/layouts/components/AppSidebar/AppSidebar.vue'
import AppHeader, { type UserProfile } from '@/layouts/components/AppHeader/AppHeader.vue'
import AppFooter from '@/layouts/components/AppFooter/AppFooter.vue'

defineProps<{
  title: string
  subtitle?: string
  menuGroups: readonly MenuGroup[]
  activeMenuItem: string
  currentUser: UserProfile
  searchQuery?: string
  appVersion?: string
}>()

const emit = defineEmits<{
  (e: 'select-menu', id: string): void
  (e: 'update:searchQuery', value: string): void
  (e: 'logout'): void
}>()

const isCollapsed = ref(false)
const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}
</script>

<template>
  <div class="admin-page">
    <AppSidebar
      :menu-groups="menuGroups"
      :active-item-id="activeMenuItem"
      :is-collapsed="isCollapsed"
      @select-menu="emit('select-menu', $event)"
      @toggle-collapse="toggleSidebar"
      @logout="emit('logout')"
    />

    <div class="admin-page__main">
      <AppHeader
        :title="title"
        :subtitle="subtitle"
        :current-user="currentUser"
        :search-query="searchQuery"
        @update:search-query="emit('update:searchQuery', $event)"
      />

      <main class="admin-page__content">
        <slot>
          <!--
            Conteúdo temporário da base administrativa.
            As telas reais serão inseridas neste slot quando suas rotas e
            fluxos de navegação forem definidos.
          -->
          <section class="admin-page__placeholder" aria-labelledby="admin-placeholder-title">
            <span class="admin-page__placeholder-kicker">Área administrativa</span>
            <h2 id="admin-placeholder-title">Base do painel pronta</h2>
            <p>
              O layout, o menu, o cabeçalho e o rodapé estão conectados. As opções do menu e a
              pesquisa permanecem sem navegação enquanto as telas administrativas são construídas.
            </p>
          </section>
        </slot>
      </main>

      <AppFooter :version="appVersion" />
    </div>
  </div>
</template>

<style scoped src="./AdminLayout.css"></style>
