<!-- src/components/auth/LoginForm.vue -->
<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AuthInput from './AuthInput.vue'
import AuthButton from './AuthButton.vue'

const router = useRouter()
const email = ref('')
const password = ref('')
const loading = ref(false)
const remember = ref(false)

const handleLogin = async () => {
  loading.value = true
  // TODO: Integrar com store + axios
  setTimeout(() => {
    router.push('/dashboard')
    loading.value = false
  }, 1200)
}
</script>

<template>
  <form @submit.prevent="handleLogin" class="space-y-6">
    <AuthInput v-model="email" type="email" label="E-mail" placeholder="seu@email.com" />
    <AuthInput v-model="password" type="password" label="Senha" placeholder="••••••••" />
    
    <div class="flex items-center justify-between text-sm">
      <label class="flex items-center gap-2">
        <input v-model="remember" type="checkbox" class="w-4 h-4 text-emerald-600">
        <span class="text-slate-600">Lembrar acesso</span>
      </label>
      <a href="#" class="text-emerald-600 hover:underline">Esqueci minha senha</a>
    </div>

    <AuthButton :loading="loading" class="w-full">
      Entrar
    </AuthButton>
  </form>
</template>