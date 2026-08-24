<template>
  <div id="app">
    <Header />
    <Hero />
    <Audiences />
    <Flow />
    <CrownCarousel :items="crownCarouselItems" />
    <Footer />
  </div>
</template>

<script setup>
import Header from '@/layouts/components/Header/Header.vue'
import Footer from '@/layouts/components/Footer/Footer.vue'
import Hero from '@/modules/landing/components/Hero/Hero.vue'
import Audiences from './modules/landing/components/Audiences/Audiences.vue'
import Flow from './modules/landing/components/Flow/Flow.vue'
import CrownCarousel from '@/components/layout/CrownCarousel/CrownCarousel.vue'
import { crownCarouselItems } from '@/components/layout/content/CrownCarousel.ts'
</script>
