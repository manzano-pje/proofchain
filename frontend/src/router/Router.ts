import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/Dashboard',
      name: 'Dashboard',
      // divisão de código em nível de rota
      // isso gera um chunk separado (About.[hash].js) para esta rota
      // que é carregado sob demanda quando a rota é visitada.
      component: () => import('../views/Dashboard.vue'),
    },
    // {
    //   path: '/Login',
    //   name: 'Login',
    //   // divisão de código em nível de rota
    //   // isso gera um chunk separado (About.[hash].js) para esta rota
    //   // que é carregado sob demanda quando a rota é visitada.
    //   component: () => import('../views/Login.vue'),
    // },

  ],
})

export default router
