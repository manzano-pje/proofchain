import { createRouter, createWebHistory } from 'vue-router'

import Landing from '@/modules/landing/views/Landing.vue'
import Login from '@/modules/auth/views/Login.vue'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import { canAccessAdminRoute } from '@/modules/business/types/Entity.types'

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
      path: '/admin/:section(.*)*',
      name: 'admin',
      component: () => import('@/modules/business/views/admin/AdminLayout.vue'),
      meta: { requiresAuth: true },
    },
  ],
})

router.beforeEach((to) => {
  const authStore = useAuthStore()

  if (to.name === 'login' && authStore.isAuthenticated) {
    return { name: 'admin' }
  }

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  if (to.meta.requiresAuth && authStore.role && !canAccessAdminRoute(to.path, authStore.role)) {
    return { name: 'admin' }
  }

  return true
})

export default router
