import { useAuthStore } from '@/stores/authStore'

export default (to, from, next) => {
  const auth = useAuthStore()
  if (!auth.isAuthenticated) {
    next('/login')
  } else {
    next()
  }
}