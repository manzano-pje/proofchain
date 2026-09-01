<script setup lang="ts">
import { ref } from 'vue'
import './Header.css'

const isMenuOpen = ref(false)

function toggleMenu() {
  isMenuOpen.value = !isMenuOpen.value
}

function closeMenu() {
  isMenuOpen.value = false
}
</script>

<template>
  <header class="header">
    <div class="header__content">
      <!-- Logo -->
      <a href="/" class="header__brand">
        <img
          src="@/assets/images/logo/logo_horizontal_black.svg"
          alt="ProofChain Logo"
          class="header__logo"
        />
      </a>

      <!-- Navegação desktop/tablet -->
      <nav class="header__nav">
        <a href="#audiences" class="header__nav-link">Para quem é</a>
        <a href="#flow" class="header__nav-link">Como funciona</a>
        <a href="#crown-carousel" class="header__nav-link">Por que escolher</a>
        <a href="#planos" class="header__nav-link">Planos</a>
      </nav>

      <!-- Ações (botões) -->
      <div class="header__actions">
        <a href="#" class="header-button">Entrar</a>
        <a href="#" class="header-button header-button--primary">Começar grátis</a>
      </div>

      <!-- Hambúrguer (mobile) -->
      <button
        class="header__hamburger"
        :class="{ 'header__hamburger--active': isMenuOpen }"
        @click="toggleMenu"
        aria-controls="mobile-menu"
        aria-label="Abrir menu"
      >
        <span class="header__hamburger-bar"></span>
        <span class="header__hamburger-bar"></span>
        <span class="header__hamburger-bar"></span>
      </button>
    </div>

    <!-- Menu mobile overlay -->
    <div
      id="mobile-menu"
      class="header__menu-overlay"
      :class="{ 'header__menu-overlay--open': isMenuOpen }"
      role="navigation"
      aria-label="Menu principal"
    >
      <div class="header__menu-inner">
        <a href="#" class="header__menu-link" @click="closeMenu">Soluções</a>
        <a href="#" class="header__menu-link" @click="closeMenu">Como funciona</a>
        <a href="#" class="header__menu-link" @click="closeMenu">Aplicações</a>
        <a href="#" class="header__menu-link" @click="closeMenu">Planos</a>
      </div>
    </div>
  </header>
</template>
