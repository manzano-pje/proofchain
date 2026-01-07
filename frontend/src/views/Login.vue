<template>
  <div class="login-container">
    <div class="login-box">
      <h2>Login</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="email">Email</label>
          <input 
            id="email" 
            v-model="email" 
            type="email" 
            required 
            placeholder="seu@email.com"
          />
        </div>
        
        <div class="form-group">
          <label for="password">Senha</label>
          <input 
            id="password" 
            v-model="password" 
            type="password" 
            required 
            placeholder="********"
          />
        </div>

        <button type="submit" class="btn btn-primary">Entrar</button>
      </form>
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const email = ref('')
const password = ref('')
const errorMessage = ref('')

const handleLogin = () => {
    // Simulação de login simples
    if (email.value && password.value) {
        // Em um app real, aqui chamaria o serviço de auth
        console.log('Login attempt:', email.value)
        router.push('/dashboard')
    } else {
        errorMessage.value = 'Por favor, preencha todos os campos.'
    }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  padding: 20px;
}

.login-box {
  background: var(--white);
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  margin-bottom: 24px;
  color: var(--green);
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: var(--black);
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--gray-medium);
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

input:focus {
  border-color: var(--green);
  outline: none;
}

.btn {
  width: 100%;
  padding: 12px;
  margin-top: 10px;
  font-size: 1.1rem;
}

.error-message {
  margin-top: 16px;
  color: var(--red);
  text-align: center;
  font-size: 0.9rem;
}
</style>
