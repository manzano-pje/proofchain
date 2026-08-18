<template>
  <div id="app">
    <Header />
    <Hero />
    <Audiences />
    <!-- <Gravityzero /> -->
    <Footer />
  </div>
</template>

<script setup>
import Header from '@/layouts/components/Header/Header.vue'
import Footer from '@/layouts/components/Footer/Footer.vue'
import Hero from '@/modules/landing/components/Hero/Hero.vue'
import Audiences from './modules/landing/components/Audiences/Audiences.vue'
// import Gravityzero from '@/components/ui/Gravityzero/GravityZero.vue'
</script>
