<template>
  <div id="app">
    <Header />
    <Hero />
    <Audiences />
    <Flow />
    <CrownCarousel />
    <Pricing />
    <Footer />
  </div>
</template>

<script setup>
import Header from '@/layouts/components/Header/Header.vue'
import Hero from '@/modules/landing/components/Hero/Hero.vue'
import Audiences from './modules/landing/components/Audiences/Audiences.vue'
import Flow from './modules/landing/components/Flow/Flow.vue'
import CrownCarousel from './modules/landing/components/CrownCarousel/CrownCarousel.vue'
import Pricing from './modules/landing/components/Pricing/Pricing.vue'
import Footer from '@/layouts/components/Footer/Footer.vue'
</script>
