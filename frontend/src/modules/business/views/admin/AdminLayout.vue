<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'

import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import { getAdminMenuForRole } from '@/modules/business/types/Entity.types'
import AppSidebar, { type MenuGroup } from '@/layouts/components/AppSidebar/AppSidebar.vue'
import AppHeader, { type UserProfile } from '@/layouts/components/AppHeader/AppHeader.vue'
import AppFooter from '@/layouts/components/AppFooter/AppFooter.vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const isCollapsed = ref(false)
const searchQuery = ref('')

const currentUser = computed<UserProfile | null>(() => {
  if (!authStore.user) return null

  return {
    name: authStore.user.name,
    role: authStore.user.role,
    avatarInitials: authStore.user.name
      .split(' ')
      .map((namePart) => namePart[0])
      .join('')
      .slice(0, 2)
      .toUpperCase(),
  }
})

const menuGroups = computed<readonly MenuGroup[]>(() =>
  authStore.role ? getAdminMenuForRole(authStore.role) : [],
)

const activeMenuItem = computed(() => {
  const item = menuGroups.value
    .flatMap((group) => group.items)
    .find((menuItem) => menuItem.route === route.path)
  return item?.id ?? 'overview'
})

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

function selectMenu(id: string): void {
  const item = menuGroups.value
    .flatMap((group) => group.items)
    .find((menuItem) => menuItem.id === id)
  if (item) void router.push(item.route)
}

function logout(): void {
  authStore.clearSession()
  void router.push({ name: 'login' })
}
</script>

<template>
  <div class="admin-page">
    <AppSidebar
      :menu-groups="menuGroups"
      :active-item-id="activeMenuItem"
      :is-collapsed="isCollapsed"
      @select-menu="selectMenu"
      @toggle-collapse="toggleSidebar"
      @logout="logout"
    />

    <div class="admin-page__main">
      <AppHeader
        v-if="currentUser"
        title="Painel administrativo"
        subtitle="Gestão da plataforma ProofChain"
        :current-user="currentUser"
        :search-query="searchQuery"
        @update:search-query="searchQuery = $event"
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

      <AppFooter version="0.1.0" />
    </div>
  </div>
</template>

<style scoped src="./AdminLayout.css"></style>
