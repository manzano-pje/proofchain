<template>
  <header class="landing-header">
    
      <div class="header-content">
        <RouterLink to="/" class="logo">
          <img :src="logo" alt="ProofChain Logo" />
        </RouterLink>

        <button class="mobile-toggle" @click="toggleMobileMenu">
          <i class="fas fa-bars"></i>
        </button>

        <nav :class="{open: isMenuOpen}">
          <ul>
            <li><a href="#recursos">Recursos</a></li>
            <li><a href="#como-funciona">Como funciona</a></li>
            <li><a href="#planos">Planos</a></li>
            <li><a href="#autenticacao" class="btn btn-outline">Autenticador</a></li>
            <li>
              <RouterLink to="/login" class="btn btn-outline btn-primary">Login</RouterLink>                
            </li>
          </ul>
        </nav>
      </div>
    </header>
</template>

<script setup>
    import { ref } from 'vue'
    import logo from '@/assets/img/logo-horizontal.png'

    const isMenuOpen = ref(false)

    const toggleMenu = () => {
        isMenuOpen.value = !isMenuOpen.value
    }
</script>