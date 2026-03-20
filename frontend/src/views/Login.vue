<template>
  <div class="login-container" >
    <div class="login-imagem">
      <img class="imagem" src="../assets/img/imagem-login.png" alt="Imagem de escudo com cadeados" />    
    </div>
    <div class="login-box">
      <div class="form-logo">
        <a href="../">
          <img class="imagem" src="../assets/img/logo-horizontal.png" alt="Imagem logotipo Proofchain" />    
        </a>
      </div>
      <h2>Login</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="email">Email</label>
          <input 
            id="email" 
            v-model="email" 
            type="email" 
            required 
            placeholder="seu@email.com"/>
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const email = ref('')
const password = ref('')

function validateEmail(email: string): boolean {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return regex.test(email)
}

function validatePassword(password: string): boolean {
  const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/
  return regex.test(password)
}

async function submitForm() {
 
  let isValid = true

  if (!validateEmail(email.value)) {
    console.log('E-mail inválido')
    isValid = false
  }

  if (!validatePassword(password.value)) {
    console.log('Senha inválida')
    isValid = false
  }

// Envio dos dados para a API
  try {
    const response = await fetch('http://localhost:8080/auth', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({
        email: email.value,
        password: password.value       
      })
    })

    const data = await response.json()

    switch (response.status) {
      case 401:
        alert(data.message)
        // console.log('Não autorizado', data.message, 'info')
        break

      case 404:
        console.log('Não encontrado', data.message, 'info')
        break

      case 500:
        console.log('Erro interno', data.message, 'danger')
        break

      default:
        console.log('Erro', 'Erro inesperado.', 'danger')
    }
    
  } catch (error) {
    console.error('Erro:', error)
  }
}
</script>

<style scoped>

.login-container {
  display: flex;
  height: 100vh;
  width: 100vw;
      background-color: blue;
}

 .login-imagem{
    width: 50%;
    height: 100%;

 }

 .login-imagem img{
   width: 100%;  
   height: 100%; 
   object-fit: cover;
 }

 .form-logo {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
 }

 .form-logo img{
  width: 200px;
 }

.login-box {
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-self: center;
  background: var(--green-light);
  padding: 60px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);  
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
  padding: 3px;
  border: 1px solid var(--green);
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
  box-shadow: 4px 4px 12px rgba(0,0,0,0.1);
}

input:focus {
  border-color: var(--green);
  outline: none;
}

.btn {
  width: 20%;
  padding: 7px;
  margin-top: 10px;
  font-size: 1.1rem;
  justify-content: center;
}

.error-message {
  margin-top: 16px;
  color: var(--red);
  text-align: center;
  font-size: 0.9rem;
}
</style>
