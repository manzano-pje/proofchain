import { createRouter, createWebHistory } from 'vue-router'

import Landing from '@/modules/landing/views/Landing.vue'
import Login from '@/modules/auth/views/Login.vue'

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
  ],
})

export default router
