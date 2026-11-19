<!--
=========================================================
Project.......: ProofChain
Module........: Auth
Feature.......: Password Recovery
File..........: RecoveryLayout.vue
Version.......: 1.0.0

Description...:
Layout compartilhado pelas etapas de recuperação de senha.

Responsibilities:
- Reutilizar a composição visual da tela de login.
- Apresentar o conteúdo da etapa recebida por slots.

Dependencies..:
- Login.css
- resetPassword.webp

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
import './RecoveryLayout.css'

import lightLogo from '@/assets/images/logo/logo_horizontal_light.svg'
import darkLogo from '@/assets/images/logo/logo_horizontal_black.svg'
import resetPasswordImage from '@/modules/auth/images/resetPassword.webp'

defineOptions({ name: 'PasswordRecoveryLayout' })

defineSlots<{
  header?: () => unknown
  default?: () => unknown
}>()
</script>

<template>
  <main class="login">
    <section class="login__brand-panel" aria-labelledby="recovery-brand-title">
      <RouterLink to="/">
        <img class="login__logo login__logo--light" :src="darkLogo" alt="ProofChain" />
      </RouterLink>
      <div class="login__dashboard">
        <img :src="resetPasswordImage" alt="Recupere seu acesso com segurança" />
      </div>
      <div class="login__brand-content">
        <h1 id="recovery-brand-title" class="login__brand-title">
          Recupere seu acesso<br />
          <span>com segurança.</span>
        </h1>
        <p class="login__brand-description">
          Valide sua solicitação por e-mail para voltar a emitir, gerenciar e certificados digitais
          na rede.
        </p>


      </div>
    </section>

    <section class="login__form-panel" aria-label="Recuperação de senha">
      <RouterLink to="/" aria-label="Página inicial">
        <img class="login__logo login__logo--dark" :src="lightLogo" alt="ProofChain" />
      </RouterLink>

      <div class="login__form-content">
        <slot name="header" />
        <slot />
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
