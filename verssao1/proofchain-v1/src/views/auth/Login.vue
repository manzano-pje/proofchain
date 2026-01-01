<template>
  <div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card p-4 shadow mb-5 bg-body rounded" style="width: 22rem;">
      <div class="card-body">
        <h3 class="card-title text-center mb-4">Acesso</h3>
        <form @submit.prevent="handleLogin">
          <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" v-model="email" required>
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" id="password" v-model="password" required>
          </div>
          <div class="d-grid gap-2">
            <button type="submit" class="btn btn-primary w-100">Entrar</button>
            <p class="text-center mt-3">
              Não tem conta?
              <a href="#" onclick="switchAuth('register')">Cadastre-se</a>
            </p>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'
import authService from '@/services/authService'

const email = ref('')
const password = ref('')
const router = useRouter()
const authStore = useAuthStore()

const handleLogin = async () => {
  try {
    const data = await authService.login({ email: email.value, password: password.value })
    authStore.login(data.token, data.user)
    router.push('/admin')
  } catch (error) {
    alert('Login failed')
  }
}
</script>
