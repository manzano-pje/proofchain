import { createRouter, createWebHistory } from 'vue-router'

import Landing from '@/modules/landing/views/Landing.vue'
import Login from '@/modules/auth/views/Login.vue'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import { canAccessAdminRoute } from '@/modules/business/types/Entity.types'
import AdminLayout from '@/modules/business/views/admin/AdminLayout.vue'

const institutionRoute = () =>
  import('@/modules/business/views/admin/institution/InstitutionPage.vue')

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
      name: 'platformAdmin',
      component: () => import('@/modules/business/views/admin/AdminLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: 'instituicao',
          name: 'institution',
          component: institutionRoute,
          meta: { allowedRoles: ['Super_admin', 'Admin'] },
        },
      ],
    },
    {
      path: '/institutionAdmin',
      name: 'institutionAdmin',
      component: () => import('@/modules/business/views/admin/AdminLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: 'instituicao',
          name: 'institutionAdmin-institution',
          component: institutionRoute,
          meta: { allowedRoles: ['Super_admin', 'Admin'] },
        },
      ],
    },
  ],
})

router.beforeEach((to) => {
  const authStore = useAuthStore()
  const isPlatformInstitution = Number(authStore.user?.tenantId) === 1

  if (to.name === 'login' && authStore.isAuthenticated && isPlatformInstitution) {
    return { name: 'platformAdmin' }
  }

  if (to.name === 'login' && authStore.isAuthenticated) {
    return { name: 'institutionAdmin' }
  }

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return { name: 'login', query: { redirect: to.fullPath } }
  }

  if (to.name === 'platformAdmin' && !isPlatformInstitution) {
    return { name: 'institutionAdmin' }
  }

  if (to.name === 'institutionAdmin' && isPlatformInstitution) {
    return { name: 'platformAdmin' }
  }

  if (to.meta.requiresAuth && authStore.role && !canAccessAdminRoute(to.path, authStore.role)) {
    return { name: isPlatformInstitution ? 'platformAdmin' : 'institutionAdmin' }
  }

  return true
})

export default router
