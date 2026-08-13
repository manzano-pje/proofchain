<!--
=========================================================
Project.......: ProofChain
Component.....: Header
File..........: Header.vue
Version.......: 1.0.1

Description...:
Reusable page header component.

Responsibility:
Provide a standardized page heading area.

Dependencies..:
- Header.css

Methodology...:
BEM
=========================================================
-->

<script setup lang="ts">
  import './Header.css'
</script>

<template>
  <header class="header">
    <div class="header__content">
      <a class="proofchain-brand">
        <img src="@/assets/images/proofchain-horizontal-dark.png" alt="ProofChain Logo" class="proofchain-logo" />
      </a>
      <nav class="proofchain-navigation">
        <a href="#" class="navigation-link">Soluções</a>
        <a href="#" class="navigation-link">Como funciona</a>
        <a href="#" class="navigation-link">APlicações</a>
        <a href="#" class="navigation-link">Planos</a>
        <a href="#" class="navigation-link">Fale conosco</a>
      </nav>
      <div class="header-actions">
        <a href="#" class="header-button">Entrar</a>
        <a href="#" class="header-button">Começar grátis</a>
      </div>
    </div>
  </header>
</template>