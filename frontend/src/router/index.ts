import { createRouter, createWebHistory } from 'vue-router'

import Landing from '@/modules/landing/views/Landing.vue'
import Login from '@/modules/auth/views/Login.vue'
import { useAuthStore } from '@/modules/auth/stores/Auth.store'
import { canAccessAdminRoute } from '@/modules/business/types/Entity.types'
import AdminLayout from '@/modules/business/views/admin/AdminLayout.vue'

const institutionRoute = () =>
  import('@/modules/business/views/admin/institution/InstitutionPage.vue')
const courseRoute = () =>
  import('@/modules/business/views/admin/courses/CoursePage.vue')

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
        {
          path: 'cursos',
          name: 'admin-courses',
          component: courseRoute,
          meta: { allowedRoles: ['Super_admin', 'Admin', 'user'] },
        },
        // Rotas para telas ainda não disponibilizadas; habilitar conforme a implementação:
        // { path: 'dashboard', name: 'admin-dashboard', component: () => import('@/modules/business/views/admin/dashboard/DashboardPage.vue') },
        // { path: 'participantes', name: 'admin-participants', component: () => import('@/modules/business/views/admin/participants/ParticipantsPage.vue') },
        // { path: 'turmas', name: 'admin-classes', component: () => import('@/modules/business/views/admin/classes/ClassesPage.vue') },
        // { path: 'instrutores', name: 'admin-instructors', component: () => import('@/modules/business/views/admin/instructors/InstructorsPage.vue') },
        // { path: 'certificados', name: 'admin-certificates', component: () => import('@/modules/business/views/admin/certificates/CertificatesPage.vue') },
        // { path: 'certificados/gerar', name: 'admin-generate-certificates', component: () => import('@/modules/business/views/admin/certificates/GenerateCertificatesPage.vue') },
        // { path: 'certificados/validar', name: 'admin-validate-certificates', component: () => import('@/modules/business/views/admin/certificates/ValidateCertificatesPage.vue') },
        // { path: 'usuarios', name: 'admin-users', component: () => import('@/modules/business/views/admin/users/UsersPage.vue') },
        // { path: 'configuracoes', name: 'admin-settings', component: () => import('@/modules/business/views/admin/settings/SettingsPage.vue') },
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
        {
          path: 'cursos',
          name: 'institutionAdmin-courses',
          component: courseRoute,
          meta: { allowedRoles: ['Super_admin', 'Admin', 'user'] },
        },
        {
          path: 'dashboard',
          name: 'admin-dashboard',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'participantes',
          name: 'admin-participants',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'turmas',
          name: 'admin-classes',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'instrutores',
          name: 'admin-instructors',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'certificados',
          name: 'admin-certificates',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'certificados/gerar',
          name: 'admin-generate-certificates',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'certificados/validar',
          name: 'admin-validate-certificates',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'usuarios',
          name: 'admin-users',
          redirect: { name: 'admin-home' },
        },
        {
          path: 'configuracoes',
          name: 'admin-settings',
          redirect: { name: 'admin-home' },
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
