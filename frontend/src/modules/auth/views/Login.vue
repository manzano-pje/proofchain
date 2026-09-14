<script setup lang="ts">
import { ref } from 'vue'

import BaseButton from '@/core/components/base/BaseButton/BaseButton.vue'
import './Login.css'

import lightLogo from '@/assets/images/logo/logo_horizontal_light.svg'
import darkLogo from '@/assets/images/logo/logo_horizontal_black.svg'
import dashboardImage from '@/modules/auth/images/dashboard.webp'

const email = ref('')
const password = ref('')
const showPassword = ref(false)
const isSubmitting = ref(false)
const emailError = ref('')
const passwordError = ref('')

function validateForm() {
  emailError.value = ''
  passwordError.value = ''

  if (!email.value || !/^\S+@\S+\.\S+$/.test(email.value)) {
    emailError.value = 'Informe um e-mail válido.'
  }

  if (!password.value) {
    passwordError.value = 'Informe sua senha.'
  }

  return !emailError.value && !passwordError.value
}

async function handleSubmit() {
  if (isSubmitting.value || !validateForm()) return

  isSubmitting.value = true
  await new Promise((resolve) => window.setTimeout(resolve, 700))
  isSubmitting.value = false
}
</script>

<template>
  <main class="login">
    <section class="login__brand-panel" aria-labelledby="login-brand-title">
      <a href="/">
        <img class="login__logo login__logo--light" :src="darkLogo" alt="ProofChain" />
      </a>
      <div class="login__brand-content">
        <h1 id="login-brand-title" class="login__brand-title">
          Segurança e autenticidade<br />
          <span>para seus certificados.</span>
        </h1>
        <p class="login__brand-description">
          Gestão moderna de certificados digitais com tecnologia blockchain.
        </p>

        <div class="login__dashboard">
          <img :src="dashboardImage" alt="Painel de gestão de certificados ProofChain" />
        </div>
      </div>
    </section>

    <section class="login__form-panel" aria-labelledby="login-title">
      <img class="login__logo login__logo--dark" :src="lightLogo" alt="ProofChain" />

      <div class="login__form-content">
        <header class="login__header">
          <h2 id="login-title">Bem-vindo de volta</h2>
          <p>Acesse sua conta para gerenciar certificados e instituições.</p>
        </header>

        <form class="login__form" novalidate @submit.prevent="handleSubmit">
          <div class="login__field">
            <label for="email">E-mail</label>
            <div class="login__input-wrap">
              <span class="login__field-icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <rect x="3" y="5" width="18" height="14" rx="2" />
                  <path d="m3 7 9 6 9-6" />
                </svg>
              </span>
              <input
                id="email"
                v-model="email"
                type="email"
                autocomplete="email"
                placeholder="seu@email.com"
                :aria-invalid="Boolean(emailError)"
                :aria-describedby="emailError ? 'email-error' : undefined"
                @input="emailError = ''"
              />
            </div>
            <p v-if="emailError" id="email-error" class="login__error" aria-live="polite">
              {{ emailError }}
            </p>
          </div>

          <div class="login__field">
            <label for="password">Senha</label>
            <div class="login__input-wrap">
              <span class="login__field-icon" aria-hidden="true">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <rect x="5" y="10" width="14" height="11" rx="2" />
                  <path d="M8 10V7a4 4 0 0 1 8 0v3" />
                </svg>
              </span>
              <input
                id="password"
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                autocomplete="current-password"
                :aria-invalid="Boolean(passwordError)"
                :aria-describedby="passwordError ? 'password-error' : undefined"
                @input="passwordError = ''"
              />
              <button
                class="login__password-toggle"
                type="button"
                :aria-label="showPassword ? 'Ocultar senha' : 'Mostrar senha'"
                @click="showPassword = !showPassword"
              >
                {{ showPassword ? 'Ocultar' : 'Mostrar' }}
              </button>
            </div>
            <p v-if="passwordError" id="password-error" class="login__error" aria-live="polite">
              {{ passwordError }}
            </p>
          </div>

          <a class="login__forgot-link" href="/recuperar-senha">Esqueci minha senha</a>

          <BaseButton
            type="submit"
            variant="primary"
            class="login__buttom"
            size="lg"
            full-width
            :loading="isSubmitting"
          >
            {{ isSubmitting ? 'Entrando...' : 'Entrar' }}
          </BaseButton>
        </form>
      </div>

      <div class="login__security" role="note">
        <span class="login__security-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <rect x="5" y="10" width="14" height="11" rx="2" />
            <path d="M8 10V7a4 4 0 0 1 8 0v3" />
          </svg>
        </span>
        <div class="login__security-text">
          <strong>Conexão segura</strong>
          <p>Seus dados e certificados estão protegidos.</p>
        </div>
      </div>
    </section>
  </main>
</template>
