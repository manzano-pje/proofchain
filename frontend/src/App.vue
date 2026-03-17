<script setup lang="ts">
import { RouterView } from 'vue-router' 
import { useRoute } from 'vue-router'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
const route = useRoute()
</script>

<template>
  <div class="app-layout">
    <Header v-if="!route.meta.hideLayout"/>
    <main>
      <RouterView />
    </main>
    <Footer v-if="!route.meta.hideLayout" />
  </div>
</template>

<style scoped>
.app-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

main {
  width: 100%;
  flex: 1; /* This makes the main content expand to fill available space, pushing footer down */
}
</style>
