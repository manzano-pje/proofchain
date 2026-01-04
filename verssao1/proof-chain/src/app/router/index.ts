import { createRouter, createWebHistory } from 'vue-router'
import Applayout from '@/shared/layouts/applayout.vue'


  const routes = [
  {
    path: '/',
    name: 'Home',
    component: Applayout
  }
]
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router




