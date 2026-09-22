import { createRouter, createWebHistory } from 'vue-router'

import Landing from '@/modules/landing/views/Landing.vue'
import Login from '@/modules/auth/views/Login.vue'
import { ADMIN_MENU } from '@/modules/business/types/Entity.types'
import type { UserProfile } from '@/layouts/components/AppHeader/AppHeader.vue'

/**
 * Dados mínimos para validar visualmente a base administrativa.
 *
 * Este mock fica no router por enquanto porque a área administrativa ainda
 * não possui store nem integração com autenticação/backend. Quando esses
 * contratos existirem, substituir este objeto por dados do estado da sessão.
 */
const adminLayoutMock = {
  title: 'Painel administrativo',
  subtitle: 'Gestão da plataforma ProofChain',
  menuGroups: ADMIN_MENU,
  activeMenuItem: 'overview',
  currentUser: {
    name: 'Ana Beatriz',
    role: 'Administradora',
    avatarInitials: 'AB',
  } satisfies UserProfile,
  searchQuery: '',
  appVersion: '0.1.0-mock',
} as const

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),

  routes: [
    {
      path: '/',
      name: 'home',
      component: Landing,
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/onboarding',
      name: 'Onboarding',
      component: () => import('@/modules/business/views/onboarding/Onboarding.vue'),
      // meta: { public: true } // se houver guard
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('@/modules/business/views/admin/AdminLayout.vue'),
      props: adminLayoutMock,
      // TODO: adicionar meta.requiresAuth quando o guard de sessão existir.
    },
  ],
})

export default router
