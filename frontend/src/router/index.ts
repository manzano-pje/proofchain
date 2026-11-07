import { createRouter, createWebHistory } from 'vue-router'

import Landing from '@/modules/landing/views/Landing.vue'
import Login from '@/modules/auth/views/Login.vue'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import { canAccessAdminRoute } from '@/modules/business/types/Entity.types'


const adminDashboardRoute = () =>
  import('@/modules/business/views/admin/AdminLayout.vue')
const participantRoute = () =>
  import('@/modules/business/views/admin/prticipant/ParticipantPage.vue')
const courseRoute = () =>
  import('@/modules/business/views/admin/courses/CoursePage.vue')
const courseClassRoute = () =>
  import('@/modules/business/views/admin/AdminLayout.vue')
const instructorRoute = () =>
  import('@/modules/business/views/admin/AdminLayout.vue')
const certificatesRoute = () =>
  import('@/modules/business/views/admin/AdminLayout.vue')
const generateCertificatesRoute = () =>
  import('@/modules/business/views/admin/AdminLayout.vue')
const institutionRoute = () =>
  import('@/modules/business/views/admin/institution/InstitutionPage.vue')
const usersRoute = () =>
  import('@/modules/business/views/admin/AdminLayout.vue')


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
      meta: { requiresAuth: true, allowedRoles: ['Admin', 'Super_admin'] },
      children: [
        {
          path: 'instituicao',
          name: 'institution',
          component: institutionRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'cursos',
          name: 'admin-courses',
          component: courseRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'participantes',
          name: 'admin-participants',
          component: participantRoute,
          meta: { allowedRoles: ['Admin'] },
        }
       ],
    },
    {
      path: '/institutionAdmin',
      name: 'institutionAdmin',
      component: () => import('@/modules/business/views/admin/AdminLayout.vue'),
      meta: { requiresAuth: true, allowedRoles: ['Admin'] },
      children: [
        {
          path: 'instituicao',
          name: 'institutionAdmin-institution',
          component: institutionRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'turmas',
          name: 'institutionAdmin-classes',
          component: courseClassRoute,
          meta: { allowedRoles: ['Admin'] },
        },
                {
          path: 'cursos',
          name: 'institutionAdmin-courses',
          component: courseRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'participantes',
          name: 'institutionAdmin-participants',
          component: participantRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'dashboard',
          name: 'admin-dashboard',
         component: adminDashboardRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'turmas',
          name: 'admin-classes',
          component: participantRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'instrutores',
          name: 'admin-instructors',
          component: instructorRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'certificados',
          name: 'admin-certificates',
          component: certificatesRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'certificados/gerar',
          name: 'admin-generate-certificates',
          component: generateCertificatesRoute,
          meta: { allowedRoles: ['Admin'] },
        },
        {
          path: 'certificados/validar',
          name: 'admin-validate-certificates',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'usuarios',
          name: 'admin-users',
          component: usersRoute,
          meta: { allowedRoles: ['Admin'] },
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

  if (
    to.meta.requiresAuth &&
    (!authStore.role || !canAccessAdminRoute(to.path, authStore.role))
  ) {
    return { name: 'home' }
  }

  return true
})

export default router
