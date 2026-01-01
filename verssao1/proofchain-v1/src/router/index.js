import { createRouter, createWebHistory } from 'vue-router'
import LandingPage from '@/views/landing/LandingPage.vue'
import Login from '@/views/auth/Login.vue'
import Dashboard from '@/views/admin/Dashboard.vue'
import authGuard from '@/guards/authGuard'

const routes = [
  {
    path: '/',
    component: LandingPage
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/admin',
    component: Dashboard,
    beforeEnter: authGuard
  },
  {
    path: '/admin/users',
    component: () => import('@/views/admin/Users.vue'),
    beforeEnter: authGuard
  },
  {
    path: '/admin/certificates',
    component: () => import('@/views/admin/Certificates.vue'),
    beforeEnter: authGuard
  },
  {
    path: '/admin/entities',
    component: () => import('@/views/admin/Entities.vue'),
    beforeEnter: authGuard
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})