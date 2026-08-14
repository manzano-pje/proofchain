
<script setup lang="ts">
  import './Hero.css'
</script>

<template>
  <section class="hero">
    <div class="hero__content">
      <h1 class="hero__title">Confiança começa<br> com uma prova.</h1>
      <p class="hero__subtitle">Toda conquista merece ser comprovada.</p>
      <p class="hero__subtitle">Toda credencial merece ser confiável.</p>
      <div class="hero__actions">
          <a href="#" class="hero-button">Começar grátis</a>
          <a href="#" class="hero-button hero-button--secondary">Saiba mais</a>
      </div>
    </div>
    <div class="hero__image">
        <img src="@/assets/ilustrations/image-hero.png" alt="Dashboard" />
    </div>

  </section>
</template>